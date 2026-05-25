package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Althy
 * @Create 2026/5/12 19:21
 * @Description
 */
public enum InspectType implements CodeEnum{
    PASS(0,"合格"),
    FAIL(1,"不合格");

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
    InspectType(Integer code,String desc){
        if (code>1 || code<0){
            throw  new BuniessException("质检批次类型不合法");
        }

        this.code = code;
        this.desc = desc;
    }


}
