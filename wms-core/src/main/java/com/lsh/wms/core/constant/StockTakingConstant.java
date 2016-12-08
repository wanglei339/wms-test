package com.lsh.wms.core.constant;

/**
 * @Author 吴昊 wuhao@lsh123.com
 * @Date 2016/12/07 下午9:01
 */
public class StockTakingConstant {
    /**
     * 盘点任务类型
     */
    public static final Integer TYPE_TEMPOARY= 1;//临时
    public static final Integer TYPE_MOVE_OFF = 2;//动销
    public static final Integer TYPE_PLAN = 3;//计划


    /**
     * 盘点状态
     */
    public static final Long Draft = 1L;

    public static final Long Assigned = 2L;

    public static final Long PendingAudit = 3L;

    public static final Long Done = 4L;

    public static final Long Cancel = 5L;
}
