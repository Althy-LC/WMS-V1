package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Althy
 * @Create 2026/5/12 18:09
 * @Description
 */
@Data
public class ReciveDto {
    @NotBlank(message = "采购入库单号不得为空")
    private String orderNo;
    @NotBlank(message = "库位不得为空")
    private String locationCode;
    @JsonIgnore
    private PurchaseInStatusEnum instatus;
    @JsonIgnore
    private PurchaseInDeatilStatusEnum detailstatus;
}
