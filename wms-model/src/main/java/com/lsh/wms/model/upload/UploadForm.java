package com.lsh.wms.model.upload;

/**
 * Created by xiaoma on 16/2/18.
 */
public class UploadForm {

    public Integer type;
    public String  appkey;
    public String source;
    public String busicode;
    public String name;
    public String icon_url;
    public String img_base64;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBusicode() {
        return busicode;
    }

    public void setBusicode(String busicode) {
        this.busicode = busicode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getImg_base64() {
        return img_base64;
    }

    public void setImg_base64(String img_base64) {
        this.img_base64 = img_base64;
    }
}
