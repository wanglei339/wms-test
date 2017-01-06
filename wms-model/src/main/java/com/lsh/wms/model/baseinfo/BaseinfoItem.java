package com.lsh.wms.model.baseinfo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseinfoItem implements Serializable {


	/**  */
	private Long id;
	/**  */
	private Long itemId;
	/** 商品id */
	private Long skuId;
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
	/**商品状态*/
	private Long status = 1L;
	/** 1级品类id */
	private Long topCat = 0L;
	/** 2级品类id */
	private Long secondCat = 0L;
	/** 3级品类id */
	private Long thirdCat = 0L;
	/** 保质期天数 */
	private BigDecimal shelfLife = BigDecimal.ZERO;
	/** 基本单位-长 */
	private BigDecimal length = new BigDecimal(0);
	/** 基本单位-宽 */
	private BigDecimal width = new BigDecimal(0);
	/** 基本单位-高 */
	private BigDecimal height = new BigDecimal(0);
	/** 基本单位-重量 */
	private BigDecimal weight = new BigDecimal(0);
	/** 2级包装－长 */
	private BigDecimal l2Length = new BigDecimal(0);
	/** 2级包装－宽 */
	private BigDecimal l2Width= new BigDecimal(0);
	/** 2级包装－高 */
	private BigDecimal l2Height= new BigDecimal(0);
	/** 2级包装－重量 */
	private BigDecimal l2Weight= new BigDecimal(0);
	/** 2级包装单位－含有基本单位个数 */
	private BigDecimal l2Unit= new BigDecimal(0);
	/** 外包装－长 */
	private BigDecimal packLength= new BigDecimal(0);
	/** 外包装－宽 */
	private BigDecimal packWidth= new BigDecimal(0);
	/** 外包装－高 */
	private BigDecimal packHeight= new BigDecimal(0);
	/** 外包装－重量 */
	private BigDecimal packWeight= new BigDecimal(0);
	/** 外包装单位-含有基本单位个数 */
	private BigDecimal packUnit= new BigDecimal(0);
	/** 售卖单位 */
	private BigDecimal saleUnit= new BigDecimal(0);
	/** 商品等级，对应A,B,C */
	private Integer itemLevel =3;
	/**  */
	private Long createdAt = 0L;
	/**  */
	private Long updatedAt = 0L;
	/** 产地 */
	private String producePlace = "";
	/** 批次号要求 */
	private Integer batchNeeded = 0;
	/** 是否可放置地堆 */
	private Integer floorAvailable = 0;
	/** 存储温度 */
	private Integer storageTemperature;
	/** 安全库存 */
	private BigDecimal safetyQty = BigDecimal.ZERO;
	/** 码盘规则 */
	private Long pileX = 0l;
	/** 码盘规则 */
	private Long pileY =0l;
	/** 码盘规则 */
	private Long pileZ =0l;
	/** 码盘数量 */
	private Long pileNumber;
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
	/**商品信息是否完整 0不完整 1完整*/
	private Integer isInfoIntact = 0;
	/**是否有保质期  0没有 1有*/
	private Integer isShelfLifeValid = 1;

	/**商品数据是否有效1 有效2无效*/
	private Integer isValid = 1;

	/**箱码*/
	private String packCode;

	private String wmTopCat = "";

	private String wmSecondCat = "";

	private String wmThirdCat = "";



	public Long getId(){
		return this.id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSkuId(){
		return this.skuId;
	}

	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}

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


	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
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

	public BigDecimal getLength(){
		return this.length;
	}

	public void setLength(BigDecimal length){
		this.length = length;
	}

	public BigDecimal getWidth(){
		return this.width;
	}

	public void setWidth(BigDecimal width){
		this.width = width;
	}

	public BigDecimal getHeight(){
		return this.height;
	}

	public void setHeight(BigDecimal height){
		this.height = height;
	}

	public BigDecimal getWeight(){
		return this.weight;
	}

	public void setWeight(BigDecimal weight){
		this.weight = weight;
	}

	public BigDecimal getL2Length(){
		return this.l2Length;
	}

	public void setL2Length(BigDecimal l2Length){
		this.l2Length = l2Length;
	}

	public BigDecimal getL2Width(){
		return this.l2Width;
	}

	public void setL2Width(BigDecimal l2Width){
		this.l2Width = l2Width;
	}

	public BigDecimal getL2Height(){
		return this.l2Height;
	}

	public void setL2Height(BigDecimal l2Height){
		this.l2Height = l2Height;
	}

	public BigDecimal getL2Weight(){
		return this.l2Weight;
	}

	public void setL2Weight(BigDecimal l2Weight){
		this.l2Weight = l2Weight;
	}

	public BigDecimal getL2Unit(){
		return this.l2Unit;
	}

	public void setL2Unit(BigDecimal l2Unit){
		this.l2Unit = l2Unit;
	}

	public BigDecimal getPackLength(){
		return this.packLength;
	}

	public void setPackLength(BigDecimal packLength){
		this.packLength = packLength;
	}

	public BigDecimal getPackWidth(){
		return this.packWidth;
	}

	public void setPackWidth(BigDecimal packWidth){
		this.packWidth = packWidth;
	}

	public BigDecimal getPackHeight(){
		return this.packHeight;
	}

	public void setPackHeight(BigDecimal packHeight){
		this.packHeight = packHeight;
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

	public Integer getFloorAvailable(){
		return this.floorAvailable;
	}

	public void setFloorAvailable(Integer floorAvailable){
		this.floorAvailable = floorAvailable;
	}

	public Integer getStorageTemperature(){
		return this.storageTemperature;
	}

	public void setStorageTemperature(Integer storageTemperature){
		this.storageTemperature = storageTemperature;
	}

	public BigDecimal getSafetyQty(){
		return this.safetyQty;
	}

	public void setSafetyQty(BigDecimal safetyQty){
		this.safetyQty = safetyQty;
	}

	public Long getPileX(){
		return this.pileX;
	}

	public void setPileX(Long pileX){
		this.pileX = pileX;
	}

	public Long getPileY(){
		return this.pileY;
	}

	public void setPileY(Long pileY){
		this.pileY = pileY;
	}

	public Long getPileZ(){
		return this.pileZ;
	}

	public void setPileZ(Long pileZ){
		this.pileZ = pileZ;
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

	public Integer getIsInfoIntact() {
		return isInfoIntact;
	}

	public void setIsInfoIntact(Integer isInfoIntact) {
		this.isInfoIntact = isInfoIntact;
	}


	public Integer getIsShelfLifeValid() {
		return isShelfLifeValid;
	}

	public void setIsShelfLifeValid(Integer isShelfLifeValid) {
		this.isShelfLifeValid = isShelfLifeValid;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getWmTopCat() {
		return wmTopCat;
	}

	public void setWmTopCat(String wmTopCat) {
		this.wmTopCat = wmTopCat;
	}

	public String getWmSecondCat() {
		return wmSecondCat;
	}

	public void setWmSecondCat(String wmSecondCat) {
		this.wmSecondCat = wmSecondCat;
	}

	public String getWmThirdCat() {
		return wmThirdCat;
	}

	public void setWmThirdCat(String wmThirdCat) {
		this.wmThirdCat = wmThirdCat;
	}
	public Long getPileNumber() {
		return pileNumber;
	}

	public void setPileNumber(Long pileNumber) {
		this.pileNumber = pileNumber;
	}
	@Override
	public String toString() {
		return "BaseinfoItem{" +
				"id=" + id +
				", itemId=" + itemId +
				", skuId=" + skuId +
				", ownerId=" + ownerId +
				", skuCode='" + skuCode + '\'' +
				", skuName='" + skuName + '\'' +
				", codeType=" + codeType +
				", code='" + code + '\'' +
				", status=" + status +
				", topCat=" + topCat +
				", secondCat=" + secondCat +
				", thirdCat=" + thirdCat +
				", shelfLife=" + shelfLife +
				", length=" + length +
				", width=" + width +
				", height=" + height +
				", weight=" + weight +
				", l2Length=" + l2Length +
				", l2Width=" + l2Width +
				", l2Height=" + l2Height +
				", l2Weight=" + l2Weight +
				", l2Unit=" + l2Unit +
				", packLength=" + packLength +
				", packWidth=" + packWidth +
				", packHeight=" + packHeight +
				", packWeight=" + packWeight +
				", packUnit=" + packUnit +
				", saleUnit=" + saleUnit +
				", itemLevel=" + itemLevel +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", producePlace='" + producePlace + '\'' +
				", batchNeeded=" + batchNeeded +
				", floorAvailable=" + floorAvailable +
				", storageTemperature=" + storageTemperature +
				", safetyQty=" + safetyQty +
				", pileX=" + pileX +
				", pileY=" + pileY +
				", pileZ=" + pileZ +
				", pileNumber=" + pileNumber +
				", unitName='" + unitName + '\'' +
				", l2Name='" + l2Name + '\'' +
				", packName='" + packName + '\'' +
				", isValuable=" + isValuable +
				", itemType=" + itemType +
				", isInfoIntact=" + isInfoIntact +
				", isShelfLifeValid=" + isShelfLifeValid +
				", isValid=" + isValid +
				", packCode='" + packCode + '\'' +
				", wmTopCat='" + wmTopCat + '\'' +
				", wmSecondCat='" + wmSecondCat + '\'' +
				", wmThirdCat='" + wmThirdCat + '\'' +
				'}';
	}


}
