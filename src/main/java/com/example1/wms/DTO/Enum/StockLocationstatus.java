package com.example1.wms.DTO.Enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StockLocationstatus implements CodeEnum{
    AVAILABLE(0,"可用"),
    UNAVAILABLE(1,"不可用"),
    Abandoned(2,"已作废");

    @JsonValue
        private  Integer code ;
        private  String desc;

    StockLocationstatus(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    public String getDesc(){
        return desc;
    }
}
