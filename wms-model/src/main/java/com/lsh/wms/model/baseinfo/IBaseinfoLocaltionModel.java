package com.lsh.wms.model.baseinfo;

/**
 * 这是个父类 只对外提供id
 * Created by zengwenjun on 16/7/23.
 */
public interface IBaseinfoLocaltionModel {
    public Long getType();
    public void setLocationId(Long locationId);
    public void setCreatedAt(Long createdAt);
    public Long getCreatedAt();
    public void setUpdatedAt(Long updatedAt);
    public Long getUpdatedAt();
    public Long getLocationId();
    public void setClassification(Integer classification);
    public Integer getClassification();
    public void setDefaultClassification();
}
