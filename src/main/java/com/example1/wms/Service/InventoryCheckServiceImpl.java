package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.DTO.Enum.AdjustStatusEnum;
import com.example1.wms.DTO.Enum.InventoryCheckStatusEnum;
import com.example1.wms.DTO.Enum.LocationStatusEnum;
import com.example1.wms.DTO.Enum.StockFlowChangeTypeEnum;
import com.example1.wms.DTO.InventoryCheckDetailDto;
import com.example1.wms.DTO.ProcessDto;
import com.example1.wms.DTO.UtilDto.PathSortDto;
import com.example1.wms.Mapper.*;
import com.example1.wms.POJO.*;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.DTO.InventoryCheckDto;
import com.example1.wms.Util.SPathSortUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Althy
 * @Create 2026/5/15 13:50
 * @Description
 */
@Service
public class InventoryCheckServiceImpl implements  InventoryCheckService{
 @Autowired
    InventoryCheckMapper inventoryCheckMapper;
 @Autowired
    SequenceService sequenceService;
 @Autowired
     HouseMapper houseMapper;
    @Autowired
    private StockLocationMapper stockLocationMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private InventoryCheckDetailMapper inventoryCheckDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockFlowMapper stockFlowMapper;


    //新建盘点单
    @Override
    @Transactional
    public void add(InventoryCheckDto inventoryCheckDto) {
        //获取每月刷新序列号
        Long seq = sequenceService.nextCodeByymd("InventoryCheck");
        CodeBulider codeBulider = new CodeBulider();
        //获取编码
        String orderNo = codeBulider.getnewInventoryCheckCode(seq);

        InventoryCheck inventoryCheck = new InventoryCheck();
        //获取仓库id
        Long warehouseId = houseMapper.getid(inventoryCheckDto.getWarehouseCode());
            if(warehouseId == null){
                throw new BuniessException("待盘点仓库不存在");
            }

        //将前端数据复制到 实体类对象
        BeanUtils.copyProperties(inventoryCheckDto,inventoryCheck);
          inventoryCheck.setWarehouseId(warehouseId);
          inventoryCheck.setOrderNo(orderNo);

          //写入盘点表，（待盘点状态）
          inventoryCheckMapper.add(inventoryCheck);

        //操作盘点明细表

            //创建库存-库位表实例对象
        List<StockLocation> stockLocations  = stockLocationMapper.select(warehouseId);
            //stream流遍历获取库位集合
            List<Long> locationids = stockLocations.stream().map(T->{
                return T.getLocationId();
            }).toList();


            //获取库位编码和所在库区类型
            List<PathSortDto> PathSorts = locationMapper.getCodeAndAreatype(locationids);

              //调用S路径工具类进行盘点路径优化
               Map<Long,Integer> map = SPathSortUtil.inventory(PathSorts);

               List<InventoryCheckDetail> inventoryCheckDetails = new ArrayList<>();

              for(int i = 0 ; i<stockLocations.size();i++) {
                 int sortindex = map.get(stockLocations.get(i).getLocationId());

                 InventoryCheckDetail Detail = new InventoryCheckDetail();
                 //写入数据
                  Detail.setOrderId(inventoryCheckMapper.getid(orderNo));
                  Detail.setLocationId(stockLocations.get(i).getLocationId());
                  Detail.setSkuId(stockLocations.get(i).getSkuId());
                  Detail.setBookQuantity(stockLocations.get(i).getAmount());

                  while (inventoryCheckDetails.size()<sortindex){
                      inventoryCheckDetails.add(null);
                  }
                  //按照s路径优化后的顺序存储
                  inventoryCheckDetails.add(sortindex,Detail);
                  //去掉add方法自动填充的null
                  inventoryCheckDetails.removeIf(Objects::isNull);

              }

                inventoryCheckDetailMapper.add(inventoryCheckDetails);

    }


