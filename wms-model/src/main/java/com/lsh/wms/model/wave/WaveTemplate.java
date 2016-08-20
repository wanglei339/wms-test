package com.lsh.wms.model.wave;

import java.io.Serializable;
import java.util.Date;

public class WaveTemplate implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long waveTemplateId = 0L;
	/**  */
    private String waveTemplateName = "";
	/**  */
    private Long status = 0L;
	/** 波次产生目的, COMMON-普通, YG-优供, SUPERMARKET-大卖场 */
    private String waveDest = "YG";
	/** 集货道组 */
    private Long collectLocations = 0L;
	/** 是否使用精细的集货位 */
    private Long useCollectBin = 0L;
	/** 捡货模型模版id */
    private Long pickModelTemplateId = 0L;
	/**  */
    private Long createdAt = 0L;
	/**  */
    private Long updatedAt = 0L;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getWaveTemplateId(){
		return this.waveTemplateId;
	}
	
	public void setWaveTemplateId(Long waveTemplateId){
		this.waveTemplateId = waveTemplateId;
	}
	
	public String getWaveTemplateName(){
		return this.waveTemplateName;
	}
	
	public void setWaveTemplateName(String waveTemplateName){
		this.waveTemplateName = waveTemplateName;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}
	
	public String getWaveDest(){
		return this.waveDest;
	}
	
	public void setWaveDest(String waveDest){
		this.waveDest = waveDest;
	}
	
	public Long getCollectLocations(){
		return this.collectLocations;
	}
	
	public void setCollectLocations(Long collectLocations){
		this.collectLocations = collectLocations;
	}
	
	public Long getUseCollectBin(){
		return this.useCollectBin;
	}
	
	public void setUseCollectBin(Long useCollectBin){
		this.useCollectBin = useCollectBin;
	}
	
	public Long getPickModelTemplateId(){
		return this.pickModelTemplateId;
	}
	
	public void setPickModelTemplateId(Long pickModelTemplateId){
		this.pickModelTemplateId = pickModelTemplateId;
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
