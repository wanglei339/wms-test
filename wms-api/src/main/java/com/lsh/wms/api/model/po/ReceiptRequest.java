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

    /** 仓库ID */
    @NotNull
    private Long warehouseId;

    /** 预约单号 */
    @Size(max=64)
    private String bookingNum;

    /** 收货单号 */
    @NotBlank
    @Size(max=100)
    private String receiptCode;

    /** 收货员 */
    @NotBlank
    @Size(max=64)
    private String receiptUser;

    /** 收货时间 */
    @NotNull
    private Date receiptTime;

    /** 收货状态，1已收货，2已上架 */
    @NotNull
    private Integer receiptStatus;

    /** 收货码头 */
    @Size(max=64)
    private String receiptWharf;

    /** 暂存区 */
    @Size(max=100)
    private String tempStoreArea;

    /** 托盘码 */
    @NotNull
    private Long containerId;

    /** 分配库位 */
    private Long location;

    /** 实际库位 */
    private Long realLocation;

    /** 商品 */
    @Valid
    @Size(min=1)
    private List<ReceiptItem> items;

    public ReceiptRequest() {

    }

    public ReceiptRequest(Long warehouseId, String bookingNum, String receiptCode, String receiptUser, Date receiptTime,
                          Integer receiptStatus, String receiptWharf, String tempStoreArea, Long containerId,
                          Long location, Long realLocation, List<ReceiptItem> items) {
        this.warehouseId = warehouseId;
        this.bookingNum = bookingNum;
        this.receiptCode = receiptCode;
        this.receiptUser = receiptUser;
        this.receiptTime = receiptTime;
        this.receiptStatus = receiptStatus;
        this.receiptWharf = receiptWharf;
        this.tempStoreArea = tempStoreArea;
        this.containerId = containerId;
        this.location = location;
        this.realLocation = realLocation;
        this.items = items;
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

    public String getReceiptCode(){
        return this.receiptCode;
    }

    public void setReceiptCode(String receiptCode){
        this.receiptCode = receiptCode;
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

    public Integer getReceiptStatus(){
        return this.receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus){
        this.receiptStatus = receiptStatus;
    }

    public String getReceiptWharf() {
        return receiptWharf;
    }

    public void setReceiptWharf(String receiptWharf) {
        this.receiptWharf = receiptWharf;
    }

    public String getTempStoreArea() {
        return tempStoreArea;
    }

    public void setTempStoreArea(String tempStoreArea) {
        this.tempStoreArea = tempStoreArea;
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Long getRealLocation() {
        return realLocation;
    }

    public void setRealLocation(Long realLocation) {
        this.realLocation = realLocation;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItem> items) {
        this.items = items;
    }
}
