package com.lsh.wms.model.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StaffPerformance implements Serializable {

	/**  */
    private Long id;
	/** 操作人员id */
    private Long operator;
	/** 任务类型 */
    private Long type;
	/** 任务子类型 */
    private Long subType;
	/** 直流在库标记 1在库 2直流 */
    private Integer businessMode;
	/** 任务工作量(ea计算) */
    private BigDecimal taskEaQty;
	/** 任务工作量(按外包装计算) */
    private BigDecimal taskPackQty;
	/** 工作任务量（按照板数） */
    private BigDecimal taskBoardQty;
	/** 托盘量 */
    private Integer containerQty;
	/** 行项目数量 */
    private Integer skuCount;
	/** 任务量 */
    private Integer taskQty;
	/**  盘点 任务量  */
    private Integer taskAmount;
	/** 完成日期 */
    private Long date;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;

	private String taskIds ;//add 表中没有该字段
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
	}
	
	public Long getSubType(){
		return this.subType;
	}
	
	public void setSubType(Long subType){
		this.subType = subType;
	}
	
	public Integer getBusinessMode(){
		return this.businessMode;
	}
	
	public void setBusinessMode(Integer businessMode){
		this.businessMode = businessMode;
	}
	
	public BigDecimal getTaskEaQty(){
		return this.taskEaQty;
	}
	
	public void setTaskEaQty(BigDecimal taskEaQty){
		this.taskEaQty = taskEaQty;
	}
	
	public BigDecimal getTaskPackQty(){
		return this.taskPackQty;
	}
	
	public void setTaskPackQty(BigDecimal taskPackQty){
		this.taskPackQty = taskPackQty;
	}
	
	public BigDecimal getTaskBoardQty(){
		return this.taskBoardQty;
	}
	
	public void setTaskBoardQty(BigDecimal taskBoardQty){
		this.taskBoardQty = taskBoardQty;
	}
	
	public Integer getContainerQty(){
		return this.containerQty;
	}
	
	public void setContainerQty(Integer containerQty){
		this.containerQty = containerQty;
	}
	
	public Integer getSkuCount(){
		return this.skuCount;
	}
	
	public void setSkuCount(Integer skuCount){
		this.skuCount = skuCount;
	}
	
	public Integer getTaskQty(){
		return this.taskQty;
	}
	
	public void setTaskQty(Integer taskQty){
		this.taskQty = taskQty;
	}
	
	public Integer getTaskAmount(){
		return this.taskAmount;
	}
	
	public void setTaskAmount(Integer taskAmount){
		this.taskAmount = taskAmount;
	}
	
	public Long getDate(){
		return this.date;
	}
	
	public void setDate(Long date){
		this.date = date;
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


	public String getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(String taskIds) {
		this.taskIds = taskIds;
	}
}
