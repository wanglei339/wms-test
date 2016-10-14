package com.lsh.wms.core.constant;

/**
 * Created by zengwenjun on 16/7/20.
 */
public class WaveConstant {
    /** 波次状态，10-新建，20-确定释放，30-释放完成，40-释放失败，50-已完成[完全出库], 100－取消 */
    public static int STATUS_NEW = 10;
    public static int STATUS_RELEASE_START = 20;
    public static int STATUS_RELEASE_SUCC = 30;
    public static int STATUS_PICK_SUCC = 31;
    public static int STATUS_QC_SUCC = 32;
    public static int STATUS_RELEASE_FAIL = 40;
    public static int STATUS_SUCC = 50;
    public static int STATUS_CANCEL = 100;

    /**
     * QC的异常状态的表现
     */
    public static int QC_EXCEPTION_NORMAL=0;  //qc正常
    public static int QC_EXCEPTION_LACK=1;  //qc少货
    public static int QC_EXCEPTION_OVERFLOW=2;   //多货
    public static int QC_EXCEPTION_NOT_MATCH=3;  //错货,不匹配
    public static int QC_EXCEPTION_DEFECT=4; //残次
    public static int QC_EXCEPTION_DATE=5;   //日期问题
    public static int QC_EXCEPTION_OTHER=6;   //其他的qc异常

    /**
     * QC异常的处理的状态
     */
    public static int QC_EXCEPTION_STATUS_UNDO=0;   //未处理
    public static int QC_EXCEPTION_STATUS_NORMAL=1;   //qc正常
    public static int QC_EXCEPTION_STATUS_DONE=2;   // 异常已经处理
    public static int QC_EXCEPTION_STATUS_SKIP=3;   //  异常忽略

    /**
     * QC次数的标识
     */
    public static int QC_TIMES_FIRST = 1;   //第一次QC
    public static int QC_TIMES_MORE = 2;    //复QC

    /**
     * QC产出的错误责任人
     */
    public static long QC_FAULT_NOMAL = 0L;   //没责任人,正常没出错
    public static long QC_FAULT_PICK = 1L;    //拣货人的失误
    public static long QC_FAULT_QC= 1L;       //QC人的失误

    /**
     * qc的状态
     */
    public static long QC_UNDONE = 0L;
    public static long QC_DONE_TO_COMBINATION = 1L;
    public static long QC_SKIP_TO_COMBINATION = 2L;







}
