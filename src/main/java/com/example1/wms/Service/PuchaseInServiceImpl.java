package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.DTO.UtilDto.FinishSQL;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.DTO.*;
import com.example1.wms.DTO.Enum.*;
import com.example1.wms.Mapper.*;
import com.example1.wms.POJO.*;
import com.example1.wms.RoolBackException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Althy
 * @Create 2026/5/7 22:44
 * @Description
 */

@Service
public class PuchaseInServiceImpl implements PuchaseInService {
    @Autowired
    PuchaseInMapper puchaseInMapper;
    @Autowired
    PuchaseInDetailMapper puchaseInDetailMapper;
    @Autowired
    SequenceService sequenceService;
    @Autowired
    StockMapper stockMapper;
    @Autowired
    StockFlowMapper stockFlowMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private StockLocationMapper stockLocationMapper;


    //新建采购入库表
    @Override
    @Transactional
    public void add(PurchaseInDto purchaseInDto) {
        //获取入库表编码
        Long seq = sequenceService.nextCodeByymd("PurchaseIn_code");
        CodeBulider codeBulider = new CodeBulider();
        String orderNo = codeBulider.getnewPuchaseInCode(seq);

        //Dto->Pojo
        PurchaseIn purchaseIn = new PurchaseIn();
        BeanUtils.copyProperties(purchaseInDto, purchaseIn);
        purchaseIn.setOrderNo(orderNo);


        //获取仓库id
        Long warehouseId = puchaseInMapper.selecthouse(purchaseInDto.getWarehouseCode());
        if (warehouseId == null) {
            throw new BuniessException("仓库编码不存在！");
        }
        purchaseIn.setWarehouseId(warehouseId);


        //创建入库表并更新序列表
        puchaseInMapper.add(purchaseIn);
        sequenceService.update("PurchaseIn_code", seq);

        //获取入库表id，给明细表使用
        Long orderId = puchaseInMapper.getid(purchaseIn.getOrderNo());
        if (orderId == null) {
            throw new BuniessException("采购入库单创建失败，未能获取到ID");
        }

        // 处理明细
        //主表List<Dto>->明细表List<Dto>->List<pojo>
        List<PurchaseInDetail> purchaseInDetails = purchaseInDto.getDetail().stream()
                .map(T -> {
                    System.out.println(T.toString());

                    PurchaseInDetail purchaseInDetail = new PurchaseInDetail();
                    BeanUtils.copyProperties(T, purchaseInDetail);

                    //获取预计入库总数
                    purchaseInDetail.setPlanQuantity(T.getPlanQuantity());
                    purchaseInDetail.setOrderId(orderId);

                    //获取商品id
                    Long skuId = puchaseInDetailMapper.getSkuid(T.getSkuCode());

                    if (skuId == null) {
                        throw new BuniessException("SKU编码不存在！");
                    }
                    purchaseInDetail.setSkuId(skuId);
                    purchaseInDetail.setStatus(PurchaseInDeatilStatusEnum.PENDING_RECEIVE);

                    return purchaseInDetail;
                }).collect(Collectors.toList());

        //新建明细表
        puchaseInDetailMapper.add(purchaseInDetails);
    }


    //获取主表指定入库单
    @Override
    public PurchaseInDto get(String orderNo) {
        PurchaseIn purchaseIn = puchaseInMapper.get(orderNo);
        House house = puchaseInMapper.selecthousebyid(purchaseIn.getWarehouseId());

        PurchaseInDto purchaseInDto = new PurchaseInDto();
        purchaseInDto.setWarehouseCode(house.getWarehouseCode());
        purchaseInDto.setWarehouseName(house.getWarehouseName());
        purchaseInDto.setStatusdesc(purchaseIn.getStatus().getDesc());
        BeanUtils.copyProperties(purchaseIn, purchaseInDto);
        return purchaseInDto;
    }

