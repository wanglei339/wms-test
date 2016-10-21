package com.lsh.wms.model.system;

import java.io.Serializable;
import java.util.Date;

public class SysLog implements Serializable {

	/**  */
    private Long id;

	private Long logId;

	/** 日志类型 1 ibd，2 obd 3 fret */
    private Integer logType;
	/**回调系统 1 wumart 2 链商OFC 3 erp*/
	private Integer targetSystem;
	/** 异常码 */
    private Long logCode;
	/** 异常信息 */
    private String logMessage;
	/** 产生时间 */
    private Long createdAt;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Integer getLogType(){
		return this.logType;
	}
	
	public void setLogType(Integer logType){
		this.logType = logType;
	}
	
	public Long getLogCode(){
		return this.logCode;
	}
	
	public void setLogCode(Long logCode){
		this.logCode = logCode;
	}
	
	public String getLogMessage(){
		return this.logMessage;
	}
	
	public void setLogMessage(String logMessage){
		this.logMessage = logMessage;
	}
	
	public Long getCreatedAt(){
		return this.createdAt;
	}
	
	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}


	public Integer getTargetSystem() {
		return targetSystem;
	}

	public void setTargetSystem(Integer targetSystem) {
		this.targetSystem = targetSystem;
	}
}
