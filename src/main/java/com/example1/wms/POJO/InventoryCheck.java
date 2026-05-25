package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.InventoryCheckStatusEnum;
import com.example1.wms.DTO.InspectDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/14 18:58
 * @Description
 */
@Data
public class InventoryCheck {
    private Long id;
    private String orderNo;
    private Long warehouseId;
    private String createUser;
    private InventoryCheckStatusEnum status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
