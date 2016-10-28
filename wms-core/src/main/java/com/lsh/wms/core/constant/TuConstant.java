package com.lsh.wms.core.constant;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午8:37
 */
public class TuConstant {
    //1待装车 2.装车中 5.已装车 9.已发货
    public static final Integer UNLOAD = 1;
    public static final Integer IN_LOADING = 2;
    public static final Integer LOAD_OVER = 5;
    public static final Integer SHIP_OVER = 9;
    //rf的尾货显示开关
    public static final Integer RF_CLOSE_REST = 1;
    public static final Integer RF_OPEN_REST = 0;
    //tu的大店小店
    public static final Integer SCALE_STORE = 1; // 小店
    public static final Integer SCALE_HYPERMARKET = 2; // 大店



}
