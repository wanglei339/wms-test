package com.lsh.wms.core.service.redis.image;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.model.image.PubImage;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class ImageRedisService {
    @Autowired
    private RedisHashDao redisHashDao;

    public static final Integer timeout = 600;//600s

    private static Logger logger = LoggerFactory.getLogger(ImageRedisService.class);

    public void insertRedis(PubImage image) {
        if (image == null || image.getId() == null) {
            logger.info("--新增用户缓存信息，数据为空--");
            return;
        }
        String redistKey = StrUtils.formatString(RedisKeyConstant.MD_IMAGE_INFO, image.getUuid());
        Map<String, String> imageMap = new LinkedHashMap<String, String>();
        imageMap.put("uid", ObjUtils.toString(image.getUid(), ""));
        imageMap.put("pic_name", ObjUtils.toString(image.getPicName(),""));
        imageMap.put("pic_path", ObjUtils.toString(image.getPicPath(),""));
        imageMap.put("pic_url", ObjUtils.toString(image.getPicUrl(),""));
        imageMap.put("bus_type", ObjUtils.toString(image.getBusType(),""));
        imageMap.put("pic_width", ObjUtils.toString(image.getPicWidth(),""));
        imageMap.put("pic_height", ObjUtils.toString(image.getPicHeight(),""));
        imageMap.put("pic_clip", ObjUtils.toString(image.getClipParam(),""));
        imageMap.put("molecular", ObjUtils.toString(image.getMolecular(),""));
        imageMap.put("denominator", ObjUtils.toString(image.getDenominator(),""));
        imageMap.put("pic_audit", ObjUtils.toString(image.getStatus(),""));
        imageMap.put("status", ObjUtils.toString(image.getStatus(),""));
        imageMap.put("created_time", ObjUtils.toString(BigDecimal.valueOf(System.currentTimeMillis()).divide(new BigDecimal(1000)).longValue()));
        imageMap.put("updated_time", ObjUtils.toString(BigDecimal.valueOf(System.currentTimeMillis()).divide(new BigDecimal(1000)).longValue()));

        redisHashDao.putAll(redistKey, imageMap);
    }

    /**
     * 获取详情
     * @param uuid
     * @return
     */
    public Map<String,String> getRedisInfo(String uuid){
        if (uuid == null) {
            logger.info("--新增缓存信息，数据为空--");
            return null;
        }
        String redistKey = StrUtils.formatString(RedisKeyConstant.MD_IMAGE_INFO, uuid);
        Map<String, String> imageMap = redisHashDao.entries(redistKey);
        return imageMap;
    }

    /**
     * 获token
     * @param
     * @return
     */
    public Map<String,String> getTokenRedis(){

        String redistKey = StrUtils.formatString(RedisKeyConstant.MD_IMAGE_INFO);
        Map<String, String> imageMap = redisHashDao.entries(redistKey);
        if(imageMap == null || imageMap.size() ==0){
            return null;
        }
        return imageMap;
    }


    public void insertRedis(Map<String, String> resultMap) {
        if (resultMap == null || resultMap.size() == 0) {
            logger.info("--数据为空--");
            return;
        }
        String redistKey = StrUtils.formatString(RedisKeyConstant.MD_IMAGE_INFO);
        redisHashDao.putAll(redistKey, resultMap);
        redisHashDao.expire(redistKey,timeout);
    }



}
