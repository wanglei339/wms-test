
package com.lsh.wms.integration.wumart.soobd;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZBAPI_R2_DELIVERYITEM complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZBAPI_R2_DELIVERYITEM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="POSNN" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="LFIMG" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="VBELN" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="POSNR" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="MBLNR" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MITEM_NO" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="MATNR" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="DMBTR" type="{urn:sap-com:document:sap:rfc:functions}curr13.2"/>
 *         &lt;element name="VERPR" type="{urn:sap-com:document:sap:rfc:functions}curr11.2"/>
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
@XmlType(name = "ZBAPI_R2_DELIVERYITEM", propOrder = {
    "posnn",
    "lfimg",
    "vbeln",
    "posnr",
    "mblnr",
    "mitemno",
    "matnr",
    "dmbtr",
    "verpr",
    "temp01",
    "temp02",
    "temp03",
    "temp04",
    "temp05"
})
public class ZBAPIR2DELIVERYITEM {

    @XmlElement(name = "POSNN", required = true)
    protected String posnn;
    @XmlElement(name = "LFIMG", required = true)
    protected BigDecimal lfimg;
    @XmlElement(name = "VBELN", required = true)
    protected String vbeln;
    @XmlElement(name = "POSNR", required = true)
    protected String posnr;
    @XmlElement(name = "MBLNR", required = true)
    protected String mblnr;
    @XmlElement(name = "MITEM_NO", required = true)
    protected String mitemno;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "DMBTR", required = true)
    protected BigDecimal dmbtr;
    @XmlElement(name = "VERPR", required = true)
    protected BigDecimal verpr;
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
     * Gets the value of the posnn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSNN() {
        return posnn;
    }

    /**
     * Sets the value of the posnn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSNN(String value) {
        this.posnn = value;
    }

    /**
     * Gets the value of the lfimg property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLFIMG() {
        return lfimg;
    }

    /**
     * Sets the value of the lfimg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLFIMG(BigDecimal value) {
        this.lfimg = value;
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
     * Gets the value of the mitemno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMITEMNO() {
        return mitemno;
    }

    /**
     * Sets the value of the mitemno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMITEMNO(String value) {
        this.mitemno = value;
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
     * Gets the value of the dmbtr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDMBTR() {
        return dmbtr;
    }

    /**
     * Sets the value of the dmbtr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDMBTR(BigDecimal value) {
        this.dmbtr = value;
    }

    /**
     * Gets the value of the verpr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVERPR() {
        return verpr;
    }

    /**
     * Sets the value of the verpr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVERPR(BigDecimal value) {
        this.verpr = value;
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
