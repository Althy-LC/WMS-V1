package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.Wavestatus;
import com.example1.wms.POJO.WaveDetail;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/23 00:32
 * @Description
 */
@Data
public class WaveDto {
    private String waveNo;
    private String warehouseCode;

    private List<WaveDetaialDto> details;
}
