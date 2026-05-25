package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.InspectType;
import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/12 17:10
 * @Description 质检状态
 */
@Data
public class InspectDto {
    @NotBlank(message = "入库单号不得为空")
    private String orderNo;
    @JsonIgnore
    private PurchaseInStatusEnum status;

    private List<I> data;

    @Data
    public static class I {
        @NotBlank(message = "商品编码不得为空")
        private String skuCode;

        @JsonIgnore
        private InspectType type;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String desc;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "合格数量不得为空")
        private Integer qualifiedQuantity;
        private Integer unqualifiedQuabtity;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "请提供质检员")
        private String inspector;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String unqualifiedReson;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String LocationCode;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Integer amount;
    }
}
