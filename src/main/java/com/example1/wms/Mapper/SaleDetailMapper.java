package com.example1.wms.Mapper;

import com.example1.wms.POJO.SaleOutDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
