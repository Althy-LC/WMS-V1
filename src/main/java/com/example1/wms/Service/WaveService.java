package com.example1.wms.Service;

import com.example1.wms.DTO.WaveDetaialDto;
import com.example1.wms.DTO.WaveDto;

import java.util.List;

public interface WaveService{
        void create();

        List<WaveDto> getAudit();

        void audit(List<String> waveNos);

        List<WaveDetaialDto> getSpathofWave(String waveNo);
}
