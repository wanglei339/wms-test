package com.lsh.wms.model.po;

import java.io.Serializable;
import java.util.Date;

public class IbdObdRelation implements Serializable {

	/**  */
    private Long id;
	/** ibd原始订单号 */
    private String ibdOtherId;
	/** ibd细单行项目号 */
    private String ibdDetailId;
	/** obd原始单据号 */
    private String obdOtherId;
	/** obd细单行项目号 */
    private String obdDetailId;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getIbdOtherId(){
		return this.ibdOtherId;
	}
	
	public void setIbdOtherId(String ibdOtherId){
		this.ibdOtherId = ibdOtherId;
	}
	
	public String getIbdDetailId(){
		return this.ibdDetailId;
	}
	
	public void setIbdDetailId(String ibdDetailId){
		this.ibdDetailId = ibdDetailId;
	}
	
	public String getObdOtherId(){
		return this.obdOtherId;
	}
	
	public void setObdOtherId(String obdOtherId){
		this.obdOtherId = obdOtherId;
	}
	
	public String getObdDetailId(){
		return this.obdDetailId;
	}
	
	public void setObdDetailId(String obdDetailId){
		this.obdDetailId = obdDetailId;
	}
	
	
}
