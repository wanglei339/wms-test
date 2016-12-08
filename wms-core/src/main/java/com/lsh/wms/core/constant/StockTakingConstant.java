package com.lsh.wms.core.constant;

/**
 * @Author 吴昊 wuhao@lsh123.com
 * @Date 2016/12/07 下午9:01
 */
public class StockTakingConstant {
    /**
     * 盘点任务类型
     */
    public static final Long TYPE_TEMPOARY= 1l;//临时
    public static final Long TYPE_MOVE_OFF = 2l;//动销
    public static final Long TYPE_PLAN = 3l;//计划


    /**
     * 盘点状态
     */
    public static final Long Draft = 1L;

    public static final Long Assigned = 2L;

    public static final Long PendingAudit = 3L;

    public static final Long Done = 4L;

    public static final Long Cancel = 5L;
}
