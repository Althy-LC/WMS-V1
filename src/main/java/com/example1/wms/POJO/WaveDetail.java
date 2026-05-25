package com.example1.wms.POJO;

import com.example1.wms.DTO.InspectDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/22 17:58
 * @Description
 */
@Data
public class WaveDetail {
    private Long id;
    private Long waveId;
    private Long orderId;
    private Long skuId;
    private Integer quantity;
    private Long locationId;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
