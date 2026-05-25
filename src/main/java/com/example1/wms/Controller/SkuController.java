package com.example1.wms.Controller;

import com.example1.wms.DTO.SkuDto;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.example1.wms.Result;
import com.example1.wms.Service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/3 17:55
 * @Description
 */
@RestController
@RequestMapping("/SKU")
public class SkuController {
@Autowired
    SkuService skuService;

Boolean isAdmin = false ;

    //新增商品
    @PostMapping
    public Result<?> add(@Validated(AddGroups.class)@RequestBody SkuDto skuDto){
        skuService.add(skuDto);
        return Result.success();
    }

    //按商品编码查询指定商品
    @GetMapping("/Code/{code}")
    public Result<?> code(@PathVariable("code") String skuCode){
       SkuDto skuDto = skuService.selectcode(skuCode, isAdmin);
       return Result.success(skuDto);
    }
    //按商品名称查指定商品
    @GetMapping("/Name")
    public Result<?> name(@RequestParam String skuName){
        List<SkuDto> skuDtos = skuService.selectName(skuName, isAdmin);
        return Result.success(skuDtos);
    }

    //按商品条码查指定商品
    @GetMapping("/Barcode/{barcode}")
    public Result<?> barcode(@PathVariable("barcode") String barcode){
        SkuDto skuDto = skuService.selectbarcode(barcode, isAdmin);
        return Result.success(skuDto);
    }

    //按商品编码删除商品
    @DeleteMapping("/{code}")
    public Result<?> delete(@PathVariable("code")String skuCode){
        skuService.delete(skuCode);
        return Result.success();
    }

    //修改商品信息
    @PutMapping
    public Result<?> put(@Validated(UpdateGroups.class)@RequestBody SkuDto skuDto){
        skuService.put(skuDto);
        return Result.success();
    }

    //按商品编码查询指定商品
    @GetMapping("/admin/Code/{code}")
    public Result<?> admincode(@PathVariable("code") String skuCode){
        isAdmin = true;

        SkuDto skuDto = skuService.selectcode(skuCode,isAdmin);
        return Result.success(skuDto);
    }
    //按商品名称查指定商品
    @GetMapping("/admin/Name")
    public Result<?> adminname(@RequestParam String skuName){
        isAdmin = true ;

        List<SkuDto> skuDtos = skuService.selectName(skuName,isAdmin);
        return Result.success(skuDtos);
    }

    //按商品条码查指定商品
    @GetMapping("/admin/Barcode/{barcode}")
    public Result<?> adminbarcode(@PathVariable("barcode") String barcode){
        isAdmin = true;

        SkuDto skuDto = skuService.selectbarcode(barcode,isAdmin);
        return Result.success(skuDto);
    }

}
