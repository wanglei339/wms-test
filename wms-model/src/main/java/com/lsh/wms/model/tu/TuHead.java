package com.lsh.wms.model.tu;

import java.io.Serializable;
import java.util.Date;

public class TuHead implements Serializable {

	/**  */
    private Long id;
	/** 运单编号 */
    private String tuId;
	/** 司机ID */
    private Long transUid;
	/** 司机手机号码 */
    private String cellphone;
	/** 司机姓名 */
    private String name;
	/** 车牌号 */
    private String carNumber;
	/** 状态1.待装车5已装车9已发货 */
    private Integer status;
	/** 门店id字符串 */
    private String storeIds;
	/** 预装板数 */
    private Long preBoard;
	/** 实际板数 */
    private Long realBoard;
	/** 提交时间 */
    private Long commitedAt;
	/** 仓库ID 天津id */
    private String warehouseId;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;
	/** 装车完毕时间 */
    private Long loadedAt;
	/** 发货人|中控 */
    private Long deliveryUid;
	/** 发货时间 */
    private Long deliveryAt;
	/** 是否有效, 0否, 1是 */
    private Integer isValid=1;
	/** 扫货装车人的uid */
	private Long loadUid;
	/** 门店范围1-小店2-大店 */
	private Integer scale;
	/** rf尾货开关0-开启尾货显示1-关闭尾货 */
	private Integer rfSwitch = 0;

	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getTuId(){
		return this.tuId;
	}

	public void setTuId(String tuId){
		this.tuId = tuId;
	}

	public Long getTransUid(){
		return this.transUid;
	}

	public void setTransUid(Long transUid){
		this.transUid = transUid;
	}

	public String getCellphone(){
		return this.cellphone;
	}

	public void setCellphone(String cellphone){
		this.cellphone = cellphone;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getCarNumber(){
		return this.carNumber;
	}

	public void setCarNumber(String carNumber){
		this.carNumber = carNumber;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getStoreIds(){
		return this.storeIds;
	}

	public void setStoreIds(String storeIds){
		this.storeIds = storeIds;
	}

	public Long getPreBoard(){
		return this.preBoard;
	}

	public void setPreBoard(Long preBoard){
		this.preBoard = preBoard;
	}

	public Long getRealBoard(){
		return this.realBoard;
	}

	public void setRealBoard(Long realBoard){
		this.realBoard = realBoard;
	}

	public Long getCommitedAt(){
		return this.commitedAt;
	}

	public void setCommitedAt(Long commitedAt){
		this.commitedAt = commitedAt;
	}

	public String getWarehouseId(){
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId){
		this.warehouseId = warehouseId;
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

	public Long getLoadedAt(){
		return this.loadedAt;
	}

	public void setLoadedAt(Long loadedAt){
		this.loadedAt = loadedAt;
	}

	public Long getDeliveryUid(){
		return this.deliveryUid;
	}

	public void setDeliveryUid(Long deliveryUid){
		this.deliveryUid = deliveryUid;
	}

	public Long getDeliveryAt(){
		return this.deliveryAt;
	}

	public void setDeliveryAt(Long deliveryAt){
		this.deliveryAt = deliveryAt;
	}

	public Integer getIsValid(){
		return this.isValid;
	}

	public void setIsValid(Integer isValid){
		this.isValid = isValid;
	}

    public Long getLoadUid() {
        return loadUid;
    }

    public void setLoadUid(Long loadUid) {
        this.loadUid = loadUid;
    }

	public Integer getScale() {
		return scale;
	}

	public Integer getRfSwitch() {
		return rfSwitch;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public void setRfSwitch(Integer rfSwitch) {
		this.rfSwitch = rfSwitch;
	}
}
