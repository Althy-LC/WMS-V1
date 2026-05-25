package com.example1.wms.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Althy
 * @Create 2026/5/3 19:28
 * @Description
 */

public class CodeBulider {
     //获取新仓库code
    public String getnewHousecode(Long seq){
        String newcode = String.format("W%03d",seq);
        return newcode;
    }

    //获取新库区编码
    public String getnewAreacode(String warehouseCode,Long seq){
        String newcode = String.format(warehouseCode+"-A%04d",seq);
        return newcode;
    }

    //获取新库位编码
    public String getnewLocationcode(String areaCode,Integer Shelf,Integer layer,Integer bin){
        String newcode = String.format(areaCode+"-%04d"+"-%02d"+"-%05d",Shelf,layer,bin);
        return newcode;
    }

    //获取新SKU编码
    public String getnewSKUcode(Long seq){
        String newcode = String.format("SKU-%08d",seq);
        return newcode;
    }

    //获取采购入库单编码
    public String getnewPuchaseInCode(Long seq){
        DateTimeFormatter ym = DateTimeFormatter.ofPattern("yyyyMM");
        String now = LocalDateTime.now().format(ym);

        String newcode = String.format("CR"+now+"%04d",seq);
        return  newcode;
    }

    //获取新盘点编码
    public String getnewInventoryCheckCode(Long seq){
        DateTimeFormatter ym = DateTimeFormatter.ofPattern("yyyyMM");
        String now = LocalDateTime.now().format(ym);

        String newcode = String.format("PD"+now+"%03d",seq);
        return  newcode;
    }

    //生成销售出库单号
    public String getSaleOrderNo(Long seq){
        DateTimeFormatter ym = DateTimeFormatter.ofPattern("yyyyMM");
        String now = LocalDateTime.now().format(ym);

        String newcode = String.format("PD"+now+"%05d",seq);
        return  newcode;
    }

    //波次编号
    public String getWaveNo(Long seq){
        DateTimeFormatter ym = DateTimeFormatter.ofPattern("yyyyMM");
        String now = LocalDateTime.now().format(ym);

        String newcode = String.format("WAVE"+now+"%05d",seq);
        return  newcode;
    }
}
