package com.example1.wms.Mapper;

import com.example1.wms.DTO.AreaDto;
import com.example1.wms.POJO.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Althy
 * @Create 2026/4/30 09:24
 * @Description
 */
@Mapper
public interface AreaMapper {
   Long getReviewLocation(Long warehouseId);

   Long getPickLocation(Long warehouseId);

    List<AreaDto> list(Integer start, Boolean isAdmin);

    void add(Area area);

    AreaDto select(String areaCode, Boolean isAdmin);

    List<AreaDto> listhouse(Long warehouseId, int start, Boolean isAdmin);

    int put(Area area);

    int delete(String areaCode);

    Long getid(String warehouseCode);

    Long getreciveByhouse(Long warehouseId,@Param("collection") Set<Long> uselocations);

    Long getReturnLocationId(Long warehouseId,@Param("collection") Set<Long> uselocations);

    Long getidByCode( String areaCode);
    //Long getid( String warehouseCode);
}
