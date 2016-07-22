package com.lsh.wms.model.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InbPoDetail implements Serializable {

	/**  */
    private Long id;
	/** 订单ID */
    private Long orderId;
	/** 商品ID */
    private Long skuId;
	/** 物美码 */
    private String skuCode;
	/** 商品名称 */
    private String skuName;
	/** 国条码 */
    private String barCode;
	/** 进货数 */
    private Long orderQty;
	/** 包装单位 */
    private Long packUnit;
	/** 价格 */
	private BigDecimal price;
	/** 产地 */
    private String madein;
	/** 实际收货数 */
    private Long inboundQty;
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public String getSkuCode(){
		return this.skuCode;
	}
	
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
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
