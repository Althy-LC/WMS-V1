package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.StockFlowChangeTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:51
 * @Description
 */
@Data
public class StockFlowDto {
    private Long skuId;
    private Long warehouseId;
    private StockFlowChangeTypeEnum changeType;
    private Integer changeQuantity;
    private Integer beforeTotal;
    private Integer afterTotal;
    private Integer beforeAvailable;
    private Integer afterAvailable;
    private String orderNo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
