
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Bapi2017GmHead01 complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bapi2017GmHead01">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PstngDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="DocDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="RefDocNo" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="BillOfLading" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="GrGiSlipNo" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="PrUname" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="HeaderTxt" type="{urn:sap-com:document:sap:rfc:functions}char25"/>
 *         &lt;element name="VerGrGiSlip" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="VerGrGiSlipx" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="ExtWms" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="RefDocNoLong" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="BillOfLadingLong" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="BarCode" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapi2017GmHead01", propOrder = {
    "pstngDate",
    "docDate",
    "refDocNo",
    "billOfLading",
    "grGiSlipNo",
    "prUname",
    "headerTxt",
    "verGrGiSlip",
    "verGrGiSlipx",
    "extWms",
    "refDocNoLong",
    "billOfLadingLong",
    "barCode"
})
public class Bapi2017GmHead01 {

    @XmlElement(name = "PstngDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar pstngDate;
    @XmlElement(name = "DocDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar docDate;
    @XmlElement(name = "RefDocNo", required = true)
    protected String refDocNo;
    @XmlElement(name = "BillOfLading", required = true)
    protected String billOfLading;
    @XmlElement(name = "GrGiSlipNo", required = true)
    protected String grGiSlipNo;
    @XmlElement(name = "PrUname", required = true)
    protected String prUname;
    @XmlElement(name = "HeaderTxt", required = true)
    protected String headerTxt;
    @XmlElement(name = "VerGrGiSlip", required = true)
    protected String verGrGiSlip;
    @XmlElement(name = "VerGrGiSlipx", required = true)
    protected String verGrGiSlipx;
    @XmlElement(name = "ExtWms", required = true)
    protected String extWms;
    @XmlElement(name = "RefDocNoLong", required = true)
    protected String refDocNoLong;
    @XmlElement(name = "BillOfLadingLong", required = true)
    protected String billOfLadingLong;
    @XmlElement(name = "BarCode", required = true)
    protected String barCode;

    /**
     * 获取pstngDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPstngDate() {
        return pstngDate;
    }

    /**
     * 设置pstngDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPstngDate(XMLGregorianCalendar value) {
        this.pstngDate = value;
    }

    /**
     * 获取docDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDocDate() {
        return docDate;
    }

    /**
     * 设置docDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDocDate(XMLGregorianCalendar value) {
        this.docDate = value;
    }

    /**
     * 获取refDocNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDocNo() {
        return refDocNo;
    }

    /**
     * 设置refDocNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDocNo(String value) {
        this.refDocNo = value;
    }

    /**
     * 获取billOfLading属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillOfLading() {
        return billOfLading;
    }

    /**
     * 设置billOfLading属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillOfLading(String value) {
        this.billOfLading = value;
    }

    /**
     * 获取grGiSlipNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrGiSlipNo() {
        return grGiSlipNo;
    }

    /**
     * 设置grGiSlipNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrGiSlipNo(String value) {
        this.grGiSlipNo = value;
    }

    /**
     * 获取prUname属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrUname() {
        return prUname;
    }

    /**
     * 设置prUname属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrUname(String value) {
        this.prUname = value;
    }

    /**
     * 获取headerTxt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeaderTxt() {
        return headerTxt;
    }

    /**
     * 设置headerTxt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeaderTxt(String value) {
        this.headerTxt = value;
    }

    /**
     * 获取verGrGiSlip属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerGrGiSlip() {
        return verGrGiSlip;
    }

    /**
     * 设置verGrGiSlip属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerGrGiSlip(String value) {
        this.verGrGiSlip = value;
    }

    /**
     * 获取verGrGiSlipx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerGrGiSlipx() {
        return verGrGiSlipx;
    }

    /**
     * 设置verGrGiSlipx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerGrGiSlipx(String value) {
        this.verGrGiSlipx = value;
    }

    /**
     * 获取extWms属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtWms() {
        return extWms;
    }

    /**
     * 设置extWms属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtWms(String value) {
        this.extWms = value;
    }

    /**
     * 获取refDocNoLong属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDocNoLong() {
        return refDocNoLong;
    }

    /**
     * 设置refDocNoLong属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDocNoLong(String value) {
        this.refDocNoLong = value;
    }

    /**
     * 获取billOfLadingLong属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillOfLadingLong() {
        return billOfLadingLong;
    }

    /**
     * 设置billOfLadingLong属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillOfLadingLong(String value) {
        this.billOfLadingLong = value;
    }

    /**
     * 获取barCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * 设置barCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBarCode(String value) {
        this.barCode = value;
    }

}
