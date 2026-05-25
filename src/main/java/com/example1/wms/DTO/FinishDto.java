package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.InspectType;
import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/12 17:16
 * @Description
 */
@Data
public class FinishDto {
    @NotBlank(message = "入库单号不得为空")
    private String orderNo;
    @JsonIgnore
    private PurchaseInStatusEnum status;
    private List<F> data;

    @Data
  static public class F {
        @NotBlank(message = "商品编码不得为空")
        private String skuCode;
        @JsonIgnore
        private String locationCode;
        private InspectType type;
        @JsonIgnore
        private PurchaseInDeatilStatusEnum detailstatus;
    }
}
