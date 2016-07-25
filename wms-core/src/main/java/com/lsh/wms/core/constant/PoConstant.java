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

    /** 订单状态，0取消，1正常，2已发货,3,已投单 4，部分收货，5已收货 */
    public static final int ORDER_THROW = 3;
    public static final int ORDER_RECTIPT_PART = 4;
    public static final int ORDER_RECTIPT_ALL = 5;

}
