package com.lsh.wms.api.model.po;

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
public class ReceiptRequest implements Serializable {

    @NotBlank
    @Size(max=100)
    private String orderOtherId;

    /** 仓库ID */
    @NotNull
    private Long warehouseId;

    /** 预约单号 */
    @Size(max=64)
    private String bookingNum;

    /** 托盘码 */
    private Long containerId;

    /** 收货员 */
    @NotBlank
    @Size(max=64)
    private String receiptUser;

    /** 收货时间 */
    @NotNull
    private Date receiptTime;

    /** 收货码头 */
    @Size(max=64)
    private String receiptWharf;

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<ReceiptItem> items;

    public ReceiptRequest() {

    }

    public ReceiptRequest(String orderOtherId, Long warehouseId, String bookingNum,Long containerId, String receiptUser, Date receiptTime,
                          String receiptWharf, List<ReceiptItem> items) {
        this.orderOtherId = orderOtherId;
        this.warehouseId = warehouseId;
        this.bookingNum = bookingNum;
        this.containerId = containerId;
        this.receiptUser = receiptUser;
        this.receiptTime = receiptTime;
        this.receiptWharf = receiptWharf;
        this.items = items;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public Long getWarehouseId(){
        return this.warehouseId;
    }

    public void setWarehouseId(Long warehouseId){
        this.warehouseId = warehouseId;
    }

    public String getBookingNum(){
        return this.bookingNum;
    }

    public void setBookingNum(String bookingNum){
        this.bookingNum = bookingNum;
    }

    public String getReceiptUser(){
        return this.receiptUser;
    }

    public void setReceiptUser(String receiptUser){
        this.receiptUser = receiptUser;
    }

    public Date getReceiptTime(){
        return this.receiptTime;
    }

    public void setReceiptTime(Date receiptTime){
        this.receiptTime = receiptTime;
    }

    public String getReceiptWharf() {
        return receiptWharf;
    }

    public void setReceiptWharf(String receiptWharf) {
        this.receiptWharf = receiptWharf;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItem> items) {
        this.items = items;
    }

}
