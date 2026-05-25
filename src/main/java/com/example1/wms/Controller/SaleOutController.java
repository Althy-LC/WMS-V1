package com.example1.wms.Controller;

import com.example1.wms.DTO.AuditOrder;
import com.example1.wms.DTO.Enum.SaleStatus;
import com.example1.wms.DTO.OutboundDto;
import com.example1.wms.DTO.SaleOutDto;
import com.example1.wms.Result;
import com.example1.wms.Service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 18:00
 * @Description
 */
@RestController
@RequestMapping("/Sale")
public class SaleOutController {
   @Autowired
    SaleService saleService;

   //创建销售出库单
   @PostMapping("/create")
    public Result<?> create(@Valid @RequestBody SaleOutDto saleOutDto){
       saleOutDto.setStatus(SaleStatus.CREATE);
      String orderNo = saleService.create(saleOutDto);
       return Result.success(orderNo);
   }

   //获取销售出库审核单
    @GetMapping("/GetAudit")
    public Result<?> getaudit(){
     List<AuditOrder> result = saleService.getAudit();
     return Result.success(result);
    }

   //审核销售出库单
    @PutMapping("/Audit")
    public Result<?> audit(@Valid @RequestBody OutboundDto outboundDto){
       if(outboundDto.getType()){
           outboundDto.setStatus(SaleStatus.AUDIT);
       }else{
           outboundDto.setStatus(SaleStatus.Rejected);
       }
        saleService.audit(outboundDto);
       return Result.success();
    }

    //拣货
    @PutMapping("/Picking/{waveNo}")
    public Result<?> pick(@PathVariable String waveNo){
       saleService.pick(waveNo);
       return  Result.success();
    }

    //复核
    @PutMapping("/Review/{orderNo}")
       public Result<?> review(@PathVariable String orderNo){
        saleService.review(orderNo);
        return Result.success();
    }

    //出库
    @PutMapping("/Outbound/{orderNo}")
    public Result<?> outbound(@PathVariable String orderNo){
        saleService.outbound(orderNo);
        return Result.success();
    }
}
