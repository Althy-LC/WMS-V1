package com.example1.wms.Mapper;

import com.example1.wms.DTO.Enum.LocationStatusEnum;
import com.example1.wms.DTO.LocationDto;
import com.example1.wms.DTO.UtilDto.PathSortDto;
import com.example1.wms.POJO.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/4/30 12:54
 * @Description
 */
@Mapper
public interface LocationMapper {

    void add(Location location);

    List<LocationDto> list(int start, Boolean isAdmin);

    List<LocationDto> listarea(Long areaId, int start, Boolean isAdmin);

    List<LocationDto> listhouse(int start, Long warehouseId, Boolean isAdmin);

    LocationDto select(String locationCode, Boolean isAdmin);

    int put(Location location);

    int delete(String locationCode);


    Long gethouseid(String warehouseCode);

    int useLocation(Long locationId);



    int useLocations(@Param("list") List<Long> locationids);

    int releaseLocations(List<Long> locationids);

    String getCode(Long locationId);


    Long getidByCode( String locationCode);

    List<PathSortDto> getCodeAndAreatype(List<Long> locationids);

    int putStatus(@Param("list") List<Long> locationIds, LocationStatusEnum status);

    Integer getAreatype(Long locationId);
}
