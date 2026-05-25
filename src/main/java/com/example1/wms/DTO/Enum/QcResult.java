package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum QcResult implements CodeEnum{
    PASS(0,"报损入库"),
    FAIL(1,"部分退回"),
    OUT(2,"全部退回");

    @JsonValue
    private Integer code;
    private String desc;

    @Override
    public Integer getCode(){
        return code;
    }

    public String getDesc(){
        return  desc;
    }

    @JsonCreator
    QcResult(Integer code,String desc){
        if (code>2 || code<0){
            throw  new BuniessException("类型不合法");
        }

        this.code = code;
        this.desc = desc;
    }
}
