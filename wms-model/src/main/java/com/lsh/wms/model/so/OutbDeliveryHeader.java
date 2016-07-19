package com.lsh.wms.model.so;

import java.io.Serializable;
import java.util.Date;

public class OutbDeliveryHeader implements Serializable {

	/**  */
    private Long id;
	/** 仓库ID */
    private Long warehouseId;
	/** 集货区编码 */
    private String shippingAreaCode;
	/** 波次号 */
    private Long waveId;
	/** TMS线路 */
    private String transPlan;
	/** 最迟出库实际 */
    private Date transTime;
	/** 出库单号 */
    private String deliveryCode;
	/** 出库人员 */
    private String deliveryUser;
	/** 出库状态 0 未出库 ，1已出库 */
    private Integer deliveryType;
	/** 出库时间 */
    private Date deliveryTime;
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getWarehouseId(){
		return this.warehouseId;
	}
	
	public void setWarehouseId(Long warehouseId){
		this.warehouseId = warehouseId;
	}
	
	public String getShippingAreaCode(){
		return this.shippingAreaCode;
	}
	
	public void setShippingAreaCode(String shippingAreaCode){
		this.shippingAreaCode = shippingAreaCode;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public String getTransPlan(){
		return this.transPlan;
	}
	
	public void setTransPlan(String transPlan){
		this.transPlan = transPlan;
	}
	
	public Date getTransTime(){
		return this.transTime;
	}
	
	public void setTransTime(Date transTime){
		this.transTime = transTime;
	}
	
	public String getDeliveryCode(){
		return this.deliveryCode;
	}
	
	public void setDeliveryCode(String deliveryCode){
		this.deliveryCode = deliveryCode;
	}
	
	public String getDeliveryUser(){
		return this.deliveryUser;
	}
	
	public void setDeliveryUser(String deliveryUser){
		this.deliveryUser = deliveryUser;
	}
	
	public Integer getDeliveryType(){
		return this.deliveryType;
	}
	
	public void setDeliveryType(Integer deliveryType){
		this.deliveryType = deliveryType;
	}
	
	public Date getDeliveryTime(){
		return this.deliveryTime;
	}
	
	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime = deliveryTime;
	}
	
	public String getInsertby(){
		return this.insertby;
	}
	
	public void setInsertby(String insertby){
		this.insertby = insertby;
	}
	
	public String getUpdateby(){
		return this.updateby;
	}
	
	public void setUpdateby(String updateby){
		this.updateby = updateby;
	}
	
	public Date getInserttime(){
		return this.inserttime;
	}
	
	public void setInserttime(Date inserttime){
		this.inserttime = inserttime;
	}
	
	public Date getUpdatetime(){
		return this.updatetime;
	}
	
	public void setUpdatetime(Date updatetime){
		this.updatetime = updatetime;
	}
	
	
}
