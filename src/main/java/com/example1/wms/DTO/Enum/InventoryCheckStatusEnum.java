package com.example1.wms.DTO.Enum;

import com.example1.wms.DTO.InspectDto;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InventoryCheckStatusEnum implements CodeEnum{
    PENDING(0,"待盘点"),
    COUNTING(1,"盘点中"),
    PENDING_DEAL(2,"差异待处理"),
    CLOSED(3,"已关闭");

    @JsonValue
    private Integer code;
    private String desc;

    InventoryCheckStatusEnum(Integer code,String desc){
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
