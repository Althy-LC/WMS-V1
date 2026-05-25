package com.example1.wms.Service;

import com.example1.wms.DTO.DictDto;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/6 08:23
 * @Description
 */
public interface DictTypeService {
    List<DictDto> getDict(String dictType, Boolean isAdmin);

    String getDictBycode(String s, String areaAreaType);

    void delete(DictDto dictDto);
}
