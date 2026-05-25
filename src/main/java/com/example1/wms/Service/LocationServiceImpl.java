package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.DTO.LocationDto;
import com.example1.wms.DTO.UtilDto.PageQuery;
import com.example1.wms.GetDesc;
import com.example1.wms.Mapper.AreaMapper;
import com.example1.wms.Mapper.LocationMapper;
import com.example1.wms.POJO.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Althy
 * @Create 2026/4/30 12:53
 * @Description
 */
@Slf4j
@Service
public class LocationServiceImpl implements  LocationService{
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    DictTypeService dictTypeService;
    @Autowired
    private AreaMapper areaMapper;


    //新增库位
    @Override
    public void add(LocationDto locationDto) {
        CodeBulider codeBulider = new CodeBulider();
        locationDto.setLocationCode(codeBulider.getnewLocationcode(locationDto.getAreaCode(),locationDto.getShelf(),locationDto.getLayer(),locationDto.getBin()));

        Location location = new Location();
        BeanUtils.copyProperties(locationDto,location);

        location.setAreaId(areaMapper.getidByCode(locationDto.getAreaCode()));
        locationMapper.add(location);

    }

    //查找指定库位
    @Override
    public LocationDto select(String locationCode, Boolean isAdmin) {
       LocationDto locationDto= locationMapper.select(locationCode,isAdmin);


        if(locationDto != null){
            locationDto.setStatusdesc(
                dictTypeService.getDictBycode(
                        String.valueOf(
                                locationDto.getLocationType())
                                ,"Location_locationtype")
            );
            locationDto.setStatusdesc(locationDto.getStatus().getDesc());
        }


        return locationDto;
    }


    //分页查找所有库位
    @Override
    public List<LocationDto> list(PageQuery pageQuery, Boolean isAdmin) {
        int start = pageQuery.getStart();
        List<LocationDto>  locationDtos  = locationMapper.list(start,isAdmin);

        Map<String,String> map = GetDesc.getdesc("Location_locationtype");

         locationDtos = locationDtos.stream().map(T->{
            System.out.println(T.toString());

            T.setLocationTypedesc(
                map.get(
                    String.valueOf(
                            T.getLocationType()
                    )
                 )
            );

            T.setStatusdesc(T.getStatus().getDesc());
            return T;
        }).toList();
        return locationDtos;
    }

    //分页查找指定库区下所有库位
    @Override
    public List<LocationDto> listarea(PageQuery pageQuery, String areaCode, Boolean isAdmin) {
        int start = pageQuery.getStart();
          Long areaId = areaMapper.getidByCode(areaCode);

        List<LocationDto> locationDtos = locationMapper.listarea(areaId,start,isAdmin);

            Map<String,String> map = GetDesc.getdesc("Location_locationtype");

           locationDtos = locationDtos.stream().map(T->{

                T.setLocationTypedesc(
                        map.get(
                            String.valueOf(
                                 T.getLocationType()
                            )
                        )
                );

                    T.setStatusdesc(T.getStatus().getDesc());
            return T;
             }).toList();

        return locationDtos;
        }

    //分页查找指定仓库下所有库位
    @Override
    public List<LocationDto> listhouse(PageQuery pageQuery, String warehouseCode, Boolean isAdmin) {
        int start = pageQuery.getStart();

        Long warehouseId = locationMapper.gethouseid(warehouseCode);

        List<LocationDto> locationDtos = locationMapper.listhouse(start,warehouseId,isAdmin);

        if(locationDtos.isEmpty()){
            throw new BuniessException("查询为空");
        }

        Map<String,String> map = GetDesc.getdesc("Location_locationtype");

        locationDtos= locationDtos.stream().map(T->{
            System.out.println(T.toString());

            T.setLocationTypedesc(
                    map.get(
                            String.valueOf(
                                    T.getLocationType()
                            )
                    )
            );

            T.setStatusdesc(T.getStatus().getDesc());
            return T;
        }).toList();

        return locationDtos;
    }


    //修改库位信息
    @Override
    public void put(LocationDto locationDto) {
        Location location = new Location();

        BeanUtils.copyProperties(locationDto,location);
        Long areaId = areaMapper.getidByCode(locationDto.getAreaCode());
        if (areaId == null){
            throw new BuniessException("找不到库区");
        }
        location.setAreaId(areaId);

            log.warn(location.toString());


        int result = locationMapper.put(location);

          if(result == 0){
            throw new BuniessException(404,"待修改库位不存在");
          }
    }

    //删除指定库位
    @Override
    public void delete(String locationCode) {
       int result = locationMapper.delete(locationCode);

       if(result == 0){
            throw new BuniessException(404,"待删除的库位不存在");
        }
    }


}
