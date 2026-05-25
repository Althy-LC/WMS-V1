package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.DTO.AreaDto;
import com.example1.wms.DTO.UtilDto.PageQuery;
import com.example1.wms.GetDesc;
import com.example1.wms.Mapper.AreaMapper;
import com.example1.wms.POJO.Area;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Althy
 * @Create 2026/4/30 09:24
 * @Description
 */
@Service
public class AreaServiceImpl implements AreaService{
 @Autowired
    AreaMapper areaMapper;
@Autowired
    SequenceService sequenceService;
@Autowired
    DictTypeService dictTypeService;

    //新建库区
    @Override
    @Transactional
    public void add(AreaDto areaDto) {
        Area area = new Area();

        Long seq = sequenceService.nextCode("Area");
        CodeBulider codeBulider = new CodeBulider();
        areaDto.setAreaCode(codeBulider.getnewAreacode(areaDto.getWarehouseCode(),seq));

        BeanUtils.copyProperties(areaDto,area);
        area.setWarehouseId(areaMapper.getid(areaDto.getWarehouseCode()));

         areaMapper.add(area);
         sequenceService.update("Area");
    }

    //分页查询所有库区
    @Override
    public List<AreaDto> list(Integer start, Boolean isAdmin) {
      List<AreaDto> areaList = areaMapper.list(start,isAdmin);

      /*
        Iterator it = areaList.iterator();
        while (it.hasNext()){
            Area area = (Area) it.next();
            System.out.println(area.toString());
        }
     */

        Map<String,String> map = GetDesc.getdesc("Area_areatype");

        List<AreaDto> areaDtoList =  areaList.stream().map( T->{
           T.setAreaTypedesc(
                    map.get(
                            String.valueOf(
                                    T.getAreaType()
                            )
                    )
            );
            return T;
        }).toList();




        return areaDtoList;
    }

    //查找指定库区信息
    @Override
    public AreaDto select(String areaCode, Boolean isAdmin) {
        AreaDto areaDto = areaMapper.select(areaCode,isAdmin);


        if(areaDto != null ) {
            areaDto.setAreaTypedesc(
                    dictTypeService.getDictBycode(
                            String.valueOf(
                                    areaDto.getAreaType()),
                            "Area_areatype")
            );
        }
        return areaDto;
    }

    //分页查找指定仓库库区信息
    @Override
    public List<AreaDto> listhouse(String houseCode, PageQuery pageQuery, Boolean isAdmin) {
        int start = pageQuery.getStart();
        Long warehouseId = areaMapper.getid(houseCode);
        List<AreaDto> areaList = areaMapper.listhouse(warehouseId,start,isAdmin);

      /*
        Iterator it = areaList.iterator();
        while (it.hasNext()){
            Area area = (Area) it.next();
            System.out.println(area.toString());
        }
     */
        Map<String,String> map = GetDesc.getdesc("Area_areatype");

        List<AreaDto> areaDtoList =  areaList.stream().map( T->{

            T.setAreaTypedesc(
                    map.get(
                            String.valueOf(
                                    T.getAreaType()
                            )
                    )
            );

            return T;
        }).toList();



        return areaDtoList;
    }

    //修改库区
    @Override
    public void put(AreaDto areaDto) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDto,area);
         if(areaDto.getWarehouseCode() != null){
             area.setWarehouseId(areaMapper.getid(areaDto.getWarehouseCode()));
         }

        int result = areaMapper.put(area);
         if(result == 0){
             throw new BuniessException(404,"要修改的库区不存在");
         }
    }

    //删除库区
    @Override
    public void delete(String areaCode) {
        int result = areaMapper.delete(areaCode);

        //删除下属库位

         if(result == 0){
             throw new BuniessException(404,"要删除的库区不存在");
         }
    }


}
