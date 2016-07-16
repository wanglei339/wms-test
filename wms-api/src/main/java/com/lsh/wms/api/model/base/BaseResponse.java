package com.lsh.wms.api.model.base;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by peter on 16/7/6.
 */
public class BaseResponse implements Serializable{
    /**
     * 返回信息状态码
     */
    private Integer status;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回时间
     */
    private Date dataKey;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDataKey() {
        return dataKey;
    }

    public void setDataKey(Date dataKey) {
        this.dataKey = dataKey;
    }
}
