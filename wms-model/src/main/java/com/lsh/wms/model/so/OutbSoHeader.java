package com.lsh.wms.model.so;

import java.io.Serializable;
import java.util.Date;

public class OutbSoHeader implements Serializable {

	/**  */
    private Long id;
	/** 仓库ID */
    private Long warehouseId;
	/** 出库订单号 */
    private String soCode;
	/** 下单用户 */
    private String soUser;
	/** 货主 */
    private Long ownerUid;
	/** 订单类型 1进货单，2退货单 */
    private Integer orderType;
	/** TMS线路号 */
    private Long waveId;
	/** TMS顺序号 */
    private Integer waveIndex;
	/** 交货时间 */
    private Date transTime;
	/** 订单状态，0取消，1正常，2拣货，3QC,4出库 */
    private Integer orderStatus;
	/** 收货地址 */
    private String deliveryAddrs;
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
	
	public String getSoCode(){
		return this.soCode;
	}
	
	public void setSoCode(String soCode){
		this.soCode = soCode;
	}
	
	public String getSoUser(){
		return this.soUser;
	}
	
	public void setSoUser(String soUser){
		this.soUser = soUser;
	}
	
	public Long getOwnerUid(){
		return this.ownerUid;
	}
	
	public void setOwnerUid(Long ownerUid){
		this.ownerUid = ownerUid;
	}
	
	public Integer getOrderType(){
		return this.orderType;
	}
	
	public void setOrderType(Integer orderType){
		this.orderType = orderType;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Integer getWaveIndex(){
		return this.waveIndex;
	}
	
	public void setWaveIndex(Integer waveIndex){
		this.waveIndex = waveIndex;
	}
	
	public Date getTransTime(){
		return this.transTime;
	}
	
	public void setTransTime(Date transTime){
		this.transTime = transTime;
	}
	
	public Integer getOrderStatus(){
		return this.orderStatus;
	}
	
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
	}
	
	public String getDeliveryAddrs(){
		return this.deliveryAddrs;
	}
	
	public void setDeliveryAddrs(String deliveryAddrs){
		this.deliveryAddrs = deliveryAddrs;
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
