package com.lsh.wms.model.wave;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class WaveQcException implements Serializable {

	/**  */
    private Long id;
	/** 波次id */
    private Long waveId = 0L;
	/** 商品id */
    private Long skuId = 0L;
	/** QC任务id */
    private Long qcTaskId = 0L;
	/** 0-正常，1-少货，2-多货，3-错货，4-残次，5-日期不好，6-其他异常 */
    private Long exceptionType = 0L;
	/** qc异常数量 */
    private BigDecimal exceptionQty = new BigDecimal("0");
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
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getQcTaskId(){
		return this.qcTaskId;
	}
	
	public void setQcTaskId(Long qcTaskId){
		this.qcTaskId = qcTaskId;
	}
	
	public Long getExceptionType(){
		return this.exceptionType;
	}
	
	public void setExceptionType(Long exceptionType){
		this.exceptionType = exceptionType;
	}
	
	public BigDecimal getExceptionQty(){
		return this.exceptionQty;
	}
	
	public void setExceptionQty(BigDecimal exceptionQty){
		this.exceptionQty = exceptionQty;
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
