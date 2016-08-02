package com.lsh.wms.core.service.user;

import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixin-mac on 16/7/28.
 */
@Component
public class UserService {
    @Autowired
    private RedisStringDao redisStringDao;
    private RedisTemplate redisTemplate;
    //TODO 账号bean注入

    public Map<String, Long> login(String userName,String passeord){
        //返回token UName
        // TODO: 16/7/28  登陆成功
        long token = RandomUtils.genId();
        long uid = 45454;
        //userid token 加入缓存
        String key = StrUtils.formatString(RedisKeyConstant.USER_UID_TOKEN,uid);
        redisStringDao.set(key,token);
        //设置token失效时间 8小时
        redisStringDao.expire(key, PropertyUtils.getLong("tokenExpire"));
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("uid",uid);
        map.put("utoken",token);
        return map;
    }


}
