package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Althy
 * @Create 2026/5/3 12:52
 * @Description 库位状态枚举类
 */
public enum LocationStatusEnum implements CodeEnum{
    Disable(0,"禁用"),
    Idle(1,"空闲"),
    Occupied(2,"占用"),
    Locked(3,"锁定");

    @JsonValue
    private final Integer code;
    private final String desc;

    LocationStatusEnum(Integer code,String desc){
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

    @JsonCreator
    public static LocationStatusEnum getBycode(Integer code) {
       if(code<1 || code>3) throw new BuniessException("状态取值在1-3之间");

        if (code == null) return null;

        for (LocationStatusEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
