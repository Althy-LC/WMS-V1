package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/7 18:30
 * @Description
 */
@Data
public class PurchaseInDto {
    private String orderNo;
    @NotBlank(message = "仓库编码不得为空")
    private String warehouseCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String warehouseName;
    @NotBlank(message = "供应商不得为空")
    private String supplier;
    @NotNull(message = "计划入库总数不得为空")
    @Min(value = 1,message = "入库总数不得小于1")
    private Integer totalQuantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PurchaseInStatusEnum status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String statusdesc;

    @NotBlank(message = "创建者不得为空")
    private String createUser;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<PurchaseInDetailDto> Detail;
}
