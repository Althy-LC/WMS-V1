package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.DTO.AuditOrder;
import com.example1.wms.DTO.Enum.SaleStatus;
import com.example1.wms.DTO.Enum.Wavestatus;
import com.example1.wms.DTO.OutboundDto;
import com.example1.wms.DTO.SaleOutDetailDto;
import com.example1.wms.DTO.SaleOutDto;
import com.example1.wms.DTO.UtilDto.salelockDto;
import com.example1.wms.Mapper.*;
import com.example1.wms.POJO.*;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.Util.RedisLockUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 18:01
 * @Description
 */
@Service
public class SaleServiceImpl implements SaleService{
@Autowired
 SequenceService sequenceService;
@Autowired
    SaleMapper saleMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private SaleDetailMapper saleDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockLocationMapper stockLocationMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private WaveMapper waveMapper;
    @Autowired
    private WaveDetailMapper waveDetailMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private StockFlowMapper stockFlowMapper;

    //创建销售出库单
    @Override
    @Transactional
    public String create(SaleOutDto saleOutDto) {
        SaleOut sale = new SaleOut();

        //复制前端数据到销售单
        BeanUtils.copyProperties(saleOutDto,sale);

        //查询仓库id
            Long warehouseId = houseMapper.getid(saleOutDto.getWarehouseCode());

        //生成销售出库单号
        CodeBulider codeBulider =new CodeBulider();
        //获取序列表中当月销售单序号
        Long seq = sequenceService.nextCodeByymd("Sale");
        String orderNo = codeBulider.getSaleOrderNo(seq);

        //设置销售单号与仓库id
        sale.setOrderNo(orderNo);
        sale.setWarehouseId(warehouseId);

        //修改序列表数据
        sequenceService.update("Sale",seq);

        //创建销售出库主表
        saleMapper.create(sale);

        //获取主表id
          Long orderId = saleMapper.getid(orderNo);

           List<SaleOutDetail> saleOutDetails = saleOutDto.getDetail().stream().map(T->{
               //获取商品id
               Long skuId = skuMapper.getid(T.getSkuCode());
               //获取库位信息
               Long locationId = stockLocationMapper.getlocationId(warehouseId,skuId);

               SaleOutDetail saleOutDetail =new SaleOutDetail();

                BeanUtils.copyProperties(T,saleOutDetail);
                saleOutDetail.setOrderId(orderId);
                saleOutDetail.setSkuId(skuId);
                saleOutDetail.setLocationId(locationId);

                return saleOutDetail;
            }).toList();

           saleDetailMapper.add(saleOutDetails);

        return orderNo;
    }


    //查询需要审批的销售单
    @Override
    public List<AuditOrder> getAudit() {
        List<SaleOut> sales = saleMapper.getBystatus(SaleStatus.CREATE);

        Long warehouseId;
        String warehouseCode;
        List<SaleOutDetailDto> saleDetailDtos = new ArrayList<>();
        List<AuditOrder> auditOrders = new ArrayList<>();

         for(int i = 0 ; i<sales.size() ; i++){
            AuditOrder auditOrder = new AuditOrder();
            SaleOutDto sale = new SaleOutDto();
            SaleOutDetailDto saleOutDetail = new SaleOutDetailDto();

            //主表数据复制
             warehouseId = sales.get(i).getWarehouseId();
            warehouseCode = houseMapper.getCode(warehouseId);

            BeanUtils.copyProperties(sales.get(i),sale);
            sale.setWarehouseCode(warehouseCode);

            //从表数据复制
             //获取从表数据
             Long orderId = sales.get(i).getId();
            List<SaleOutDetail> saleOutDetails = saleDetailMapper.getByorderId(orderId);

                    //获取对应的从表数据
                  saleDetailDtos =  saleOutDetails.stream().map(T->{
                     SaleOutDetailDto saleOutDetailDto = new SaleOutDetailDto();

                      Long skuId,locationId;

                      skuId = T.getSkuId();
                     locationId = T.getLocationId();

                     //获取商品名称和库位编码
                   SKU sku = skuMapper.getCodeAndName(skuId);

                   saleOutDetailDto.setSkuCode(sku.getSkuCode());
                   saleOutDetailDto.setSkuName(sku.getSkuName());

                   return saleOutDetailDto;
                  }).toList();

                  auditOrder.setSale(sale);
                  auditOrder.setSaleOutDetailDtos(saleDetailDtos);
                  auditOrders.add(auditOrder);
         }

         return auditOrders;
    }

