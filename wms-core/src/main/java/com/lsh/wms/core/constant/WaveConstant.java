package com.lsh.wms.core.constant;

/**
 * Created by zengwenjun on 16/7/20.
 */
public class WaveConstant {
    /** 波次状态，10-新建，20-确定释放，30-释放完成，40-释放失败，50-已完成[完全出库], 100－取消 */
    public static int STATUS_NEW = 10;
    public static int STATUS_RELEASE_START = 20;
    public static int STATUS_RELEASE_SUCC = 30;
    public static int STATUS_RELEASE_FAIL = 40;
    public static int STATUS_SUCC = 50;
    public static int STATUS_CANCEL = 100;
}
