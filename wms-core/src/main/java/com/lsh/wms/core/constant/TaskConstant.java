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
    public static final Long TYPE_SET_GOODS = 109L;

    public static final Long TYPE_QC = 110L;
    public static final Long TYPE_SHIP = 111L;

    public static final Long TYPE_BACK_IN_STORAGE = 112L;
    public static final Long TYPE_BACK_SHELVE = 113L;
    public static final Long TYPE_BACK_OUT = 114L;
    public static final Long TYPE_MERGE = 115L;

    public static final Long TYPE_DIRECT_SHIP = 116L;


    public static final Long EVENT_TASK_FINISH  = 100000L;
    public static final Long EVENT_SO_ACCEPT = 100001L;
    public static final Long EVENT_WAVE_RELEASE = 100002L;
    public static final Long EVENT_OUT_OF_STOCK = 100003L;
    public static final Long EVENT_PROCUREMENT_CANCEL = 100004L;
    public static final Long EVENT_STOCK_TRANSFER_CANCEL = 100005L;

    //直流和在库的区别sub_type
    public static final Long TASK_INBOUND = 0L; //在库
    public static final Long TASK_DIRECT = 1L; //直流订单收货
    public static final Long TASK_STORE_DIRECT = 2L; //直流门店收货
    public static final Long TASK_DIRECT_SMALL_SHIP = 5L; //小店门店发货
    public static final Long TASK_DIRECT_LARGE_SHIP = 6L; //大店门店发货

    public static final Long QC_TYPE_QC_GROUP = 20L; //QC并组盘
    public static final Long QC_TYPE_ONLY_GROUP = 21L; //只组盘






    // 作业模式
    public static final Long MODE_INBOUND = 1L; // 在库
    public static final Long MODE_DIRECT = 2L; // 直流
    public static final Long MODE_MIX = 3L; // 直流
    //是否跳过QC的Q明细
    public static final int QC_SKIP = 1; // 跳过
    public static final int QC_NOT_SKIP = 0; // 不跳过

}