    //审核
    @Override
    @Transactional
    public void audit(OutboundDto outboundDto) {
        String orderNo = outboundDto.getOrderNo();
        SaleStatus status = outboundDto.getStatus();

        //获取销售出库数据
        SaleOut sale = saleMapper.getsale(orderNo);
        //获取乐观锁版本
        Integer saleversion = sale.getVersion();
        //获取仓库号
        Long warehouseId = sale.getWarehouseId();
        //获取销售单id
        Long orderId = sale.getId();

        //获取明细数据
       List<SaleOutDetail> saleOutDetails = saleDetailMapper.getsku(orderId);

            List<salelockDto> salelockDtos = saleOutDetails.stream().map(T->{
               salelockDto salelockDto = new salelockDto();
                //获取stock乐观锁
                Integer stockversion = stockMapper.getversion(warehouseId,T.getSkuId());

                BeanUtils.copyProperties(T,salelockDto);
                 salelockDto.setVersion(stockversion);

                return salelockDto;
            }).toList();

       //防超卖设计：
            //1.使用分布式锁保证库存预占的原子性
            //2.为每个SKU获取分布式锁,锁的粒度:仓库+商品
        List<String> lockKeys = new ArrayList<>();
        Integer result;
        try {
            // 为每个SKU获取锁
            for (salelockDto salelockDto : salelockDtos) {
                String lockKey = "lock:stock:" + warehouseId + ":" + salelockDto.getSkuId();
                lockKeys.add(lockKey);

                // 尝试获取锁,超时时间3秒
                boolean locked = redisLockUtil.tryLock(lockKey, 3);
                if (!locked) {
                    // 释放已获取的所有锁
                    for (String key : lockKeys) {
                        redisLockUtil.unlock(key);
                    }
                    throw new BuniessException("系统繁忙,请稍后重试");
                }
            }

            // 所有锁都获取成功,执行预占库存（wms_stock表）
            result = stockMapper.salelocked(warehouseId, salelockDtos);
            if (result != saleOutDetails.size()) {
                throw new BuniessException("预占库存失败");
            }

            // 预占stock_location库存（amount减，locked_amount加）
            List<StockLocation> stockLocations = new ArrayList<>();
            for (SaleOutDetail detail : saleOutDetails) {
                StockLocation stockLocation = new StockLocation();
                stockLocation.setWarehouseId(warehouseId);
                stockLocation.setSkuId(detail.getSkuId());
                stockLocation.setLocationId(detail.getLocationId());
                stockLocation.setAmount(detail.getQuantity());
                stockLocations.add(stockLocation);
            }
            stockLocationMapper.lockStock(stockLocations);

            // 记录stock_flow流水
            List<StockFlow> stockFlows = new ArrayList<>();
            for (SaleOutDetail detail : saleOutDetails) {
                StockFlow stockFlow = new StockFlow();
                stockFlow.setSkuId(detail.getSkuId());
                stockFlow.setWarehouseId(warehouseId);
                stockFlow.setChangeType(com.example1.wms.DTO.Enum.StockFlowChangeTypeEnum.RELEASE_LOCK);
                stockFlow.setChangeQuantity(detail.getQuantity());
                stockFlow.setOrderNo(orderNo);
                stockFlows.add(stockFlow);
            }
            if (!stockFlows.isEmpty()) {
                stockFlowMapper.add(stockFlows);
            }
        } finally {
            // 释放所有锁
            for (String lockKey : lockKeys) {
                redisLockUtil.unlock(lockKey);
            }
        }

        //修改销售单状态
            sale = new SaleOut();
            sale.setId(orderId);
            sale.setStatus(status);
            sale.setVersion(saleversion);

            result = saleMapper.update(sale);
            if (result != 1){
                throw new BuniessException("状态修改异常");
            }

    }

