package com.lsh.wms.model.so;

import java.io.Serializable;
import java.util.Date;

public class OutbDeliveryDetail implements Serializable {

	/**  */
    private Long id;
	/** 出库单ID */
    private Long deliveryId;
	/** 订单ID */
    private Long orderId;
	/** 商品ID */
    private Long itemId;
	/** 仓库商品编码 */
    private Long skuId;
	/** 商品名称 */
    private String skuName;
	/** 国条码 */
    private String barCode;
	/** 订货数 */
    private Long orderQty;
	/** 包装单位 */
    private Long packUnit;
	/** 批次号 */
    private String lotNum;
	/** 出货数 */
    private Long deliveryNum;
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
	
	public Long getDeliveryId(){
		return this.deliveryId;
	}
	
	public void setDeliveryId(Long deliveryId){
		this.deliveryId = deliveryId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
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
	
	public String getLotNum(){
		return this.lotNum;
	}
	
	public void setLotNum(String lotNum){
		this.lotNum = lotNum;
	}
	
	public Long getDeliveryNum(){
		return this.deliveryNum;
	}
	
	public void setDeliveryNum(Long deliveryNum){
		this.deliveryNum = deliveryNum;
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
