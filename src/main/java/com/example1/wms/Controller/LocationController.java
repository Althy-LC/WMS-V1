package com.example1.wms.Controller;

import com.example1.wms.DTO.LocationDto;
import com.example1.wms.DTO.UtilDto.PageQuery;
import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.example1.wms.Result;
import com.example1.wms.Service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/30 12:52
 * @Description  库位控制器
 */

@RestController
@RequestMapping("/Location")
public class LocationController {
    @Autowired
    LocationService locationService;

    Boolean isAdmin = false;

    //新建库位
    @PostMapping
    public Result<?> add(@Validated(AddGroups.class) @RequestBody LocationDto locationDto){
          locationService.add(locationDto);
          return Result.success();
    }

    //查询指定库位
    @GetMapping("/{locationCode}")
    public Result<?> select(@PathVariable String locationCode){
        LocationDto locationDto = locationService.select(locationCode, isAdmin);
        return Result.success(locationDto);
    }

    //分页查询所有库位
    @GetMapping("/list")
    public Result<?> list(@Valid @RequestBody PageQuery pageQuery){
        List<LocationDto> locationDtos = locationService.list(pageQuery, isAdmin);
        return Result.success(locationDtos);
    }

    //分页查询指定库区所有库位
    @GetMapping("/Area/{areaCode}")
    public Result<?> listarea(@Valid @RequestBody PageQuery pageQuery,@PathVariable("areaCode") String area_code){
        List<LocationDto> locationDtos = locationService.listarea(pageQuery,area_code, isAdmin);
        return Result.success(locationDtos);
    }

    //分页查询指定仓库下所有库位
    @GetMapping("/House/{warehouseCode}")
    public Result<?> listhouse(@Valid @RequestBody PageQuery pageQuery,@PathVariable("warehouseCode") String warehouseCode){
        List<LocationDto> locationDtos = locationService.listhouse(pageQuery,warehouseCode, isAdmin);
        return Result.success(locationDtos);
    }

    //修改库位信息
    @PutMapping("/put")
    public  Result<?> put (@Validated(UpdateGroups.class) @RequestBody LocationDto locationDto){
        locationService.put(locationDto);
        return Result.success();
    }

    //删除库位
    @DeleteMapping("/delete/{locationCode}")
    public  Result<?> delete (@PathVariable("locationCode") String locationCode){
        locationService.delete(locationCode);
        return  Result.success();
    }

    //查询指定库位
    @GetMapping("/admin/{locationCode}")
    public Result<?> adminselect(@PathVariable String locationCode){
        isAdmin =true;

        LocationDto locationDto = locationService.select(locationCode,isAdmin);
        return Result.success(locationDto);
    }

    //分页查询所有库位
    @GetMapping("/admin/list")
    public Result<?> adminlist(@Valid @RequestBody PageQuery pageQuery){
        isAdmin =true;

        List<LocationDto> locationDtos = locationService.list(pageQuery,isAdmin);
        return Result.success(locationDtos);
    }

    //分页查询指定库区所有库位
    @GetMapping("/admin/Area/{areaCode}")
    public Result<?> adminlistarea(@Valid @RequestBody PageQuery pageQuery,@PathVariable("areaCode") String area_code){
        isAdmin =true;

        List<LocationDto> locationDtos = locationService.listarea(pageQuery,area_code,isAdmin);
        return Result.success(locationDtos);
    }

    //分页查询指定仓库下所有库位
    @GetMapping("/admin/House/{warehouseCode}")
    public Result<?> adminlisthouse(@Valid @RequestBody PageQuery pageQuery,@PathVariable("warehouseCode") String warehouseCode){
        isAdmin =true;

        List<LocationDto> locationDtos = locationService.listhouse(pageQuery,warehouseCode,isAdmin);
        return Result.success(locationDtos);
    }
}
