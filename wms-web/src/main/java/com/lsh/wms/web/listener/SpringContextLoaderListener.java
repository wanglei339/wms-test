package com.lsh.wms.web.listener;

import com.lsh.base.common.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

public class SpringContextLoaderListener extends ContextLoaderListener {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            super.contextInitialized(event);
            ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
            SpringUtils.setApplicationContext(ctx);
        } catch (Exception ex) {
            logger.error("设置spring context环境异常！", ex);
        }
    }

}
