package com.example1.wms;

import com.example1.wms.DTO.DictDto;
import com.example1.wms.Service.DictTypeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Althy
 * @Create 2026/5/6 09:04
 * @Description
 */
@Component
public class GetDesc {

    private static DictTypeService dictTypeService;

    public GetDesc(DictTypeService dictTypeService) {
        GetDesc.dictTypeService = dictTypeService;
    }

    // 静态工具方法
    public static Map<String,String> getdesc( String DictType) {
        Boolean isAdmin = true;
        List<DictDto> dictDtos = dictTypeService.getDict(DictType, isAdmin);
        return dictDtos.stream()
                .collect(Collectors.toMap(DictDto::getDictCode, DictDto::getDictName));
    }
}