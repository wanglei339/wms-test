package com.lsh.wms.model.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InbReceiptHeader implements Serializable {

	/**  */
    private Long id;
	/** 仓库ID */
    private Long warehouseId;
	/** 预约单号 */
    private String bookingNum;
	/** 收货单号 */
    private Long receiptOrderId;
	/** 上架批次流水号 */
    private Long lotSeqId;
	/** 收货码头 */
    private String receiptWharf;
	/** 暂存区 */
    private String tempStoreArea;
	/** 托盘码 */
    private Long containerId;
	/** 分配库位 */
    private Long location;
	/** 实际库位 */
    private Long realLocation;
	/** 收货员 */
    private String receiptUser;
	/** 收货时间 */
    private Date receiptTime;
	/** 收货状态，1已收货，2已上架 */
    private Integer receiptStatus;
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;

	private Object receiptDetails;

	public Object getReceiptDetails() {
		return receiptDetails;
	}

	public void setReceiptDetails(Object receiptDetails) {
		this.receiptDetails = receiptDetails;
	}

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
	
	public String getBookingNum(){
		return this.bookingNum;
	}
	
	public void setBookingNum(String bookingNum){
		this.bookingNum = bookingNum;
	}
	
	public Long getReceiptOrderId(){
		return this.receiptOrderId;
	}
	
	public void setReceiptOrderId(Long receiptOrderId){
		this.receiptOrderId = receiptOrderId;
	}
	
	public Long getLotSeqId(){
		return this.lotSeqId;
	}
	
	public void setLotSeqId(Long lotSeqId){
		this.lotSeqId = lotSeqId;
	}
	
	public String getReceiptWharf(){
		return this.receiptWharf;
	}
	
	public void setReceiptWharf(String receiptWharf){
		this.receiptWharf = receiptWharf;
	}
	
	public String getTempStoreArea(){
		return this.tempStoreArea;
	}
	
	public void setTempStoreArea(String tempStoreArea){
		this.tempStoreArea = tempStoreArea;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getLocation(){
		return this.location;
	}
	
	public void setLocation(Long location){
		this.location = location;
	}
	
	public Long getRealLocation(){
		return this.realLocation;
	}
	
	public void setRealLocation(Long realLocation){
		this.realLocation = realLocation;
	}
	
	public String getReceiptUser(){
		return this.receiptUser;
	}
	
	public void setReceiptUser(String receiptUser){
		this.receiptUser = receiptUser;
	}
	
	public Date getReceiptTime(){
		return this.receiptTime;
	}
	
	public void setReceiptTime(Date receiptTime){
		this.receiptTime = receiptTime;
	}
	
	public Integer getReceiptStatus(){
		return this.receiptStatus;
	}
	
	public void setReceiptStatus(Integer receiptStatus){
		this.receiptStatus = receiptStatus;
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
