package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.StockFlowChangeTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:47
 * @Description
 */
@Data
public class StockFlow {
private Long id;
private Long skuId;
private Long warehouseId;
//变动类型
private StockFlowChangeTypeEnum changeType;
//变动数量
private Integer changeQuantity;
//变动前库存
private Integer beforeTotal;
//变动后库存
private Integer afterTotal;
//变动前可用库存
private Integer beforeAvailable;
//变动后可用库存
private Integer afterAvailable;
private String orderNo;
private LocalDateTime createTime;
private LocalDateTime updateTime;
}
