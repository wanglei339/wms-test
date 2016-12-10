package com.lsh.wms.core.constant;

/**
 * @Author 吴昊 wuhao@lsh123.com
 * @Date 2016/12/07 下午9:01
 */
public class StockTakingConstant {
    /**
     * 盘点任务类型
     */

    public static final Long TYPE_TEMPOARY= 1L;//临时
    public static final Long TYPE_MOVE_OFF = 2L;//动销
    public static final Long TYPE_PLAN = 3L;//计划
    public static final Long TYPE_REPLAY = 4L;//复盘

    /**
     * 盘点状态
     */
    public static final Long Draft = 1L;

    public static final Long Assigned = 2L;

    public static final Long PendingAudit = 3L;

    public static final Long Done = 4L;

    public static final Long Cancel = 5L;
}
