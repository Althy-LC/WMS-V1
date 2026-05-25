package com.example1.wms.Service;

import com.example1.wms.DTO.AuditOrder;
import com.example1.wms.DTO.OutboundDto;
import com.example1.wms.DTO.SaleOutDto;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 18:01
 * @Description
 */
public interface SaleService {
    String create(SaleOutDto saleOutDto);

    List<AuditOrder> getAudit();

    void audit(OutboundDto outboundDto);

    void pick(String waveNo);

    void review(String orderNo);

    void outbound(String orderNo);
}
