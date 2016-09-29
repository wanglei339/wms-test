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


    @NotBlank
    @Size(max=100)
    private String orderOtherId;

    private String orderOtherRefId = "";

    /** 售达方编码 */
    @NotBlank
    @Size(max=64)
    private String orderUserCode;

    /** 下单客户（售达方名称） */
    @NotBlank
    @Size(max=64)
    private String orderUser;

    /** 送达方名称 */
    @NotBlank
    @Size(max=64)
    private String deliveryName;

    /** 送达方编码 */
    @NotBlank
    @Size(max=64)
    private String deliveryCode;

    /** 货主 */
    @NotNull
    private Long ownerUid;

    /** 1收货单，2退货单，3调货单 */
    @NotNull
    private Integer orderType;

    /** 波次号 */
    //@NotNull
    private Long waveId = 0l;

    /** TMS线路 */
    @Size(max=64)
    private String transPlan;

    /** TMS顺序号 */
    //@NotNull
    private Integer waveIndex = 0;

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

    public SoRequest(String deliveryAddrs, String deliveryCode, String deliveryName, List<SoItem> items, String orderOtherId, String orderOtherRefId, Integer orderType, String orderUser, String orderUserCode, Long ownerUid, String transPlan, Date transTime, Long waveId, Integer waveIndex) {
        this.deliveryAddrs = deliveryAddrs;
        this.deliveryCode = deliveryCode;
        this.deliveryName = deliveryName;
        this.items = items;
        this.orderOtherId = orderOtherId;
        this.orderOtherRefId = orderOtherRefId;
        this.orderType = orderType;
        this.orderUser = orderUser;
        this.orderUserCode = orderUserCode;
        this.ownerUid = ownerUid;
        this.transPlan = transPlan;
        this.transTime = transTime;
        this.waveId = waveId;
        this.waveIndex = waveIndex;
    }

    public String getDeliveryAddrs() {
        return deliveryAddrs;
    }

    public void setDeliveryAddrs(String deliveryAddrs) {
        this.deliveryAddrs = deliveryAddrs;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public List<SoItem> getItems() {
        return items;
    }

    public void setItems(List<SoItem> items) {
        this.items = items;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public String getOrderOtherRefId() {
        return orderOtherRefId;
    }

    public void setOrderOtherRefId(String orderOtherRefId) {
        this.orderOtherRefId = orderOtherRefId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public String getOrderUserCode() {
        return orderUserCode;
    }

    public void setOrderUserCode(String orderUserCode) {
        this.orderUserCode = orderUserCode;
    }

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getTransPlan() {
        return transPlan;
    }

    public void setTransPlan(String transPlan) {
        this.transPlan = transPlan;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public Long getWaveId() {
        return waveId;
    }

    public void setWaveId(Long waveId) {
        this.waveId = waveId;
    }

    public Integer getWaveIndex() {
        return waveIndex;
    }

    public void setWaveIndex(Integer waveIndex) {
        this.waveIndex = waveIndex;
    }
}
