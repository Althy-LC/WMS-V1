package com.example1.wms.Mapper;

import com.example1.wms.DTO.Enum.SaleStatus;
import com.example1.wms.POJO.SaleOut;
import com.example1.wms.POJO.SaleOutDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 18:01
 * @Description
 */
@Mapper
public interface SaleMapper {
    void create(SaleOut sale);

    Long getid(String orderNo);

    List<SaleOut> getBystatus(SaleStatus status);

    SaleOut getsale(String orderNo);

    Integer update(SaleOut sale);

    List<SaleOut> getsaleBystatus(SaleStatus status);

    String getorderNo(Long orderId);

    Integer getversion(Long orderId);

    void updates(List<SaleOut> sale);

    /**
     * 根据订单号更新销售单状态为复核状态
     */
    Integer updateToReview(@org.apache.ibatis.annotations.Param("orderNo") String orderNo, 
                          @org.apache.ibatis.annotations.Param("status") com.example1.wms.DTO.Enum.SaleStatus status,
                          @org.apache.ibatis.annotations.Param("version") Integer version);

    /**
     * 获取销售单的明细信息（包含库位信息）
     */
    List<SaleOutDetail> getDetailsWithLocation(@Param("orderId") Long orderId);

    /**
     * 根据订单号更新状态为出库状态
     */
    Integer updateToOutbound(@Param("orderNo") String orderNo,
                            @Param("status") SaleStatus status,
                            @Param("version") Integer version);
}
