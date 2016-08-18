package com.lsh.wms.model.task;

import java.io.Serializable;
import java.util.Date;

public class TaskTrigger implements Serializable {

	/**  */
    private Long id;
	/** 触发器名称 */
    private String name;
	/** 起始任务类型 */
    private Long oriType;
	/** 起始任务子类型*/
	private Long oriSubType;
	/** 起始执行函数 */
    private String oriMethod;
	/** 目标任务类型 */
    private Long destType;
	/** 目标执行函数 */
    private String destMethod;
	/** 1-执行开始前，2-执行结束后，3-异常发生时 */
    private Long timming;
	/** 异常代码 */
    private Long exception;
	/** 优先级 */
    private Long priority;
	/** 0-无效，1-有效 */
    private Long isValid;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Long getOriType(){
		return this.oriType;
	}
	
	public void setOriType(Long oriType){
		this.oriType = oriType;
	}

	public Long getOriSubType() {
		return oriSubType;
	}

	public void setOriSubType(Long oriSubType) {
		this.oriSubType = oriSubType;
	}

	public String getOriMethod(){
		return this.oriMethod;
	}
	
	public void setOriMethod(String oriMethod){
		this.oriMethod = oriMethod;
	}
	
	public Long getDestType(){
		return this.destType;
	}
	
	public void setDestType(Long destType){
		this.destType = destType;
	}
	
	public String getDestMethod(){
		return this.destMethod;
	}
	
	public void setDestMethod(String destMethod){
		this.destMethod = destMethod;
	}
	
	public Long getTimming(){
		return this.timming;
	}
	
	public void setTimming(Long timming){
		this.timming = timming;
	}
	
	public Long getException(){
		return this.exception;
	}
	
	public void setException(Long exception){
		this.exception = exception;
	}
	
	public Long getPriority(){
		return this.priority;
	}
	
	public void setPriority(Long priority){
		this.priority = priority;
	}
	
	public Long getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Long isValid){
		this.isValid = isValid;
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
	
	
}
