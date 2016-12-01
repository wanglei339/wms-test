package com.lsh.wms.model.stock;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/7/28.
 */
public class StockQuantCondition implements Serializable {

    /**  */
    private Long id;
    /** 商品id */
    private Long skuId;
    /** 存储库位id */
    private Long locationId;
    /** 容器设备id */
    private Long containerId;
    /** 占位 task_id */
    private Long reserveTaskId;
    /** 0-可用，1-被冻结 */
    private Long isFrozen;
    /** 货物供应商id */
    private Long supplierId;
    /** 货物所属公司id */
    private Long ownerId;
    /** 批次号 */
    private Long lotId;
    /** 入库时间 */
    private Long inDate;
    /** 保质期失效时间 */
    private Long expireDate;
    /** 商品 id */
    private Long itemId;
    /** 0-非残次，1-残次 */
    private Long isDefect;
    /** 0-非退货，1-退货 */
    private Long isRefund;

    private List<Long> locationIdList;

    private List<BaseinfoLocation> locationList;

    private boolean isNormal;

    private Long taskId;
    private BigDecimal requiredQty;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getRequiredQty() {
        return requiredQty;
    }

    public void setRequiredQty(BigDecimal requiredQty) {
        this.requiredQty = requiredQty;
    }


    public boolean isNormal() {
        return isNormal;
    }

    public void setIsNormal(boolean isNormal) {
        this.isNormal = isNormal;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public Long getReserveTaskId() {
        return reserveTaskId;
    }

    public void setReserveTaskId(Long reserveTaskId) {
        this.reserveTaskId = reserveTaskId;
    }

    public Long getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(Long isFrozen) {
        this.isFrozen = isFrozen;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public Long getInDate() {
        return inDate;
    }

    public void setInDate(Long inDate) {
        this.inDate = inDate;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getIsDefect() {
        return isDefect;
    }

    public void setIsDefect(Long isDefect) {
        this.isDefect = isDefect;
    }

    public Long getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Long isRefund) {
        this.isRefund = isRefund;
    }

    public List<BaseinfoLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<BaseinfoLocation> locationList) {
        this.locationList = locationList;
    }

    public List<Long> getLocationIdList() {
        return locationIdList;
    }

    public void setLocationIdList(List<Long> locationIdList) {
        this.locationIdList = locationIdList;
    }
}
