package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

/**
 * @author Althy
 * @Create 2026/5/22 19:39
 * @Description
 */
@Data
public class OutboundDto {
    @NotBlank(message = "单号不得为空")
private String orderNo;
    @NotNull(message = "审核状态不得为空")
private Boolean type ;
@JsonIgnore
private SaleStatus status;
}
