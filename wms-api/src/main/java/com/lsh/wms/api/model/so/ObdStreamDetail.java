package com.lsh.wms.api.model.so;

import java.math.BigDecimal;

/**
 * Created by lixin-mac on 2016/10/11.
 */
public class ObdStreamDetail {

    /** 订单id */
    private Long orderId;
    /** ibd订单类型 */
    private Long ibdOrderType;

    /** 商品码 */
    private Long itemId;
    /** 商品id */
    private Long skuId;
    /** 货主id */
    private Long ownerId;
    /** 批次id */
    private Long locId = 0L;
    /** 0-新建，呵呵，冗余一下，以后看用不用吧 */
    private Long status = 1L;
    /** 分配库存单位名称 */
    private String allocUnitName = "EA";
    /** 容器id,非常重要的字断，务必维护好当前真实的商品所在的container信息，否则就惨了 */
    private Long containerId = 0L;

    /**收货的数量*/
    private BigDecimal receiptQty = BigDecimal.ZERO;

    public ObdStreamDetail(){}


    public ObdStreamDetail(String allocUnitName, Long containerId, Long ibdOrderType, Long itemId, Long locId, Long orderId, Long ownerId, BigDecimal receiptQty, Long skuId, Long status) {
        this.allocUnitName = allocUnitName;
        this.containerId = containerId;
        this.ibdOrderType = ibdOrderType;
        this.itemId = itemId;
        this.locId = locId;
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.receiptQty = receiptQty;
        this.skuId = skuId;
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getAllocUnitName() {
        return allocUnitName;
    }

    public void setAllocUnitName(String allocUnitName) {
        this.allocUnitName = allocUnitName;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public Long getIbdOrderType() {
        return ibdOrderType;
    }

    public void setIbdOrderType(Long ibdOrderType) {
        this.ibdOrderType = ibdOrderType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BigDecimal getReceiptQty() {
        return receiptQty;
    }

    public void setReceiptQty(BigDecimal receiptQty) {
        this.receiptQty = receiptQty;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
