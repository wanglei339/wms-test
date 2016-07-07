package com.lsh.wms.model.image;

import java.io.Serializable;
import java.util.Date;

public class PubImage implements Serializable {

	/**  */
    private Long id;
	/**  */
    private Long uid;
	/**  */
    private String uuid;
	/**  */
    private String picName;
	/**  */
    private String picPath;
	/**  */
    private String picUrl;
	/**业务系统ID */
    private Integer busType;
	/**  */
    private Integer picWidth;
	/**  */
    private Integer picHeight;
	/**  */
    private String clipParam;
	/** 分子 */
    private Integer molecular;
	/** 分母 */
    private Integer denominator;
	/**  */
    private Integer picAudit;
	/**  */
    private Integer status;
	/**  */
    private Date createdTime;
	/**  */
    private Date updatedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getBusType() {
		return busType;
	}

	public void setBusType(Integer busType) {
		this.busType = busType;
	}

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public Integer getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(Integer picHeight) {
		this.picHeight = picHeight;
	}

	public String getClipParam() {
		return clipParam;
	}

	public void setClipParam(String clipParam) {
		this.clipParam = clipParam;
	}

	public Integer getMolecular() {
		return molecular;
	}

	public void setMolecular(Integer molecular) {
		this.molecular = molecular;
	}

	public Integer getDenominator() {
		return denominator;
	}

	public void setDenominator(Integer denominator) {
		this.denominator = denominator;
	}

	public Integer getPicAudit() {
		return picAudit;
	}

	public void setPicAudit(Integer picAudit) {
		this.picAudit = picAudit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
}
