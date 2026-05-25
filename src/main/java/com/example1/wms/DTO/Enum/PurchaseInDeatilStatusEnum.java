package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PurchaseInDeatilStatusEnum implements CodeEnum{
    PENDING_RECEIVE(1,"待收货"),
    REVEIVED(2,"已收货"),
    WAIT(3,"待入库"),
    PUTAWAY(3,"已入库"),
    WAITPUTOUT(4,"待退回"),
    PUTOUT(5,"已退回");




    @JsonValue
    private final Integer code;
    private final String desc;

    PurchaseInDeatilStatusEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    public String getDesc(){
        return  desc;
    }

    @JsonCreator
    public PurchaseInDeatilStatusEnum getBycode(Integer code ){
        if(code<1 || code>6) throw new BuniessException("状态取值在1-6之间");

        if (code == null) return null;

        for (PurchaseInDeatilStatusEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
