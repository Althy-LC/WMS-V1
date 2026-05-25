package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.SkuStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/3 12:23
 * @Description 商品SKU表实体类
 */
@Data
public class SKU {
    private Long id;
    //商品编号
    private String skuCode;
    private String skuName;
    //商品条码
    private String barcode;
    //商品规格
    private String spec;
    //商品单位，default：件
    private String unit;
    /**
     *  状态
     *     1.正常
     *     2.锁定
     *     3.缺货
     *     4.下架
     */
    private SkuStatusEnum status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
