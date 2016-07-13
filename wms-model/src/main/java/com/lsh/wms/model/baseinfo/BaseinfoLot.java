package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BaseinfoLot implements Serializable {

	/**  */
    private Long id;
	/** 批次id */
    private Long lotId;
	/** 商品id */
    private Long skuId;
	/** 生产批次号 */
    private String serialNo;
	/** 入库时间 */
    private Long inDate;
	/**  */
    private Long productDate;
	/** 保质期失效时间 */
    private Long expireDate;
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
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public String getSerialNo(){
		return this.serialNo;
	}
	
	public void setSerialNo(String serialNo){
		this.serialNo = serialNo;
	}
	
	public Long getInDate(){
		return this.inDate;
	}
	
	public void setInDate(Long inDate){
		this.inDate = inDate;
	}
	
	public Long getProductDate(){
		return this.productDate;
	}
	
	public void setProductDate(Long productDate){
		this.productDate = productDate;
	}
	
	public Long getExpireDate(){
		return this.expireDate;
	}
	
	public void setExpireDate(Long expireDate){
		this.expireDate = expireDate;
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
