package com.lsh.wms.service.inhouse;

import java.math.BigDecimal;

/**
 * Created by mali on 16/7/30.
 */
public class StockTransferParam {
    /** 移库任务id */
    private Long taskId = 0L;
    /** 移库人员id */
    private Long staffId = 0L;
    /** 商品id */
    private Long itemId = 0L;
    /** 商品国条 */
    private String barcode = "";
    /** 移入库位id */
    private Long locationId = 0L;
    /** 商品单位转换id */
    private String packName = "";
    /** 以包装单位计量的库移数量 */
    private BigDecimal uomQty = BigDecimal.ZERO;
}
