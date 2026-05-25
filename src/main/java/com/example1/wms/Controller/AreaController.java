package com.example1.wms.Controller;

import com.example1.wms.DTO.AreaDto;
import com.example1.wms.DTO.UtilDto.PageQuery;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.example1.wms.Result;
import com.example1.wms.Service.AreaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/30 09:23
 * @Description Area控制器
 */
@RestController
@RequestMapping("/Area")
public class AreaController {
  @Autowired
    AreaService areaService;

    Boolean isAdmin = false;
  //增加库区
    @PostMapping
    public Result<?> add(@Validated(AddGroups.class) @RequestBody AreaDto areaDto){
           areaService.add(areaDto);
           return Result.success();
    }

    //分页查找所有库区
    @GetMapping("/list")
    public Result<?> list(@Valid @RequestBody PageQuery pageQuery){


           Integer start = pageQuery.getStart();
        List<AreaDto> areaDtoList = areaService.list(start, isAdmin);
        return Result.success(areaDtoList);
    }

    //分页查找指定仓库下所有库区
    @GetMapping("/House/{houseCode}")
    public Result<?> listhouse(@PathVariable("houseCode") String houseCode,@Valid @RequestBody PageQuery pageQuery){
        List<AreaDto> areaDtoList = areaService.listhouse(houseCode,pageQuery, isAdmin);
        System.out.println(isAdmin);
        return Result.success(areaDtoList);
    }

    //查找指定库区信息
    @GetMapping("/select/{areaCode}")
    public Result<?> select(@PathVariable("areaCode") String areaCode){
        AreaDto areaDto = areaService.select(areaCode, isAdmin);
        return Result.success(areaDto);
    }

    //修改库区信息
    @PutMapping("/put")
    public Result<?> put(@Validated(UpdateGroups.class) @RequestBody AreaDto areaDto){
         areaService.put(areaDto);
        return Result.success();
    }

    //删除库区
    @DeleteMapping("/delete/{areaCode}")
    public Result<?> delete(@PathVariable("areaCode") String areaCode){
        areaService.delete(areaCode);
        return Result.success();
    }

    //分页查找所有库区
    @GetMapping("/admin/list")
    public Result<?> adminlist(@Valid @RequestBody PageQuery pageQuery){
         isAdmin = true;

        Integer start = pageQuery.getStart();
        List<AreaDto> areaDtoList = areaService.list(start,isAdmin);
        return Result.success(areaDtoList);
    }

    //分页查找指定仓库下所有库区
    @GetMapping("/admin/House/{houseCode}")
    public Result<?> adminlisthouse(@PathVariable("houseCode") String houseCode,@Valid @RequestBody PageQuery pageQuery){
        isAdmin = true;

        List<AreaDto> areaDtoList = areaService.listhouse(houseCode,pageQuery,isAdmin);
        return Result.success(areaDtoList);
    }

    //查找指定库区信息
    @GetMapping("/admin/select/{areaCode}")
    public Result<?> adminselect(@PathVariable("areaCode") String areaCode){
        isAdmin = true;
        AreaDto areaDto = areaService.select(areaCode,isAdmin);
        return Result.success(areaDto);
    }
}
