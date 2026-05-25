package com.example1.wms.Service;

import com.example1.wms.DTO.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/7 22:43
 * @Description
 */
public interface PuchaseInService {

    void add(PurchaseInDto purchaseInDto);

    void recive(ReciveDto reciveDto);

    PurchaseInDto get(String orderNo);

    List<PurchaseInDetailDto> getDetail(String orderNo);

    void Audit(AuditDto auditDto);


    void inSpect(InspectDto inspectDto);

    List<InspectDto.I> getlocation(String orderNo);

    void finish(@Valid FinishDto finishDto);
}
