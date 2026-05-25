package com.example1.wms.Mapper;

import com.example1.wms.DTO.UtilDto.salelockDto;
import com.example1.wms.POJO.Stock;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Althy
 * @Create 2026/5/11 23:03
 * @Description
 */
@Mapper
public interface StockMapper {
    void addOrUpdate(Set<Stock> stocks);

    Map<String, Object> getTotal(Long warehouseId, Long skuId);

    @MapKey("skuId")
    Map<Long, Stock> getStock(Long warehouseId, Long skuId);

    Integer salelocked(Long warehouseId,@Param("list") List<salelockDto> salelockDtos);

    Integer getversion(Long warehouseId, Long skuId);

    /**
     * 出库扣减库存：total_stock减，sale_locked减
     */
    Integer outboundStock(Long warehouseId, @Param("list") List<salelockDto> salelockDtos);
}