    //开始盘点
    @Override
    @Transactional
    public List<InventoryCheckDetailDto> start(String orderNo) {
       //修改判断主表状态为盘点中
        Long orderId = inventoryCheckMapper.getid(orderNo);
        inventoryCheckMapper.update(orderNo, InventoryCheckStatusEnum.COUNTING);

        //获取盘点S型路径
         List<InventoryCheckDetail> inventoryCheckDetails = inventoryCheckDetailMapper.getdetail(orderId);
              List<InventoryCheckDetailDto> detailDtos =  inventoryCheckDetails.stream().map(T->{

                  InventoryCheckDetailDto detailDto = new InventoryCheckDetailDto();


                    String code = locationMapper.getCode(T.getLocationId());
                    SKU sku = skuMapper.getCodeAndName(T.getSkuId());

                    BeanUtils.copyProperties(T,detailDto);

                    detailDto.setSkuName(sku.getSkuName());
                    detailDto.setSkuCode(sku.getSkuCode());
                    detailDto.setLocationCode(code);

                    return detailDto;

              }).collect(Collectors.toList());
            detailDtos.removeIf(Objects::isNull);
              return detailDtos;
    }

    //盘点完成
    @Override
    @Transactional
    public void end(@Valid List<InventoryCheckDetailDto> inventoryCheckDetailDtos) {

        Integer difference = null;

        List<InventoryCheckDetail> inventoryCheckDetails = new ArrayList<>();
        Long orderId = 0L;
        String orderNo = null;
        InventoryCheckStatusEnum status = null;
        List<Long> locationIds = new ArrayList<>();

        for (int i=0;i<inventoryCheckDetailDtos.size();i++){

            InventoryCheckDetail inventoryCheckDetail = new InventoryCheckDetail();


            //获取商品在库信息
            orderNo = inventoryCheckDetailDtos.get(i).getOrderNo();
            orderId = inventoryCheckMapper.getid(orderNo);
            Long locationId = locationMapper.getidByCode(inventoryCheckDetailDtos.get(i).getLocationCode());
            Long skuId = skuMapper.getid(inventoryCheckDetailDtos.get(i).getSkuCode());
            //获取账面数量
            Integer book = inventoryCheckDetailMapper.getbook(orderId, locationId, skuId);
            //计算差异
            difference = book - inventoryCheckDetailDtos.get(i).getActualQuantity();
            //记录修改盘点单状态
                if(difference ==0) {
                    inventoryCheckDetail.setAdjustStatus(AdjustStatusEnum.ADJEST);
                    status = InventoryCheckStatusEnum.CLOSED;
                }else {
                    inventoryCheckDetail.setAdjustStatus(AdjustStatusEnum.UNADJEST);
                    status = InventoryCheckStatusEnum.PENDING_DEAL;
                    locationIds.add(locationId);
                }

                //
                inventoryCheckDetail.setOrderId(orderId);
                inventoryCheckDetail.setSkuId(skuId);
                inventoryCheckDetail.setLocationId(locationId);
                inventoryCheckDetail.setBookQuantity(book);
                inventoryCheckDetail.setActualQuantity(inventoryCheckDetailDtos.get(i).getActualQuantity());
                inventoryCheckDetail.setDifference(difference);

                //
                inventoryCheckDetails.add(inventoryCheckDetail);
        }

            //修改盘点状态
             inventoryCheckMapper.update(orderNo,status);

            //修改detail
            int result = inventoryCheckDetailMapper.update(inventoryCheckDetails);

         //   if (result != inventoryCheckDetailDtos.size()){
       //         throw new BuniessException("盘点明细修改失败");
        //    }

            //锁定盘点盈亏库位
            if(locationIds != null){
               result = locationMapper.putStatus(locationIds, LocationStatusEnum.Locked);
               if (result != locationIds.size()){
                   throw new BuniessException("库位锁定失败");
               }
            }
    }


