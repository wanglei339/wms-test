package com.lsh.wms.model.seed;

import java.io.Serializable;
import java.math.BigDecimal;

public class SeedingTaskHead implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId;
	/** 门店号 */
    private String storeNo;
	/** poId */
	private Long orderId;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	/** 所需数量 */
    private BigDecimal requireQty;
	/** 播种目标托盘id */
	private Long realContainerId = 0L;
	/** 订单箱规 */
	private BigDecimal packUnit;
	/** po详情id */
	private String ibdDetailId;
	/** so详情id */
	private String obdDetailId;
	/** 是否使用例外代码*/
	private Integer isUseExceptionCode = 0;
	/** 门店类型*/
	private String storeType = "";
	/** 任务状态*/
	private Long status = 1l;
	/** 商品id*/
	private Long skuId = 0l;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
	}
	
	public String getStoreNo(){
		return this.storeNo;
	}
	
	public void setStoreNo(String storeNo){
		this.storeNo = storeNo;
	}
	
	public Long getCreatedAt(){
		return this.createdAt;
	}
	
	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}
	
	public Long getUpdatedAt(){
		return this.updatedAt;
	}
	
	public void setUpdatedAt(Long updatedAt){
		this.updatedAt = updatedAt;
	}
	
	public BigDecimal getRequireQty(){
		return this.requireQty;
	}
	
	public void setRequireQty(BigDecimal requireQty){
		this.requireQty = requireQty;
	}

	public Long getRealContainerId() {
		return realContainerId;
	}

	public void setRealContainerId(Long realContainerId) {
		this.realContainerId = realContainerId;
	}

	public BigDecimal getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(BigDecimal packUnit) {
		this.packUnit = packUnit;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getIsUseExceptionCode() {
		return isUseExceptionCode;
	}

	public void setIsUseExceptionCode(Integer isUseExceptionCode) {
		this.isUseExceptionCode = isUseExceptionCode;
	}

	public String getIbdDetailId() {
		return ibdDetailId;
	}

	public void setIbdDetailId(String ibdDetailId) {
		this.ibdDetailId = ibdDetailId;
	}

	public String getObdDetailId() {
		return obdDetailId;
	}

	public void setObdDetailId(String obdDetailId) {
		this.obdDetailId = obdDetailId;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	@Override
	public String toString() {
		return "SeedingTaskHead{" +
				"id=" + id +
				", taskId=" + taskId +
				", storeNo='" + storeNo + '\'' +
				", orderId=" + orderId +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", requireQty=" + requireQty +
				", realContainerId=" + realContainerId +
				", packUnit=" + packUnit +
				", ibdDetailId='" + ibdDetailId + '\'' +
				", obdDetailId='" + obdDetailId + '\'' +
				", isUseExceptionCode=" + isUseExceptionCode +
				", storeType='" + storeType + '\'' +
				", status=" + status +
				", skuId=" + skuId +
				'}';
	}
}
