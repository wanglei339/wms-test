package com.lsh.wms.api.model.tu;

import java.math.BigDecimal;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/11/5 下午2:00
 */
public class GroupRestResponse {
    /**  */
    private Long id;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 波次id
     */
    private Long waveId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 收货id
     */
    private Long receiptId;
    /**
     * 当前LocationId, 分配查找使用
     */
    private Long locationId;
    /**
     * 商品id，分配查找用
     */
    private Long skuId;
    /**
     * 货主id，分配查找用
     */
    private Long ownerId;
    /**
     * itemid，分配查找用
     */
    private Long itemId;
    /**
     * 移入库位id
     */
    private Long fromLocationId;
    /**
     * 移入库位id
     */
    private Long toLocationId;
    private Long realFromLocationId;
    private Long realToLocationId;
    private BigDecimal qtyDone;
    /**
     * 数量
     */
    private BigDecimal qty;
    private BigDecimal qtyUom;
    private BigDecimal qtyDoneUom;
    /**
     * 包装单位
     */
    private BigDecimal packUnit;
    /**
     * 包装名称
     */
    private String packName;
    /**
     * 容器id，分配查找用
     */
    private Long containerId;
    /**
     * 操作人员id
     */
    private Long operator;
    /**
     * 发起人员id
     */
    private Long planner;
    /**
     * 任务类型，100-盘点， 101-收货，102-波次， 103-上架，104-补货, 105-移库
     */
    private Long type;
    /**
     * 任务子类型, 由个任务类型自解释,可以不需要
     */
    private Long subType;
    /**
     * 任务状态，1-draft,2-assigned, 3-done, 4-cancel
     */
    private Long status;
    /**
     * 资源分配状态, 1-未分配， 2-已分配
     */
    private Long isAllocated;
    /**
     * 优先级
     */
    private Long priority;
    /**
     * 创建时间
     */
    private Long draftTime;
    /**
     * 分配时间
     */
    private Long assignTime;
    /**
     * 最晚完成时间
     */
    private Long dueTime;
    /**
     * 实际完成时间
     */
    private Long finishTime;
    /**
     * 取消时间
     */
    private Long cancelTime;
    /**
     * 扩展字段
     */
    private Long ext1;
    /**
     * 扩展字段
     */
    private Long ext2;
    /**
     * 扩展字段
     */
    private Long ext3;
    /**
     * 扩展字段
     */
    private Long ext4;
    /**
     * 扩展字段
     */
    private Long ext5;
    /**
     * 扩展字段
     */
    private String ext6;
    /**
     * 扩展字段
     */
    private String ext7;
    /**
     * 扩展字段
     */
    private String ext8;
    /**
     * 扩展字段
     */
    private String ext9;
    /**
     * 合板后关系容器id
     */
    private Long mergedContainerId;
    /**
     * 子任务执行状态
     */
    private int step;
    /**
     * 是否显示，非必须
     */
    private int isShow;
    /**
     * 任务执行顺序
     */
    private Long taskOrder;
    /**
     * 直流在库标记 1在库 2直流
     */
    private Long businessMode;

    /**  */
    private Long createdAt;
    /**  */
    private Long updatedAt;


    public Long date;
    /**
     * TMS线路
     */
    private String transPlan;

    /**
     * 任务工作量(按外包装计算)
     */
    private BigDecimal taskPackQty;
    /**
     * 任务工作量(ea计算)
     */
    private BigDecimal taskEaQty;

    private BigDecimal taskQty;

    /**
     * QC任务之前承接的taskId，用于判断生成qc前是什么任务，好去wave_detail中去，去需要qc的数量，不一定是拣货
     */
    private Long qcPreviousTaskId;

    /**
     * 是否跳过q明细，0不跳过1-跳过
     */
    private Integer qcSkip;

    //是否余货
    private Boolean isRest;
    //是否贵品
    private Boolean isExpensive;

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Long getPlanId() {
        return planId;
    }

