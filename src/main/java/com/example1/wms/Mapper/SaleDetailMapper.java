package com.example1.wms.Mapper;

import com.example1.wms.POJO.SaleOutDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Althy
 * @Create 2026/5/22 18:44
 * @Description
 */
@Mapper
public interface SaleDetailMapper {
    void add(List<SaleOutDetail> saleOutDetails);

    List<SaleOutDetail> getByorderId(Long orderId);

    List<SaleOutDetail> getsku(Long orderId);

    List<SaleOutDetail> getByorderIds(List<Long> orderid);

    void update(List<SaleOutDetail> saleOutDetails);

    /**
     * 查询近N天出库TOP M的商品
     * @param days 天数
     * @param topN TOP数量
     * @return 商品排行列表，包含skuId, skuCode, skuName, totalQuantity
     */
    List<Map<String, Object>> getOutboundTopN(@Param("days") int days, @Param("topN") int topN);
}
