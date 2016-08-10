package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BaseinfoShelflifeRule implements Serializable {

	/**  */
    private Long id;
	/** 规则ID */
	private Long ruleId;
	/** 保质期天数 */
    private BigDecimal shelfLife;
	/** 拒收期限% */
    private BigDecimal rejectionTerm;
	/** 拒发期限（%） */
    private BigDecimal refusalPeriod;
	/** 预警期限（%） */
    private BigDecimal warningPeriod;
	/**  */
	private Long createdAt = 0L;
	/**  */
	private Long updatedAt = 0L;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public Long getRuleId(){
		return this.ruleId;
	}

	public void setRuleId(Long ruleId){
		this.ruleId = ruleId;
	}
	
	public BigDecimal getShelfLife(){
		return this.shelfLife;
	}
	
	public void setShelfLife(BigDecimal shelfLife){
		this.shelfLife = shelfLife;
	}
	
	public BigDecimal getRejectionTerm(){
		return this.rejectionTerm;
	}
	
	public void setRejectionTerm(BigDecimal rejectionTerm){
		this.rejectionTerm = rejectionTerm;
	}
	
	public BigDecimal getRefusalPeriod(){
		return this.refusalPeriod;
	}
	
	public void setRefusalPeriod(BigDecimal refusalPeriod){
		this.refusalPeriod = refusalPeriod;
	}
	
	public BigDecimal getWarningPeriod(){
		return this.warningPeriod;
	}
	
	public void setWarningPeriod(BigDecimal warningPeriod){
		this.warningPeriod = warningPeriod;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}
}
