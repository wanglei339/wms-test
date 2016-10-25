package com.lsh.wms.model.system;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
public class SysMsg implements Serializable{

    private Long id;
    private Long type;
    private Integer targetSystem;
    private Long timestamp = DateUtils.getCurrentSeconds();
    private String msgBody;

    public SysMsg(){}

    public SysMsg(Long id, String msgBody, Integer targetSystem, Long timestamp, Long type) {
        this.id = id;
        this.msgBody = msgBody;
        this.targetSystem = targetSystem;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public Integer getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(Integer targetSystem) {
        this.targetSystem = targetSystem;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
}
