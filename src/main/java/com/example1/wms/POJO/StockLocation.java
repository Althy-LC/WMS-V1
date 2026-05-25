package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.StockLocationstatus;
import lombok.Data;

/**
 * @author Althy
 * @Create 2026/5/15 19:14
 * @Description
 */
@Data
public class StockLocation {
    private  Long id;
    private Long warehouseId;
    private Long skuId;
    private Long locationId;
    private Integer amount;
    private Integer lockedAmount;
    private StockLocationstatus status;
}
