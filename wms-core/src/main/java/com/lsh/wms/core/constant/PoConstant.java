package com.lsh.wms.core.constant;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/20
 * Time: 16/7/20.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.constant.
 * desc:类功能描述
 */
public class PoConstant {
    /**
     * 是否有效-是
     */
    public static final int ORDER_YES = 1;
    /**
     * 是否有效-否
     */
    public static final int ORDER_NO = 0;

    /** 订单状态，0取消，1正常，2待投单,3,待收货 4，部分收货，5已收货,6收货中 */
    public static final int ORDER_THROW = 3;
    public static final int ORDER_RECTIPT_PART = 4;
    public static final int ORDER_RECTIPT_ALL = 5;
    public static final int ORDER_RECTIPTING = 6;

    // TODO: 16/8/8 加了订单类型
    /** 订单类型 1PO ,2 返仓 ,3 调拨单*/
    public static final int ORDER_TYPE_PO = 1;
    public static final int ORDER_TYPE_SO_BACK = 2;
    public static final int ORDER_TYPE_TRANSFERS = 3;

}
