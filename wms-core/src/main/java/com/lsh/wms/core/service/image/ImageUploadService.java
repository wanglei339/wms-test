package com.lsh.wms.core.service.image;

import com.lsh.base.ali.OssClientUtils;
import com.lsh.base.ali.StsServiceUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.model.image.PubImage;
import com.lsh.wms.core.service.redis.image.ImageRedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Component
public class ImageUploadService {
    private static Logger logger = LoggerFactory.getLogger(ImageUploadService.class);
    @Autowired
    private PubImageService pubImageService;

    @Autowired
    private ImageRedisService imageRedisService;


    public Map<String, String> getRedisInfo(String uuid) {
        return imageRedisService.getRedisInfo(uuid);
    }


    public PubImage insertImage(String picUrl,Long uid,int busType,String reduceRatio){
        if(StringUtils.isEmpty(picUrl)) return null;
        StsServiceUtils.ImageInfo imageInfo = StsServiceUtils.getImageInfo(picUrl);
        if(imageInfo == null) return null;

        Param param = new Param();
        if("1:1".equals(reduceRatio)){
            param = reduce1to1(imageInfo.getWidth(),imageInfo.getHeight());
        }
        PubImage pubImage = getImageByParam(param,uid,picUrl,busType);
        pubImageService.insert(pubImage);
        imageRedisService.insertRedis(pubImage);

        return pubImage;
    }

    private PubImage getImageByParam(Param param,Long uid,String picUrl,int busType){
        PubImage pubImage = new PubImage();
        pubImage.setUid(uid);
        pubImage.setUuid(OssClientUtils.getImageId());
        pubImage.setClipParam(param.getClipParam());
        pubImage.setPicAudit(1);
        pubImage.setBusType(busType);
        pubImage.setPicHeight(param.getHeight());
        pubImage.setPicWidth(param.getWidth());
        pubImage.setMolecular(param.getMolecular());
        pubImage.setDenominator(param.getDenominator());
        pubImage.setPicName("");
        pubImage.setStatus(1);
        pubImage.setPicPath("");
        pubImage.setPicUrl(picUrl);
        pubImage.setCreatedTime(new Date());
        pubImage.setUpdatedTime(new Date());

        return pubImage;

    }
    private String getUUID() {
        return new SimpleDateFormat("yyyyMM").format(new Date()) + RandomUtils.uuid2();
    }

    /**
     * 图片裁剪计算-小视频 1-1
     * @param w
     * @param h
     * @return
     */
    public Param reduce1to1(Integer w,Integer h){
        Param param = new Param();
        param.setWidth(w);
        param.setHeight(h);
        String clipParam = "";

        double rate = div(param.getWidth(),param.getHeight(),2);
        //  w/h >1 横图 @hxh-5rc
        if (rate >= 1) {
            clipParam = "@"+param.getHeight()+"x"+param.getHeight()+"-5rc";
            param.setDenominator(1);
            param.setMolecular(1);
        }else{
            clipParam = "@"+param.getWidth()+"x"+param.getWidth()+"-5rc";
            param.setDenominator(1);
            param.setMolecular(1);
        }
        param.setClipParam(clipParam);
        return param;
    }



    private  Double div(Integer a,Integer b,int scale){
        BigDecimal w = BigDecimal.valueOf(a);
        BigDecimal h = BigDecimal.valueOf(b);
        return w.divide(h, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    class Param{
        Integer molecular; //分子
        Integer denominator; //分母
        String clipParam;//裁剪参数
        Integer width;
        Integer height;



        public Integer getMolecular() {
            return molecular;
        }

        public void setMolecular(Integer molecular) {
            this.molecular = molecular;
        }

        public Integer getDenominator() {
            return denominator;
        }

        public void setDenominator(Integer denominator) {
            this.denominator = denominator;
        }

        public String getClipParam() {
            return clipParam;
        }

        public void setClipParam(String clipParam) {
            this.clipParam = clipParam;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }


}
