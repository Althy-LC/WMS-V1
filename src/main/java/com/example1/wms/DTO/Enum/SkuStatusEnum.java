package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Althy
 * @Create 2026/5/3 17:35
 * @Description
 */
public enum SkuStatusEnum implements CodeEnum {
    NORMAL(1,"正常"),
    LOCKED(2,"锁定"),
    OUT_OF_STOCK(3,"缺货"),
    OFF_SHELF(4,"下架");

    @JsonValue
    private final Integer code;
    private final String desc;


    SkuStatusEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode(){
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public SkuStatusEnum getByCode(Integer code){
        if(code<1 || code>3){
            throw new BuniessException("商品状态取值1-3之间");
        }
        if(code == null){
            return null;
        }

        for(SkuStatusEnum type : values()){
            if(type.code.equals(code)) return type;
        }
        return null;
    }
}
