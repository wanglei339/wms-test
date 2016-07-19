package com.lsh.wms.api.model.base;

import com.lsh.base.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/19
 * Time: 16/7/19.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.model.base.
 * desc:类功能描述
 */
public class Head implements Serializable{
    /**
     * 返回信息状态码
     */
    private Integer status;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回时间
     */
    private String timestamp = DateUtils.FORMAT_TIME_NO_BAR.format(new Date());



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
