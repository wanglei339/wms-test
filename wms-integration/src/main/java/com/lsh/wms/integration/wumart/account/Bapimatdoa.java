
package com.lsh.wms.integration.wumart.account;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Bapimatdoa complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapimatdoa">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MatlDesc" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="OldMatNo" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="MatlType" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="IndSector" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Division" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="MatlGroup" type="{urn:sap-com:document:sap:rfc:functions}char9"/>
 *         &lt;element name="ProdHier" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="BasicMatl" type="{urn:sap-com:document:sap:rfc:functions}char14"/>
 *         &lt;element name="StdDescr" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="LabDesign" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="ProdMemo" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="Pageformat" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Container" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="StorConds" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="TempConds" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="BaseUom" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="EanUpc" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="EanCat" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="SizeDim" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *         &lt;element name="GrossWt" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="NetWeight" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="UnitOfWt" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="Volume" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="Volumeunit" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="Length" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="Width" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="Height" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="UnitDim" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="ManuMat" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="MfrNo" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="BaseUomIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="UnitOfWtIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="VolumeunitIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="UnitDimIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="CreatedOn" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="CreatedBy" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="LastChnge" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="ChangedBy" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="MatlCat" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="Emptiesbom" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="BasicMatlNew" type="{urn:sap-com:document:sap:rfc:functions}char48"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapimatdoa", propOrder = {
    "matlDesc",
    "oldMatNo",
    "matlType",
    "indSector",
    "division",
    "matlGroup",
    "prodHier",
    "basicMatl",
    "stdDescr",
    "labDesign",
    "prodMemo",
    "pageformat",
    "container",
    "storConds",
    "tempConds",
    "baseUom",
    "eanUpc",
    "eanCat",
    "sizeDim",
    "grossWt",
    "netWeight",
    "unitOfWt",
    "volume",
    "volumeunit",
    "length",
    "width",
    "height",
    "unitDim",
    "manuMat",
    "mfrNo",
    "baseUomIso",
    "unitOfWtIso",
    "volumeunitIso",
    "unitDimIso",
    "createdOn",
    "createdBy",
    "lastChnge",
    "changedBy",
    "matlCat",
    "emptiesbom",
    "basicMatlNew"
})
public class Bapimatdoa {

    @XmlElement(name = "MatlDesc", required = true)
    protected String matlDesc;
    @XmlElement(name = "OldMatNo", required = true)
    protected String oldMatNo;
    @XmlElement(name = "MatlType", required = true)
    protected String matlType;
    @XmlElement(name = "IndSector", required = true)
    protected String indSector;
    @XmlElement(name = "Division", required = true)
    protected String division;
    @XmlElement(name = "MatlGroup", required = true)
    protected String matlGroup;
    @XmlElement(name = "ProdHier", required = true)
    protected String prodHier;
    @XmlElement(name = "BasicMatl", required = true)
    protected String basicMatl;
    @XmlElement(name = "StdDescr", required = true)
    protected String stdDescr;
    @XmlElement(name = "LabDesign", required = true)
    protected String labDesign;
    @XmlElement(name = "ProdMemo", required = true)
    protected String prodMemo;
    @XmlElement(name = "Pageformat", required = true)
    protected String pageformat;
    @XmlElement(name = "Container", required = true)
    protected String container;
    @XmlElement(name = "StorConds", required = true)
    protected String storConds;
    @XmlElement(name = "TempConds", required = true)
    protected String tempConds;
    @XmlElement(name = "BaseUom", required = true)
    protected String baseUom;
    @XmlElement(name = "EanUpc", required = true)
    protected String eanUpc;
    @XmlElement(name = "EanCat", required = true)
    protected String eanCat;
    @XmlElement(name = "SizeDim", required = true)
    protected String sizeDim;
    @XmlElement(name = "GrossWt", required = true)
    protected BigDecimal grossWt;
    @XmlElement(name = "NetWeight", required = true)
    protected BigDecimal netWeight;
    @XmlElement(name = "UnitOfWt", required = true)
    protected String unitOfWt;
    @XmlElement(name = "Volume", required = true)
    protected BigDecimal volume;
    @XmlElement(name = "Volumeunit", required = true)
    protected String volumeunit;
    @XmlElement(name = "Length", required = true)
    protected BigDecimal length;
    @XmlElement(name = "Width", required = true)
    protected BigDecimal width;
    @XmlElement(name = "Height", required = true)
    protected BigDecimal height;
    @XmlElement(name = "UnitDim", required = true)
    protected String unitDim;
    @XmlElement(name = "ManuMat", required = true)
    protected String manuMat;
    @XmlElement(name = "MfrNo", required = true)
    protected String mfrNo;
    @XmlElement(name = "BaseUomIso", required = true)
    protected String baseUomIso;
    @XmlElement(name = "UnitOfWtIso", required = true)
    protected String unitOfWtIso;
    @XmlElement(name = "VolumeunitIso", required = true)
    protected String volumeunitIso;
    @XmlElement(name = "UnitDimIso", required = true)
    protected String unitDimIso;
    @XmlElement(name = "CreatedOn", required = true)
    protected XMLGregorianCalendar createdOn;
    @XmlElement(name = "CreatedBy", required = true)
    protected String createdBy;
    @XmlElement(name = "LastChnge", required = true)
    protected XMLGregorianCalendar lastChnge;
    @XmlElement(name = "ChangedBy", required = true)
    protected String changedBy;
    @XmlElement(name = "MatlCat", required = true)
    protected String matlCat;
    @XmlElement(name = "Emptiesbom", required = true)
    protected String emptiesbom;
    @XmlElement(name = "BasicMatlNew", required = true)
    protected String basicMatlNew;

