package com.example1.wms.POJO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:43
 * @Description
 */
@Data
public class Stock {
    private Long id;
    private Long warehouseId;
    private Long skuId;
    //总库存
    private Integer totalStock;
    private Integer saleLocked;
    //可用库存
    private Integer availableStock;
    //锁定库存
    private Integer lockedStock;
    //乐观锁版本
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
