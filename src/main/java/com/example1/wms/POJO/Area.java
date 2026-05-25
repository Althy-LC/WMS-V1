package com.example1.wms.POJO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/4/30 08:37
 * @Description 库区表实体类
 */
@Data
public class Area {
    private Long id;
    private String areaCode;
    private String areaName;
    private Long warehouseId;
    /**
     * 库区类型
     *  1.收货区
     *  2.存储区
     *  3.拣货区
     *  4.复核区
     */
    private Integer areaType;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
