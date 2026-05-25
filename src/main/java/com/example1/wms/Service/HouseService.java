package com.example1.wms.Service;


import com.example1.wms.DTO.HouseDto;
import com.example1.wms.DTO.UtilDto.PageQuery;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/29 00:30
 * @Description HouseService接口
 */

public interface HouseService {
    void add(HouseDto houseDto);

    List<HouseDto> list(PageQuery pageQuery, Boolean isAdmin);

    HouseDto select(String warehouseCode, Boolean isAdmin);

    void delete(String warehouseCode);

    void put(HouseDto houseDto);
}
