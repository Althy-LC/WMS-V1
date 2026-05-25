package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PurchaseInStatusEnum implements CodeEnum {
    CREATE(1,"创建"),
    AUDIT(2,"待审核"),
    QUALITY_INSPECT(3,"待质检"),
    SHELF(4,"待上架"),
    FINISH(5,"完成");

    @JsonValue
    private final Integer code;
    private final String desc;

    PurchaseInStatusEnum(Integer code,String desc){
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

    @JsonCreator
    public static PurchaseInStatusEnum getBycode(Integer code) {
        if(code<1 || code>5) throw new BuniessException("库位状态取值在1-5之间");

        if (code == null) return null;

        for (PurchaseInStatusEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
