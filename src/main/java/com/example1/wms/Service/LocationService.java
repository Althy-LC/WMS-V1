package com.example1.wms.Service;

import com.example1.wms.DTO.LocationDto;
import com.example1.wms.DTO.UtilDto.PageQuery;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/30 12:53
 * @Description
 */
public interface LocationService {
    void add(LocationDto locationDto);

    List<LocationDto> list(@Valid PageQuery pageQuery, Boolean isAdmin);

    List<LocationDto> listarea(@Valid PageQuery pageQuery, String areaCode, Boolean isAdmin);

    List<LocationDto> listhouse(@Valid PageQuery pageQuery, String warehouseCode, Boolean isAdmin);

    LocationDto select(String locationCode, Boolean isAdmin);

    void put(LocationDto locationDto);

    void delete(String locationCode);
}
