package com.lsh.wms.model.so;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OutBoundTime implements Serializable {

	/**  */
	private String beginTime = "1970-01-01";
	/**  */
	private String endTime = "1970-01-01";

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public OutBoundTime(Long beginTime, Long endTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.beginTime = "'"+format.format(new Date(beginTime * 1000))+"'";
		this.endTime = "'"+format.format(new Date(endTime * 1000))+"'";
	}
}
