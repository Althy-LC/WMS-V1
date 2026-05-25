package com.example1.wms.Controller;

import com.example1.wms.DTO.*;
import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.example1.wms.Result;
import com.example1.wms.Service.PuchaseInService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/7 21:17
 * @Description
 */
@RestController
@RequestMapping("/Purchase")
public class PuchaseInController {
 @Autowired
    PuchaseInService puchaseInService;

    //新建采购入库单
    @PostMapping
    public Result<?> add (@Valid @RequestBody PurchaseInDto purchaseInDto){

        purchaseInDto.setStatus(PurchaseInStatusEnum.CREATE);
        puchaseInService.add(purchaseInDto);
        return Result.success();
    }

    //收货
    //放入收货库区，状态流转为待审核
    @PutMapping("/Recive")
    public Result<?> recive (@Valid @RequestBody ReciveDto reciveDto){
        reciveDto.setInstatus(PurchaseInStatusEnum.AUDIT);
        reciveDto.setDetailstatus(PurchaseInDeatilStatusEnum.REVEIVED);
        puchaseInService.recive(reciveDto);
        return Result.success();
    }

    //审核
    //放入质检库区，状态流转为待质检
    @PutMapping("/Audit")
    public Result<?> audit(@Valid @RequestBody AuditDto auditDto){

        auditDto.setStatus(PurchaseInStatusEnum.QUALITY_INSPECT);
        puchaseInService.Audit(auditDto);
        return Result.success();
    }

    //质检
    //质检结果，状态改为待上架，不移动库区
    //推荐可用库位（基于合格率进行情况考量）
    /**
     * 合格率>95% 前端必须存储 质检不通过原因
     * 70%<合格率<95% 合格->入存储库区 不合格->退货库区
     * 合格率<70% 直接全部进退货库区
     */
    @PutMapping("/Inspect")
    public Result<?> inspect(@Valid @RequestBody InspectDto inspectDto){
        inspectDto.setStatus(PurchaseInStatusEnum.SHELF);
        puchaseInService.inSpect(inspectDto);
        return Result.success();
    }

    //获取推荐库位
    @GetMapping("/location/{orderNo}")
    public Result<?> getlocation(@PathVariable String orderNo){
        List<InspectDto.I> inspectDto = puchaseInService.getlocation(orderNo);
        return Result.success(inspectDto);
    }

    //上架
    //可传存储数据参数，不传默认使用推荐库位，传参则修改入库明细表中库位为实际存储位置
    @PutMapping("/Finish")
    public Result<?> finish(@Valid @RequestBody FinishDto finishDto){
        finishDto.setStatus(PurchaseInStatusEnum.FINISH);
        puchaseInService.finish(finishDto);
        return Result.success();
    }

    //查看采购入库单
    @GetMapping("/{orderNo}")
    public Result<?> get(@PathVariable("orderNo")String orderNo){
         PurchaseInDto purchaseInDto = puchaseInService.get(orderNo);
        return Result.success(purchaseInDto);
    }

    //查看采购入库明细
    @GetMapping("/Detail/{orderNo}")
    public  Result<?> getDetail(@PathVariable("orderNo") String orderNo){
       List<PurchaseInDetailDto> purchaseInDetailDtos = puchaseInService.getDetail(orderNo);
        return Result.success(purchaseInDetailDtos);
    }


}
