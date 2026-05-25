package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 18:05
 * @Description
 */
@Data
public class SaleOutDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderNo;
    private String sourceOrderNo;
    private String warehouseCode;
    private String customer;
    @JsonIgnore
    private SaleStatus status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String desc;

    private List<Detail> detail;

    @Data
    public static class Detail{
        private String skuCode;
        private Integer quantity;
    }
}
