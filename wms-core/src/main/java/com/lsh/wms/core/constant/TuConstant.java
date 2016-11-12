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
    public static final Integer RF_CLOSE_STATE = 2; //关闭rf的跳过开关状态
    public static final Integer RF_OPEN_STATE = 1;  //开启rf的的开启状态
    //tu的大店小店
    public static final Integer SCALE_STORE = 1; // 小店
    public static final Integer SCALE_HYPERMARKET = 2; // 大店
    //是否贵品
    public static final Integer IS_REST = 1;
    public static final Integer NOT_REST = 0;
    //是否余货
    public static final Integer IS_EXPENSIVE = 1;
    public static final Integer NOT_EXPENSIVE = 0;
    //类型 1-门店 2-优供
    public static final Integer TYPE_STORE = 1;
    public static final Integer TYPE_YOUGONG = 2;
    //仓库名字



}
