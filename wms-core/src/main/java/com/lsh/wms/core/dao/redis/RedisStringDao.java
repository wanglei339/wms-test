package com.lsh.wms.core.dao.redis;


import com.lsh.base.common.utils.ObjUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * redis string
 */
@Repository
public class RedisStringDao extends RedisBaseDao {

    @Resource(name = "redisTemplate_r")
    private ValueOperations<String, String> valOp_r;

    @Resource(name = "redisTemplate_w")
    private ValueOperations<String, String> valOp_w;


    public Long increment(String key) {
        return valOp_r.increment(key, 1L);
    }

    public String get(String key) {
        return valOp_r.get(key);
    }

    public void set(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        String valueStr = ObjUtils.toString(value, "");
        if (StringUtils.isBlank(valueStr)) {
            valueStr = NULL_STR;
        }
        valOp_w.set(key, valueStr);
    }

}
