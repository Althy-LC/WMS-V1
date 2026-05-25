package com.example1.wms.DTO.UtilDto;

import com.example1.wms.DTO.Enum.InspectType;
import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import lombok.Data;

/**
 * @author Althy
 * @Create 2026/5/13 21:53
 * @Description
 */
@Data
public class FinishSQL {
    private Long orderId;
    private Long skuId;
    private InspectType type;
    private PurchaseInDeatilStatusEnum detailStatus;
}