    // 通过单号查流水
    @Override
    public List<PurchaseInDetailDto> getDetail(String orderNo) {

        Long orderId = puchaseInMapper.getid(orderNo);
        if (orderId == null) {
            throw new BuniessException("订单不存在：" + orderNo);
        }


        List<Map<Long, Object>> skuInfos = puchaseInDetailMapper.getsku();
        Map<Long, Map<String, String>> skuInfoMap = new HashMap<>();

        System.out.println(skuInfos.toString());

        if (skuInfos != null && !skuInfos.isEmpty()) {
            for (Map<Long, Object> map : skuInfos) {
                Long skuId = (Long) map.get("id");
                String skuCode = (String) map.get("sku_code");

                System.out.println(map.get("sku_code"));

                String skuName = (String) map.get("sku_name");
                if (skuId != null && skuCode != null && skuName != null) {
                    skuInfoMap.put(skuId, Map.of("skuCode", skuCode, "skuName", skuName));
                }
            }
        }

        List<Map<String, Object>> locationInfos = puchaseInDetailMapper.getlocation();
        Map<Long, String> locationInfoMap = new HashMap<>();
        if (locationInfos != null && !locationInfos.isEmpty()) {
            for (Map<String, Object> map : locationInfos) {
                Long locationId = (Long) map.get("id");
                String locationCode = (String) map.get("location_code");
                if (locationId != null && locationCode != null) {
                    locationInfoMap.put(locationId, locationCode);
                }
            }
        }

        List<PurchaseInDetail> purchaseInDetailList = puchaseInDetailMapper.getDetail(orderId);
        if (purchaseInDetailList == null || purchaseInDetailList.isEmpty()) {
            return null;
        }

        List<PurchaseInDetailDto> detailDtos = purchaseInDetailList.stream().map(detail -> {
            PurchaseInDetailDto dto = new PurchaseInDetailDto();


            BeanUtils.copyProperties(detail, dto);

            dto.setOrderNo(orderNo);

            Map<String, String> skuMap = skuInfoMap.get(detail.getSkuId());
            if (skuMap != null) {
                dto.setSkuCode(skuMap.get("skuCode"));
                dto.setSkuName(skuMap.get("skuName"));
            }

            String locationCode = locationInfoMap.get(detail.getLocationId());
            dto.setLocationCode(locationCode);
            dto.setDesc(dto.getStatus().getDesc());
            dto.setQcdesc(dto.getQcResult().getDesc());

            return dto;
        }).collect(Collectors.toList());

        return detailDtos;
    }

    //收货状态
    @Transactional
    @Override
    public void recive(ReciveDto reciveDto) {
        System.out.println("1."+ reciveDto.getLocationCode());

        Long locationId = locationMapper.getidByCode(reciveDto.getLocationCode());
        if (locationId == null) {
            throw new BuniessException("库位不存在");
        }

        int result = puchaseInMapper.update(reciveDto.getOrderNo(), reciveDto.getInstatus());
        if (result == 0) {
            throw new BuniessException("采购入库单号不存在");
        }

        result = puchaseInDetailMapper.recive(reciveDto.getDetailstatus(), locationId);
        if (result == 0) {
            throw new BuniessException("明细表库位修改失败");
        }

        result = locationMapper.useLocation(locationId);
        if (result == 0) {
            throw new BuniessException("库位占用失败");
        }

    }

    //审核
    @Override
    @Transactional
    public void Audit(AuditDto auditDto) {

        // 获取采购入库单ID
        Long orderId = puchaseInMapper.getid(auditDto.getOrderNo());

        // 查询当前采购单占用的所有库位ID
        List<Long> locationidlist = new ArrayList<>(puchaseInDetailMapper.getlocationids(orderId));

        System.out.println("占用了几个库位："+locationidlist.size());
        System.out.println(locationidlist.toString());

        // 释放库位
        int result = locationMapper.releaseLocations(locationidlist);

        System.out.println("释放了几个库位："+result);

        if (result != locationidlist.size()) {
            throw new BuniessException("部分收货区库位释放失败");
        }

        //修改入库单状态
        result = puchaseInMapper.update(auditDto.getOrderNo(), auditDto.getStatus());
        if (result == 0) {
            throw new BuniessException("提供的入库单不存在");
        }


        List<Long> locationids = new ArrayList<>();

        //从前端数据体中拆出Detail部分的数据
        List<AuditDto.A> data = auditDto.getData();

        //将dto->pojo
        List<PurchaseInDetail> purchaseInDetails = data.stream()
                .map(R -> {
                    PurchaseInDetail purchaseInDetail = new PurchaseInDetail();
                    BeanUtils.copyProperties(R, purchaseInDetail);

                    purchaseInDetail.setOrderId(orderId);

                    //获取skuId
                    Long skuId = skuMapper.getid(R.getSkuCode());
                    if (skuId == null) {
                        throw new BuniessException("部分商品编码不存在");
                    }
                    purchaseInDetail.setSkuId(skuId);

                    //获取当前商品质检区库位id
                    Long locationId = locationMapper.getidByCode(R.getLocationCode());
                    if (locationId == null) {
                        throw new BuniessException("存入库位不可用");
                    }

                    //存入待修改库位list
                    locationids.add(locationId);
                    purchaseInDetail.setLocationId(locationId);


                    return purchaseInDetail;
                }).collect(Collectors.toList());

        //修改入库明细表
        puchaseInDetailMapper.put(purchaseInDetails);

        System.out.println("需要占用几个库位"+locationids.size());
        System.out.println(locationids.toString());

        //批量占用库位
        result = locationMapper.useLocations(locationids);
        System.out.println(result);
        if (result != locationids.size()) {
            throw new BuniessException("部分质检区库位占用失败");
        }

    }


