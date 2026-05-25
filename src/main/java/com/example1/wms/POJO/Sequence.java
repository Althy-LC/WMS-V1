package com.example1.wms.POJO;

/**
 * @author Althy
 * @Create 2026/5/7 22:58
 * @Description
 */

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Sequence {
    private String seqName;
    private Long  currentVal;
    private Integer step;
    private LocalDateTime updateTime;
}
