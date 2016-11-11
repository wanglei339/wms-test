package com.lsh.wms.model.baseinfo;

import java.io.Serializable;
import java.util.Date;

public class BassinfoExceptionCode implements Serializable {

	/**  */
    private Long id;
	/** 例外代码 */
    private String exceptionCode;
	/** 例外代码名称*/
	private String exceptionName;
	/** 例外说明描述 */
    private String exceptionExplain;
	/** 状态 0禁用 1可用 */
    private Long status;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getExceptionCode(){
		return this.exceptionCode;
	}
	
	public void setExceptionCode(String exceptionCode){
		this.exceptionCode = exceptionCode;
	}
	
	public String getExceptionExplain(){
		return this.exceptionExplain;
	}
	
	public void setExceptionExplain(String exceptionExplain){
		this.exceptionExplain = exceptionExplain;
	}
	
	public Long getStatus(){
		return this.status;
	}
	
	public void setStatus(Long status){
		this.status = status;
	}


	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}
}
