package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.DTO.DictDto;
import com.example1.wms.Mapper.DictMapper;
import com.example1.wms.POJO.Dict;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/6 08:23
 * @Description
 */

@Service
public class DictTYpeServiceImpl implements DictTypeService{
@Autowired
    DictMapper dictMapper;

    //查字典列表
    @Override
    public List<DictDto> getDict(String dictType, Boolean isAdmin) {
        List<Dict> dicts = dictMapper.getDict(dictType,isAdmin);
        List<DictDto> dictDtos = dicts.stream().map(T->{
            DictDto dictDto = new DictDto();
            BeanUtils.copyProperties(T,dictDto);
            return  dictDto;
        }).toList();

        return  dictDtos;
    }

    //查字典值名称
    @Override
    public String getDictBycode(String s, String dictType) {
       Dict dict =  dictMapper.getDictBycode(s,dictType);
        return dict.getDictName();
    }

    //删除字典项
    @Override
    public void delete(DictDto dictDto) {
       int result = dictMapper.delete(dictDto);
        if (result == 0){
            throw  new BuniessException("待删除字典项不存在");
        }
    }

}
