package com.lsh.wms.model.pick;

import java.io.Serializable;
import java.util.Date;

public class PickWaveHead implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long waveId;
	/**  */
    private String waveName;
	/** 波次状态，10-新建，20-确定释放，30-释放完成，40-释放失败，50-已完成[完全出库], 100－取消 */
    private Long status;
	/** 波次类型 */
    private Long waveType;
	/** 波次模版id */
    private Long waveTemplateId;
	/** 波次产生来源，SYS-系统，TMS-运输系统 */
    private String waveSource;
	/** 波次产生目的, COMMON-普通, YG-优供, SUPERMARKET-大卖场 */
    private String waveDest;
	/** 捡货模型模版id */
    private Long pickModelTemplateId;
	/** 释放人 */
    private Long releaseUid;
	/** 释放人名称 */
    private String releaseUname;
	/** 释放时间 */
    private Long releaseAt;
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
	
	public Long getWaveId(){
		return this.waveId;
	}
	
	public void setWaveId(Long waveId){
		this.waveId = waveId;
	}
	
	public String getWaveName(){
		return this.waveName;
	}
	
	public void setWaveName(String waveName){
		this.waveName = waveName;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public Long getWaveType(){
		return this.waveType;
	}
	
	public void setWaveType(Long waveType){
		this.waveType = waveType;
	}
	
	public Long getWaveTemplateId(){
		return this.waveTemplateId;
	}
	
	public void setWaveTemplateId(Long waveTemplateId){
		this.waveTemplateId = waveTemplateId;
	}
	
	public String getWaveSource(){
		return this.waveSource;
	}
	
	public void setWaveSource(String waveSource){
		this.waveSource = waveSource;
	}
	
	public String getWaveDest(){
		return this.waveDest;
	}
	
	public void setWaveDest(String waveDest){
		this.waveDest = waveDest;
	}
	
	public Long getPickModelTemplateId(){
		return this.pickModelTemplateId;
	}
	
	public void setPickModelTemplateId(Long pickModelTemplateId){
		this.pickModelTemplateId = pickModelTemplateId;
	}
	
	public Long getReleaseUid(){
		return this.releaseUid;
	}
	
	public void setReleaseUid(Long releaseUid){
		this.releaseUid = releaseUid;
	}
	
	public String getReleaseUname(){
		return this.releaseUname;
	}
	
	public void setReleaseUname(String releaseUname){
		this.releaseUname = releaseUname;
	}
	
	public Long getReleaseAt(){
		return this.releaseAt;
	}
	
	public void setReleaseAt(Long releaseAt){
		this.releaseAt = releaseAt;
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