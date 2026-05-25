package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import com.example1.wms.DTO.Enum.QcResult;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:40
 * @Description
 */
@Data
public class PurchaseInDetailDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderNo;
    @NotBlank(message = "商品编码不得为空")
    private String skuCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String skuName;
    @NotNull(message = "计划收货总数不得为空")
    private Integer planQuantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer actualQuantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer qualifiedQuantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String locationCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String inspector;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private QcResult qcResult;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String qcdesc;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String unqualifiedReson;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PurchaseInDeatilStatusEnum status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String desc;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;
}
