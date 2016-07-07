package com.lsh.wms.web.utils;

import com.google.common.collect.Lists;
import com.lsh.base.common.utils.ExceptionUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.SpringUtils;
import com.lsh.wms.model.system.SysFunction;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.core.service.system.SysFunctionService;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.web.constant.WebConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class WebUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    private static SysFunctionService sysFunctionService;
    private static SysUserService sysUserService;

    static {
        if (sysFunctionService == null) {
            sysFunctionService = SpringUtils.getBean(SysFunctionService.class, "sysFunctionService");
        }
        if (sysUserService == null) {
            sysUserService = SpringUtils.getBean( SysUserService.class,"sysUserService");
        }
    }

    /**
     * 获取登录用户对象
     *
     * @param request
     * @return
     */
    public static SysUser getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }
        return (SysUser) session.getAttribute(WebConstant.SESSION_KEY_USER);
    }

    /**
     * 获取登录用户的中文用户名，为null时返回空字符串
     *
     * @param request
     * @return
     */
    public static String getLoginUserName(HttpServletRequest request) {
        SysUser sysUser = getLoginUser(request);
        if (sysUser == null) {
            return StringUtils.EMPTY;
        }
        return ObjUtils.ifNull(sysUser.getUserName(), StringUtils.EMPTY);
    }

    /**
     * 重定向至登录页面
     *
     * @param request
     * @param response
     */
    public static void gotoLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + WebConstant.LOGIN_URL);
        } catch (IOException e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    /**
     * 获取拥有权限的功能
     *
     * @param request
     * @param parentFuncId
     * @return
     */
    public static List<SysFunction> getUserSysFuncList(HttpServletRequest request, Integer parentFuncId) {
        SysUser sysUser = getLoginUser(request);
        if (sysUser == null) {
            return Lists.newArrayList();
        }
        List<SysFunction> funcList = sysFunctionService.getUserSysFuncList(sysUser.getId(), parentFuncId);
        if (funcList == null) {
            return Lists.newArrayList();
        }
        return funcList;
    }

    public static boolean isSysFuncActive(SysFunction func, Integer fid) {
        if (func == null || fid == null || fid <= 0) {
            return false;
        }
        if (func.getIsLeaf() != null && func.getIsLeaf() == 1) {
            //如果是叶子节点，直接判断id是否相同
            return func.getId() == fid;
        } else {
            //非叶子节点，递归activeFId的所有父节点，查看是否在父节点中
            Set<Integer> parentSysFuncIdSet = sysFunctionService.getAllParentSysFuncIdSet(fid);
            if (parentSysFuncIdSet.contains(func.getId())) {
                return true;
            }
        }
        return false;
    }

    public static String addUrlParam(String url, String key, String value) {
        if (url == null) {
            return url;
        }
        if (url.indexOf("?") >= 0) {
            return url + "&" + key + "=" + value;
        } else {
            return url + "?" + key + "=" + value;
        }
    }

}