    //质检
    @Transactional
    @Override
    public void inSpect(InspectDto inspectDto) {
        // 获取采购入库单ID
        Long orderId = puchaseInMapper.getid(inspectDto.getOrderNo());
        List<PurchaseInDetail> newpurchaseInDetails = new ArrayList<>();

        // 查询当前采购单占用的所有库位ID
        List<Long> locationids = new ArrayList<>(puchaseInDetailMapper.getlocationids(orderId)) ;

        // 释放库位
        int result = locationMapper.releaseLocations(locationids);

        if (result != locationids.size()) {
            throw new BuniessException("部分质检区库位释放失败");
        }

        // 更新采购入库单状态
        result = puchaseInMapper.update(inspectDto.getOrderNo(), inspectDto.getStatus());
        if (result == 0) {
            throw new BuniessException("修改状态异常");
        }

        List<Long> skuIds = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();


        //获取前端传递的质检数据
        List<InspectDto.I> datas = inspectDto.getData();
        PurchaseIn purchaseIn = puchaseInMapper.get(inspectDto.getOrderNo());
        while(true) {
            List<PurchaseInDetail> purchaseInDetails = datas.stream().map(I -> {

                PurchaseInDetail purchaseInDetail = new PurchaseInDetail();

                // 查询商品ID
                Long skuId = skuMapper.getid(I.getSkuCode());

                //查询当前入库单商品总数量
                Double total = puchaseInDetailMapper.getDetailByidAndCode(purchaseIn.getId(), skuId);

                BeanUtils.copyProperties(I, purchaseInDetail);

                // 设置入库单ID
                purchaseInDetail.setOrderId(orderId);

                // 设置商品ID
                purchaseInDetail.setSkuId(skuId);


                // 自动分配库位
                Long locationId = null;
                locationids.clear();
                Set<Long> uselocations = new HashSet<>();

                //全合格，>95->存储库区   <70->退货库区
                if (I.getUnqualifiedQuabtity() == null && I.getQualifiedQuantity().equals(purchaseIn.getTotalQuantity())) {
                    // 分配收货区库位
                 do {
                     locationId = areaMapper.getreciveByhouse(purchaseIn.getWarehouseId(),uselocations);
                 }while (!uselocations.add(locationId));

                } else if (I.getUnqualifiedQuabtity() != null) {
                    double unqualifiedRate = 1- I.getUnqualifiedQuabtity() / total;

                    if (unqualifiedRate < 0.7) {
                        // 不合格率 < 70% → 分配退货库区库位
                        do {
                            locationId = areaMapper.getReturnLocationId(purchaseIn.getWarehouseId(),uselocations);
                        }while (!uselocations.add(locationId));

                         purchaseInDetail.setQcResult(QcResult.OUT);
                    } else if (unqualifiedRate > 0.95) {
                        do {
                            locationId = areaMapper.getreciveByhouse(purchaseIn.getWarehouseId(), uselocations);
                        }while (!uselocations.add(locationId));

                        if(I.getUnqualifiedReson() == null || I.getUnqualifiedReson().isBlank()){
                            throw new BuniessException("请填写不合格原因");
                        }
                        purchaseInDetail.setQcResult(QcResult.PASS);
                        //记录不合格原因
                        purchaseInDetail.setUnqualifiedReson(I.getUnqualifiedReson());
                    } else {
                        purchaseInDetail.setQcResult(QcResult.FAIL);
                        skuIds.add(skuId);
                        amount.add(I.getUnqualifiedQuabtity());
                        do {
                            locationId = areaMapper.getreciveByhouse(purchaseIn.getWarehouseId(), uselocations);
                        }while (!uselocations.add(locationId));
                    }
                }


                // 设置分配好的库位ID
                purchaseInDetail.setLocationId(locationId);
                //修改状态为待入库
                purchaseInDetail.setStatus(PurchaseInDeatilStatusEnum.WAIT);
                //填写质检员
                if(I.getInspector() == null || I.getInspector().isBlank()){
                    throw new BuniessException("请绑定质检员");
                }
                purchaseInDetail.setInspector(I.getInspector());

                locationids.add(locationId);

                // 返回封装好的明细对象
                return purchaseInDetail;
            }).collect(Collectors.toList());


            //修改明细表数据
            puchaseInDetailMapper.put(purchaseInDetails);

            Set<Long> uselocations = new HashSet<>();
            Long locationId;
            //明细表中新建需部分退货的数据
            for (int i = 0; i < skuIds.size(); i++) {
                do {
                     locationId = areaMapper.getReturnLocationId(purchaseIn.getWarehouseId(), uselocations);
                }while (!uselocations.add(locationId));
                PurchaseInDetail purchaseInDetail = new PurchaseInDetail();
                purchaseInDetail.setOrderId(orderId);
                purchaseInDetail.setSkuId(skuIds.get(i));
                purchaseInDetail.setStatus(PurchaseInDeatilStatusEnum.WAIT);
                purchaseInDetail.setLocationId(locationId);
                locationids.add(locationId);
                purchaseInDetail.setActualQuantity(amount.get(i));
                purchaseInDetail.setPlanQuantity(amount.get(i));
                purchaseInDetail.setQualifiedQuantity(0);
                purchaseInDetail.setQcResult(QcResult.OUT);
                newpurchaseInDetails.add(purchaseInDetail);
            }

            //批量占用库位
            result = locationMapper.useLocations(locationids);
            try {
                if (result != locationids.size()) {
                    throw new RoolBackException("占用库位异常执行回滚");
                }
            }catch (RoolBackException e){
                e.printStackTrace();
                System.out.println("回滚完毕");
                continue;
            }
            break;
        }

        puchaseInDetailMapper.addFail(newpurchaseInDetails);


    }

