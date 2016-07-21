package com.lsh.wms.model.po;

import java.io.Serializable;
import java.util.Date;

public class InbReceiptDetail implements Serializable {

	/**  */
    private Long id;
	/** 收货单ID */
    private Long receiptOrderId;
	/**采购订单号  */
	private String orderOtherId;
	/** 订单ID */
    private Long orderId;
	/** 批次号 */
    private String lotNum;
	/** 仓库商品ID */
    private Long skuId;
	/** 商品ID */
    private Long itemId;
	/** 商品名称 */
    private String skuName;
	/** 国条码 */
    private String barCode;
	/** 进货数 */
    private Long orderQty;
	/** 包装单位 */
    private Long packUnit;
	/** 产地 */
    private String madein;
	/** 实际收货数 */
    private Long inboundQty;
	/** 到货数 */
    private Long arriveNum;
	/** 残次数 */
    private Long defectNum;
	/** 生产日期 */
    private Date proTime;
	/** 拒收原因 */
    private String refuseReason;
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;

	public String getOrderOtherId() {
		return orderOtherId;
	}

	public void setOrderOtherId(String orderOtherId) {
		this.orderOtherId = orderOtherId;
	}

	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getReceiptOrderId(){
		return this.receiptOrderId;
	}
	
	public void setReceiptOrderId(Long receiptOrderId){
		this.receiptOrderId = receiptOrderId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public String getLotNum(){
		return this.lotNum;
	}
	
	public void setLotNum(String lotNum){
		this.lotNum = lotNum;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public String getSkuName(){
		return this.skuName;
	}
	
	public void setSkuName(String skuName){
		this.skuName = skuName;
	}
	
	public String getBarCode(){
		return this.barCode;
	}
	
	public void setBarCode(String barCode){
		this.barCode = barCode;
	}
	
	public Long getOrderQty(){
		return this.orderQty;
	}
	
	public void setOrderQty(Long orderQty){
		this.orderQty = orderQty;
	}
	
	public Long getPackUnit(){
		return this.packUnit;
	}
	
	public void setPackUnit(Long packUnit){
		this.packUnit = packUnit;
	}
	
	public String getMadein(){
		return this.madein;
	}
	
	public void setMadein(String madein){
		this.madein = madein;
	}
	
	public Long getInboundQty(){
		return this.inboundQty;
	}
	
	public void setInboundQty(Long inboundQty){
		this.inboundQty = inboundQty;
	}
	
	public Long getArriveNum(){
		return this.arriveNum;
	}
	
	public void setArriveNum(Long arriveNum){
		this.arriveNum = arriveNum;
	}
	
	public Long getDefectNum(){
		return this.defectNum;
	}
	
	public void setDefectNum(Long defectNum){
		this.defectNum = defectNum;
	}
	
	public Date getProTime(){
		return this.proTime;
	}
	
	public void setProTime(Date proTime){
		this.proTime = proTime;
	}
	
	public String getRefuseReason(){
		return this.refuseReason;
	}
	
	public void setRefuseReason(String refuseReason){
		this.refuseReason = refuseReason;
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
