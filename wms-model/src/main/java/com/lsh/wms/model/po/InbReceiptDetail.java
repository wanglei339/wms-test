package com.lsh.wms.model.po;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private BigDecimal orderQty;
	/** 包装单位 */
    private BigDecimal packUnit;
	/** 产地 */
    private String madein;
	/** 实际收货数 */
    private BigDecimal inboundQty;
	/** 到货数 */
    private BigDecimal arriveNum;
	/** 残次数 */
    private BigDecimal defectNum;
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


	public BigDecimal getArriveNum() {
		return arriveNum;
	}

	public void setArriveNum(BigDecimal arriveNum) {
		this.arriveNum = arriveNum;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public BigDecimal getDefectNum() {
		return defectNum;
	}

	public void setDefectNum(BigDecimal defectNum) {
		this.defectNum = defectNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getInboundQty() {
		return inboundQty;
	}

	public void setInboundQty(BigDecimal inboundQty) {
		this.inboundQty = inboundQty;
	}

	public String getInsertby() {
		return insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	public Date getInserttime() {
		return inserttime;
	}

	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getMadein() {
		return madein;
	}

	public void setMadein(String madein) {
		this.madein = madein;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderOtherId() {
		return orderOtherId;
	}

	public void setOrderOtherId(String orderOtherId) {
		this.orderOtherId = orderOtherId;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public BigDecimal getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(BigDecimal packUnit) {
		this.packUnit = packUnit;
	}

	public Date getProTime() {
		return proTime;
	}

	public void setProTime(Date proTime) {
		this.proTime = proTime;
	}

	public Long getReceiptOrderId() {
		return receiptOrderId;
	}

	public void setReceiptOrderId(Long receiptOrderId) {
		this.receiptOrderId = receiptOrderId;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}
