package com.example1.wms.POJO;

import com.example1.wms.DTO.InspectDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/22 17:43
 * @Description
 */
@Data
public class SaleOutDetail {
    private Long id;
    private Long orderId;
    private Long skuId;
    private Integer quantity;
    private Integer pickedQuantity;
    private Long locationId;
    private LocalDateTime createTime;
}
