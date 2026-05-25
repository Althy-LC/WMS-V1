package com.example1.wms.DTO;

import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/4/30 08:37
 * @Description 库区Dto
 */
@Data
public class AreaDto {

    private String areaCode;

    @NotBlank(message = "库区名称不可为空",groups = AddGroups.class)
    @Length(min = 0,max = 32,message = "名称长度在0-32之间",groups = {UpdateGroups.class, AddGroups.class})
    private String areaName;

    @JsonIgnore
    private Long warehouseId;
    @NotBlank(message = "所在仓库不可为空",groups = AddGroups.class)
    private String warehouseCode;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String warehouseName;

    @NotNull(message = "库区类型不可为空",groups = AddGroups.class)
    private Integer areaType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String areaTypedesc;

    @Min(value = 1,message = "状态不可小于1",groups = UpdateGroups.class)
    @Max(value = 1,message = "状态不可大于1",groups = UpdateGroups.class)
    private Integer status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;


}
