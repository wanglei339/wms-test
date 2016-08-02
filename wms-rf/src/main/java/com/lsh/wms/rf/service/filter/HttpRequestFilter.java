package com.lsh.wms.rf.service.filter;

import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by lixin-mac on 16/7/29.
 */
public class HttpRequestFilter implements ContainerRequestFilter {

    // TODO: 16/7/29 能不能使用autowire自动注入
    @Autowired
    private RedisStringDao redisStringDao;


    public void filter(ContainerRequestContext context) throws IOException {
        MultivaluedMap<String, String> map = context.getHeaders();

        String uid = map.getFirst("uid");
        String token = map.getFirst("utoken");
        String key = StrUtils.formatString(RedisKeyConstant.USER_UID_TOKEN,uid);
        //redis中获取key
        String value = redisStringDao.get(key);
        // TODO: 16/7/30 校验token过期时间
        if (value == null || !value.equals(token)) {

        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redisStringDao.expire(key, PropertyUtils.getLong("tokenExpire"));
    }

    /**
     * 异常信息组装
     * @return
     */
    private Response bulidHeaderCheckNotPassResponse(){
        BaseResponse br = new BaseResponse();
        // TODO: 16/7/29 setHead setBody
        return Response.ok().status(Response.Status.OK).entity(br).build();
    }
}
