package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoStore implements Serializable {

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
    private Integer isValid;
	/** 创建时间 */
    private Long createAt;
	/** 更新时间 */
    private Long updateAt;
	/** 地址 */
    private String address;
	/** 门店id-暂留 */
    private Long storeId;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getStoreNo(){
		return this.storeNo;
	}
	
	public void setStoreNo(String storeNo){
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
	
	public Integer getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(Integer isValid){
		this.isValid = isValid;
	}
	
	public Long getCreateAt(){
		return this.createAt;
	}
	
	public void setCreateAt(Long createAt){
		this.createAt = createAt;
	}
	
	public Long getUpdateAt(){
		return this.updateAt;
	}
	
	public void setUpdateAt(Long updateAt){
		this.updateAt = updateAt;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public Long getStoreId(){
		return this.storeId;
	}
	
	public void setStoreId(Long storeId){
		this.storeId = storeId;
	}
	
	
}
