package com.lsh.wms.model.task;

import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class TaskInfo implements Serializable {

	/**  */
    private Long id;
	/** 任务id */
    private Long taskId = 0L;
	/** 任务名称 */
    private String taskName = "";
	/** 计划id */
    private Long planId = 0L;
	/** 波次id */
    private Long waveId = 0L;
	/** 订单id */
    private Long orderId = 0L;
	/** 收货id */
	private Long receiptId = 0L;
	/** 当前LocationId, 分配查找使用 */
    private Long locationId = 0L;
	/** 商品id，分配查找用 */
    private Long skuId = 0L;
	/** 容器id，分配查找用 */
    private Long containerId = 0L;
	/** 操作人员id */
    private Long operator = 0L;
	/** 发起人员id */
    private Long planner = 0L;
	/** 任务类型，100-盘点， 101-收货，102-波次， 103-上架，104-补货, 105-移库 */
    private Long type = 0L;
	/** 任务子类型, 由个任务类型自解释,可以不需要 **/
	private Long subType = 0L;
	/** 任务状态，1-draft, 2-waiting, 3-assigned, 4-allocated, 5-done, 6-cancel */
    private Long status = 0L;
	/** 优先级 */
    private Long priority = 0L;
	/** 创建时间 */
    private Long draftTime = 0L;
	/** 分配时间 */
    private Long assignTime = 0L;
	/** 最晚完成时间 */
    private Long dueTime = 0L;
	/** 实际完成时间 */
    private Long finishTime = 0L;
	/** 取消时间 */
    private Long cancelTime = 0L ;
	/** 扩展字段 */
    private Long ext1 = 0L;
	/** 扩展字段 */
    private Long ext2 = 0L;
	/** 扩展字段 */
    private Long ext3 = 0L;
	/** 扩展字段 */
    private Long ext4 = 0L;
	/** 扩展字段 */
    private Long ext5 = 0L;
	/** 扩展字段 */
    private String ext6 = "";
	/** 扩展字段 */
    private String ext7 = "";
	/** 扩展字段 */
    private String ext8 = "";
	/** 扩展字段 */
    private String ext9 = "";
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	
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
	
	public String getTaskName(){
		return this.taskName;
	}
	
	public void setTaskName(String taskName){
		this.taskName = taskName;
	}
	
	public Long getPlanId(){
		return this.planId;
	}
	
	public void setPlanId(Long planId){
		this.planId = planId;
	}
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(Long orderId){
		this.orderId = orderId;
	}

	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public Long getOperator(){
		return this.operator;
	}
	
	public void setOperator(Long operator){
		this.operator = operator;
	}
	
	public Long getPlanner(){
		return this.planner;
	}
	
	public void setPlanner(Long planner){
		this.planner = planner;
	}
	
	public Long getType(){
		return this.type;
	}
	
	public void setType(Long type){
		this.type = type;
	}

	public Long getSubType(){
		return this.subType;
	}

	public void setSubType(Long subType){
		this.subType = subType;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getPriority(){
		return this.priority;
	}
	
	public void setPriority(Long priority){
		this.priority = priority;
	}
	
	public Long getDraftTime(){
		return this.draftTime;
	}
	
	public void setDraftTime(Long draftTime){
		this.draftTime = draftTime;
	}
	
	public Long getAssignTime(){
		return this.assignTime;
	}
	
	public void setAssignTime(Long assignTime){
		this.assignTime = assignTime;
	}
	
	public Long getDueTime(){
		return this.dueTime;
	}
	
	public void setDueTime(Long dueTime){
		this.dueTime = dueTime;
	}
	
	public Long getFinishTime(){
		return this.finishTime;
	}
	
	public void setFinishTime(Long finishTime){
		this.finishTime = finishTime;
	}
	
	public Long getCancelTime(){
		return this.cancelTime;
	}
	
	public void setCancelTime(Long cancelTime){
		this.cancelTime = cancelTime;
	}
	
	public Long getExt1(){
		return this.ext1;
	}
	
	public void setExt1(Long ext1){
		this.ext1 = ext1;
	}
	
	public Long getExt2(){
		return this.ext2;
	}
	
	public void setExt2(Long ext2){
		this.ext2 = ext2;
	}
	
	public Long getExt3(){
		return this.ext3;
	}
	
	public void setExt3(Long ext3){
		this.ext3 = ext3;
	}
	
	public Long getExt4(){
		return this.ext4;
	}
	
	public void setExt4(Long ext4){
		this.ext4 = ext4;
	}
	
	public Long getExt5(){
		return this.ext5;
	}
	
	public void setExt5(Long ext5){
		this.ext5 = ext5;
	}
	
	public String getExt6(){
		return this.ext6;
	}
	
	public void setExt6(String ext6){
		this.ext6 = ext6;
	}
	
	public String getExt7(){
		return this.ext7;
	}
	
	public void setExt7(String ext7){
		this.ext7 = ext7;
	}
	
	public String getExt8(){
		return this.ext8;
	}
	
	public void setExt8(String ext8){
		this.ext8 = ext8;
	}
	
	public String getExt9(){
		return this.ext9;
	}
	
	public void setExt9(String ext9){
		this.ext9 = ext9;
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
	
	
}
