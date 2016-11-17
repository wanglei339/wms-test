package com.lsh.wms.core.constant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wulin on 16/7/16.
 */
public class StaffConstant {
    /* 记录正常 */
    public static final int RECORD_STATUS_NORMAL = 1;

    /* 记录已删除 */
    public static final int RECORD_STATUS_DELETED = 2;

    /* 在职 */
    public static final int STAFF_INFO_STATUS_ON = 1;

    /* 离职 */
    public static final int STAFF_INFO_STATUS_OFF = 2;

    /**
     *系统用户可用
     */
    public static final Integer USER_USE= 1;

    /**
     * 系统用户禁用
     */
    public static final Integer USER_FORBIDDEN = 2;


    //收货员、上架员、拣货员、盘点员、QC、装车工、调度、文员、合板工
    public static final Long JOB_RECEIVE = 1L;
    public static final Long JOB_SHELF_UP_DOWN = 2L;
    public static final Long JOB_PICK = 3L;
    public static final Long JOB_STOCKTAKING = 4L;
    public static final Long JOB_QC = 5L;
    public static final Long JOB_SHIP = 6L;
    public static final Long JOB_DISPATCH = 7L;
    public static final Long JOB_ASSISTANT = 8L;


    public static final Map<Long, String> JOB_NAMES = new HashMap<Long, String>() {
        {
            //虚拟区
            put(JOB_RECEIVE, "收货员");
            put(JOB_SHELF_UP_DOWN, "高叉");
            put(JOB_PICK, "拣货员");
            put(JOB_STOCKTAKING, "盘点员");
            put(JOB_QC, "QC");
            put(JOB_SHIP, "发货员");
            put(JOB_DISPATCH, "调度");
            put(JOB_ASSISTANT, "文员");
        }
    };


}
