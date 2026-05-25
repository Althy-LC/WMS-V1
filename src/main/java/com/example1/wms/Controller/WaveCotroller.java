package com.example1.wms.Controller;

import com.example1.wms.DTO.WaveDetaialDto;
import com.example1.wms.DTO.WaveDto;
import com.example1.wms.POJO.Wave;
import com.example1.wms.Result;
import com.example1.wms.Service.WaveService;
import com.example1.wms.WmsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/22 20:59
 * @Description
 */
@RestController
@RequestMapping("/Wave")
public class WaveCotroller {
    @Autowired
    WaveService waveService;

    //创建波次
    @PostMapping("/create")
    public Result<?> create(){
        waveService.create();
       return Result.success();
    }

    //获取审核波次
        @GetMapping("/getAudit")
    public Result<?> getAudit(){
        List<WaveDto> waveDtos = waveService.getAudit();
        return Result.success(waveDtos);
    }

    //审核
    @PutMapping("/Audit")
    public Result<?> Audit(@RequestBody List<String> waveNos){
        waveService.audit(waveNos);
        return  Result.success();
    }

    //获取S型路径波次拣货单
    @GetMapping("/getSpathofWave/{waveNo}")
    public Result<?> getSpathofWave(@PathVariable String waveNo){
        List<WaveDetaialDto> waveDetaialDtos = waveService.getSpathofWave(waveNo);
        return Result.success(waveDetaialDtos);
    }

}
