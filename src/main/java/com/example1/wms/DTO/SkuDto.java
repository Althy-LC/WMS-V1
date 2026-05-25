package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.SkuStatusEnum;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/3 17:59
 * @Description
 */
@Data
public class SkuDto {
    private String skuCode;

    @NotBlank(message = "商品名称不得为空",groups = AddGroups.class)
    private String skuName;

    @NotBlank(message = "商品条码不得为空",groups = AddGroups.class)
    @Length(min = 8,max = 20,message = "barcode非法",groups = {AddGroups.class,UpdateGroups.class})
    private String barcode;

    @NotBlank(message = "商品规格不得为空",groups = AddGroups.class)
    private String spec;

    @NotBlank(message = "商品单位不得为空",groups = AddGroups.class)
    private String unit;

    private SkuStatusEnum status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String statusdesc;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;
}
