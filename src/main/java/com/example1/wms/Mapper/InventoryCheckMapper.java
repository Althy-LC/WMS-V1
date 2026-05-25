package com.example1.wms.Mapper;

import com.example1.wms.DTO.Enum.InventoryCheckStatusEnum;
import com.example1.wms.POJO.InventoryCheck;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Althy
 * @Create 2026/5/15 13:51
 * @Description
 */
@Mapper
public interface InventoryCheckMapper {
    void add(InventoryCheck inventoryCheck);

    Long getid(String orderNo);

    void update(String orderNo, InventoryCheckStatusEnum status);

    InventoryCheck getAllByorderNo( String orderNo);
}
