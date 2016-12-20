package com.lsh.wms.model.tu;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TuDetail implements Serializable {

    /**  */
    private Long id;
    /**
     * 运单号
     */
    private String tuId;
    /**
     * 合板的板id
     */
    private Long mergedContainerId;
    /**
     * 组盘后板上总箱数
     */
    private BigDecimal boxNum;
    /**
     * 托盘数聚合
     */
    private Integer containerNum;
    /**
     * 组盘后板上的周转箱数
     */
    private Long turnoverBoxNum;
    /**
     * 是否余货，0不是，1是
     */
    private Integer isRest = 0;
    /**
     * '是否贵品，0不是，1是',
     */
    private Integer isExpensive = 0;
    /**
     * 创建时间
     */
    private Long createdAt;
    /**
     * 更新时间
     */
    private Long updatedAt;
    /**
     * 装车扫板时间
     */
    private Long loadAt;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 是否有效, 0否, 1是
     */
    private Integer isValid = 1;
    /**
     * 一板多板数字段默认1
     */
    private Long boardNum = 1L;

    /** 客户名称 */
    private String customerName;
    /** 客户地址 */
    private String customerAddress;
    /** 客户的编码 */
    private String customerCode;
    /** 集货位置id */
    private Long collectionId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTuId() {
        return this.tuId;
    }

    public void setTuId(String tuId) {
        this.tuId = tuId;
    }

    public Long getMergedContainerId() {
        return this.mergedContainerId;
    }

    public void setMergedContainerId(Long mergedContainerId) {
        this.mergedContainerId = mergedContainerId;
    }

    public BigDecimal getBoxNum() {
        return this.boxNum;
    }

    public void setBoxNum(BigDecimal boxNum) {
        this.boxNum = boxNum;
    }

    public Integer getContainerNum() {
        return this.containerNum;
    }

    public void setContainerNum(Integer containerNum) {
        this.containerNum = containerNum;
    }

    public Long getTurnoverBoxNum() {
        return this.turnoverBoxNum;
    }

    public void setTurnoverBoxNum(Long turnoverBoxNum) {
        this.turnoverBoxNum = turnoverBoxNum;
    }

    public Integer getIsRest() {
        return this.isRest;
    }

    public void setIsRest(Integer isRest) {
        this.isRest = isRest;
    }

    public void setIsExpensive(Integer isExpensive) {
        this.isExpensive = isExpensive;
    }

    public Integer getIsExpensive() {
        return isExpensive;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getLoadAt() {
        return this.loadAt;
    }

    public void setLoadAt(Long loadAt) {
        this.loadAt = loadAt;
    }

    public Long getStoreId() {
        return this.storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getIsValid() {
        return this.isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Long getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(Long boardNum) {
        this.boardNum = boardNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }
}
