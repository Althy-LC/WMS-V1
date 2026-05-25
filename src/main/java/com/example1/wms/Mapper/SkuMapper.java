package com.example1.wms.Mapper;

import com.example1.wms.POJO.SKU;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/3 18:08
 * @Description
 */
@Mapper
public interface SkuMapper {
    void add(SKU sku);

    SKU code(String skuCode, Boolean isAdmin);

    List<SKU> name(String skuName, Boolean isAdmin);

    SKU barcode(String barcode, Boolean isAdmin);

    int delete(String skuCode);

    int put(SKU sku);

    Long getid(@NotBlank(message = "商品编码不得为空") String skuCode);

    String getCode(Long skuId);

    SKU getCodeAndName(Long skuId);
}
