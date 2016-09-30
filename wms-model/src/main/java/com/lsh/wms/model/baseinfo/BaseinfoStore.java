package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStore implements Serializable {

	/**  */
    private Long id;
	/** 门店号0-在库内xxxx是门店号 */
    private Long storeNo;
	/** 门店名称 */
    private String storeName;
	/** 区域名称 */
    private String region;
	/** 规模1-小店2-大店 */
    private Integer scale;
	/** 运营情况1-正常2-关闭 */
    private Integer isOpen;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getStoreNo(){
		return this.storeNo;
	}
	
	public void setStoreNo(Long storeNo){
		this.storeNo = storeNo;
	}
	
	public String getStoreName(){
		return this.storeName;
	}
	
	public void setStoreName(String storeName){
		this.storeName = storeName;
	}
	
	public String getRegion(){
		return this.region;
	}
	
	public void setRegion(String region){
		this.region = region;
	}
	
	public Integer getScale(){
		return this.scale;
	}
	
	public void setScale(Integer scale){
		this.scale = scale;
	}
	
	public Integer getIsOpen(){
		return this.isOpen;
	}
	
	public void setIsOpen(Integer isOpen){
		this.isOpen = isOpen;
	}
	
	
}
