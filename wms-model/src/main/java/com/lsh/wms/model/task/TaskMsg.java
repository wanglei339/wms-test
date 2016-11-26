package com.lsh.wms.model.task;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class TaskMsg implements Serializable {

	/**  */
    private Long id;
	/** 源任务id */
    private Long sourceTaskId;
	/** 消息类型 */
    private Long type;
	/** 消息状态 */
    private Long status;
	/** 业务id, 可以使托盘码，库位码等业务id */
    private Long businessId;
	/** 异常代码 */
    private String errorCode;
	/** 异常原因 */
    private String errorMsg;
	/** 消息体，json串 */
    private String msgContent;
	/** 消息生成时间 */
    private Long timestamp;
	/** 优先级 */
    private Long priority;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt = DateUtils.getCurrentSeconds();
    private Map<String, Object> msgBody;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getSourceTaskId(){
		return this.sourceTaskId;
	}
	
	public void setSourceTaskId(Long sourceTaskId){
		this.sourceTaskId = sourceTaskId;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getBusinessId(){
		return this.businessId;
	}
	
	public void setBusinessId(Long businessId){
		this.businessId = businessId;
	}
	
	public String getErrorCode(){
		return this.errorCode;
	}
	
	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg(){
		return this.errorMsg;
	}
	
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	public String getMsgContent(){
        this.msgBody = JsonUtils.json2Obj(msgContent, Map.class);
		return this.msgContent;
	}
	
	public void setMsgContent(String msgContent){
		this.msgContent = msgContent;
	}
	
	public Long getTimestamp(){
		return this.timestamp;
	}
	
	public void setTimestamp(Long timestamp){
		this.timestamp = timestamp;
	}
	
	public Long getPriority(){
		return this.priority;
	}
	
	public void setPriority(Long priority){
		this.priority = priority;
	}
	
	public Long getCreatedAt(){
		return this.createdAt;
	}
	
	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}
	
	public Long getUpdatedAt(){
		return this.updatedAt;
	}
	
	public void setUpdatedAt(Long updatedAt){
		this.updatedAt = updatedAt;
	}


    public Map<String, Object> getMsgBody() {
        msgBody = JsonUtils.json2Obj(msgContent, Map.class);
        return msgBody;
    }

    public void setMsgBody(Map<String, Object> msgBody) {
        this.msgBody = msgBody;
        this.msgContent = JsonUtils.obj2Json(msgBody);
    }

}
