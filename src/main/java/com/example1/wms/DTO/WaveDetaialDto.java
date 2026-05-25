package com.example1.wms.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/23 00:34
 * @Description
 */
@Data
public class WaveDetaialDto {
    private String waveNo;
    private String orderNo;
    private String skuCode;
    private Integer quantity;
    private String locationCode;
    private Integer sortOrder;
}
