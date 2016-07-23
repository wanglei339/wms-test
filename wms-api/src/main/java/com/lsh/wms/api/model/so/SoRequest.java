package com.lsh.wms.api.model.so;

import com.lsh.wms.api.model.po.PoItem;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by panxudong on 16/7/14.
 */
public class SoRequest implements Serializable {

    /** 仓库ID */
    @NotNull
    private Long warehouseId;

    @NotBlank
    @Size(max=100)
    private String orderOtherId;

    /** 下单用户 */
    @NotBlank
    @Size(max=64)
    private String orderUser;

    /** 货主 */
    @NotNull
    private Long ownerUid;

    /** 1收货单，2退货单，3调货单 */
    @NotNull
    private Integer orderType;

    /** 波次号 */
    @NotNull
    private Long waveId;

    /** TMS线路 */
    @Size(max=64)
    private String transPlan;

    /** TMS顺序号 */
    @NotNull
    private Integer waveIndex;

    /** 交货时间 */
    private Date transTime;

    /** 收货地址 */
    @Size(max=1000)
    private String deliveryAddrs;

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<SoItem> items;

    public SoRequest() {
    }

    public SoRequest(Long warehouseId, String orderOtherId, String orderUser, Long ownerUid, Integer orderType,
                     Long waveId, String transPlan, Integer waveIndex, Date transTime, String deliveryAddrs,
                     List<SoItem> items) {
        this.warehouseId = warehouseId;
        this.orderOtherId = orderOtherId;
        this.orderUser = orderUser;
        this.ownerUid = ownerUid;
        this.orderType = orderType;
        this.waveId = waveId;
        this.transPlan = transPlan;
        this.waveIndex = waveIndex;
        this.transTime = transTime;
        this.deliveryAddrs = deliveryAddrs;
        this.items = items;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getWaveId() {
        return waveId;
    }

    public void setWaveId(Long waveId) {
        this.waveId = waveId;
    }

    public String getTransPlan() {
        return transPlan;
    }

    public void setTransPlan(String transPlan) {
        this.transPlan = transPlan;
    }

    public Integer getWaveIndex() {
        return waveIndex;
    }

    public void setWaveIndex(Integer waveIndex) {
        this.waveIndex = waveIndex;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getDeliveryAddrs() {
        return deliveryAddrs;
    }

    public void setDeliveryAddrs(String deliveryAddrs) {
        this.deliveryAddrs = deliveryAddrs;
    }

    public List<SoItem> getItems() {
        return items;
    }

    public void setItems(List<SoItem> items) {
        this.items = items;
    }
}
