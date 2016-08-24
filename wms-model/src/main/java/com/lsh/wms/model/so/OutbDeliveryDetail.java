package com.lsh.wms.model.so;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private BigDecimal orderQty;
	/** 包装单位 */
    private BigDecimal packUnit;
	/** 批次号 */
    private String lotNum;
	/** 批号ID */
	private Long lotId;

	/** 出货数 */
    private BigDecimal deliveryNum;
	/**  */
    private String insertby;
	/**  */
    private String updateby;
	/**  */
    private Date inserttime;
	/**  */
    private Date updatetime;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public BigDecimal getDeliveryNum() {
		return deliveryNum;
	}

	public void setDeliveryNum(BigDecimal deliveryNum) {
		this.deliveryNum = deliveryNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getLotId() {
		return lotId;
	}

	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
