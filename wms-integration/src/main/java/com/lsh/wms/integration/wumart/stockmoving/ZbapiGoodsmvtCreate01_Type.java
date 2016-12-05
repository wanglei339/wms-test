
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
 *         &lt;element name="Extensionin" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiparex" minOccurs="0"/>
 *         &lt;element name="GoodsmvtCode" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapi2017GmCode" minOccurs="0"/>
 *         &lt;element name="GoodsmvtHeader" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapi2017GmHead01" minOccurs="0"/>
 *         &lt;element name="GoodsmvtItem" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapi2017GmItemCreate" minOccurs="0"/>
 *         &lt;element name="GoodsmvtItemCwm" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOf_-cwm_-bapi2017GmItemCreate" minOccurs="0"/>
 *         &lt;element name="GoodsmvtRefEwm" type="{urn:sap-com:document:sap:soap:functions:mc-style}_-spe_-bapi2017GmRefEwm" minOccurs="0"/>
 *         &lt;element name="GoodsmvtSerialnumber" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapi2017GmSerialnumber" minOccurs="0"/>
 *         &lt;element name="GoodsmvtServPartData" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOf_-spe_-bapi2017ServicepartData" minOccurs="0"/>
 *         &lt;element name="Return" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiret2" minOccurs="0"/>
 *         &lt;element name="Testrun" type="{urn:sap-com:document:sap:rfc:functions}char1" minOccurs="0"/>
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
    "goodsmvtCode",
    "goodsmvtHeader",
    "goodsmvtItem",
    "goodsmvtItemCwm",
    "goodsmvtRefEwm",
    "goodsmvtSerialnumber",
    "goodsmvtServPartData",
    "_return",
    "testrun"
})
@XmlRootElement(name = "ZbapiGoodsmvtCreate01")
public class ZbapiGoodsmvtCreate01_Type {

    @XmlElement(name = "Extensionin")
    protected TableOfBapiparex extensionin;
    @XmlElement(name = "GoodsmvtCode")
    protected Bapi2017GmCode goodsmvtCode;
    @XmlElement(name = "GoodsmvtHeader")
    protected Bapi2017GmHead01 goodsmvtHeader;
    @XmlElement(name = "GoodsmvtItem")
    protected TableOfBapi2017GmItemCreate goodsmvtItem;
    @XmlElement(name = "GoodsmvtItemCwm")
    protected TableOfCwmBapi2017GmItemCreate goodsmvtItemCwm;
    @XmlElement(name = "GoodsmvtRefEwm")
    protected SpeBapi2017GmRefEwm goodsmvtRefEwm;
    @XmlElement(name = "GoodsmvtSerialnumber")
    protected TableOfBapi2017GmSerialnumber goodsmvtSerialnumber;
    @XmlElement(name = "GoodsmvtServPartData")
    protected TableOfSpeBapi2017ServicepartData goodsmvtServPartData;
    @XmlElement(name = "Return")
    protected TableOfBapiret2 _return;
    @XmlElement(name = "Testrun")
    protected String testrun;

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
     * 获取goodsmvtCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Bapi2017GmCode }
     *     
     */
    public Bapi2017GmCode getGoodsmvtCode() {
        return goodsmvtCode;
    }

    /**
     * 设置goodsmvtCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Bapi2017GmCode }
     *     
     */
    public void setGoodsmvtCode(Bapi2017GmCode value) {
        this.goodsmvtCode = value;
    }

    /**
     * 获取goodsmvtHeader属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Bapi2017GmHead01 }
     *     
     */
    public Bapi2017GmHead01 getGoodsmvtHeader() {
        return goodsmvtHeader;
    }

    /**
     * 设置goodsmvtHeader属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Bapi2017GmHead01 }
     *     
     */
    public void setGoodsmvtHeader(Bapi2017GmHead01 value) {
        this.goodsmvtHeader = value;
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
     * 获取goodsmvtRefEwm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SpeBapi2017GmRefEwm }
     *     
     */
    public SpeBapi2017GmRefEwm getGoodsmvtRefEwm() {
        return goodsmvtRefEwm;
    }

    /**
     * 设置goodsmvtRefEwm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SpeBapi2017GmRefEwm }
     *     
     */
    public void setGoodsmvtRefEwm(SpeBapi2017GmRefEwm value) {
        this.goodsmvtRefEwm = value;
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

    /**
     * 获取testrun属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestrun() {
        return testrun;
    }

    /**
     * 设置testrun属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestrun(String value) {
        this.testrun = value;
    }

}
