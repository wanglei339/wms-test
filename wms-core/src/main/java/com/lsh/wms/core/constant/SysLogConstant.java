package com.lsh.wms.core.constant;

/**
 * Created by lixin-mac on 2016/10/18.
 */
public class SysLogConstant {

    /** 日志类型 1 ibd，2 obd 3 fret 4回传wumart ibd 5回传wumart obd 6直流ibd 7直流obd 8回传ofc obd 9报损 10报溢 11回传erp*/
    public static final int LOG_TYPE_IBD = 1;

    public static final int LOG_TYPE_OBD = 2;

    public static final int LOG_TYPE_FRET = 3;

    public static final int LOG_TYPE_WUMART_IBD = 4;

    public static final int LOG_TYPE_WUMART_OBD = 5;

    public static final int LOG_TYPE_DIRECT_IBD = 6;

    public static final int LOG_TYPE_DIRECT_OBD = 7;

    public static final int LOG_TYPE_OFC_OBD = 8;

    public static final int LOG_TYPE_LOSS_WIN = 9;

    //public static final int LOG_TYPE_WIN = 10;

    public static final int LOG_TYPE_ERP_IBD = 11;


    /**回调系统 1 wumart 2 链商OFC 3 erp*/
    public static final int LOG_TARGET_WUMART = 1;

    public static final int LOG_TARGET_LSHOFC = 2;

    public static final int LOG_TARGET_ERP = 3;

    /**当前处理状态,0-新建,1-处理中,2-处理完成,3-失败 4-部分成功*/

    public static final int LOG_STATUS_CREATE = 0;

    public static final int LOG_STATUS_DEAL = 1;

    public static final int LOG_STATUS_FINISH = 2;

    public static final int LOG_STATUS_FAILED = 3;

    public static final int LOG_STATUS_PART_FINISH = 4;

    /** step 1 ibd创建成功 2 ibd过账成功 3 obd创建成功 4 obd过账成功 */
    public static final int LOG_STEP_INIT = 0;

    public static final int LOG_STEP_IBDCREATE = 1;

    public static final int LOG_STEP_IBDFINISH = 2;

    public static final int LOG_STEP_OBDCREATE = 3;

    public static final int LOG_STEP_OBDFINISH = 4;






}
