package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Wavestatus implements CodeEnum{
    CREATE(0,"创建"),
    AUDIT(1,"已审核"),
    PICKING(2,"拣货中"),
    FINISH(3,"完成");
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
    Wavestatus(Integer code,String desc){
        if (code>5 || code<0){
            throw  new BuniessException("类型不合法");
        }

        this.code = code;
        this.desc = desc;
    }
}
