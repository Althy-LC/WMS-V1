package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.DTO.HouseDto;

import com.example1.wms.DTO.UtilDto.PageQuery;
import com.example1.wms.Mapper.HouseMapper;
import com.example1.wms.POJO.House;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Althy
 * @Create 2026/4/29 00:31
 * @Description HouseService实现类
 *          仓库表增删改查
 */
@Service
@Transactional
public class HouseServicerImpl implements HouseService{
@Autowired
HouseMapper houseMapper;
@Autowired
SequenceService sequenceService;

    //添加仓库方法
    @Override
    public void add(HouseDto houseDto) {
        Long seq = sequenceService.nextCode("House");
        CodeBulider codeBulider = new CodeBulider();
        houseDto.setWarehouseCode(codeBulider.getnewHousecode(seq));

        House house = new House();
       BeanUtils.copyProperties(houseDto,house);
        houseMapper.add(house);
        sequenceService.update("House");

    }

    //分页查询仓库
    @Override
    public List<HouseDto> list(PageQuery pageQuery, Boolean isAdmin) {
         int start = pageQuery.getStart();
        List<House> houseList = houseMapper.list(start,isAdmin);
        /*
           Iterator<House> it = houseList.iterator();
           while (it.hasNext()){
               House house = it.next();
               System.out.println(house.toString());
           }
         */

        List<HouseDto> houseDtos = houseList.stream().map(T->{
            HouseDto houseDto = new HouseDto();
            BeanUtils.copyProperties(T,houseDto);
            return houseDto;
        }).collect(Collectors.toList());

        return houseDtos;
    }

    //查找指定仓库信息
    @Override
    public HouseDto select(String warehouseCode, Boolean isAdmin) {
        House house = houseMapper.select(warehouseCode,isAdmin);

           HouseDto houseDto = new HouseDto();
            BeanUtils.copyProperties(house,houseDto);


        return houseDto;
    }

    //删除指定仓库
    @Override
    public void delete(String warehouseCode) {
       int result = houseMapper.delete(warehouseCode);

        //删除下属库区
        //删除下属库位

        if(result == 0){
            throw new BuniessException(404,"要删除的仓库不存在");
        }
    }

    //修改数据
    @Override
    public void put(HouseDto houseDto) {
        House house = new House();
        BeanUtils.copyProperties(houseDto,house);
     int result =  houseMapper.put(house);
      if (result == 0){
          throw new BuniessException(404,"修改的仓库不存在");
      }
    }
}
