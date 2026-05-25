package com.example1.wms.Service;

import com.example1.wms.DTO.InventoryCheckDetailDto;
import com.example1.wms.DTO.InventoryCheckDto;
import com.example1.wms.DTO.ProcessDto;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/15 13:50
 * @Description
 */
public interface InventoryCheckService {
   void add(InventoryCheckDto inventoryCheckDto);

    List<InventoryCheckDetailDto> start(String orderNo);

    void end(List<InventoryCheckDetailDto> inventoryCheckDetailDtos);

    void process(ProcessDto processes);
}
