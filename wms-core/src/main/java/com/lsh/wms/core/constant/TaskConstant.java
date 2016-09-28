package com.lsh.wms.core.constant;

/**
 * Created by mali on 16/7/11.
 */
public class TaskConstant {
    public static final Long Draft = 1L;

    public static final Long Assigned = 2L;

    public static final Long Allocated = 3L;

    public static final Long Done = 4L;

    public static final Long Cancel = 5L;

    public static final Long TYPE_STOCK_TAKING = 100L;
    public static final Long TYPE_PO = 101L;
    public static final Long TYPE_PICK = 102L; //测试一下
    public static final Long TYPE_SHELVE = 103L;
    public static final Long TYPE_PROCUREMENT = 104L;
    public static final Long TYPE_STOCK_TRANSFER = 105L;
    public static final Long TYPE_ATTIC_SHELVE = 106L;
    public static final Long TYPE_PICK_UP_SHELVE = 107L;
    public static final Long TYPE_SEED = 108L;

    public static final Long TYPE_QC = 110L;
    public static final Long TYPE_SHIP = 111L;

    public static final Long EVENT_TASK_FINISH  = 100000L;
    public static final Long EVENT_SO_ACCEPT = 100001L;
    public static final Long EVENT_WAVE_RELEASE = 100002L;
    public static final Long EVENT_OUT_OF_STOCK = 100003L;
    public static final Long EVENT_PROCUREMENT_CANCEL = 100004L;
    public static final Long EVENT_STOCK_TRANSFER_CANCEL = 100005L;
}
