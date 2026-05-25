package com.example1.wms.Mapper;

import com.example1.wms.POJO.SaleOutDetail;
import com.example1.wms.POJO.StockLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/15 19:37
 * @Description
 */
@Mapper
public interface StockLocationMapper {

    void addorupdate(List<StockLocation> stockLocations);

    List<StockLocation> select(Long warehouseId);

    void updateAmount(@Param("list") List<StockLocation> stockLocations);

    Long getlocationId(Long warehouseId, Long skuId);

    Long getlocation(Long warehouseId, SaleOutDetail t);

    List<StockLocation> getByhouseandsku(Long warehouseId, Long skuId);

    /**
     * 预占库存：amount减，locked_amount加
     */
    void lockStock(@Param("list") List<StockLocation> stockLocations);

    /**
     * 扣减锁定库存并出库
     */
    void deductLockedStock(@Param("list") List<StockLocation> stockLocations);

    /**
     * 查询amount为0的stock_location记录
     */
    List<StockLocation> getZeroAmountLocations(@Param("warehouseId") Long warehouseId, 
                                               @Param("skuId") Long skuId, 
                                               @Param("locationId") Long locationId);

    /**
     * 更新stock_location状态为已作废
     */
    void updateStatusToAbandoned(@Param("list") List<Long> ids);
}