    //确认盘点差异后解锁商品修改库存
    @Transactional
    @Override
    public void process(ProcessDto processes) {

        //获取盘点单
        InventoryCheck inventoryCheck = inventoryCheckMapper.getAllByorderNo(processes.getOrderNo());
        if (inventoryCheck == null) {
            throw new RuntimeException("盘点单不存在");
        }

        List<StockLocation> stockLocations = new ArrayList<>();
        List<Long> locationIds = new ArrayList<>();


        List<StockFlow> stockFlows = new ArrayList<>();

        //获取盘点明细数据
        Long orderId = inventoryCheckMapper.getid(processes.getOrderNo());
        List<InventoryCheckDetail> inventoryCheckDetails = inventoryCheckDetailMapper.getdetailByAdjust(orderId);

        //获取库存及明细表数
            //初始化中间变量
        Map<Long,Stock> stocks = new HashMap<>();
        Stock stock = new Stock();

        Long warehouseId = inventoryCheck.getWarehouseId();

        for (int i = 0 ; i<inventoryCheckDetails.size() ;i++){
            InventoryCheckDetail T = inventoryCheckDetails.get(i);

            Integer area_type = locationMapper.getAreatype(T.getLocationId());


            StockFlow stockFlow = new StockFlow();
            StockFlowChangeTypeEnum type = null;


            //获取库位id及库存库位数据
                locationIds.add(T.getLocationId());

                StockLocation stockLocation = new StockLocation();
                    stockLocation.setAmount(T.getActualQuantity());
                    stockLocation.setWarehouseId(warehouseId);
                    stockLocation.setLocationId(T.getLocationId());
                    stockLocations.add(stockLocation);

            //获取库存信息
                    //避免重复查库
                    if(stocks.get(T.getSkuId())==null){
                        Map<Long,Stock> map = stockMapper.getStock(warehouseId,T.getSkuId());
                        stocks.put(T.getSkuId(),map.get(T.getSkuId()));
                    }

                    //取出stocks中数据
                       stock = stocks.get(T.getSkuId());
                       Integer beforetotal = stock.getTotalStock(),
                               beforeavilable = stock.getAvailableStock(),
                               beforelocked =stock.getLockedStock(),
                               aftertotal = 0,
                               afteravilable = 0,
                               afterlocked =0;


             switch (area_type){
                 case 3:    //存储区库位出现盘点盈亏
                        if(T.getActualQuantity()>T.getBookQuantity()){
                            //盘点盈
                             aftertotal = beforetotal+T.getDifference();
                             afteravilable = beforeavilable+T.getDifference();
                             type = StockFlowChangeTypeEnum.INVENTORY_SURPLUS;
                        }else{
                            //盘点亏
                            aftertotal = beforetotal - T.getDifference();
                            afteravilable = beforeavilable - T.getDifference();
                            type = StockFlowChangeTypeEnum.INVENTORY_LOSS;
                        }
                        break;
                 case 4:    //退货区库位出现盘点盈亏
                     if(T.getActualQuantity()>T.getBookQuantity()){
                         //盘点盈
                         aftertotal = beforetotal+T.getDifference();
                         afterlocked = beforelocked + T.getDifference();
                         type = StockFlowChangeTypeEnum.INVENTORY_SURPLUS;
                     }else{
                         //盘点亏
                         aftertotal = beforetotal - T.getDifference();
                         afterlocked = beforelocked - T.getDifference();
                         type = StockFlowChangeTypeEnum.INVENTORY_LOSS;
                     }
                     break;
             }
             //更新后的数据写回stocks
             stock.setTotalStock(aftertotal);
             stock.setAvailableStock(afteravilable);
             stock.setLockedStock(afterlocked);
             stock.setWarehouseId(warehouseId);
             stock.setSkuId(T.getSkuId());

            //写入库存表操作列表
             stocks.put(stock.getSkuId(),stock);

            //为明细表载入数据
             stockFlow.setBeforeTotal(beforetotal);
             stockFlow.setAfterTotal(aftertotal);
             stockFlow.setBeforeAvailable(beforeavilable);
             stockFlow.setAfterAvailable(afteravilable);
             stockFlow.setOrderNo(processes.getOrderNo());
             stockFlow.setSkuId(stock.getSkuId());
             stockFlow.setChangeQuantity(T.getDifference());
             stockFlow.setChangeType(type);
             stockFlow.setWarehouseId(warehouseId);
             stockFlow.setSkuId(T.getSkuId());
                //写入明细表修改列表
                stockFlows.add(stockFlow);
        }
            Set<Stock> stockSet = new HashSet<>(stocks.values());

        //更新库存表及库存明细表
            stockMapper.addOrUpdate(stockSet);
            stockFlowMapper.add(stockFlows);

        //批量更新库位库存
        if (!stockLocations.isEmpty()) {
            stockLocationMapper.updateAmount(stockLocations);
        }

        //解锁库位
            Integer result = locationMapper.useLocations(locationIds);
                if(result != locationIds.size()){
                    throw  new BuniessException("库位解锁失败");
                }
        //更新盘点单状态
        inventoryCheckMapper.update(processes.getOrderNo(), processes.getCheckStatus());

        //更新盘点明细状态
        if (!locationIds.isEmpty()) {
            inventoryCheckDetailMapper.updateAdjust(inventoryCheck.getId(), locationIds, processes.getDetailStatus());
        }
    }

}
