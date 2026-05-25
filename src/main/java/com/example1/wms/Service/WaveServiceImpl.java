package com.example1.wms.Service;

import com.example1.wms.DTO.Enum.SaleStatus;
import com.example1.wms.DTO.Enum.Wavestatus;
import com.example1.wms.DTO.UtilDto.PathSortDto;
import com.example1.wms.DTO.WaveDetaialDto;
import com.example1.wms.DTO.WaveDto;
import com.example1.wms.Mapper.*;
import com.example1.wms.POJO.*;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.Util.SPathSortUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Althy
 * @Create 2026/5/22 21:00
 * @Description
 */
@Service
public class WaveServiceImpl implements WaveService{
@Autowired
    WaveMapper waveMapper;
@Autowired
    WaveDetailMapper waveDetailMapper;
@Autowired
    SaleMapper saleMapper;
    @Autowired
    private SaleDetailMapper saleDetailMapper;
    @Autowired
    private StockLocationMapper stockLocationMapper;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private LocationMapper locationMapper;


    //创建波次
    @Override
    @Transactional
    public void create() {
        //获取已审核的销售出库记录
        List<SaleOut> sales = saleMapper.getsaleBystatus(SaleStatus.AUDIT);
        //统计同一仓库下合并为一个波次，同一商品尽量从一个库位取出
        //map做分组容器
        Map<Long, List<Long>> map = new HashMap<>();
        List<Long> orderids = new ArrayList<>();

        //按仓库将订单id分组
        for (int i = 0; i < sales.size(); i++) {
            Long warehouseId = sales.get(i).getWarehouseId();
            Long orderId = sales.get(i).getId();

            if (map.get(warehouseId) != null) {
                //存在就取出订单id序列
                orderids = map.get(warehouseId);
            }
            orderids.add(orderId);
            map.put(warehouseId, orderids);
        }

        //获取波次编号

        CodeBulider codeBulider = new CodeBulider();


        List<WaveDetail> waveDetails = new ArrayList<>();
        List<Wave> waves = new ArrayList<>();
        Map<String, List<SaleOutDetail>> map1 = new HashMap<>();
        //创建对应的detail
        for (Long warehouseid : map.keySet()) {
            Wave wave = new Wave();
            //获取波次编号
            Long seq = sequenceService.nextCodeByymd("WAVE");
            String waveNo = codeBulider.getWaveNo(seq);

            wave.setWaveNo(waveNo);
            wave.setWarehouseId(warehouseid);
            wave.setStatus(Wavestatus.CREATE);

            //载入wave列表
            waves.add(wave);

            //更新波次序列表
            sequenceService.update("WAVE", seq);

            //获取detail数据
            orderids = map.get(warehouseid);
            List<SaleOutDetail> saleOutDetails = saleDetailMapper.getByorderIds(orderids);

            map1.put(waveNo, saleOutDetails);

        }

        //写入Wave表
        waveMapper.add(waves);

        //
        for (String waveNo : map1.keySet()) {
            Long waveId = waveMapper.getid(waveNo);

            List<SaleOutDetail> saleOutDetails = map1.get(waveNo);
            List<WaveDetail> details = saleOutDetails.stream().map(T -> {
                    WaveDetail waveDetail = new WaveDetail();
                        waveDetail.setWaveId(waveId);
                        waveDetail.setSkuId(T.getSkuId());
                        waveDetail.setQuantity(T.getQuantity());
                        waveDetail.setOrderId(T.getOrderId());

                        return waveDetail;
            }).toList();

            waveDetails.addAll(waveDetails);
        }

        //创建Detail
            waveDetailMapper.add(waveDetails);
    }

    //获取待审核波次
    @Override
    public List<WaveDto> getAudit() {
        //通过状态获取待审核波次及明细子记录
        List<WaveDto> waveDtos =  new ArrayList<>();

        List<Wave> waves = waveMapper.getByStatus(Wavestatus.CREATE);

          for (Wave wave : waves){
              WaveDto waveDto = new WaveDto();

             List<WaveDetaialDto> waveDetaialDtos = new ArrayList<>();

              List<WaveDetail> waveDetails = waveDetailMapper.getByWaveid(wave.getId());
               for (WaveDetail waveDetail : waveDetails){
                    WaveDetaialDto waveDetaialDto = new WaveDetaialDto();
                    String skuCode = skuMapper.getCode(waveDetail.getSkuId());
                    String orderNo = saleMapper.getorderNo(waveDetail.getOrderId());

                     waveDetaialDto.setQuantity(waveDetail.getQuantity());
                     waveDetaialDto.setOrderNo(orderNo);
                     waveDetaialDto.setSkuCode(skuCode);

                     waveDetaialDtos.add(waveDetaialDto);
               }

               String warehouseCode = houseMapper.getCode(wave.getWarehouseId());

               waveDto.setWaveNo(wave.getWaveNo());
               waveDto.setWarehouseCode(warehouseCode);
               waveDto.setDetails(waveDetaialDtos);
               waveDtos.add(waveDto);
          }

          return  waveDtos;
    }

