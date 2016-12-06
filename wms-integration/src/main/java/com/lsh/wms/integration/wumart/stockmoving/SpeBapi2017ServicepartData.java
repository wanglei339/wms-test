
package com.lsh.wms.integration.wumart.stockmoving;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>_-spe_-bapi2017ServicepartData complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="_-spe_-bapi2017ServicepartData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LineId" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="RetAuthNumber" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="DelivNumber" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DelivItem" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="HuNumber" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="InspoutGuid" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *         &lt;element name="Event" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Date" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="Time" type="{urn:sap-com:document:sap:rfc:functions}time"/>
 *         &lt;element name="Zonlo" type="{urn:sap-com:document:sap:rfc:functions}char6"/>
 *         &lt;element name="Timestamp" type="{urn:sap-com:document:sap:rfc:functions}decimal15.0"/>
 *         &lt;element name="ScrapIndicator" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="KeepQuantity" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="GtsStockType" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MoveGtsStockType" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="KeepQuantityConversion" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="ZeroQuantity" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Numerator" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="Denominatr" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_-spe_-bapi2017ServicepartData", propOrder = {
    "lineId",
    "retAuthNumber",
    "delivNumber",
    "delivItem",
    "huNumber",
    "inspoutGuid",
    "event",
    "date",
    "time",
    "zonlo",
    "timestamp",
    "scrapIndicator",
    "keepQuantity",
    "gtsStockType",
    "moveGtsStockType",
    "keepQuantityConversion",
    "zeroQuantity",
    "numerator",
    "denominatr"
})
public class SpeBapi2017ServicepartData {

    @XmlElement(name = "LineId", required = true)
    protected String lineId;
    @XmlElement(name = "RetAuthNumber", required = true)
    protected String retAuthNumber;
    @XmlElement(name = "DelivNumber", required = true)
    protected String delivNumber;
    @XmlElement(name = "DelivItem", required = true)
    protected String delivItem;
    @XmlElement(name = "HuNumber", required = true)
    protected String huNumber;
    @XmlElement(name = "InspoutGuid", required = true)
    protected String inspoutGuid;
    @XmlElement(name = "Event", required = true)
    protected String event;
    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "Time", required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar time;
    @XmlElement(name = "Zonlo", required = true)
    protected String zonlo;
    @XmlElement(name = "Timestamp", required = true)
    protected BigDecimal timestamp;
    @XmlElement(name = "ScrapIndicator", required = true)
    protected String scrapIndicator;
    @XmlElement(name = "KeepQuantity", required = true)
    protected BigDecimal keepQuantity;
    @XmlElement(name = "GtsStockType", required = true)
    protected String gtsStockType;
    @XmlElement(name = "MoveGtsStockType", required = true)
    protected String moveGtsStockType;
    @XmlElement(name = "KeepQuantityConversion", required = true)
    protected String keepQuantityConversion;
    @XmlElement(name = "ZeroQuantity", required = true)
    protected String zeroQuantity;
    @XmlElement(name = "Numerator", required = true)
    protected BigDecimal numerator;
    @XmlElement(name = "Denominatr", required = true)
    protected BigDecimal denominatr;

    /**
     * 获取lineId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineId() {
        return lineId;
    }

    /**
     * 设置lineId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineId(String value) {
        this.lineId = value;
    }

    /**
     * 获取retAuthNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetAuthNumber() {
        return retAuthNumber;
    }

    /**
     * 设置retAuthNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetAuthNumber(String value) {
        this.retAuthNumber = value;
    }

    /**
     * 获取delivNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivNumber() {
        return delivNumber;
    }

    /**
     * 设置delivNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivNumber(String value) {
        this.delivNumber = value;
    }

    /**
     * 获取delivItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivItem() {
        return delivItem;
    }

    /**
     * 设置delivItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivItem(String value) {
        this.delivItem = value;
    }

    /**
     * 获取huNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHuNumber() {
        return huNumber;
    }

    /**
     * 设置huNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHuNumber(String value) {
        this.huNumber = value;
    }

    /**
     * 获取inspoutGuid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInspoutGuid() {
        return inspoutGuid;
    }

    /**
     * 设置inspoutGuid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInspoutGuid(String value) {
        this.inspoutGuid = value;
    }

    /**
     * 获取event属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvent() {
        return event;
    }

    /**
     * 设置event属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvent(String value) {
        this.event = value;
    }

    /**
     * 获取date属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * 设置date属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * 获取time属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * 设置time属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
    }

    /**
     * 获取zonlo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZonlo() {
        return zonlo;
    }

    /**
     * 设置zonlo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZonlo(String value) {
        this.zonlo = value;
    }

    /**
     * 获取timestamp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTimestamp() {
        return timestamp;
    }

    /**
     * 设置timestamp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTimestamp(BigDecimal value) {
        this.timestamp = value;
    }

    /**
     * 获取scrapIndicator属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScrapIndicator() {
        return scrapIndicator;
    }

    /**
     * 设置scrapIndicator属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScrapIndicator(String value) {
        this.scrapIndicator = value;
    }

    /**
     * 获取keepQuantity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getKeepQuantity() {
        return keepQuantity;
    }

    /**
     * 设置keepQuantity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setKeepQuantity(BigDecimal value) {
        this.keepQuantity = value;
    }

    /**
     * 获取gtsStockType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGtsStockType() {
        return gtsStockType;
    }

    /**
     * 设置gtsStockType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGtsStockType(String value) {
        this.gtsStockType = value;
    }

    /**
     * 获取moveGtsStockType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveGtsStockType() {
        return moveGtsStockType;
    }

    /**
     * 设置moveGtsStockType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveGtsStockType(String value) {
        this.moveGtsStockType = value;
    }

    /**
     * 获取keepQuantityConversion属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeepQuantityConversion() {
        return keepQuantityConversion;
    }

    /**
     * 设置keepQuantityConversion属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeepQuantityConversion(String value) {
        this.keepQuantityConversion = value;
    }

    /**
     * 获取zeroQuantity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZeroQuantity() {
        return zeroQuantity;
    }

    /**
     * 设置zeroQuantity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZeroQuantity(String value) {
        this.zeroQuantity = value;
    }

    /**
     * 获取numerator属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumerator() {
        return numerator;
    }

    /**
     * 设置numerator属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumerator(BigDecimal value) {
        this.numerator = value;
    }

    /**
     * 获取denominatr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDenominatr() {
        return denominatr;
    }

    /**
     * 设置denominatr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDenominatr(BigDecimal value) {
        this.denominatr = value;
    }

}
