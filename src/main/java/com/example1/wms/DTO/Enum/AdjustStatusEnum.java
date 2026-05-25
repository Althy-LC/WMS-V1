package com.example1.wms.DTO.Enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AdjustStatusEnum implements CodeEnum {
   ADJEST(0,"已调整"),
 UNADJEST(1,"未调整");

    @JsonValue
    private Integer code;
    private String desc;

    AdjustStatusEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode(){
        return code;
    }

    public String getDesc(){
        return  desc;
    }

}
