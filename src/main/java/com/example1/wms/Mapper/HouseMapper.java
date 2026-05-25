package com.example1.wms.Mapper;

import com.example1.wms.POJO.House;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *@author Althy
 *@Create 2026/4/29 00:56
 *@Description
 */
@Mapper
public interface HouseMapper {

    void add(House house);

    List<House> list(int start, Boolean isAdmin);

    House select(String warehouseCode, Boolean isAdmin);

    int delete(String warehouseCode);

    int put(House house);

    Long getid( String warehouseCode);

    String getCode(Long warehouseId);
}
