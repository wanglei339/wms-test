package com.lsh.wms.model;

/**
 * Created by xiaoma on 16/3/18.
 */
public class ClientInfo {

    private Long uid;//用户id
    private String utoken;//用户token
    private String random;//随机码，唯一标识单次请求
    private String appversion;//客户端版本
    private String appkey;//产品分包控制（appkey=操作系统+渠道+型号）
    private String sign;// 根据加密算法得到的加密字符串。（指定加密接口传，普通接口可以不传）
    private Integer ostype;//平台类型（1、ios，2、android、3wp）


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUtoken() {
        return utoken;
    }

    public void setUtoken(String utoken) {
        this.utoken = utoken;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getOstype() {
        return ostype;
    }

    public void setOstype(Integer ostype) {
        this.ostype = ostype;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "uid='" + uid + '\'' +
                ", utoken='" + utoken + '\'' +
                ", random='" + random + '\'' +
                ", appversion='" + appversion + '\'' +
                ", appkey='" + appkey + '\'' +
                ", sign='" + sign + '\'' +
                ", ostype='" + ostype + '\'' +
                '}';
    }
}
