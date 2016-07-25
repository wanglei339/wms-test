package com.lsh.wms.model.stock;

//import com.lsh.base.common.utils.ClockUtils;
//import com.lsh.base.common.utils.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockQuant implements Serializable,Cloneable {

	private static final Logger logger = LoggerFactory.getLogger(StockQuant.class);
	/**  */
    private Long id;
	/** 商品id */
    private Long skuId;
	/** 存储库位id */
    private Long locationId;
	/** 容器设备id */
    private Long containerId = 0L;
	/** 数量 */
    private BigDecimal qty = BigDecimal.ZERO;
	/** 商品单位转换id */
    private Long skuUomId = 0L;
	/** 库存价值 */
    private BigDecimal value = BigDecimal.ZERO;
	/** 库存成本 */
    private BigDecimal cost = BigDecimal.ZERO;
	/** 占位stock_move id */
    private Long reserveMoveId = 0L;
	/** 0-可用，1-被冻结 */
    private Long isFrozen = 0L;
	/** 货物供应商id */
    private Long supplierId = 0L;
	/** 货物所属公司id */
    private Long ownerId = 0L;
	/** 批次号 */
    private Long lotId = 0L;
	/** 入库时间 */
    private Long inDate = 0L;
	/** 保质期失效时间 */
    private Long expireDate = 0L;
	/**  */
    private Long createdAt;
	/**  */
    private Long updatedAt;

	private Long itemId;

	private String stockLot;


    public Long getId(){

		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getSkuId(){
		return this.skuId;
	}
	
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}
	
	public Long getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(Long locationId){
		this.locationId = locationId;
	}
	
	public Long getContainerId(){
		return this.containerId;
	}
	
	public void setContainerId(Long containerId){
		this.containerId = containerId;
	}
	
	public BigDecimal getQty(){
		return this.qty;
	}
	
	public void setQty(BigDecimal qty){
		this.qty = qty;
	}
	
	public Long getSkuUomId(){
		return this.skuUomId;
	}
	
	public void setSkuUomId(Long skuUomId){
		this.skuUomId = skuUomId;
	}
	
	public BigDecimal getValue(){
		return this.value;
	}
	
	public void setValue(BigDecimal value){
		this.value = value;
	}
	
	public BigDecimal getCost(){
		return this.cost;
	}
	
	public void setCost(BigDecimal cost){
		this.cost = cost;
	}
	
	public Long getReserveMoveId(){
		return this.reserveMoveId;
	}
	
	public void setReserveMoveId(Long reserveMoveId){
		this.reserveMoveId = reserveMoveId;
	}
	
	public Long getIsFrozen(){
		return this.isFrozen;
	}
	
	public void setIsFrozen(Long isFrozen){
		this.isFrozen = isFrozen;
	}
	
	public Long getSupplierId(){
		return this.supplierId;
	}
	
	public void setSupplierId(Long supplierId){
		this.supplierId = supplierId;
	}
	
	public Long getOwnerId(){
		return this.ownerId;
	}
	
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	public Long getLotId(){
		return this.lotId;
	}
	
	public void setLotId(Long lotId){
		this.lotId = lotId;
	}
	
	public Long getInDate(){
		return this.inDate;
	}
	
	public void setInDate(Long inDate){
		this.inDate = inDate;
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

	public String getStockLot() {
		return stockLot;
	}

	public void setStockLot(String stockLot) {
		this.stockLot = stockLot;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}



	@Override
    public Object clone() {
        StockQuant stockQuant = null;
        try{
            stockQuant = (StockQuant)super.clone();
            stockQuant.setId(null);
            stockQuant.setQty(null);
        }catch ( CloneNotSupportedException ex){
            logger.error(ex.toString());
        }
        return stockQuant;
    }
}
