package com.lsh.wms.core.dao.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Set;

/**
 * redis set
 */
@Repository
public class RedisSetDao extends RedisBaseDao {

    @Resource(name = "redisTemplate_r")
    private SetOperations<String, String> setOp_r;

    @Resource(name = "redisTemplate_w")
    private SetOperations<String, String> setOp_w;

    public Set<String> members(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return setOp_r.members(key);
    }

    public Boolean isMember(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return setOp_r.isMember(key, value);
    }

    public void add(String key, String... values) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        setOp_w.add(key, values);
    }

    public void remove(String key, String... values) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        setOp_w.remove(key, values);
    }

}