    //审核
    @Override
    public void audit(List<String> waveNos) {
        Wavestatus status = Wavestatus.AUDIT;
          waveMapper.updateStatus(waveNos,status);

    }

    //获取S型路径优化后的波次
    @Override
    public List<WaveDetaialDto> getSpathofWave(String waveNo) {
      Long waveId = waveMapper.getid(waveNo);
      Long warehouseId = waveMapper.getWarehouse(waveId);

      Wavestatus status = Wavestatus.PICKING;
       waveMapper.putStatus(waveNo,status);

        //业务逻辑库存扣减
        Map<Long,Integer> stocklocationmap = new HashMap<>();

        //map<orderid,map<skuid,locationid>>
        Map<Long,Map<Long,Long>> detaillocationmap = new HashMap<>();

        //为波次商品分配取货库位
        List<WaveDetail> waveDetails = waveDetailMapper.getByWaveid(waveId);
         for (WaveDetail waveDetail : waveDetails){
             Long locationId;
             Integer stock;
             Map<Long,Long> map = new HashMap<>();

             Long orderId = waveDetail.getOrderId();
             Long skuId = waveDetail.getSkuId();

             //获取当前仓库下当前商品所有的库存信息
             List<StockLocation> stockLocations = stockLocationMapper.getByhouseandsku(warehouseId,skuId);
                    //遍历库位库存
                    for (StockLocation st : stockLocations) {
                        locationId = st.getLocationId();
                       stock = stocklocationmap.get(locationId);

                        if(stock >= waveDetail.getQuantity()){
                            stock -= waveDetail.getQuantity();
                            stocklocationmap.put(locationId,stock);

                            map.put(skuId,locationId);
                        }else {
                            continue;
                        }
                    }
                    detaillocationmap.put(orderId,map);
         }

         WaveDetail waveDetail = new WaveDetail();
         List<PathSortDto> path = new ArrayList<>();
         List<SaleOutDetail> saleOutDetails = new ArrayList<>();
         //库位写入销售明细表
            for(Long orderid : detaillocationmap.keySet()){
                PathSortDto pathSortDto = new PathSortDto();
                Map<Long,Long> map = detaillocationmap.get(orderid);
                    for (Long skuid : map.keySet()){
                        Long locationId = map.get(skuid);
                        SaleOutDetail saleOutDetail = new SaleOutDetail();

                            String locationCode = locationMapper.getCode(locationId);
                            pathSortDto.setLocationCode(locationCode);
                            pathSortDto.setAreaType(3);//存储库区
                            pathSortDto.setId(locationId);
                            path.add(pathSortDto);

                            waveDetail.setLocationId(locationId);
                            waveDetail.setSkuId(skuid);
                            waveDetail.setOrderId(orderid);
                            waveDetail.setWaveId(waveId);

                            waveDetails.add(waveDetail);

                            saleOutDetail.setLocationId(locationId);
                            saleOutDetail.setOrderId(orderid);
                            saleOutDetail.setSkuId(skuid);

                            saleOutDetails.add(saleOutDetail);
                    }
            }

            waveDetailMapper.update(waveDetails);
            saleDetailMapper.update(saleOutDetails);

            Map<Long,Integer> Spath = SPathSortUtil.inventory(path);
                for(Long locationid : Spath.keySet()){
                    Integer sort = Spath.get(locationid);
                     waveDetailMapper.updateSort(locationid,sort);
                }

         waveDetails = waveDetailMapper.getByWaveid(waveId);
                List<WaveDetaialDto> waveDetaialDtos = waveDetails.stream().map(T->{
                    WaveDetaialDto waveDetaialDto = new WaveDetaialDto();

                    String skuCode = skuMapper.getCode(T.getSkuId());
                    String orderNo = saleMapper.getorderNo(T.getOrderId());
                    String locationCode = locationMapper.getCode(T.getLocationId());

                    BeanUtils.copyProperties(T,waveDetaialDto);
                    waveDetaialDto.setLocationCode(locationCode);
                    waveDetaialDto.setWaveNo(waveNo);
                    waveDetaialDto.setOrderNo(orderNo);
                    waveDetaialDto.setSkuCode(skuCode);

                    return waveDetaialDto;
                }).toList();

                return waveDetaialDtos;
    }
}
