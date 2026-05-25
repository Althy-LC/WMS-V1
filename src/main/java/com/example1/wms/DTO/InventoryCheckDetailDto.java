package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.AdjustStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/14 19:30
 * @Description
 */
@Data
public class InventoryCheckDetailDto {
    @NotBlank(message = "盘点单号不得为空")
    private String orderNo;
    @NotBlank(message = "商品编码不得为空")
    private String skuCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String skuName;
    @NotBlank(message = "库位编码不得为空")
    private String locationCode;
    @NotNull(message = "实盘数量不得为空")
    private Integer actualQuantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updataTime;
}
