package com.example1.wms.Mapper;

import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.example1.wms.POJO.House;
import com.example1.wms.POJO.PurchaseIn;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Althy
 * @Create 2026/5/7 22:44
 * @Description
 */
@Mapper
public interface PuchaseInMapper {
    void add(PurchaseIn purchaseIn);

    Long selecthouse( String warehouseCode);

    Long getid(String orderNo);

    int update(String orderNo, PurchaseInStatusEnum status);

    PurchaseIn get(String orderNo);

    House selecthousebyid(Long warehouseId);

    Long getWarehouseId(Long orderId);
}
