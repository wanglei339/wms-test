package com.lsh.wms.api.model.location;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/11/20 下午12:19
 */
public class LocationDetailUpdateRequest implements Serializable {
    /**  */
    private Long id;
    /** 位置id */
    private Long locationId;
    /** 位置编码 */
    private String locationCode;
    /** 父级位置id */
    private Long fatherId;
    /** 位置类型 */
    private Long type;
    /** 类型名 */
    private String typeName;
    /** 是否为叶子位置节点 */
    private Integer isLeaf;
    /** 是否可用 */
    private Integer isValid;
    /** 是否是存储用位置 */
    private Integer canStore;
    /** 可容纳容器数量 */
    private Long containerVol;
    /** 区域坐标，四维坐标-区位坐标 */
    private Long regionNo;
    /** 通道坐标，四维坐标-通道坐标 */
    private Long passageNo;
    /** 货架层坐标，四维坐标-层数坐标 */
    private Long shelfLevelNo;
    /** 货位同层坐标，四维坐标-同层 */
    private Long binPositionNo;
    /** 描述 */
    private String description;
    /** 创建日期 */
    private Long createdAt;
    /** 更新日期 */
    private Long updatedAt;
    /** 区别库区库位-0为其他1-为库区-2为库位 */
    private Integer classification;
    /** 是否现在可用0-不可用1-可用 */
    private Integer canUse;
    /** 0-未上锁1-上锁 */
    private Integer isLocked;
    private Long curContainerVol;

    private String supplierNo;

    private String storeNo;
    /** 库位的用途1-拣货2-存货 */
    private Integer binUsage;
    /** 所在库区type */
    private Long regionType;

    public Long getRegionType() {
        return regionType;
    }

    public void setBinUsage(Integer binUsage) {
        this.binUsage = binUsage;
    }

    public void setRegionType(Long regionType) {
        this.regionType = regionType;
    }

    public Integer getBinUsage() {
        return binUsage;
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

    public void setCurContainerVol(Long curContainerVol) {
        this.curContainerVol = curContainerVol;
    }

    public Long getCurContainerVol() {
        return curContainerVol;
    }
    //...........................bin独有的...........................
    /**
     * 商品的id
     */
    private Long itemId;
    /**
     * 仓位体积
     */
    private BigDecimal volume;
    /**
     * 承重，默认单位kg，默认0，能承受东西很轻
     */
    private BigDecimal weigh;
    /**
     * 货位所在温区
     */
    private Integer zoneType;
    //..................................Dock独有的
    /**
     * 码头名
     */
    private String dockName;
    /**
     * 是否存在地秤
     */
    private Integer haveScales;
    /**
     * 用途，0-进货，1-出货
     */
    private Integer dockApplication;

    private Integer regionStrategy;

    public Integer getRegionStrategy() {
        return regionStrategy;
    }

    public void setRegionStrategy(Integer regionStrategy) {
        this.regionStrategy = regionStrategy;
    }
    //==============================================通道和码头共有direction,插入操作可以用type区分
    /**
     * 方位，0-东，1-南，2-西，3-北
     */
    private Integer direction;
    //==============================================
    /**
     * 长度默认单位 米
     */
    private BigDecimal width;
    /**
     * 高度默认单位 米
     */
    private BigDecimal height;
    //.................................通道独有的.................
    //方位，0-南北，1-东西  同码头

    //...............................区域
    //区域名字regionName在Bin已经定义

    /**
     * 区域的货主概念
     */
    private Long ownerid;

    public Long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Long ownerid) {
        this.ownerid = ownerid;
    }
    //..................................货架和阁楼(type通过主表判断)
    /**
     * 货架层数
     */
    private Long level;
    /**
     * 货架进深
     */
    private Long depth;

    //........................仓库
    /**
     * 仓库名
     */
    private String warehouseName;
    /**
     * 地址
     */
    private String address;
    /**
     * 电话
     */
    private String phoneNo;

    /** 长度默认单位 米 */
    private BigDecimal length;

    public Long getId() {
        return id;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public Long getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public Integer getCanStore() {
        return canStore;
    }

    public Long getContainerVol() {
        return containerVol;
    }

    public Long getRegionNo() {
        return regionNo;
    }

    public Long getPassageNo() {
        return passageNo;
    }

    public Long getShelfLevelNo() {
        return shelfLevelNo;
    }

    public Long getBinPositionNo() {
        return binPositionNo;
    }

    public String getDescription() {
        return description;
    }

    public Integer getZoneType() {
        return zoneType;
    }

    public String getDockName() {
        return dockName;
    }

    public Integer getHaveScales() {
        return haveScales;
    }

    public Integer getDockApplication() {
        return dockApplication;
    }

    public Integer getDirection() {
        return direction;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public Long getLevel() {
        return level;
    }

    public Long getDepth() {
        return depth;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public BigDecimal getLength() {
        return length;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Integer getClassification() {
        return classification;
    }

    public Integer getCanUse() {
        return canUse;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public Long getItemId() {
        return itemId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getWeigh() {
        return weigh;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public void setCanStore(Integer canStore) {
        this.canStore = canStore;
    }

    public void setContainerVol(Long containerVol) {
        this.containerVol = containerVol;
    }

    public void setRegionNo(Long regionNo) {
        this.regionNo = regionNo;
    }

    public void setPassageNo(Long passageNo) {
        this.passageNo = passageNo;
    }

    public void setShelfLevelNo(Long shelfLevelNo) {
        this.shelfLevelNo = shelfLevelNo;
    }

    public void setBinPositionNo(Long binPositionNo) {
        this.binPositionNo = binPositionNo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setZoneType(Integer zoneType) {
        this.zoneType = zoneType;
    }

    public void setDockName(String dockName) {
        this.dockName = dockName;
    }

    public void setHaveScales(Integer haveScales) {
        this.haveScales = haveScales;
    }

    public void setDockApplication(Integer dockApplication) {
        this.dockApplication = dockApplication;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public void setDepth(Long depth) {
        this.depth = depth;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public void setCanUse(Integer canUse) {
        this.canUse = canUse;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public void setWeigh(BigDecimal weigh) {
        this.weigh = weigh;
    }
}
