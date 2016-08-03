package com.lsh.wms.api.model.location;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 前端校验
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/24 下午8:16
 */
public class LocationDetailRequest implements Serializable {

    /**
     * 位置id
     */
    @NotNull
    private Long locationId;
    /**
     * 位置编码
     */
    @NotBlank
    private String locationCode;
    /**
     * 父级位置id
     */
    @NotNull
    private Long fatherId;
    /**
     * 存储位类型
     */
    @NotNull
    private Long type;
    /**
     * 类型名
     */
    // TODO 不是表的数据(前端传type即可)
    private String typeName;  //自己带过来type(选择哪个type,传type的值)
    /**
     * 是否为叶子位置节点
     */
    @NotNull
    private Integer isLeaf;
    /**
     * 是否可用
     */
    @NotNull
    private Integer isValid;   // TODO 必须是未删除的,自己带过来
    /**
     * 是否是存储用位置
     */
    @NotNull
    private Integer canStore;   // TODO 需要标出
    /**
     * 是否被占用，0-未使用，1-已占用
     */
    @NotNull
    private Integer inUse;  // TODO  需要填写
    /**
     * 可容纳容器数量
     */
    @NotNull
    private Long containerVol;  //TODO  需要填写
    /**
     * 区域坐标，四维坐标-区位坐标
     */
    @NotNull
    private Long regionNo;
    /**
     * 通道坐标，四维坐标-通道坐标
     */   // TODO 需要填写
    @NotNull
    private Long passageNo;
    /**
     * 货架层坐标，四维坐标-层数坐标
     */ // TODO 需要填写
    private Long shelfLevelNo;
    /**
     * 货位同层坐标，四维坐标-同层
     */   // TODO 需要填写
    private Long binPositionNo;

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
     * 描述
     */
    private String description;
    /**
     * 创建日期
     */
    private Long createdAt;
    /**
     * 更新日期
     */
    private Long updatedAt;
    /**
     * 0可用1不可用
     */
    private String isUsed;
    /**
     * 常温或者非常温
     */
    private String zoonType;
    /**
     * 所属仓库
     */
    private String regionName;

    //..................................Dock独有的
    /**
     * 位置类型/码头区域 0-A区
     */
    private Long dockType;
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
    /**
     * 使用用途,进库/出库
     */
    private String applicationName;

    //.................................通道独有的.................
    //方位，0-南北，1-东西  同码头

    //...............................区域
    //区域名字regionName在Bin已经定义

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
    /**
     * 货位所在温区
     */
    private Integer temperature;

    /** 长度默认单位 米 */
    private BigDecimal length;

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
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

    public void setInUse(Integer inUse) {
        this.inUse = inUse;
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

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public void setWeigh(BigDecimal weigh) {
        this.weigh = weigh;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public void setZoonType(String zoonType) {
        this.zoonType = zoonType;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setDockType(Long dockType) {
        this.dockType = dockType;
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

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
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

    public Integer getInUse() {
        return inUse;
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

    public Long getItemId() {
        return itemId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getWeigh() {
        return weigh;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public String getZoonType() {
        return zoonType;
    }

    public String getRegionName() {
        return regionName;
    }

    public Long getDockType() {
        return dockType;
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

    public String getApplicationName() {
        return applicationName;
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

    public Integer getTemperature() {
        return temperature;
    }
}