    //拣货
    @Override
    @Transactional
    public void pick(String waveNo) {
        SaleStatus saleStatus = SaleStatus.PICKING;
        Long waveId = waveMapper.getid(waveNo);
        List<WaveDetail> waveDetails = waveDetailMapper.getByWaveid(waveId);

        if (waveDetails == null || waveDetails.isEmpty()) {
            throw new BuniessException("波次明细不存在");
        }

        Long warehouseId = waveMapper.getWarehouse(waveId);

        // 获取拣货区库位 (area_type = 5)
        Long pickLocationId = areaMapper.getPickLocation(warehouseId);
        if (pickLocationId == null) {
            throw new BuniessException("未找到可用的拣货区库位");
        }

        List<SaleOutDetail> saleOutDetails = new ArrayList<>();
        List<SaleOut> saleUpdates = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();

        for (WaveDetail waveDetail : waveDetails) {
            Long orderId = waveDetail.getOrderId();

            // 收集需要更新的销售单
            if (!orderIds.contains(orderId)) {
                SaleOut saleUpdate = new SaleOut();
                saleUpdate.setVersion(saleMapper.getversion(orderId));
                saleUpdate.setId(orderId);
                saleUpdate.setStatus(saleStatus);
                saleUpdates.add(saleUpdate);
                orderIds.add(orderId);
            }

            // 构建销售明细更新对象
            SaleOutDetail saleOutDetail = new SaleOutDetail();
            saleOutDetail.setLocationId(pickLocationId);
            saleOutDetail.setSkuId(waveDetail.getSkuId());
            saleOutDetail.setOrderId(waveDetail.getOrderId());
            saleOutDetail.setPickedQuantity(waveDetail.getQuantity());
            saleOutDetails.add(saleOutDetail);
        }

        // 批量更新销售单状态
        saleMapper.updates(saleUpdates);

        // 批量更新销售明细的库位为拣货区库位
        saleDetailMapper.update(saleOutDetails);

        // 更新波次状态为完成
        waveMapper.putStatus(waveNo, Wavestatus.FINISH);

        // 占用拣货区库位
        locationMapper.useLocation(pickLocationId);
    }


    //复核
    @Override
    @Transactional
    public void review(String orderNo) {
        //获取销售出库单信息
        SaleOut sale = saleMapper.getsale(orderNo);
        if (sale == null) {
            throw new BuniessException("销售出库单不存在");
        }

        Long orderId = sale.getId();
        Long warehouseId = sale.getWarehouseId();
        Integer version = sale.getVersion();

        // 获取销售单明细（包含库位信息）
        List<SaleOutDetail> saleOutDetails = saleMapper.getDetailsWithLocation(orderId);
        if (saleOutDetails == null || saleOutDetails.isEmpty()) {
            throw new BuniessException("销售出库单明细不存在");
        }

        //获取复核区库位
        Long reviewLocationId = areaMapper.getReviewLocation(warehouseId);
        if (reviewLocationId == null) {
            throw new BuniessException("未找到可用的复核区库位");
        }

        // 收集需要释放的库位ID
        List<Long> releaseLocationIds = new ArrayList<>();
        for (SaleOutDetail detail : saleOutDetails) {
            if (detail.getLocationId() != null) {
                releaseLocationIds.add(detail.getLocationId());
            }
        }

        //更新销售单状态为复核状态（使用乐观锁）
        int result = saleMapper.updateToReview(orderNo, SaleStatus.REVIEW, version);
        if (result != 1) {
            throw new BuniessException("更新销售单状态失败，可能已被其他操作修改");
        }

        //占用复核区库位（将库位状态设置为占用）
        locationMapper.useLocation(reviewLocationId);

        //更新销售明细的库位为复核区库位
        List<SaleOutDetail> updateDetails = new ArrayList<>();
        for (SaleOutDetail detail : saleOutDetails) {
            SaleOutDetail updateDetail = new SaleOutDetail();
            updateDetail.setOrderId(orderId);
            updateDetail.setSkuId(detail.getSkuId());
            updateDetail.setLocationId(reviewLocationId);
            updateDetails.add(updateDetail);
        }
        saleDetailMapper.update(updateDetails);

        //释放原拣货区库位（将库位状态从占用改为空闲）
        if (!releaseLocationIds.isEmpty()) {
            locationMapper.releaseLocations(releaseLocationIds);
        }
    }

