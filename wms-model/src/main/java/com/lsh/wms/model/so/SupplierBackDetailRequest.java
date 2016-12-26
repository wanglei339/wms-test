package com.lsh.wms.model.so;

import java.io.Serializable;
import java.math.BigDecimal;

public class SupplierBackDetailRequest implements Serializable {


	/** 退货订单id，对应obd_header的order_id */
    private Long orderId;
	/** 上游细单id,对应obd_detail的detail_other_id */
    private String detailOtherId;
	/** 商品码 */
    private Long itemId;
	/** 批次id */
    private Long lotId;
	/** 需求量 */
    private BigDecimal reqQty;
	/** 库存量 */
    private BigDecimal allocQty;
	/** 分配库存单位名称 */
    private String allocUnitName;
	/** 分配库存单位数量 */
    private BigDecimal allocUnitQty;
	/** 存储位 */
    private Long locationId;


	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}
	
	public String getDetailOtherId(){
		return this.detailOtherId;
	}
	
	public void setDetailOtherId(String detailOtherId){
		this.detailOtherId = detailOtherId;
	}
	
	public Long getItemId(){
		return this.itemId;
	}
	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}

	public BigDecimal getReqQty(){
		return this.reqQty;
	}
	
	public void setReqQty(BigDecimal reqQty){
		this.reqQty = reqQty;
	}
	
	public BigDecimal getAllocQty(){
		return this.allocQty;
	}
	
	public void setAllocQty(BigDecimal allocQty){
		this.allocQty = allocQty;
	}
	
	public String getAllocUnitName(){
		return this.allocUnitName;
	}
	
	public void setAllocUnitName(String allocUnitName){
		this.allocUnitName = allocUnitName;
	}
	
	public BigDecimal getAllocUnitQty(){
		return this.allocUnitQty;
	}
	
	public void setAllocUnitQty(BigDecimal allocUnitQty){
		this.allocUnitQty = allocUnitQty;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}

}
