package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.AdjustStatusEnum;
import com.example1.wms.DTO.Enum.InventoryCheckStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/19 13:41
 * @Description
 */
@Data
public class ProcessDto {
    @JsonIgnore
    private AdjustStatusEnum detailStatus;
    @JsonIgnore
    private InventoryCheckStatusEnum checkStatus;
    @NotBlank(message = "盘点单编码不得为空")
    private String orderNo;
    @NotEmpty(message = "库位id不得为空")
    private List<String> locationCodes;

}
