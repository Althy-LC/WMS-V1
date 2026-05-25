package com.example1.wms.Controller;

import com.example1.wms.DTO.HouseDto;
import com.example1.wms.DTO.UtilDto.PageQuery;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.example1.wms.Result;
import com.example1.wms.Service.HouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/29 00:27
 * @Description 仓库表控制层
 */
@RestController
@RequestMapping("/house")
public class HouseController {
   @Autowired
    HouseService houseService;

   Boolean isAdmin=false;

   //增加仓库
    @PostMapping
    public Result<?> AddHouse(@Validated(AddGroups.class) @RequestBody HouseDto house){
         houseService.add(house);
        return Result.success();
    }

    //分页查找所有仓库
    @GetMapping("/list")
    public Result<?> list(@Valid @RequestBody PageQuery pageQuery){
        List<HouseDto> houseDto = houseService.list(pageQuery, isAdmin);
        return Result.success(houseDto);
    }

    //查找指定仓库
    @GetMapping("/select/{warehouseCode}")
    public Result<?> select( @PathVariable("warehouseCode")String warehouseCode){

        HouseDto houseDto = houseService.select(warehouseCode, isAdmin);
        return Result.success(houseDto);
    }

    //修改仓库数据
    @PutMapping("/put")
    public Result<?> put(@Validated(UpdateGroups.class) @RequestBody HouseDto houseDto){
        houseService.put(houseDto);
        return Result.success();
    }

    //删除指定仓库
    @DeleteMapping("/delete/{warehouseCode}")
    public Result<?> delete( @PathVariable("warehouseCode")String warehouseCode){

         houseService.delete(warehouseCode);
         return Result.success();
    }

    //分页查找所有仓库
    @GetMapping("/admin/list")
    public Result<?> adminlist(@Valid @RequestBody PageQuery pageQuery){
        isAdmin =true;
        List<HouseDto> houseDto = houseService.list(pageQuery,isAdmin);
        return Result.success(houseDto);
    }

    //查找指定仓库
    @GetMapping("/admin/select/{warehouseCode}")
    public Result<?> adminselect( @PathVariable("warehouseCode")String warehouseCode){
        isAdmin=true;

        HouseDto houseDto = houseService.select(warehouseCode,isAdmin);
        return Result.success(houseDto);
    }

}
