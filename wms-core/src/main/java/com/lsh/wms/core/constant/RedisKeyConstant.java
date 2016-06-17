package com.lsh.wms.core.constant;


public class RedisKeyConstant {

    /**
     * 升级-appkey与产品包id的对应关系
     */
    public static final String UP_PKG_KEY = "up:pkg:key:{0}";
    /**
     * 升级-产品包版本信息
     */
    public static final String UP_PKG_VER = "up:pkg:ver:{0}";
    /**
     * 升级-版本详细信息
     */
    public static final String UP_VER_INFO = "up:ver:info:{0}";
    /**
     * 升级-升级规则
     */
    public static final String UP_RULE = "up:rule:{0}";
    /**
     * 升级-版本规则列表
     */
    public static final String UP_VER_RULE_LIST = "up:ver:rule:list:{0}";
    /**
     * 升级-版本规则关联信息
     */
    public static final String UP_VER_RULE_INFO = "up:ver:rule:info:{0}";
    /**
     * 升级-版本规则条件列表
     */
    public static final String UP_VER_RULE_CONLIST = "up:ver:rule:conlist:{0}";
    /**
     * 升级-版本规则条件明细
     */
    public static final String UP_VER_RULE_CONINFO = "up:ver:rule:coninfo:{0}";

    /********媒资相关********/
    public static final String MD_VIDEO_INFO = "md:video:info:{0}";
    public static final String MD_IMAGE_INFO = "md:image:info:{0}";

}
