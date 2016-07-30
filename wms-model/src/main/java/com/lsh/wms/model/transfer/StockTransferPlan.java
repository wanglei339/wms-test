package com.lsh.wms.model.transfer;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockTransferPlan implements Serializable {
	/** 移库计划id */
    private Long planId = 0L;
	/** 移库任务id */
    private Long taskId = 0L;
	/** 商品id */
    private Long itemId = 0L;
	/** 移入库位id */
    private Long fromLocationId = 0L;
	/** 移入库位id */
    private Long toLocationId = 0L;
	/** 数量 */
    private BigDecimal qty = BigDecimal.ZERO;
	/** 商品单位转换id */
    private String packName = "";
	/** 以包装单位计量的库移数量 */
    private BigDecimal uomQty = BigDecimal.ZERO;
	
	public Long getPlanId(){
		return this.planId;
	}
	
	public void setPlanId(Long planId){
		this.planId = planId;
	}
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getFromLocationId(){
		return this.fromLocationId;
	}
	
	public void setFromLocationId(Long fromLocationId){
		this.fromLocationId = fromLocationId;
	}
	
	public Long getToLocationId(){
		return this.toLocationId;
	}
	
	public void setToLocationId(Long toLocationId){
		this.toLocationId = toLocationId;
	}
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	public String getPackName(){
		return this.packName;
	}
	
	public void setPackName(String packName){
		this.packName = packName;
	}
	
	public BigDecimal getUomQty(){
		return this.uomQty;
	}
	
	public void setUomQty(BigDecimal uomQty){
		this.uomQty = uomQty;
	}
	
}
