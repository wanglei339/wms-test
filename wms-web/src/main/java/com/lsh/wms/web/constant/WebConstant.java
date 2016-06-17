package com.lsh.wms.web.constant;


import com.lsh.base.common.config.PropertyUtils;

public class WebConstant {

    /**
     * 不需要验证登录的url
     */
    public static final String[] LOGIN_FILTER_EXCLUDES = PropertyUtils.getStringArray("login.filter.excludes");
    /**
     * 登录用户session
     */
    public static final String SESSION_KEY_USER = "SESSION_KEY_USER";
    /**
     * 登录页面
     */
    public static final String LOGIN_URL = "/login";

    /**
     * 默认密码
     */
    public static final String PAZZWORD = "123456";

}
