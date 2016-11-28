package com.lsh.wms.api.model.so;

import java.math.BigDecimal;

/**
 * Created by lixin-mac on 2016/10/11.
 */
public class ObdStreamDetail {

    /** 订单id */
    private Long orderId = 0L;
    /** ibd订单类型 */
    private Long ibdOrderType;

    /** 商品码 */
    private Long itemId = 0L;
    /** 商品id */
    private Long skuId = 0L;
    /** 货主id */
    private Long ownerId =0L;
    /** 批次id */
    private Long locId = 0L;
    /** 分配数量 */
    private BigDecimal allocQty = BigDecimal.ZERO;
    /** 0-新建，呵呵，冗余一下，以后看用不用吧 */
    private Long status = 1L;
    /** 分配库存单位名称 */
    private String allocUnitName = "EA";
    /**库存包装单位数量*/
    private BigDecimal allocUnitQty = BigDecimal.ZERO;

    /** 容器id,非常重要的字断，务必维护好当前真实的商品所在的container信息，否则就惨了 */
    private Long containerId = 0L;

    /**收货的数量*/
    private BigDecimal pickQty = BigDecimal.ZERO;

    /** 分配的集货位 */
    private Long allocCollectLocation = 0L;
    /** 实际的集货位 */
    private Long realCollectLocation = 0L;

    public ObdStreamDetail(){}

    public ObdStreamDetail(Long allocCollectLocation, BigDecimal allocQty, String allocUnitName, BigDecimal allocUnitQty, Long containerId, Long ibdOrderType, Long itemId, Long locId, Long orderId, Long ownerId, BigDecimal pickQty, Long realCollectLocation, Long skuId, Long status) {
        this.allocCollectLocation = allocCollectLocation;
        this.allocQty = allocQty;
        this.allocUnitName = allocUnitName;
        this.allocUnitQty = allocUnitQty;
        this.containerId = containerId;
        this.ibdOrderType = ibdOrderType;
        this.itemId = itemId;
        this.locId = locId;
        this.orderId = orderId;
        this.ownerId = ownerId;
        this.pickQty = pickQty;
        this.realCollectLocation = realCollectLocation;
        this.skuId = skuId;
        this.status = status;
    }

    public String getAllocUnitName() {
        return allocUnitName;
    }

    public void setAllocUnitName(String allocUnitName) {
        this.allocUnitName = allocUnitName;
    }

    public BigDecimal getAllocUnitQty() {
        return allocUnitQty;
    }

    public void setAllocUnitQty(BigDecimal allocUnitQty) {
        this.allocUnitQty = allocUnitQty;
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

    public BigDecimal getPickQty() {
        return pickQty;
    }

    public void setPickQty(BigDecimal pickQty) {
        this.pickQty = pickQty;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public BigDecimal getAllocQty() {
        return allocQty;
    }

    public void setAllocQty(BigDecimal allocQty) {
        this.allocQty = allocQty;
    }

    public Long getAllocCollectLocation() {
        return allocCollectLocation;
    }

    public void setAllocCollectLocation(Long allocCollectLocation) {
        this.allocCollectLocation = allocCollectLocation;
    }

    public Long getRealCollectLocation() {
        return realCollectLocation;
    }

    public void setRealCollectLocation(Long realCollectLocation) {
        this.realCollectLocation = realCollectLocation;
    }
}
