package com.example1.wms.Service;

import com.example1.wms.DTO.AreaDto;
import com.example1.wms.DTO.UtilDto.PageQuery;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/30 09:23
 * @Description AreaService接口
 */
public interface AreaService {
    void add(AreaDto areaDto);

    List<AreaDto> list(Integer start, Boolean isAdmin);

    AreaDto select(String areaCode, Boolean isAdmin);

    List<AreaDto> listhouse(String houseCode, PageQuery pageQuery, Boolean isAdmin);

    void put(AreaDto areaDto);

    void delete(String areaCode);
}
