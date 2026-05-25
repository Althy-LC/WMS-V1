package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.Wavestatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/22 17:50
 * @Description
 */
@Data
public class Wave {
    private Long id;
    private String waveNo;
    private Long warehouseId;
    private Wavestatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
