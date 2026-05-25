package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/12 17:03
 * @Description
 */
@Data
public class AuditDto {
    @NotBlank(message = "入库单号不得为空")
    private String orderNo;
    @JsonIgnore
    private PurchaseInStatusEnum status;

    private List<A> data;

    @Data
   static public class A {
        @NotBlank(message = "商品编码不得为空")
        private String skuCode;
        @NotNull(message = "实际收货数量不得为空")
        private Integer actualQuantity;
        @NotNull(message = "入库库位不得为空")
        private String locationCode;
    }

}
