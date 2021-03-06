package com.lsh.wms.model.baseinfo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseinfoItemRequest implements Serializable {


	/** 货主id */
	private Long ownerId;
	/** 货主商品编号 */
	private String skuCode;
	/** 商品名称 */
	private String skuName;
	/** 标准码类型, 1 - 国条, 2 - ISBN */
	private Integer codeType = 1;
	/** 标准唯一码 */
	private String code;
	/** 1级品类id */
	private Long topCat = 0L;
	/** 2级品类id */
	private Long secondCat = 0L;
	/** 3级品类id */
	private Long thirdCat = 0L;
	/** 保质期天数 */
	private BigDecimal shelfLife = BigDecimal.ZERO;
	/** 2级包装单位－含有基本单位个数 */
	private BigDecimal l2Unit= new BigDecimal(0);
	/** 外包装－重量 */
	private BigDecimal packWeight= new BigDecimal(0);
	/** 外包装单位-含有基本单位个数 */
	private BigDecimal packUnit= new BigDecimal(0);
	/** 售卖单位 */
	private BigDecimal saleUnit= new BigDecimal(0);
	/** 商品等级，对应A,B,C */
	private Integer itemLevel =3;
	/** 产地 */
	private String producePlace = "";
	/** 批次号要求 */
	private Integer batchNeeded = 0;
	/** 存储温度 */
	private Integer storageTemperature;
	/** 基本单位名称 */
	private String unitName = "";
	/** 二级单位名称 */
	private String l2Name = "";
	/** 外包装名称 */
	private String packName = "";
	/**是否贵品 1贵品 2 非贵品*/
	private Integer isValuable = 0;
	/**商品类型*/
	private Integer itemType = 0;
	/**是否有保质期  0没有 1有*/
	private Integer isShelfLifeValid = 1;
	/**箱码*/
	private String packCode;


	public Long getOwnerId(){
		return this.ownerId;
	}

	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
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

	public Integer getCodeType(){
		return this.codeType;
	}

	public void setCodeType(Integer codeType){
		this.codeType = codeType;
	}

	public String getCode(){
		return this.code;
	}

	public void setCode(String code){
		this.code = code;
	}

	public Long getTopCat(){
		return this.topCat;
	}

	public void setTopCat(Long topCat){
		this.topCat = topCat;
	}

	public Long getSecondCat(){
		return this.secondCat;
	}

	public void setSecondCat(Long secondCat){
		this.secondCat = secondCat;
	}

	public Long getThirdCat(){
		return this.thirdCat;
	}

	public void setThirdCat(Long thirdCat){
		this.thirdCat = thirdCat;
	}

	public BigDecimal getShelfLife(){
		return this.shelfLife;
	}

	public void setShelfLife(BigDecimal shelfLife){
		this.shelfLife = shelfLife;
	}

	public BigDecimal getL2Unit(){
		return this.l2Unit;
	}

	public void setL2Unit(BigDecimal l2Unit){
		this.l2Unit = l2Unit;
	}


	public BigDecimal getPackWeight(){
		return this.packWeight;
	}

	public void setPackWeight(BigDecimal packWeight){
		this.packWeight = packWeight;
	}

	public BigDecimal getPackUnit(){
		return this.packUnit;
	}

	public void setPackUnit(BigDecimal packUnit){
		this.packUnit = packUnit;
	}

	public BigDecimal getSaleUnit(){
		return this.saleUnit;
	}

	public void setSaleUnit(BigDecimal saleUnit){
		this.saleUnit = saleUnit;
	}

	public Integer getItemLevel(){
		return this.itemLevel;
	}

	public void setItemLevel(Integer itemLevel){
		this.itemLevel = itemLevel;
	}

	public String getProducePlace(){
		return this.producePlace;
	}

	public void setProducePlace(String producePlace){
		this.producePlace = producePlace;
	}

	public Integer getBatchNeeded(){
		return this.batchNeeded;
	}

	public void setBatchNeeded(Integer batchNeeded){
		this.batchNeeded = batchNeeded;
	}

	public Integer getStorageTemperature(){
		return this.storageTemperature;
	}

	public void setStorageTemperature(Integer storageTemperature){
		this.storageTemperature = storageTemperature;
	}

	public String getL2Name() {
		return l2Name;
	}

	public void setL2Name(String l2Name) {
		this.l2Name = l2Name;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getIsValuable() {
		return isValuable;
	}

	public void setIsValuable(Integer isValuable) {
		this.isValuable = isValuable;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}


	public Integer getIsShelfLifeValid() {
		return isShelfLifeValid;
	}

	public void setIsShelfLifeValid(Integer isShelfLifeValid) {
		this.isShelfLifeValid = isShelfLifeValid;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

}
