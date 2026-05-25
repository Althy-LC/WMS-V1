package com.example1.wms.Mapper;

import com.example1.wms.POJO.Sequence;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Althy
 * @Create 2026/5/3 19:06
 * @Description
 */
@Mapper
public interface SequenceMapper {
    Long nextCode(String seqName);

    int update(String seqName);

    void add(String seqName);

    Sequence nextCodeByymd(String seqName);

    int updateByseq(String seqName, Long seq);
}
