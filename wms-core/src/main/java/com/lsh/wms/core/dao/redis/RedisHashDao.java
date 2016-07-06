package com.lsh.wms.core.dao.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * redis hash
 */
@Repository
public class RedisHashDao extends RedisBaseDao {

    @Resource(name = "redisTemplate_r")
    private HashOperations<String, String, String> hashOp_r;

    @Resource(name = "redisTemplate_w")
    private HashOperations<String, String, String> hashOp_w;

    public String get(String key, String field) {
        if (StringUtils.isBlank(key) && StringUtils.isBlank(field)) {
            return null;
        }
        return hashOp_r.get(key, field);
    }

    public void put(String key, String field, String value) {
        if (StringUtils.isBlank(key) && StringUtils.isBlank(field)) {
            return;
        }
        hashOp_w.put(key, field, value);
    }

    public Map<String, String> entries(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return hashOp_r.entries(key);
    }

    public void putAll(String key, Map map) {
        if (StringUtils.isBlank(key) && map == null) {
            return;
        }
        hashOp_w.putAll(key, map);
    }

    /**
     * 判断Key是否存在
     *
     * @param key
     * @param field
     * @return
     */
    public boolean hasKey(String key, String field) {
        return hashOp_r.hasKey(key, field);
    }

    /**
     * 删除Map中的key
     *
     * @param key
     * @param field
     * @return
     */
    public void delete(String key, String field) {
        hashOp_w.delete(key, field);
    }

}
