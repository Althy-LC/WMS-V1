package com.example1.wms.Controller;

import com.example1.wms.DTO.DictDto;
import com.example1.wms.Result;
import com.example1.wms.Service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/4 23:00
 * @Description 字典控制器
 */

@RestController
@RequestMapping("/Dict")
public class DictController {
@Autowired
    DictTypeService dictTypeService;

Boolean isAdmin = false;

    @GetMapping
    public Result<?> getDict(@RequestParam String dictType){
        List<DictDto> dictDtos = dictTypeService.getDict(dictType, isAdmin);
        return  Result.success(dictDtos);
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody DictDto dictDto){
        dictTypeService.delete(dictDto);
        return Result.success();
    }

    @GetMapping("/admin")
    public Result<?> admingetDict(@RequestParam String dictType){
        isAdmin =true;
        List<DictDto> dictDtos = dictTypeService.getDict(dictType,isAdmin);
        return  Result.success(dictDtos);
    }
}

