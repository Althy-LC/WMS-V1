package com.example1.wms.Mapper;

import com.example1.wms.POJO.StockFlow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/12 00:21
 * @Description
 */
@Mapper
public interface StockFlowMapper {
    void add(List<StockFlow> stockFlows);
}
