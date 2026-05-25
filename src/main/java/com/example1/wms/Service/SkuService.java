package com.example1.wms.Service;

import com.example1.wms.DTO.SkuDto;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/3 18:08
 * @Description
 */
public interface SkuService {
    void add(SkuDto skuDto);

    SkuDto selectcode(String skucode, Boolean isAdmin);

    List<SkuDto> selectName(String skuName, Boolean isAdmin);

    SkuDto selectbarcode(String barcode, Boolean isAdmin);

    void delete(String skuCode);

    void put(SkuDto skuDto);
}
