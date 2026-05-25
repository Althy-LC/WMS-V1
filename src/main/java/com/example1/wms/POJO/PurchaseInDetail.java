package com.example1.wms.POJO;

import com.example1.wms.DTO.Enum.PurchaseInDeatilStatusEnum;
import com.example1.wms.DTO.Enum.PurchaseInStatusEnum;
import com.example1.wms.DTO.Enum.QcResult;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/7 18:17
 * @Description 采购入库——明细
 */
@Data
public class PurchaseInDetail {
    private Long id;
    //入库单主表id
    private Long orderId;
    private Long skuId;
    //计划入库总数
    private Integer planQuantity;
    //实际收货总数
    private Integer actualQuantity;
    //合格数量
    private Integer qualifiedQuantity;
    private Long locationId;
    //质检员
    private String inspector;
    /*
     *质检结果
     * 1.通过（合格率)95%
     *      -不合格部分报损，填不合格原因
     * 2.部分合格（合格率70%-95%）
     *      -不合格部分退回供应商
     * 3.不合格（合格率<70%）
     *      -全部退回供应商
     */
    private QcResult qcResult;
    //不合格原因
    private String unqualifiedReson;
    private PurchaseInDeatilStatusEnum status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
