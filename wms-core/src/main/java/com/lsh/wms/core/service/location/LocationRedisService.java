package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/9/5 下午12:00
 */
@Component
public class LocationRedisService {
    @Autowired
    private RedisHashDao redisHashDao;
    @Autowired
    private RedisStringDao redisStringDao;

    public static final Integer timeout = 600;//600s 过期时间

    private static Logger logger = LoggerFactory.getLogger(LocationRedisService.class);

    public void insertLocationRedis(BaseinfoLocation location) {
        if (location == null || location.getLocationId() == null) {
            logger.info("--新增位置缓存信息，数据为空--");
            return;
        }
        //hash值插入 (locationId-location)
        String redistKey = StrUtils.formatString(RedisKeyConstant.LOCATION_LOCATIONID, location.getLocationId());
        Map<String, String> locationMap = new HashMap<String, String>();
        locationMap.put("locationId", ObjUtils.toString(location.getLocationId(), ""));
        locationMap.put("locationCode", ObjUtils.toString(location.getLocationCode(), ""));
        locationMap.put("fatherId", ObjUtils.toString(location.getFatherId(), ""));
        locationMap.put("leftRange", ObjUtils.toString(location.getLeftRange(), ""));
        locationMap.put("rightRange", ObjUtils.toString(location.getRightRange(), ""));
        locationMap.put("level", ObjUtils.toString(location.getLevel(), ""));
        locationMap.put("type", ObjUtils.toString(location.getType(), ""));
        locationMap.put("typeName", ObjUtils.toString(location.getTypeName(), ""));
        locationMap.put("isLeaf", ObjUtils.toString(location.getIsLeaf(), ""));
        locationMap.put("isValid", ObjUtils.toString(location.getIsValid(), ""));
        locationMap.put("canStore", ObjUtils.toString(location.getCanStore(), ""));
        locationMap.put("containerVol", ObjUtils.toString(location.getContainerVol(), ""));
        locationMap.put("regionNo", ObjUtils.toString(location.getRegionNo(), ""));
        locationMap.put("passageNo", ObjUtils.toString(location.getPassageNo(), ""));
        locationMap.put("shelfLevelNo", ObjUtils.toString(location.getShelfLevelNo(), ""));
        locationMap.put("binPositionNo", ObjUtils.toString(location.getBinPositionNo(), ""));
        locationMap.put("createdAt", ObjUtils.toString(location.getCreatedAt(), ""));
        locationMap.put("updatedAt", ObjUtils.toString(location.getUpdatedAt(), ""));
        locationMap.put("classification", ObjUtils.toString(location.getClassification(), ""));
        locationMap.put("canUse", ObjUtils.toString(location.getCanUse(), ""));
        locationMap.put("isLocked", ObjUtils.toString(location.getIsLocked(), ""));
        locationMap.put("curContainerVol", ObjUtils.toString(location.getCurContainerVol(), ""));
        locationMap.put("description", ObjUtils.toString(location.getDescription(), ""));
        locationMap.put("storeNo", ObjUtils.toString(location.getStoreNo(), ""));

        redisHashDao.putAll(redistKey, locationMap);

        //string值插入k-v (code-locationId)
        String redisCodeKey = StrUtils.formatString(RedisKeyConstant.LOCATION_CODE, location.getLocationCode());
        redisStringDao.set(redisCodeKey, location.getLocationId());
    }

    /**
     * 通过locationId获取redis中的hash值
     *
     * @param locationId 位置id
     * @return 位置参数map
     */
    public Map<String, String> getRedisLocation(Long locationId) {
        if (locationId == null) {
            logger.info("--新增位置缓存信息，数据为空--");
            return null;
        }
        String redistKey = StrUtils.formatString(RedisKeyConstant.LOCATION_LOCATIONID, locationId);
        Map<String, String> locationMap = redisHashDao.entries(redistKey);
        return locationMap;
    }

    /**
     * 通过位置编码查找redis缓存中的locationId
     *
     * @param code 位置编码
     * @return 位置id
     */
    public Long getRedisLocationIdByCode(String code) {
        if (null == code) {
            logger.info("--新增位置缓存信息，数据为空--");
        }
        String redisKey = StrUtils.formatString(RedisKeyConstant.LOCATION_CODE, code);
        String idStr = redisStringDao.get(redisKey);
        Long locationId = ObjUtils.toLong(idStr);
        return locationId;
    }


    /**
     * 通过id删除
     * @param locationId
     */
    public void delLocationRedis(Long locationId) {
        String redistKey = StrUtils.formatString(RedisKeyConstant.LOCATION_LOCATIONID, locationId);
        redisHashDao.delete(redistKey);
    }

    /**
     * 删除code-locationId的缓存
     * @param code 位置编码
     */
    public  void delLocationCodeRedis(String code){
        String redisKey = StrUtils.formatString(RedisKeyConstant.LOCATION_CODE,code);
        redisStringDao.delete(redisKey);
    }


}
