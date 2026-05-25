package com.example1.wms.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:46
 * @Description
 */
@Data
public class StockDto {
    private Long warehouseId;
    private Long skuId;
    private Integer totalStock;
    private Integer availableStock;
    private Integer lockedStock;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
