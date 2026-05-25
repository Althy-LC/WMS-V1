package com.example1.wms.POJO;

import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/4/29 00:18
 * @Description 仓库表
 */
@Data
public class House {
    private Long id;
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
