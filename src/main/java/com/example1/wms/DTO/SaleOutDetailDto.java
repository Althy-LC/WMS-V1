package com.example1.wms.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/22 18:20
 * @Description
 */
@Data
public class SaleOutDetailDto {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String orderNo;
    private String skuCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String skuName;
    private Integer quantity;
    private Integer pickedQuantity;
    private String locationCode;
}
