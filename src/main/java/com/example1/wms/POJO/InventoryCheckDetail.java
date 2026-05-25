package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.AdjustStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/14 19:23
 * @Description
 */

@Data
public class InventoryCheckDetail {
    private Long id;
    private Long orderId;
    private Long skuId;
    private Long locationId;
    private Integer bookQuantity;
    private Integer actualQuantity;
    private Integer difference;
    private AdjustStatusEnum adjustStatus;
    private LocalDateTime createTime;
    private LocalDateTime updataTime;
}
