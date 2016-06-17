package com.lsh.wms.core.dao.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * redis list
 */
@Repository
public class RedisListDao extends RedisBaseDao {

    @Resource(name = "redisTemplate_r")
    private ListOperations<String, String> listOp_r;

    @Resource(name = "redisTemplate_w")
    private ListOperations<String, String> listOp_w;


    public Long leftPush(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_w.leftPush(key, value);
    }

    public String leftPop(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_w.leftPop(key);
    }

    public Long rightPush(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_w.rightPush(key, value);
    }

    public String rightPop(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_w.rightPop(key);
    }

    public Long size(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_r.size(key);
    }

    public List<String> range(String key, int start, int end) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_r.range(key, start, end);
    }

    public Long remove(String key, long i, String value) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_w.remove(key, i, value);
    }

    public String index(String key, long index) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return listOp_r.index(key, index);
    }

    public void set(String key, long index, String value) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        listOp_w.set(key, index, value);
    }

    public void trim(String key, long start, int end) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        listOp_r.trim(key, start, end);
    }

}
