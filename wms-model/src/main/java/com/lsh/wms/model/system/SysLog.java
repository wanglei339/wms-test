package com.lsh.wms.model.system;

import java.io.Serializable;
import java.util.Date;

public class SysLog implements Serializable {

	/**  */
    private Long id;

	/** 流水号 */
	private Long logId;

	/** 业务id, 用于追查问题使用 */
	private Long businessId;

	/** 状态类型 0-新建，1-处理中，2-处理完成 */
	private Long status;

	/** 业务内部处理阶段, 为解决多步骤重传问题 */
	private Long step;

	/** 日志类型 1 ibd，2 obd 3 fret */
    private Integer logType;
	/**回调系统 1 wumart 2 链商OFC 3 erp*/
	private Integer targetSystem;
	/** 系统异常码 */
	private String sysCode;
	/** 系统异常信息*/
	private String SysMessage;
	/** 异常码 */
    private String logCode;
	/** 异常信息 */
    private String logMessage;
	/** 产生时间 */
    private Long createdAt;
	private Long updatedAt;
	
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
	
	public String getLogCode(){
		return this.logCode;
	}
	
	public void setLogCode(String logCode){
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

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getStep() {
		return step;
	}

	public void setStep(Long step) {
		this.step = step;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getSysMessage() {
		return SysMessage;
	}

	public void setSysMessage(String SYSMessage) {
		this.SysMessage = SYSMessage;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}
}
