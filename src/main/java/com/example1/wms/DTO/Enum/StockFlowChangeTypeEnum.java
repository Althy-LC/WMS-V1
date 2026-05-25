package com.example1.wms.DTO.Enum;

import com.example1.wms.BuniessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StockFlowChangeTypeEnum implements CodeEnum{
  PURCHASE_IN(1,"采购入库"),
  SALES_OUT(2,"销售出库"),
  UNLOCK_STOCK(3,"释放"),
  RELEASE_LOCK(4,"锁定"),
  INVENTORY_SURPLUS(5,"盘点盈"),
  INVENTORY_LOSS(6,"盘点亏"),
  RETURN_IN(7,"退货入库");

  @JsonValue
    private final Integer code;
    private final String desc;

    StockFlowChangeTypeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public StockFlowChangeTypeEnum getBycode(Integer code){
        if(code<1 || code>7){
            throw new BuniessException("状态取值1-7之间");
        }
        if(code == null){
            return null;
        }

        for(StockFlowChangeTypeEnum type : values()){
            if(type.code.equals(code)) return type;
        }
        return null;
    }
}
