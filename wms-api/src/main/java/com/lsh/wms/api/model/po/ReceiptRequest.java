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

    /** 采购订单号 */
    @NotBlank
    @Size(max=100)
    private String orderOtherId;

    /** 仓库ID */
    @NotNull
    private Long warehouseId = 1L;

    /** 预约单号 */
    @Size(max=64)
    private String bookingNum = "";

    /** 托盘码 */
    private Long containerId;

    /** 收货员 */
    @NotBlank
    @Size(max=64)
    private String receiptUser ;

    /** 收货时间 */
    @NotNull
    private Date receiptTime = new Date();

    /** 收货码头 */
    @Size(max=64)
    private String receiptWharf = "";

    private Long staffId;

    /**门店编码*/
    private String storeId;

    /** 是否生成任务*/
    private int isCreateTask = 1;
    /** 商品 */
    @Valid
    @Size(min=1)
    private List<ReceiptItem> items;

    public ReceiptRequest() {

    }

    public ReceiptRequest(String bookingNum, Long containerId, List<ReceiptItem> items, String orderOtherId, Date receiptTime, String receiptUser, String receiptWharf, Long staffId, String storeId, Long warehouseId) {
        this.bookingNum = bookingNum;
        this.containerId = containerId;
        this.items = items;
        this.orderOtherId = orderOtherId;
        this.receiptTime = receiptTime;
        this.receiptUser = receiptUser;
        this.receiptWharf = receiptWharf;
        this.staffId = staffId;
        this.storeId = storeId;
        this.warehouseId = warehouseId;
    }

    public String getBookingNum() {
        return bookingNum;
    }

    public void setBookingNum(String bookingNum) {
        this.bookingNum = bookingNum;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItem> items) {
        this.items = items;
    }

    public String getOrderOtherId() {
        return orderOtherId;
    }

    public void setOrderOtherId(String orderOtherId) {
        this.orderOtherId = orderOtherId;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getReceiptUser() {
        return receiptUser;
    }

    public void setReceiptUser(String receiptUser) {
        this.receiptUser = receiptUser;
    }

    public String getReceiptWharf() {
        return receiptWharf;
    }

    public void setReceiptWharf(String receiptWharf) {
        this.receiptWharf = receiptWharf;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getIsCreateTask() {
        return isCreateTask;
    }

    public void setIsCreateTask(int isCreateTask) {
        this.isCreateTask = isCreateTask;
    }
}
