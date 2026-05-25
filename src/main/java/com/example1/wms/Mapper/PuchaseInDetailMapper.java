package com.example1.wms.Mapper;

import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import com.example1.wms.DTO.Enum.QcResult;
import com.example1.wms.DTO.UtilDto.FinishSQL;
import com.example1.wms.POJO.PurchaseInDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Althy
 * @Create 2026/5/7 22:46
 * @Description
 */
@Mapper
public interface PuchaseInDetailMapper {
    void add(List<PurchaseInDetail> purchaseInDetail);

    Long getSkuid(String skuCode);

    int put(List<PurchaseInDetail> purchaseInDetails);

    Long getlocationid(Long orderId);

    List<Map<Long, Object>> getsku();

    List<Map<String, Object>> getlocation();

    List<PurchaseInDetail> getDetail(Long orderId);

    int recive(PurchaseInDeatilStatusEnum detailstatus, Long locationId);

    List<Long> getlocationids(Long orderId);

    Double getDetailByidAndCode(Long id, Long skuId);

    Integer getTotalBystatus(Long orderId, Long skuId, PurchaseInDeatilStatusEnum status);

    void finish(List<FinishSQL> finishSQLS);

    void addFail(List<PurchaseInDetail> newpurchaseInDetails);

    Integer getFailTotal(Long orderId, Long skuId);

    Integer getTotalByQcresult(Long orderId, Long skuId, QcResult qcResult);
}
