package com.lsh.wms.model.task;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.sun.java.swing.plaf.windows.WindowsBorders;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/16.
 */
public class TaskMsg implements Serializable {

    private Long id = RandomUtils.genId();
    private Long type;
    private Long sourceTaskId;
    private Long timestamp = DateUtils.getCurrentSeconds();
    private Long priority;
    private Map<String, Object> msgBody;

    public TaskMsg() {
        this.id = RandomUtils.genId();
        this.timestamp = DateUtils.getCurrentSeconds();
        this.priority = 0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getSourceTaskId() {
        return sourceTaskId;
    }

    public void setSourceTaskId(Long sourceTaskId) {
        this.sourceTaskId = sourceTaskId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Map<String, Object> getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(Map<String, Object> msgBody) {
        this.msgBody = msgBody;
    }

}
