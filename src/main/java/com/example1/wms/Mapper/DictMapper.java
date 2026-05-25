package com.example1.wms.Mapper;

import com.example1.wms.DTO.DictDto;
import com.example1.wms.POJO.Dict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/6 08:28
 * @Description
 */
@Mapper
public interface DictMapper {

    List<Dict> getDict(String dictType, Boolean isAdmin);

    Dict getDictBycode(String s, String dictType);

    int delete(DictDto dictDto);
}
