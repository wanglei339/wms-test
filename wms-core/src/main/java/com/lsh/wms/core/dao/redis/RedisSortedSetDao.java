package com.lsh.wms.core.dao.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Set;

/**
 * redis sorted set
 */
@Repository
public class RedisSortedSetDao extends RedisBaseDao {

    @Resource(name = "redisTemplate_r")
    private ZSetOperations<String, String> zSetOp_r;

    @Resource(name = "redisTemplate_w")
    private ZSetOperations<String, String> zSetOp_w;

    /**
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public Set<String> range(String key, long start, long stop) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return zSetOp_r.range(key, start, stop);
    }

    /**
     * 返回个数
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public Long count(String key,double start,double stop){
        return zSetOp_r.count(key, start, stop);
    }


    /**
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean add(String key, String value, double score) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return zSetOp_w.add(key, value, score);
    }

    /**
     * @param key
     * @param set
     * @return
     */
    public Long add(String key, Set<ZSetOperations.TypedTuple<String>> set) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return zSetOp_w.add(key, set);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public Long remove(String key, String... values) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return zSetOp_w.remove(key, values);
    }

    /**
     * 删除指定范围元素
     * @param key
     * @param start 从第几个元素
     * @param end 到第几个元素结束
     * @return 删除元素个数
     */
    public Long removeRange(String key, long start, long end) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return zSetOp_w.removeRange(key, start, end);
    }

    /**
     * 判断值是否存在
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean hasZSet(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return zSetOp_r.rank(key, value) != null;
    }

}