    /**
     * Gets the value of the matlDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlDesc() {
        return matlDesc;
    }

    /**
     * Sets the value of the matlDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlDesc(String value) {
        this.matlDesc = value;
    }

    /**
     * Gets the value of the oldMatNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldMatNo() {
        return oldMatNo;
    }

    /**
     * Sets the value of the oldMatNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldMatNo(String value) {
        this.oldMatNo = value;
    }

    /**
     * Gets the value of the matlType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlType() {
        return matlType;
    }

    /**
     * Sets the value of the matlType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlType(String value) {
        this.matlType = value;
    }

    /**
     * Gets the value of the indSector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndSector() {
        return indSector;
    }

    /**
     * Sets the value of the indSector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndSector(String value) {
        this.indSector = value;
    }

    /**
     * Gets the value of the division property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the value of the division property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDivision(String value) {
        this.division = value;
    }

    /**
     * Gets the value of the matlGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlGroup() {
        return matlGroup;
    }

    /**
     * Sets the value of the matlGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlGroup(String value) {
        this.matlGroup = value;
    }

    /**
     * Gets the value of the prodHier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProdHier() {
        return prodHier;
    }

    /**
     * Sets the value of the prodHier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProdHier(String value) {
        this.prodHier = value;
    }

    /**
     * Gets the value of the basicMatl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasicMatl() {
        return basicMatl;
    }

    /**
     * Sets the value of the basicMatl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasicMatl(String value) {
        this.basicMatl = value;
    }

    /**
     * Gets the value of the stdDescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStdDescr() {
        return stdDescr;
    }

    /**
     * Sets the value of the stdDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStdDescr(String value) {
        this.stdDescr = value;
    }

    /**
     * Gets the value of the labDesign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabDesign() {
        return labDesign;
    }

    /**
     * Sets the value of the labDesign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabDesign(String value) {
        this.labDesign = value;
    }

    /**
     * Gets the value of the prodMemo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProdMemo() {
        return prodMemo;
    }

    /**
     * Sets the value of the prodMemo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProdMemo(String value) {
        this.prodMemo = value;
    }

    /**
     * Gets the value of the pageformat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPageformat() {
        return pageformat;
    }

    /**
     * Sets the value of the pageformat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPageformat(String value) {
        this.pageformat = value;
    }

    /**
     * Gets the value of the container property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContainer() {
        return container;
    }

    /**
     * Sets the value of the container property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContainer(String value) {
        this.container = value;
    }

    /**
     * Gets the value of the storConds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStorConds() {
        return storConds;
    }

    /**
     * Sets the value of the storConds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStorConds(String value) {
        this.storConds = value;
    }

    /**
     * Gets the value of the tempConds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTempConds() {
        return tempConds;
    }

    /**
     * Sets the value of the tempConds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTempConds(String value) {
        this.tempConds = value;
    }

    /**
     * Gets the value of the baseUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseUom() {
        return baseUom;
    }

    /**
     * Sets the value of the baseUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseUom(String value) {
        this.baseUom = value;
    }

    /**
     * Gets the value of the eanUpc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEanUpc() {
        return eanUpc;
    }

    /**
     * Sets the value of the eanUpc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEanUpc(String value) {
        this.eanUpc = value;
    }

    /**
     * Gets the value of the eanCat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEanCat() {
        return eanCat;
    }

    /**
     * Sets the value of the eanCat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEanCat(String value) {
        this.eanCat = value;
    }

    /**
     * Gets the value of the sizeDim property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSizeDim() {
        return sizeDim;
    }

    /**
     * Sets the value of the sizeDim property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSizeDim(String value) {
        this.sizeDim = value;
    }

    /**
     * Gets the value of the grossWt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGrossWt() {
        return grossWt;
    }

    /**
     * Sets the value of the grossWt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGrossWt(BigDecimal value) {
        this.grossWt = value;
    }

    /**
     * Gets the value of the netWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetWeight() {
        return netWeight;
    }

    /**
     * Sets the value of the netWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetWeight(BigDecimal value) {
        this.netWeight = value;
    }

    /**
     * Gets the value of the unitOfWt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfWt() {
        return unitOfWt;
    }

    /**
     * Sets the value of the unitOfWt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfWt(String value) {
        this.unitOfWt = value;
    }

    /**
     * Gets the value of the volume property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * Sets the value of the volume property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVolume(BigDecimal value) {
        this.volume = value;
    }

    /**
     * Gets the value of the volumeunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeunit() {
        return volumeunit;
    }

    /**
     * Sets the value of the volumeunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeunit(String value) {
        this.volumeunit = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLength(BigDecimal value) {
        this.length = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWidth(BigDecimal value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHeight(BigDecimal value) {
        this.height = value;
    }

    /**
     * Gets the value of the unitDim property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitDim() {
        return unitDim;
    }

    /**
     * Sets the value of the unitDim property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitDim(String value) {
        this.unitDim = value;
    }

    /**
     * Gets the value of the manuMat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManuMat() {
        return manuMat;
    }

    /**
     * Sets the value of the manuMat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManuMat(String value) {
        this.manuMat = value;
    }

    /**
     * Gets the value of the mfrNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMfrNo() {
        return mfrNo;
    }

    /**
     * Sets the value of the mfrNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMfrNo(String value) {
        this.mfrNo = value;
    }

    /**
     * Gets the value of the baseUomIso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseUomIso() {
        return baseUomIso;
    }

    /**
     * Sets the value of the baseUomIso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseUomIso(String value) {
        this.baseUomIso = value;
    }

    /**
     * Gets the value of the unitOfWtIso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfWtIso() {
        return unitOfWtIso;
    }

    /**
     * Sets the value of the unitOfWtIso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfWtIso(String value) {
        this.unitOfWtIso = value;
    }

    /**
     * Gets the value of the volumeunitIso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeunitIso() {
        return volumeunitIso;
    }

    /**
     * Sets the value of the volumeunitIso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeunitIso(String value) {
        this.volumeunitIso = value;
    }

    /**
     * Gets the value of the unitDimIso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitDimIso() {
        return unitDimIso;
    }

    /**
     * Sets the value of the unitDimIso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitDimIso(String value) {
        this.unitDimIso = value;
    }

    /**
     * Gets the value of the createdOn property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the value of the createdOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedOn(XMLGregorianCalendar value) {
        this.createdOn = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the lastChnge property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastChnge() {
        return lastChnge;
    }

    /**
     * Sets the value of the lastChnge property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastChnge(XMLGregorianCalendar value) {
        this.lastChnge = value;
    }

    /**
     * Gets the value of the changedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the value of the changedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChangedBy(String value) {
        this.changedBy = value;
    }

    /**
     * Gets the value of the matlCat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlCat() {
        return matlCat;
    }

    /**
     * Sets the value of the matlCat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlCat(String value) {
        this.matlCat = value;
    }

    /**
     * Gets the value of the emptiesbom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmptiesbom() {
        return emptiesbom;
    }

    /**
     * Sets the value of the emptiesbom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmptiesbom(String value) {
        this.emptiesbom = value;
    }

    /**
     * Gets the value of the basicMatlNew property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasicMatlNew() {
        return basicMatlNew;
    }

    /**
     * Sets the value of the basicMatlNew property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasicMatlNew(String value) {
        this.basicMatlNew = value;
    }

}
