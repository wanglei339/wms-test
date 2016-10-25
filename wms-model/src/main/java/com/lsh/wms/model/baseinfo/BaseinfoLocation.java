package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoLocation implements Serializable, IBaseinfoLocaltionModel {

    /**  */
    private Long id;
    /**
     * 位置id
     */
    protected Long locationId;
    /**
     * 位置编码
     */
    protected String locationCode;
    /**
     * 父级位置id
     */
    protected Long fatherId;
    /**
     * 子节点范围起点
     */
    private Long leftRange;
    /**
     * 子节点范围终点
     */
    private Long rightRange;
    /**
     * 在树形结构的第几层
     */
    private Long level;
    /**
     * 位置类型
     */
    protected Long type;
    /**
     * 类型名
     */
    protected String typeName;
    /**
     * 是否为叶子位置节点
     */
    protected Integer isLeaf;
    /**
     * 是否可用
     */
    protected Integer isValid;
    /**
     * 是否是存储用位置
     */
    protected Integer canStore;
    /**
     * 可容纳容器数量
     */
    protected Long containerVol;
    /**
     * 区域坐标，四维坐标-区位坐标
     */
    protected Long regionNo;
    /**
     * 通道坐标，四维坐标-通道坐标
     */
    protected Long passageNo;
    /**
     * 货架层坐标，四维坐标-层数坐标
     */
    protected Long shelfLevelNo;
    /**
     * 货位同层坐标，四维坐标-同层
     */
    protected Long binPositionNo;
    /**
     * 描述
     */
    protected String description;
    /**
     * 创建日期
     */
    protected Long createdAt;
    /**
     * 更新日期
     */
    protected Long updatedAt;
    /**
     * 区别库区库位-3为其他1-为库区-2为库位4-货架
     */
    protected Integer classification;
    /**
     * 是否现在可用0-不可用1-可用
     */
    protected Integer canUse;
    /**
     * 0-未上锁1-上锁
     */
    protected Integer isLocked;
    /**
     * 当前容器的数量
     */
    private Long curContainerVol = 0L;
    /**
     * 门店号用于直流操作，0是库内操作1-99是区分门店的
     */
    private String storeNo = "0";
    /**
     * 供商号用于退货操作
     */
    private String supplierNo = "";

    public Long getCurContainerVol() {
        return curContainerVol;
    }

    public void setCurContainerVol(Long curContainerVol) {
        this.curContainerVol = curContainerVol;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Long getFatherId() {
        return this.fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public Long getLeftRange() {
        return this.leftRange;
    }

    public void setLeftRange(Long leftRange) {
        this.leftRange = leftRange;
    }

    public Long getRightRange() {
        return this.rightRange;
    }

    public void setRightRange(Long rightRange) {
        this.rightRange = rightRange;
    }

    public Long getLevel() {
        return this.level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getIsLeaf() {
        return this.isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getIsValid() {
        return this.isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Integer getCanStore() {
        return this.canStore;
    }

    public void setCanStore(Integer canStore) {
        this.canStore = canStore;
    }

    public Long getContainerVol() {
        return this.containerVol;
    }

    public void setContainerVol(Long containerVol) {
        this.containerVol = containerVol;
    }

    public Long getRegionNo() {
        return this.regionNo;
    }

    public void setRegionNo(Long regionNo) {
        this.regionNo = regionNo;
    }

    public Long getPassageNo() {
        return this.passageNo;
    }

    public void setPassageNo(Long passageNo) {
        this.passageNo = passageNo;
    }

    public Long getShelfLevelNo() {
        return this.shelfLevelNo;
    }

    public void setShelfLevelNo(Long shelfLevelNo) {
        this.shelfLevelNo = shelfLevelNo;
    }

    public Long getBinPositionNo() {
        return this.binPositionNo;
    }

    public void setBinPositionNo(Long binPositionNo) {
        this.binPositionNo = binPositionNo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getClassification() {
        return this.classification;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public Integer getCanUse() {
        return this.canUse;
    }

    public void setCanUse(Integer canUse) {
        this.canUse = canUse;
    }

    public Integer getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @Override
    public String toString() {
        return "BaseinfoLocation{" +
                "id=" + id +
                ", locationId=" + locationId +
                ", locationCode='" + locationCode + '\'' +
                ", fatherId=" + fatherId +
                ", leftRange=" + leftRange +
                ", rightRange=" + rightRange +
                ", level=" + level +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", isLeaf=" + isLeaf +
                ", isValid=" + isValid +
                ", canStore=" + canStore +
                ", containerVol=" + containerVol +
                ", regionNo=" + regionNo +
                ", passageNo=" + passageNo +
                ", shelfLevelNo=" + shelfLevelNo +
                ", binPositionNo=" + binPositionNo +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", classification=" + classification +
                ", canUse=" + canUse +
                ", isLocked=" + isLocked +
                ", curContainerVol=" + curContainerVol +
                ", storeNo='" + storeNo + '\'' +
                ", supplierNo=" + supplierNo +
                '}';
    }
}
