package com.lsh.wms.model.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.acl.LastOwnerException;
import java.util.Date;

public class StockLot implements Serializable {

	/**  */
    private Long id;
	/** 批次id */
    private Long lotId;
	/** 商品id */
    private Long skuId;
	/** 生产批次号 */
    private String serialNo;
	/** 入库时间 */
    private Long inDate;
	/** 生产时间 */
    private Long productDate;
	/** 保质期失效时间 */
    private Long expireDate;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;

	private Long itemId;

	private Long poId;

	private Long receiptId;
	/** 包装单位*/
	private BigDecimal packUnit= new BigDecimal(0);
	/** 包装名称 */
	private String packName = "";

	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getLotId(){
		return this.lotId;
	}

	public void setLotId(Long lotId){
		this.lotId = lotId;
	}

	public Long getSkuId(){
		return this.skuId;
	}

	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}

	public String getSerialNo(){
		return this.serialNo;
	}

	public void setSerialNo(String serialNo){
		this.serialNo = serialNo;
	}

	public Long getInDate(){
		return this.inDate;
	}

	public void setInDate(Long inDate){
		this.inDate = inDate;
	}

	public Long getProductDate(){
		return this.productDate;
	}

	public void setProductDate(Long productDate){
		this.productDate = productDate;
	}

	public Long getExpireDate(){
		return this.expireDate;
	}

	public void setExpireDate(Long expireDate){
		this.expireDate = expireDate;
	}

	public Long getCreatedAt(){
		return this.createdAt;
	}

	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt(){
		return this.updatedAt;
	}

	public void setUpdatedAt(Long updatedAt){
		this.updatedAt = updatedAt;
	}

	public Long getPoId() {
		return poId;
	}

	public void setPoId(Long poId) {
		this.poId = poId;
	}

	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getPackUnit(){
		return packUnit;
	}

	public void setPackUnit(BigDecimal packUnit){
		this.packUnit = packUnit;
	}

	public String getPackName(){
		return packName;
	}

	public void  setPackName(String packName){
		this.packName = packName;
	}

}
