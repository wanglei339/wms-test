package com.lsh.wms.core.constant;

/**
 * Created by lixin-mac on 2016/9/29.
 */
public class SoConstant {

    /** 订单类型 1so ,2供商退货  ,3 sto ,4直流出库订单*/
    public static final int ORDER_TYPE_SO = 1;
    public static final int ORDER_TYPE_PO_BACK = 2;
    public static final int ORDER_TYPE_STO = 3;
    public static final int ORDER_TYPE_DIRECT = 4;

    public static final Long STOCK_NOT_CHECK = 0L;
    public static final Long STOCK_SOFT_CHECK = 1L;
    public static final Long STOCK_HARD_CHECK = 2L;

    /**回传状态 1 未过账 2 过账成功*/
    public static final int DELIVERY_DETAIL_STATUS_FAILED = 1;
    public static final int DELIVERY_DETAIL_STATUS_SUCCESS = 2;

}
