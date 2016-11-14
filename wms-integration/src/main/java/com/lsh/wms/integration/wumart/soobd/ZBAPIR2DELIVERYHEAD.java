
package com.lsh.wms.integration.wumart.soobd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZBAPI_R2_DELIVERYHEAD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZBAPI_R2_DELIVERYHEAD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ORDER_STYLE" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="ORDER_NO" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="ORDER_TYPE" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="VSTEL" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="VBELN" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MBLNR" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="TEMP01" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="TEMP02" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="TEMP03" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="TEMP04" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="TEMP05" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZBAPI_R2_DELIVERYHEAD", propOrder = {
    "orderstyle",
    "orderno",
    "ordertype",
    "vstel",
    "vbeln",
    "mblnr",
    "temp01",
    "temp02",
    "temp03",
    "temp04",
    "temp05"
})
public class ZBAPIR2DELIVERYHEAD {

    @XmlElement(name = "ORDER_STYLE", required = true)
    protected String orderstyle;
    @XmlElement(name = "ORDER_NO", required = true)
    protected String orderno;
    @XmlElement(name = "ORDER_TYPE", required = true)
    protected String ordertype;
    @XmlElement(name = "VSTEL", required = true)
    protected String vstel;
    @XmlElement(name = "VBELN", required = true)
    protected String vbeln;
    @XmlElement(name = "MBLNR", required = true)
    protected String mblnr;
    @XmlElement(name = "TEMP01", required = true)
    protected String temp01;
    @XmlElement(name = "TEMP02", required = true)
    protected String temp02;
    @XmlElement(name = "TEMP03", required = true)
    protected String temp03;
    @XmlElement(name = "TEMP04", required = true)
    protected String temp04;
    @XmlElement(name = "TEMP05", required = true)
    protected String temp05;

    /**
     * Gets the value of the orderstyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERSTYLE() {
        return orderstyle;
    }

    /**
     * Sets the value of the orderstyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERSTYLE(String value) {
        this.orderstyle = value;
    }

    /**
     * Gets the value of the orderno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERNO() {
        return orderno;
    }

    /**
     * Sets the value of the orderno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERNO(String value) {
        this.orderno = value;
    }

    /**
     * Gets the value of the ordertype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERTYPE() {
        return ordertype;
    }

    /**
     * Sets the value of the ordertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERTYPE(String value) {
        this.ordertype = value;
    }

    /**
     * Gets the value of the vstel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVSTEL() {
        return vstel;
    }

    /**
     * Sets the value of the vstel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVSTEL(String value) {
        this.vstel = value;
    }

    /**
     * Gets the value of the vbeln property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBELN() {
        return vbeln;
    }

    /**
     * Sets the value of the vbeln property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBELN(String value) {
        this.vbeln = value;
    }

    /**
     * Gets the value of the mblnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMBLNR() {
        return mblnr;
    }

    /**
     * Sets the value of the mblnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMBLNR(String value) {
        this.mblnr = value;
    }

    /**
     * Gets the value of the temp01 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEMP01() {
        return temp01;
    }

    /**
     * Sets the value of the temp01 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEMP01(String value) {
        this.temp01 = value;
    }

    /**
     * Gets the value of the temp02 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEMP02() {
        return temp02;
    }

    /**
     * Sets the value of the temp02 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEMP02(String value) {
        this.temp02 = value;
    }

    /**
     * Gets the value of the temp03 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEMP03() {
        return temp03;
    }

    /**
     * Sets the value of the temp03 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEMP03(String value) {
        this.temp03 = value;
    }

    /**
     * Gets the value of the temp04 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEMP04() {
        return temp04;
    }

    /**
     * Sets the value of the temp04 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEMP04(String value) {
        this.temp04 = value;
    }

    /**
     * Gets the value of the temp05 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEMP05() {
        return temp05;
    }

    /**
     * Sets the value of the temp05 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEMP05(String value) {
        this.temp05 = value;
    }

}
