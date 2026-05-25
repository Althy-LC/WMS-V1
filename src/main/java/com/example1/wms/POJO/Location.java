package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.LocationStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/4/30 12:57
 * @Description
 */
@Data
public class Location {
    private Long id;
    private String locationCode;
    private Long areaId;
    /*
     *locationType
     *  1.普通货架
     *  2.地堆
     *  3.流利架
     */
    private Integer locationType;
    /**
     * status
     * 0.禁用
     * 1.空闲
     * 2.占用
     * 3.锁定
     */
    private LocationStatusEnum status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}