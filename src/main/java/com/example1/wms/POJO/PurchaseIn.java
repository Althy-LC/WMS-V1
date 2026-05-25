package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:17
 * @Description 采购入库表实体类
 */
@Data
public class PurchaseIn {
    private Long id;
    //入库单号
    private String orderNo;
    private Long warehouseId;
    //供应商
    private String supplier;
    //计划入库总数
    private Integer totalQuantity;
    private PurchaseInStatusEnum status;
    private String createUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
