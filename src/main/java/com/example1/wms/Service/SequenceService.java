package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.Mapper.SequenceMapper;
import com.example1.wms.POJO.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Althy
 * @Create 2026/5/3 19:05
 * @Description
 */
@Service
public class SequenceService {
@Autowired
    SequenceMapper sequenceMapper;

    //获取Code编码
    public Long nextCode(String seqName){


        Long nextcode = sequenceMapper.nextCode(seqName);
        if (nextcode == null){
            nextcode = 1L;
        }else{
             nextcode++;
        }
        return nextcode;
    }

    //更新Code编码
    public void update(String seqName){
       int result = sequenceMapper.update(seqName);
        if(result == 0){
            sequenceMapper.add(seqName);
        }
    }

    //根据日期获取seq
    public Long nextCodeByymd(String seqName){
        Long nextcode;


        DateTimeFormatter ym = DateTimeFormatter.ofPattern("yyyyMM");

        String now = LocalDateTime.now().format(ym);

        Sequence sequence = sequenceMapper.nextCodeByymd(seqName);
           if(sequence != null) {
               String setime = sequence.getUpdateTime().format(ym);
               if (now.equals(setime)) {
                  nextcode = sequence.getCurrentVal()+1;
                  return nextcode ;
               }
           }
            nextcode = 1L;
           return nextcode;
    }

    //按时间获取编号后的修改方法（确保每月更新）
    public void update(String seqName,Long seq){
        int result = sequenceMapper.updateByseq(seqName,seq);
        if ((result == 0)){
            sequenceMapper.add(seqName);
        }
    }

}
