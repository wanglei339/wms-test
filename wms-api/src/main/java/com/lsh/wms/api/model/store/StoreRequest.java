package com.lsh.wms.api.model.store;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/26 下午4:14
 */
public class StoreRequest {
    /**  */
    private Long id;
    /** 门店号0-在库内xxxx是门店号 */
    private String storeNo;
    /** 门店名称 */
    private String storeName;
    /** 区域名称 */
    private String region;
    /** 规模1-小店2-大店 */
    private Integer scale;
    /** 运营情况1-正常2-关闭 */
    private Integer isOpen;
    /** 0无效1-有效 */
    private Integer isValid =1;
    /** 创建时间 */
    private Long createAt;
    /** 更新时间 */
    private Long updateAt;
    /** 地址 */
    private String address;
    /** 门店id-暂留 */
    private Long storeId;

    public Long getId() {
        return id;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getRegion() {
        return region;
    }

    public Integer getScale() {
        return scale;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public String getAddress() {
        return address;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