    //获取推荐库位
    @Override
    public List<InspectDto.I> getlocation(String orderNo) {
         Long orderId = puchaseInMapper.getid(orderNo);
            List<PurchaseInDetail> purchaseInDetails = puchaseInDetailMapper.getDetail(orderId);
          List<InspectDto.I> inspectDtos =  purchaseInDetails.stream().map(T->{
                  InspectDto.I inspectDto = new InspectDto.I();
                    if(T.getQcResult() == QcResult.OUT ) {
                        inspectDto.setType(InspectType.FAIL);
                        inspectDto.setAmount(T.getActualQuantity());
                    }else {
                        inspectDto.setType(InspectType.PASS);
                        inspectDto.setAmount(T.getQualifiedQuantity());
                    }
                  inspectDto.setDesc(inspectDto.getType().getDesc());

                    String skuCode = skuMapper.getCode(T.getSkuId());
                   inspectDto.setSkuCode(skuCode);
                    String locationCode =  locationMapper.getCode(T.getLocationId());
                    inspectDto.setLocationCode(locationCode);
                    return inspectDto;
            }).collect(Collectors.toList());
        return inspectDtos;
    }

    //完成状态
    //完成状态
    @Transactional
    @Override
    public void finish(FinishDto finishDto) {
        // 获取采购入库单ID
        Long orderId = puchaseInMapper.getid(finishDto.getOrderNo());

        List<FinishDto.F> data = finishDto.getData();

        List<Long> skuids = data.stream().map(T->{

            System.out.println(T.toString());

            Long skuId = skuMapper.getid( T.getSkuCode());
            return skuId;
        }).collect(Collectors.toList());

        System.out.println(skuids.toString());



        List<FinishSQL> finishSQLS = data.stream().map(F->{
            FinishSQL finishSQL = new FinishSQL();

            //获取商品id
            Long skuId = skuMapper.getid( F.getSkuCode());

            //判断记录是合格还是不合格
            //合格状态->已入库
            //不合格状态->待退回
            System.out.println(F.getType());
            if(F.getType().equals(InspectType.FAIL) ) {
                System.out.println("WAITPUTOUT");
                finishSQL.setDetailStatus(PurchaseInDeatilStatusEnum.WAITPUTOUT);
            }else{
                System.out.println("PUTAWAY");
                finishSQL.setDetailStatus(PurchaseInDeatilStatusEnum.PUTAWAY);
            }

            finishSQL.setOrderId(orderId);
            finishSQL.setSkuId(skuId);

            //此部分商品是否合格，用来判断对应的记录
            finishSQL.setType(F.getType());

            return finishSQL;
        }).collect(Collectors.toList());

        //修改主表状态，明细表状态和库位
        PurchaseInStatusEnum InStatus = finishDto.getStatus();

        puchaseInMapper.update(finishDto.getOrderNo(),InStatus);

        puchaseInDetailMapper.finish(finishSQLS);


        //操作库存表，库存明细表

        //入库（写入stock并更新stockflow）
        //stock
        System.out.println("入库操作");
        //获取所在仓库id

        Long warehouseId = puchaseInMapper.getWarehouseId(orderId);
        Set<Stock> stocks =  skuids.stream().map(T->{
            Stock stock = new Stock();
            Integer total,lockstock;

            total = puchaseInDetailMapper.getTotalByQcresult(orderId, T, QcResult.FAIL);
            lockstock = puchaseInDetailMapper.getTotalByQcresult(orderId, T, QcResult.OUT);

            if(total == null) {

                total = puchaseInDetailMapper.getTotalByQcresult(orderId, T, QcResult.PASS);
                Integer available = puchaseInDetailMapper.getFailTotal(orderId, T);


                if (total == null) {

                    System.out.println("合格率<70%");

                    total = puchaseInDetailMapper.getTotalByQcresult(orderId, T, QcResult.OUT);
                    lockstock = total;
                    stock.setAvailableStock(0);

                }else {

                    System.out.println("合格率>95%");

                    lockstock = total - available;
                    stock.setAvailableStock(available);
                }
            }else{

                System.out.println("70%<合格率<95%");

                stock.setAvailableStock(total-lockstock);
            }

            stock.setWarehouseId(warehouseId);
            stock.setSkuId(T);
            stock.setTotalStock(total);
            stock.setLockedStock(lockstock);



            return stock;
        }).collect(Collectors.toSet());

        System.out.println("stocks:"+stocks.toString());

        System.out.println("操作明细表");

        List<StockFlow> stockFlows = stocks.stream().map(T->{
            StockFlow stockFlow = new StockFlow();

            //获取修改前商品库存数据
            Map<String,Object> map = stockMapper.getTotal(warehouseId,T.getSkuId());

            if (map == null){
                map = new HashMap<>();
                map.put("total",0);
                map.put("available",0);
            }

            System.out.println(map.toString());

            //获取新入库数量
            Integer total = T.getTotalStock();
            Integer newavailavle = T.getAvailableStock();

            System.out.println("获取属性值");

            stockFlow.setSkuId(T.getSkuId());
            stockFlow.setWarehouseId(warehouseId);
            stockFlow.setBeforeTotal((Integer) map.get("total"));
            stockFlow.setBeforeAvailable((Integer)map.get("available"));

            System.out.println(stockFlow.getBeforeAvailable());

            stockFlow.setOrderNo(finishDto.getOrderNo());
            stockFlow.setAfterTotal(total+(Integer)map.get("total"));
            stockFlow.setAfterAvailable(newavailavle+(Integer)map.get("available"));
            stockFlow.setChangeType(StockFlowChangeTypeEnum.PURCHASE_IN);
            stockFlow.setChangeQuantity(total);

            return stockFlow;
        }).collect(Collectors.toList());

        stockMapper.addOrUpdate(stocks);

        stockFlowMapper.add(stockFlows);

    //库存库位表
        List<PurchaseInDetail> detail = puchaseInDetailMapper.getDetail(orderId);

        List<StockLocation> stockLocations = detail.stream().map(T->{
            StockLocation stockLocation = new StockLocation();

            if(T.getStatus()==PurchaseInDeatilStatusEnum.WAITPUTOUT){
                stockLocation.setStatus(StockLocationstatus.UNAVAILABLE);
                stockLocation.setAmount(T.getActualQuantity());
            }else {
                stockLocation.setStatus(StockLocationstatus.AVAILABLE);
                stockLocation.setAmount(T.getQualifiedQuantity());
            }

            stockLocation.setWarehouseId(warehouseId);
            stockLocation.setSkuId(T.getSkuId());
            stockLocation.setLocationId(T.getLocationId());

            return stockLocation;

        }).collect(Collectors.toList());

        stockLocationMapper.addorupdate(stockLocations);

    }
}
