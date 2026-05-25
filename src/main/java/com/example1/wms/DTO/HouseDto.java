package com.example1.wms.DTO;

import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
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
 * @Create 2026/4/29 00:26
 * @Description 仓库表DTO
 */
@Data
public class HouseDto {

    private String warehouseCode;

    @NotBlank(message = "仓库名不可为空",groups = AddGroups.class)
    @Length(min = 0,max = 32,message = "名称长度在0-32之间",groups = {UpdateGroups.class, AddGroups.class})
    private String warehouseName;

    @NotBlank(message = "地址不可为空",groups = AddGroups.class)
    private String address;

    @Min(value = 1,message = "状态不可小于1",groups = UpdateGroups.class)
    @Max(value = 1,message = "状态不可大于1",groups = UpdateGroups.class)
    private Integer status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;
}
