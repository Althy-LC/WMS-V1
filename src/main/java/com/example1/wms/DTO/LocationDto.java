package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.LocationStatusEnum;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/4/30 12:57
 * @Description
 */
@Data
public class LocationDto {

    private String locationCode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "所在货架号不可为空",groups = AddGroups.class)
    @Min(value = 1,message = "所在货架号非法",groups = AddGroups.class)
    private Integer Shelf;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "所在层不可为空",groups = AddGroups.class)
    @Min(value = 1,message = "所在层非法",groups = AddGroups.class)
    private Integer layer;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "所在位号不可为空",groups = AddGroups.class)
    @Min(value = 1, message ="所在位号非法",groups = AddGroups.class )
    private Integer bin;

    @JsonIgnore
    private Long areaId;
    @NotBlank(message = "库区编码不得为空",groups = AddGroups.class)
    private String areaCode;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String areaName;

    @NotNull(message = "库位类型不得为空",groups = AddGroups.class)
    private Integer locationType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String locationTypedesc;

    private LocationStatusEnum status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String statusdesc;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;


}
