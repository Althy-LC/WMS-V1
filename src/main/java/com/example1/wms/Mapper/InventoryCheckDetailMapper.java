package com.example1.wms.Mapper;


import com.example1.wms.DTO.Enum.AdjustStatusEnum;
import com.example1.wms.POJO.InventoryCheckDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/16 00:48
 * @Description
 */
@Mapper
public interface InventoryCheckDetailMapper {
    void add(List<InventoryCheckDetail> inventoryCheckDetails);


    List<InventoryCheckDetail> getdetail(Long orderId);

    Integer getbook(Long orderId, Long locationId, Long skuId);

    int update(List<InventoryCheckDetail> inventoryCheckDetails);

    void updateAdjust(Long orderId, @Param("list") List<Long> locationIds, AdjustStatusEnum status);

    List<InventoryCheckDetail> getdetailByAdjust(Long orderId);
}