    //出库
    @Override
    @Transactional
    public void outbound(String orderNo) {
        //获取销售出库单信息
        SaleOut sale = saleMapper.getsale(orderNo);
        if (sale == null) {
            throw new BuniessException("销售出库单不存在");
        }

        Long orderId = sale.getId();
        Long warehouseId = sale.getWarehouseId();
        Integer version = sale.getVersion();

        // 获取销售单明细（包含库位信息）
        List<SaleOutDetail> saleOutDetails = saleMapper.getDetailsWithLocation(orderId);
        if (saleOutDetails == null || saleOutDetails.isEmpty()) {
            throw new BuniessException("销售出库单明细不存在");
        }

        //更新销售单状态为OUTBOUND（使用乐观锁）
        int result = saleMapper.updateToOutbound(orderNo, SaleStatus.OUTBOUND, version);
        if (result != 1) {
            throw new BuniessException("更新销售单状态失败，可能已被其他操作修改");
        }

        //收集需要释放的库位ID（复核区库位）
        List<Long> releaseLocationIds = new ArrayList<>();
        for (SaleOutDetail detail : saleOutDetails) {
            if (detail.getLocationId() != null) {
                releaseLocationIds.add(detail.getLocationId());
            }
        }

        //  释放销售明细中的库位（将location_id置空或标记为已出库）
        // 注意：这里可以根据业务需求决定是否需要清空location_id
        // 如果只是标记状态，可以不需要额外操作

        // 获取分布式锁和stock乐观锁，扣减库存
        List<salelockDto> salelockDtos = saleOutDetails.stream().map(T -> {
            salelockDto salelockDto = new salelockDto();
            // 获取stock乐观锁
            Integer stockversion = stockMapper.getversion(warehouseId, T.getSkuId());

            BeanUtils.copyProperties(T, salelockDto);
            salelockDto.setVersion(stockversion);

            return salelockDto;
        }).toList();

        List<String> lockKeys = new ArrayList<>();
        try {
            // 为每个SKU获取锁
            for (salelockDto salelockDto : salelockDtos) {
                String lockKey = "lock:stock:" + warehouseId + ":" + salelockDto.getSkuId();
                lockKeys.add(lockKey);

                // 尝试获取锁,超时时间3秒
                boolean locked = redisLockUtil.tryLock(lockKey, 3);
                if (!locked) {
                    // 释放已获取的所有锁
                    for (String key : lockKeys) {
                        redisLockUtil.unlock(key);
                    }
                    throw new BuniessException("系统繁忙,请稍后重试");
                }
            }

            // 所有锁都获取成功,执行出库扣减库存
            result = stockMapper.outboundStock(warehouseId, salelockDtos);
            if (result != saleOutDetails.size()) {
                throw new BuniessException("出库扣减库存失败");
            }

            //记录stock流水
            List<StockFlow> stockFlows = new ArrayList<>();
            for (SaleOutDetail detail : saleOutDetails) {
                StockFlow stockFlow = new StockFlow();
                stockFlow.setSkuId(detail.getSkuId());
                stockFlow.setWarehouseId(warehouseId);
                stockFlow.setChangeType(com.example1.wms.DTO.Enum.StockFlowChangeTypeEnum.SALES_OUT);
                stockFlow.setChangeQuantity(detail.getQuantity());
                stockFlow.setOrderNo(orderNo);
                stockFlows.add(stockFlow);
            }
            if (!stockFlows.isEmpty()) {
                stockFlowMapper.add(stockFlows);
            }

            //扣减stock_location的锁定库存
            List<StockLocation> stockLocations = new ArrayList<>();
            for (SaleOutDetail detail : saleOutDetails) {
                StockLocation stockLocation = new StockLocation();
                stockLocation.setWarehouseId(warehouseId);
                stockLocation.setSkuId(detail.getSkuId());
                stockLocation.setLocationId(detail.getLocationId());
                stockLocation.setAmount(detail.getQuantity());
                stockLocations.add(stockLocation);
            }
            stockLocationMapper.deductLockedStock(stockLocations);

            // 检查amount为0的stock_location记录，释放库位并更新状态为Abandoned
            List<Long> abandonedIds = new ArrayList<>();
            for (SaleOutDetail detail : saleOutDetails) {
                List<StockLocation> zeroAmountLocations = stockLocationMapper.getZeroAmountLocations(
                    warehouseId, detail.getSkuId(), detail.getLocationId()
                );
                for (StockLocation location : zeroAmountLocations) {
                    abandonedIds.add(location.getId());
                }
            }

            // 将amount为0的记录状态更新为Abandoned
            if (!abandonedIds.isEmpty()) {
                stockLocationMapper.updateStatusToAbandoned(abandonedIds);
            }

        } finally {
            // 释放所有锁
            for (String lockKey : lockKeys) {
                redisLockUtil.unlock(lockKey);
            }
        }

        //释放复核区库位
        if (!releaseLocationIds.isEmpty()) {
            locationMapper.releaseLocations(releaseLocationIds);
        }
    }


}
