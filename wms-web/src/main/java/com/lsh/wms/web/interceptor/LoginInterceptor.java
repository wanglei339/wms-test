package com.lsh.wms.web.interceptor;

import com.lsh.wms.web.constant.WebConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isStaticResource(request) || isNotFilter(request)) {
            logger.debug("不需要验证登录的请求：{}", request.getServletPath());
            return super.preHandle(request, response, handler);
        }
        if (isLogin(request.getSession())) {
            return super.preHandle(request, response, handler);
        } else {
            response.sendRedirect(request.getContextPath() + WebConstant.LOGIN_URL);
            return false;
        }
    }

    public static boolean isStaticResource(HttpServletRequest req) {
        String requestPath = req.getServletPath();
        if (StringUtils.lowerCase(requestPath).indexOf(StringUtils.lowerCase("/assets/")) > -1) {
            return true;
        }
        return false;
    }

    public static boolean isNotFilter(HttpServletRequest req) {
        String requestPath = req.getServletPath();
        String[] filterExcludes = WebConstant.LOGIN_FILTER_EXCLUDES;
        for (String filterExclude : filterExcludes) {
            if (StringUtils.lowerCase(requestPath).indexOf(StringUtils.lowerCase(filterExclude)) > -1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLogin(HttpSession session) {
        if (session != null && session.getAttribute(WebConstant.SESSION_KEY_USER) != null) {
            return true;
        } else {
            return false;
        }
    }

}
