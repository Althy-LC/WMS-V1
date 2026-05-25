package com.example1.wms.Controller;

import com.example1.wms.DTO.Enum.AdjustStatusEnum;
import com.example1.wms.DTO.Enum.InventoryCheckStatusEnum;
import com.example1.wms.DTO.InventoryCheckDetailDto;
import com.example1.wms.DTO.InventoryCheckDto;
import com.example1.wms.DTO.ProcessDto;
import com.example1.wms.Result;
import com.example1.wms.Service.InventoryCheckService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/15 13:23
 * @Description
 */
@RestController
@RequestMapping("/InventoryCheck")
public class InventoryCheckController {
@Autowired
    InventoryCheckService inventoryCheckService;

    //新建盘点
    @PostMapping("/Create")
    public Result<?> add(@Valid @RequestBody InventoryCheckDto inventoryCheckDto){
        inventoryCheckDto.setStatus(InventoryCheckStatusEnum.PENDING);
          inventoryCheckService.add(inventoryCheckDto);
        return Result.success();
    }

    //开始盘点
    @PutMapping("/Start/{orderNo}")
    public Result<?> start(@PathVariable String orderNo){
        List<InventoryCheckDetailDto> detailDtos = inventoryCheckService.start(orderNo);
        return Result.success(detailDtos);
    }

    //盘点完成
    @PutMapping("/Ending")
    public Result<?> ending(@Valid @RequestBody List<InventoryCheckDetailDto> inventoryCheckDetailDtos){
        inventoryCheckService.end(inventoryCheckDetailDtos);
        return Result.success();
    }

    //差异处理完毕
    @PutMapping("/Process")
    public Result<?> process(@RequestBody ProcessDto processes){
        processes.setCheckStatus(InventoryCheckStatusEnum.CLOSED);
        processes.setDetailStatus(AdjustStatusEnum.ADJEST);

        inventoryCheckService.process(processes);
        return Result.success();
    }
}
