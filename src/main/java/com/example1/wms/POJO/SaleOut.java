package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.SaleStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/22 17:34
 * @Description
 */
@Data
public class SaleOut {
    private Long id;
    private String orderNo;
    private String sourceOrderNo;
    private Long warehouseId;
    private String customer;
    private SaleStatus status;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
