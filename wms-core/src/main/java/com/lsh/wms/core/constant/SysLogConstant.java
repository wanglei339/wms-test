package com.lsh.wms.core.constant;

/**
 * Created by lixin-mac on 2016/10/18.
 */
public class SysLogConstant {

    /** 日志类型 1 ibd，2 obd 3 fret 4回传wumart ibd 5 回传wumart obd*/
    public static final int LOG_TYPE_IBD = 1;

    public static final int LOG_TYPE_OBD = 2;

    public static final int LOG_TYPE_FRET = 3;

    public static final int LOG_TYPE_WUMART_IBD = 4;

    public static final int LOG_TYPE_WUMART_OBD = 5;

    /**回调系统 1 wumart 2 链商OFC 3 erp*/
    public static final int LOG_TARGET_WUMART = 1;

    public static final int LOG_TARGET_LSHOFC = 2;

    public static final int LOG_TARGET_ERP = 3;


}
