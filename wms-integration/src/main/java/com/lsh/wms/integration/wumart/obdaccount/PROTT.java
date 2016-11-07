
package com.lsh.wms.integration.wumart.obdaccount;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PROTT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PROTT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VBELN" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="POSNR" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="MATNR" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="ARKTX" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="LFIMG" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="VRKME" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="CHARG" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MSGNO" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="MSGTY" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MSGID" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="MSGV1" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MSGV2" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MSGV3" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MSGV4" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PROTT", propOrder = {
    "vbeln",
    "posnr",
    "matnr",
    "arktx",
    "lfimg",
    "vrkme",
    "charg",
    "msgno",
    "msgty",
    "msgid",
    "msgv1",
    "msgv2",
    "msgv3",
    "msgv4"
})
public class PROTT {

    @XmlElement(name = "VBELN", required = true)
    protected String vbeln;
    @XmlElement(name = "POSNR", required = true)
    protected String posnr;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "ARKTX", required = true)
    protected String arktx;
    @XmlElement(name = "LFIMG", required = true)
    protected BigDecimal lfimg;
    @XmlElement(name = "VRKME", required = true)
    protected String vrkme;
    @XmlElement(name = "CHARG", required = true)
    protected String charg;
    @XmlElement(name = "MSGNO", required = true)
    protected String msgno;
    @XmlElement(name = "MSGTY", required = true)
    protected String msgty;
    @XmlElement(name = "MSGID", required = true)
    protected String msgid;
    @XmlElement(name = "MSGV1", required = true)
    protected String msgv1;
    @XmlElement(name = "MSGV2", required = true)
    protected String msgv2;
    @XmlElement(name = "MSGV3", required = true)
    protected String msgv3;
    @XmlElement(name = "MSGV4", required = true)
    protected String msgv4;

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
     * Gets the value of the arktx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getARKTX() {
        return arktx;
    }

    /**
     * Sets the value of the arktx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setARKTX(String value) {
        this.arktx = value;
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

    /**
     * Gets the value of the charg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHARG() {
        return charg;
    }

    /**
     * Sets the value of the charg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHARG(String value) {
        this.charg = value;
    }

    /**
     * Gets the value of the msgno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGNO() {
        return msgno;
    }

    /**
     * Sets the value of the msgno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGNO(String value) {
        this.msgno = value;
    }

    /**
     * Gets the value of the msgty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGTY() {
        return msgty;
    }

    /**
     * Sets the value of the msgty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGTY(String value) {
        this.msgty = value;
    }

    /**
     * Gets the value of the msgid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGID() {
        return msgid;
    }

    /**
     * Sets the value of the msgid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGID(String value) {
        this.msgid = value;
    }

    /**
     * Gets the value of the msgv1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGV1() {
        return msgv1;
    }

    /**
     * Sets the value of the msgv1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGV1(String value) {
        this.msgv1 = value;
    }

    /**
     * Gets the value of the msgv2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGV2() {
        return msgv2;
    }

    /**
     * Sets the value of the msgv2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGV2(String value) {
        this.msgv2 = value;
    }

    /**
     * Gets the value of the msgv3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGV3() {
        return msgv3;
    }

    /**
     * Sets the value of the msgv3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGV3(String value) {
        this.msgv3 = value;
    }

    /**
     * Gets the value of the msgv4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGV4() {
        return msgv4;
    }

    /**
     * Sets the value of the msgv4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGV4(String value) {
        this.msgv4 = value;
    }

}
