package com.lsh.wms.core.constant;

import com.lsh.base.common.utils.ObjUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 升级管理相关
 */
public class UpgradeConstant {

    /**
     * 版本号正则
     */
    public static Pattern PATTERN_VER_STR = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
    /**
     * 升级文件状态-初始化
     */
    public static final int UP_FILE_STATUS_INIT = 0;
    /**
     * 升级文件状态-成功
     */
    public static final int UP_FILE_STATUS_SUCCESS = 1;
    /**
     * 升级文件状态-失败
     */
    public static final int UP_FILE_STATUS_FAIL = 2;

    /**
     * 规则类型-黑名单
     */
    public static final int RULE_TYPE_BLACK = 1;
    /**
     * 规则类型-白名单
     */
    public static final int RULE_TYPE_WHITE = 2;

    /**
     * 判断类型-在范围内
     */
    public static final int JUDGE_WAY_IN = 1;
    /**
     * 判断类型-不在范围内
     */
    public static final int JUDGE_WAY_OUT = 2;

    /**
     * 升级规则ID-强制升级
     */
    public static final int RULE_ID_FORCEUP = 1;
    /**
     * 升级规则ID-按地区限制升级
     */
    public static final int RULE_ID_AREA = 2;

    /**
     * 字符串版本号转整数版本号
     * @param verStr
     * @return
     */
    public static Integer verStr2Int(String verStr) {
        if (StringUtils.isEmpty(verStr)) {
            return 0;
        }
        Matcher matcher = PATTERN_VER_STR.matcher(verStr);
        if (matcher.find()) {
            Integer f = ObjUtils.toInteger(matcher.group(1), 0);
            Integer s = ObjUtils.toInteger(matcher.group(2), 0);
            Integer t = ObjUtils.toInteger(matcher.group(3), 0);
            return f * 1000000 + s * 1000 + t;
        }
        return 0;
    }

}
