package com.lsh.wms.model.system;

import java.io.Serializable;
import java.util.Date;

public class ModifyLog implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long modifyId;
	/** 日志类型 1 验收单修改数量 2 收货明细修改数量 */
    private Integer modifyType;
	/** 业务id */
    private Long businessId;
	/** 业务细单id */
    private String detailId;
	/** 修改信息 */
    private String modifyMessage;
	/** 操作人员id */
    private Long operator;
	/** 产生时间 */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getModifyId(){
		return this.modifyId;
	}
	
	public void setModifyId(Long modifyId){
		this.modifyId = modifyId;
	}
	
	public Integer getModifyType(){
		return this.modifyType;
	}
	
	public void setModifyType(Integer modifyType){
		this.modifyType = modifyType;
	}
	
	public Long getBusinessId(){
		return this.businessId;
	}
	
	public void setBusinessId(Long businessId){
		this.businessId = businessId;
	}
	
	public String getDetailId(){
		return this.detailId;
	}
	
	public void setDetailId(String detailId){
		this.detailId = detailId;
	}
	
	public String getModifyMessage(){
		return this.modifyMessage;
	}
	
	public void setModifyMessage(String modifyMessage){
		this.modifyMessage = modifyMessage;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
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
