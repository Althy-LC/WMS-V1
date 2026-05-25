package com.example1.wms.Mapper;

import com.example1.wms.POJO.WaveDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/23 00:39
 * @Description
 */
@Mapper
public interface WaveDetailMapper {
    void add(List<WaveDetail> waveDetails);

    List<WaveDetail> getByWaveid(Long waveId);

    void update(List<WaveDetail> waveDetails);

    void updateSort(Long locationid, Integer sort);
}
