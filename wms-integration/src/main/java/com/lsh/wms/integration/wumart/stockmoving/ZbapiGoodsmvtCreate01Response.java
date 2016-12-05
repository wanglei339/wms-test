
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Extensionin" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiparex"/>
 *         &lt;element name="GoodsmvtHeadret" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapi2017GmHeadRet"/>
 *         &lt;element name="GoodsmvtItem" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapi2017GmItemCreate"/>
 *         &lt;element name="GoodsmvtItemCwm" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOf_-cwm_-bapi2017GmItemCreate"/>
 *         &lt;element name="GoodsmvtSerialnumber" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapi2017GmSerialnumber"/>
 *         &lt;element name="GoodsmvtServPartData" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOf_-spe_-bapi2017ServicepartData"/>
 *         &lt;element name="Matdocumentyear" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="Materialdocument" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Return" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiret2"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "extensionin",
    "goodsmvtHeadret",
    "goodsmvtItem",
    "goodsmvtItemCwm",
    "goodsmvtSerialnumber",
    "goodsmvtServPartData",
    "matdocumentyear",
    "materialdocument",
    "_return"
})
@XmlRootElement(name = "ZbapiGoodsmvtCreate01Response")
public class ZbapiGoodsmvtCreate01Response {

    @XmlElement(name = "Extensionin", required = true)
    protected TableOfBapiparex extensionin;
    @XmlElement(name = "GoodsmvtHeadret", required = true)
    protected Bapi2017GmHeadRet goodsmvtHeadret;
    @XmlElement(name = "GoodsmvtItem", required = true)
    protected TableOfBapi2017GmItemCreate goodsmvtItem;
    @XmlElement(name = "GoodsmvtItemCwm", required = true)
    protected TableOfCwmBapi2017GmItemCreate goodsmvtItemCwm;
    @XmlElement(name = "GoodsmvtSerialnumber", required = true)
    protected TableOfBapi2017GmSerialnumber goodsmvtSerialnumber;
    @XmlElement(name = "GoodsmvtServPartData", required = true)
    protected TableOfSpeBapi2017ServicepartData goodsmvtServPartData;
    @XmlElement(name = "Matdocumentyear", required = true)
    protected String matdocumentyear;
    @XmlElement(name = "Materialdocument", required = true)
    protected String materialdocument;
    @XmlElement(name = "Return", required = true)
    protected TableOfBapiret2 _return;

    /**
     * 获取extensionin属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapiparex }
     *     
     */
    public TableOfBapiparex getExtensionin() {
        return extensionin;
    }

    /**
     * 设置extensionin属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapiparex }
     *     
     */
    public void setExtensionin(TableOfBapiparex value) {
        this.extensionin = value;
    }

    /**
     * 获取goodsmvtHeadret属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Bapi2017GmHeadRet }
     *     
     */
    public Bapi2017GmHeadRet getGoodsmvtHeadret() {
        return goodsmvtHeadret;
    }

    /**
     * 设置goodsmvtHeadret属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Bapi2017GmHeadRet }
     *     
     */
    public void setGoodsmvtHeadret(Bapi2017GmHeadRet value) {
        this.goodsmvtHeadret = value;
    }

    /**
     * 获取goodsmvtItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapi2017GmItemCreate }
     *     
     */
    public TableOfBapi2017GmItemCreate getGoodsmvtItem() {
        return goodsmvtItem;
    }

    /**
     * 设置goodsmvtItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapi2017GmItemCreate }
     *     
     */
    public void setGoodsmvtItem(TableOfBapi2017GmItemCreate value) {
        this.goodsmvtItem = value;
    }

    /**
     * 获取goodsmvtItemCwm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfCwmBapi2017GmItemCreate }
     *     
     */
    public TableOfCwmBapi2017GmItemCreate getGoodsmvtItemCwm() {
        return goodsmvtItemCwm;
    }

    /**
     * 设置goodsmvtItemCwm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfCwmBapi2017GmItemCreate }
     *     
     */
    public void setGoodsmvtItemCwm(TableOfCwmBapi2017GmItemCreate value) {
        this.goodsmvtItemCwm = value;
    }

    /**
     * 获取goodsmvtSerialnumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapi2017GmSerialnumber }
     *     
     */
    public TableOfBapi2017GmSerialnumber getGoodsmvtSerialnumber() {
        return goodsmvtSerialnumber;
    }

    /**
     * 设置goodsmvtSerialnumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapi2017GmSerialnumber }
     *     
     */
    public void setGoodsmvtSerialnumber(TableOfBapi2017GmSerialnumber value) {
        this.goodsmvtSerialnumber = value;
    }

    /**
     * 获取goodsmvtServPartData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfSpeBapi2017ServicepartData }
     *     
     */
    public TableOfSpeBapi2017ServicepartData getGoodsmvtServPartData() {
        return goodsmvtServPartData;
    }

    /**
     * 设置goodsmvtServPartData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfSpeBapi2017ServicepartData }
     *     
     */
    public void setGoodsmvtServPartData(TableOfSpeBapi2017ServicepartData value) {
        this.goodsmvtServPartData = value;
    }

    /**
     * 获取matdocumentyear属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatdocumentyear() {
        return matdocumentyear;
    }

    /**
     * 设置matdocumentyear属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatdocumentyear(String value) {
        this.matdocumentyear = value;
    }

    /**
     * 获取materialdocument属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialdocument() {
        return materialdocument;
    }

    /**
     * 设置materialdocument属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialdocument(String value) {
        this.materialdocument = value;
    }

    /**
     * 获取return属性的值。
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapiret2 }
     *     
     */
    public TableOfBapiret2 getReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapiret2 }
     *     
     */
    public void setReturn(TableOfBapiret2 value) {
        this._return = value;
    }

}
