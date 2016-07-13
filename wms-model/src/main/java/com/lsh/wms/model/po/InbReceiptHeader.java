package com.lsh.wms.model.po;

import java.io.Serializable;
import java.util.Date;

public class InbReceiptHeader implements Serializable {

	/**  */
    private Long id;
	/** 仓库ID */
    private Long warehouseId;
	/** 预约单号 */
    private String bookingNum;
	/** 收货单号 */
    private String receiptCode;
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
	private String receiptDetails;

	public String getReceiptDetails() {
		return receiptDetails;
	}

	public void setReceiptDetails(String receiptDetails) {
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
	
	public String getReceiptCode(){
		return this.receiptCode;
	}
	
	public void setReceiptCode(String receiptCode){
		this.receiptCode = receiptCode;
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
