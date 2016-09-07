package com.lsh.wms.model.so;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OutbSoDetail implements Serializable {

	/**  */
    private Long id;
	/** 上游细单ID */
	private String detailOtherId;
	/** 订单ID */
    private Long orderId;
	/** 外部商品编码 */
    private String skuCode;
	/** 仓库商品编码 */
    private Long skuId;
	/** 仓库商品ID */
	private Long itemId;

	/** 商品名称 */
    private String skuName;
	/** 国条码 */
    private String barCode;
	/** 订货数 */
    private BigDecimal orderQty;
	/** 包装单位 */
    private BigDecimal packUnit;
	/** 批次号 */
    private String lotNum;
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

	public String getDetailOtherId(){
		return this.detailOtherId;
	}

	public void setDetailOtherId(String detailOtherId){
		this.detailOtherId = detailOtherId;
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
	
	public BigDecimal getOrderQty(){
		return this.orderQty;
	}
	
	public void setOrderQty(BigDecimal orderQty){
		this.orderQty = orderQty;
	}
	
	public BigDecimal getPackUnit(){
		return this.packUnit;
	}
	
	public void setPackUnit(BigDecimal packUnit){
		this.packUnit = packUnit;
	}
	
	public String getLotNum(){
		return this.lotNum;
	}
	
	public void setLotNum(String lotNum){
		this.lotNum = lotNum;
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

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
}
