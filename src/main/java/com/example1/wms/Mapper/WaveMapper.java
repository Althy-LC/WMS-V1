package com.example1.wms.Mapper;

import com.example1.wms.DTO.Enum.Wavestatus;
import com.example1.wms.POJO.Wave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 21:01
 * @Description
 */
@Mapper
public interface WaveMapper {
    void add(List<Wave> waves);

    Long getid(String waveNo);

    List<Wave> getByStatus(Wavestatus status);

    void updateStatus(@Param("list") List<String> waveNos, Wavestatus status);

    Long getWarehouse(Long waveId);

    void putStatus(String waveNo, Wavestatus status);
}
