
package com.lsh.wms.integration.wumart.obdaccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZDELIVERYEXPORT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZDELIVERYEXPORT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VBELN" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="POSNR" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="WBSTK" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="RETURN" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MATNR" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="WERKS" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="VRKME" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZDELIVERYEXPORT", propOrder = {
    "vbeln",
    "posnr",
    "wbstk",
    "_return",
    "matnr",
    "werks",
    "vrkme"
})
public class ZDELIVERYEXPORT {

    @XmlElement(name = "VBELN", required = true)
    protected String vbeln;
    @XmlElement(name = "POSNR", required = true)
    protected String posnr;
    @XmlElement(name = "WBSTK", required = true)
    protected String wbstk;
    @XmlElement(name = "RETURN", required = true)
    protected String _return;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "WERKS", required = true)
    protected String werks;
    @XmlElement(name = "VRKME", required = true)
    protected String vrkme;

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
     * Gets the value of the posnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSNR() {
        return posnr;
    }

    /**
     * Sets the value of the posnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSNR(String value) {
        this.posnr = value;
    }

    /**
     * Gets the value of the wbstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWBSTK() {
        return wbstk;
    }

    /**
     * Sets the value of the wbstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWBSTK(String value) {
        this.wbstk = value;
    }

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETURN() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETURN(String value) {
        this._return = value;
    }

    /**
     * Gets the value of the matnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATNR() {
        return matnr;
    }

    /**
     * Sets the value of the matnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATNR(String value) {
        this.matnr = value;
    }

    /**
     * Gets the value of the werks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWERKS() {
        return werks;
    }

    /**
     * Sets the value of the werks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWERKS(String value) {
        this.werks = value;
    }

    /**
     * Gets the value of the vrkme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVRKME() {
        return vrkme;
    }

    /**
     * Sets the value of the vrkme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVRKME(String value) {
        this.vrkme = value;
    }

}
