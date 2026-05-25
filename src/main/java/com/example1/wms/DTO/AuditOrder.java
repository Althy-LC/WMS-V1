package com.example1.wms.DTO;

import lombok.Data;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 18:59
 * @Description
 */
@Data
public class AuditOrder {
    private SaleOutDto sale;
    private List<SaleOutDetailDto> saleOutDetailDtos;
}