    public Long getWaveId() {
        return waveId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getFromLocationId() {
        return fromLocationId;
    }

    public Long getToLocationId() {
        return toLocationId;
    }

    public Long getRealFromLocationId() {
        return realFromLocationId;
    }

    public Long getRealToLocationId() {
        return realToLocationId;
    }

    public BigDecimal getQtyDone() {
        return qtyDone;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public BigDecimal getQtyUom() {
        return qtyUom;
    }

    public BigDecimal getQtyDoneUom() {
        return qtyDoneUom;
    }

    public BigDecimal getPackUnit() {
        return packUnit;
    }

    public String getPackName() {
        return packName;
    }

    public Long getContainerId() {
        return containerId;
    }

    public Long getOperator() {
        return operator;
    }

    public Long getPlanner() {
        return planner;
    }

    public Long getType() {
        return type;
    }

    public Long getSubType() {
        return subType;
    }

    public Long getStatus() {
        return status;
    }

    public Long getIsAllocated() {
        return isAllocated;
    }

    public Long getPriority() {
        return priority;
    }

    public Long getDraftTime() {
        return draftTime;
    }

    public Long getAssignTime() {
        return assignTime;
    }

    public Long getDueTime() {
        return dueTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public Long getCancelTime() {
        return cancelTime;
    }

    public Long getExt1() {
        return ext1;
    }

    public Long getExt2() {
        return ext2;
    }

    public Long getExt3() {
        return ext3;
    }

    public Long getExt4() {
        return ext4;
    }

    public Long getExt5() {
        return ext5;
    }

    public String getExt6() {
        return ext6;
    }

    public String getExt7() {
        return ext7;
    }

    public String getExt8() {
        return ext8;
    }

    public String getExt9() {
        return ext9;
    }

    public Long getMergedContainerId() {
        return mergedContainerId;
    }

    public int getStep() {
        return step;
    }

    public int getIsShow() {
        return isShow;
    }

    public Long getTaskOrder() {
        return taskOrder;
    }

    public Long getBusinessMode() {
        return businessMode;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Long getDate() {
        return date;
    }

    public String getTransPlan() {
        return transPlan;
    }

    public BigDecimal getTaskPackQty() {
        return taskPackQty;
    }

    public BigDecimal getTaskEaQty() {
        return taskEaQty;
    }

    public BigDecimal getTaskQty() {
        return taskQty;
    }

    public Long getQcPreviousTaskId() {
        return qcPreviousTaskId;
    }

    public Integer getQcSkip() {
        return qcSkip;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public void setWaveId(Long waveId) {
        this.waveId = waveId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setFromLocationId(Long fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public void setToLocationId(Long toLocationId) {
        this.toLocationId = toLocationId;
    }

    public void setRealFromLocationId(Long realFromLocationId) {
        this.realFromLocationId = realFromLocationId;
    }

    public void setRealToLocationId(Long realToLocationId) {
        this.realToLocationId = realToLocationId;
    }

    public void setQtyDone(BigDecimal qtyDone) {
        this.qtyDone = qtyDone;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void setQtyUom(BigDecimal qtyUom) {
        this.qtyUom = qtyUom;
    }

    public void setQtyDoneUom(BigDecimal qtyDoneUom) {
        this.qtyDoneUom = qtyDoneUom;
    }

    public void setPackUnit(BigDecimal packUnit) {
        this.packUnit = packUnit;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public void setPlanner(Long planner) {
        this.planner = planner;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public void setSubType(Long subType) {
        this.subType = subType;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public void setIsAllocated(Long isAllocated) {
        this.isAllocated = isAllocated;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public void setDraftTime(Long draftTime) {
        this.draftTime = draftTime;
    }

    public void setAssignTime(Long assignTime) {
        this.assignTime = assignTime;
    }

    public void setDueTime(Long dueTime) {
        this.dueTime = dueTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public void setCancelTime(Long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public void setExt1(Long ext1) {
        this.ext1 = ext1;
    }

    public void setExt2(Long ext2) {
        this.ext2 = ext2;
    }

    public void setExt3(Long ext3) {
        this.ext3 = ext3;
    }

    public void setExt4(Long ext4) {
        this.ext4 = ext4;
    }

    public void setExt5(Long ext5) {
        this.ext5 = ext5;
    }

    public void setExt6(String ext6) {
        this.ext6 = ext6;
    }

    public void setExt7(String ext7) {
        this.ext7 = ext7;
    }

    public void setExt8(String ext8) {
        this.ext8 = ext8;
    }

    public void setExt9(String ext9) {
        this.ext9 = ext9;
    }

    public void setMergedContainerId(Long mergedContainerId) {
        this.mergedContainerId = mergedContainerId;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public void setTaskOrder(Long taskOrder) {
        this.taskOrder = taskOrder;
    }

    public void setBusinessMode(Long businessMode) {
        this.businessMode = businessMode;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setTransPlan(String transPlan) {
        this.transPlan = transPlan;
    }

    public void setTaskPackQty(BigDecimal taskPackQty) {
        this.taskPackQty = taskPackQty;
    }

    public void setTaskEaQty(BigDecimal taskEaQty) {
        this.taskEaQty = taskEaQty;
    }

    public void setTaskQty(BigDecimal taskQty) {
        this.taskQty = taskQty;
    }

    public void setQcPreviousTaskId(Long qcPreviousTaskId) {
        this.qcPreviousTaskId = qcPreviousTaskId;
    }

    public void setQcSkip(Integer qcSkip) {
        this.qcSkip = qcSkip;
    }

    public Boolean getExpensive() {
        return isExpensive;
    }

    public void setExpensive(Boolean expensive) {
        isExpensive = expensive;
    }

    public Boolean getRest() {
        return isRest;
    }

    public void setRest(Boolean rest) {
        isRest = rest;
    }
}
