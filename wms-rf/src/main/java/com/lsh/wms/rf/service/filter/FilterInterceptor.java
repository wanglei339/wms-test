package com.lsh.wms.rf.service.filter;

import com.alibaba.dubbo.rpc.RpcContext;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/3.
 */
@Aspect
@Component
public class FilterInterceptor{
    private static Logger logger = LoggerFactory.getLogger(FilterInterceptor.class);

    @Autowired
    private RedisStringDao redisStringDao;

    /**
     * 切入点表达式.
     */
    @Pointcut("execution(* com.lsh.wms.rf.service.*.*Service.*(..))")
    public void declareJointPointExpression(){}


    @Around("declareJointPointExpression()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        if("0".equals(PropertyUtils.getString("variable"))) {
            try {
               return pjp.proceed();
            } catch (Throwable ex) {
                throw ex;
            }
        }else{
//        String mname = pjp.getSignature().getName();
//        String cname = pjp.getTarget().getClass().getName();
            logger.info("method name ~~~~~~~~~~"+pjp.getSignature().getName() +"~~~~~~~~~~~~~~~~");
            if("userLogin".equals(pjp.getSignature().getName())){
                try {
                  return pjp.proceed();
                } catch (Throwable ex) {
                    throw ex;
                }
            }else{
                logger.info("~~~~~~~~~~~~~~~~~~~~~~~~");
                HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
                Map<String, String> map = new HashMap<String, String>();
                String utoken = request.getHeader("utoken");
                String uid =request.getHeader("uid");
                String key = StrUtils.formatString(RedisKeyConstant.USER_UID_TOKEN,uid);
                //redis中获取key
                String value = redisStringDao.get(key);
                if (value == null || !value.equals(utoken)) {
                    throw new BizCheckedException("2660003");
                }else{
                    //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
                    redisStringDao.expire(key, PropertyUtils.getLong("tokenExpire"));
                    try {
                      return pjp.proceed();
                    } catch (Throwable ex) {
                        throw ex;

                    }
                }

            }

        }

    }

}
