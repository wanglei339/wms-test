package com.lsh.wms.core.service.tu;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/26 上午11:47
 */
@Component
public class TuRedisService {
    @Autowired
    private RedisHashDao redisHashDao;
    @Autowired
    private RedisStringDao redisStringDao;

    public static final Integer timeout = 86400;//600s 过期时间 一天
    private static Logger logger = LoggerFactory.getLogger(TuRedisService.class);

    /**
     * 将托盘(板子)的detail的信息写入redis
     *
     * @param containerDetail
     */
    public void insertTuContainerRedis(Map<String, Object> containerDetail) {
        if (null == containerDetail || containerDetail.get("containerId") == null) {
            logger.info("--新增位置缓存信息，数据为空--");
            return;
        }
        //哈希值插入(containerId-containerDetail)
        String redisKey = StrUtils.formatString(RedisKeyConstant.TU_CONTAINER, containerDetail.get("containerId"));
        Map<String, String> containerDetailMap = new HashMap<String, String>();
        containerDetailMap.put("containerId", ObjUtils.toString(containerDetail.get("containerId"), ""));
        containerDetailMap.put("storeId", ObjUtils.toString(containerDetail.get("storeId"), ""));
        containerDetailMap.put("isLoaded", ObjUtils.toString(containerDetail.get("isLoaded"), ""));
        containerDetailMap.put("isExpensive", ObjUtils.toString(containerDetail.get("isExpensive"), ""));
        containerDetailMap.put("isRest", ObjUtils.toString(containerDetail.get("isRest"), ""));
        containerDetailMap.put("boxNum", ObjUtils.toString(containerDetail.get("boxNum"), ""));
        containerDetailMap.put("turnoverBoxNum", ObjUtils.toString(containerDetail.get("turnoverBoxNum"), ""));
        containerDetailMap.put("containerNum", ObjUtils.toString(containerDetail.get("containerNum"), ""));
        redisHashDao.putAll(redisKey, containerDetailMap);
        redisHashDao.expire(redisKey,timeout);
    }

    /**
     * 通过托盘码(合板的托盘码)获取redis的hash值
     * @param containerId
     * @return
     */
    public Map<String, String> getRedisTuContainerDetail(Long containerId) {
        if (null == containerId) {
            logger.info("--新增tu的托盘查询，数据为空--");
            return null;
        }
        String redisKey = StrUtils.formatString(RedisKeyConstant.TU_CONTAINER, containerId);
        Map<String, String> containerDetailMap = redisHashDao.entries(redisKey);
        return containerDetailMap;
    }
}
