package com.lsh.wms.model.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ReceiveDetail implements Serializable {

	/**  */
    private Long id;
	/** 上游细单id */
    private String detailOtherId;
	/** 验收id */
    private Long receiveId;
	/** 物美码 */
    private String skuCode;
	/** 商品名称 */
    private String skuName;
	/** 标准唯一码*/
	private String code;
	/** 进货数 */
    private BigDecimal orderQty;
	/** 包装名称 */
    private String packName;
	/** 包装单位 */
    private BigDecimal packUnit;
	/** 基本单位名称 */
    private String unitName;
	/** 基本单位数量 */
    private BigDecimal unitQty;
	/** 价格 */
    private BigDecimal price;
	/** 实际收货数 */
    private BigDecimal inboundQty = BigDecimal.ZERO;
	/** 批次号 */
    private String lotCode = "";
	/** 保质期例外收货 0校验保质期1不校验 */
    private String exceptionReceipt;
	/** 返仓单生成移库任务的taskid */
    private Long taskId = 0l;
	/**  */
    private Long createdAt = 0l;
	/**  */
    private Long updatedAt = 0l;

	/** 物美回传创建ibd号 */
	private String ibdId = "";
	/** 创建ibd 行项目号 */
	private String ibdDetailId = "";
	/** 物美过账成功后返回的凭证号 */
	private String accountId = "";
	/** 过账凭证明细 */
	private String accountDetailId = "";

	/**回传状态 1未过账 2过账成功*/
	private Integer backStatus = 1;

	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getDetailOtherId(){
		return this.detailOtherId;
	}
	
	public void setDetailOtherId(String detailOtherId){
		this.detailOtherId = detailOtherId;
	}
	
	public Long getReceiveId(){
		return this.receiveId;
	}
	
	public void setReceiveId(Long receiveId){
		this.receiveId = receiveId;
	}
	
	public String getSkuCode(){
		return this.skuCode;
	}
	
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;
	}
	
	public String getSkuName(){
		return this.skuName;
	}
	
	public void setSkuName(String skuName){
		this.skuName = skuName;
	}
	
	public BigDecimal getOrderQty(){
		return this.orderQty;
	}
	
	public void setOrderQty(BigDecimal orderQty){
		this.orderQty = orderQty;
	}
	
	public String getPackName(){
		return this.packName;
	}
	
	public void setPackName(String packName){
		this.packName = packName;
	}
	
	public BigDecimal getPackUnit(){
		return this.packUnit;
	}
	
	public void setPackUnit(BigDecimal packUnit){
		this.packUnit = packUnit;
	}
	
	public String getUnitName(){
		return this.unitName;
	}
	
	public void setUnitName(String unitName){
		this.unitName = unitName;
	}
	
	public BigDecimal getUnitQty(){
		return this.unitQty;
	}
	
	public void setUnitQty(BigDecimal unitQty){
		this.unitQty = unitQty;
	}
	
	public BigDecimal getPrice(){
		return this.price;
	}
	
	public void setPrice(BigDecimal price){
		this.price = price;
	}
	
	public BigDecimal getInboundQty(){
		return this.inboundQty;
	}
	
	public void setInboundQty(BigDecimal inboundQty){
		this.inboundQty = inboundQty;
	}
	
	public String getLotCode(){
		return this.lotCode;
	}
	
	public void setLotCode(String lotCode){
		this.lotCode = lotCode;
	}
	
	public String getExceptionReceipt(){
		return this.exceptionReceipt;
	}
	
	public void setExceptionReceipt(String exceptionReceipt){
		this.exceptionReceipt = exceptionReceipt;
	}
	
	public Long getTaskId(){
		return this.taskId;
	}
	
	public void setTaskId(Long taskId){
		this.taskId = taskId;
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


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccountDetailId() {
		return accountDetailId;
	}

	public void setAccountDetailId(String accountDetailId) {
		this.accountDetailId = accountDetailId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIbdDetailId() {
		return ibdDetailId;
	}

	public void setIbdDetailId(String ibdDetailId) {
		this.ibdDetailId = ibdDetailId;
	}

	public String getIbdId() {
		return ibdId;
	}

	public void setIbdId(String ibdId) {
		this.ibdId = ibdId;
	}

	public Integer getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}
}
