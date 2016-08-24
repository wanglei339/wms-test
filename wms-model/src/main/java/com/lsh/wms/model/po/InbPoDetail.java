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
    private BigDecimal orderQty;
	/** 包装单位 */
    private BigDecimal packUnit;
	/** 包装名称 */
	private String packName;

	/** 价格 */
	private BigDecimal price;
	/** 产地 */
    private String madein;
	/** 实际收货数 */
    private BigDecimal inboundQty = new BigDecimal(0);
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;
	/** 批次号 */
	private String lotNum;

	/**保质期例外收货*/
	private Integer exceptionReceipt;

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

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
	
	public String getMadein(){
		return this.madein;
	}
	
	public void setMadein(String madein){
		this.madein = madein;
	}
	
	public BigDecimal getInboundQty(){
		return this.inboundQty;
	}
	
	public void setInboundQty(BigDecimal inboundQty){
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

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public Integer getExceptionReceipt() {
		return exceptionReceipt;
	}

	public void setExceptionReceipt(Integer exceptionReceipt) {
		this.exceptionReceipt = exceptionReceipt;
	}
}
