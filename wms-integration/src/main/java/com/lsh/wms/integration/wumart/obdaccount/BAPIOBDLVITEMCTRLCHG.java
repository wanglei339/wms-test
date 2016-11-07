
package com.lsh.wms.integration.wumart.obdaccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BAPIOBDLVITEMCTRLCHG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BAPIOBDLVITEMCTRLCHG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DELIV_NUMB" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DELIV_ITEM" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="CHG_DELQTY" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="DEL_ITEM" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="VOLUME_FLG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="NET_WT_FLG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="GROSS_WT_FLG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BAPIOBDLVITEMCTRLCHG", propOrder = {
    "delivnumb",
    "delivitem",
    "chgdelqty",
    "delitem",
    "volumeflg",
    "netwtflg",
    "grosswtflg"
})
public class BAPIOBDLVITEMCTRLCHG {

    @XmlElement(name = "DELIV_NUMB", required = true)
    protected String delivnumb;
    @XmlElement(name = "DELIV_ITEM", required = true)
    protected String delivitem;
    @XmlElement(name = "CHG_DELQTY", required = true)
    protected String chgdelqty;
    @XmlElement(name = "DEL_ITEM", required = true)
    protected String delitem;
    @XmlElement(name = "VOLUME_FLG", required = true)
    protected String volumeflg;
    @XmlElement(name = "NET_WT_FLG", required = true)
    protected String netwtflg;
    @XmlElement(name = "GROSS_WT_FLG", required = true)
    protected String grosswtflg;

    /**
     * Gets the value of the delivnumb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVNUMB() {
        return delivnumb;
    }

    /**
     * Sets the value of the delivnumb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVNUMB(String value) {
        this.delivnumb = value;
    }

    /**
     * Gets the value of the delivitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVITEM() {
        return delivitem;
    }

    /**
     * Sets the value of the delivitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVITEM(String value) {
        this.delivitem = value;
    }

    /**
     * Gets the value of the chgdelqty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHGDELQTY() {
        return chgdelqty;
    }

    /**
     * Sets the value of the chgdelqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHGDELQTY(String value) {
        this.chgdelqty = value;
    }

    /**
     * Gets the value of the delitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELITEM() {
        return delitem;
    }

    /**
     * Sets the value of the delitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELITEM(String value) {
        this.delitem = value;
    }

    /**
     * Gets the value of the volumeflg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOLUMEFLG() {
        return volumeflg;
    }

    /**
     * Sets the value of the volumeflg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOLUMEFLG(String value) {
        this.volumeflg = value;
    }

    /**
     * Gets the value of the netwtflg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNETWTFLG() {
        return netwtflg;
    }

    /**
     * Sets the value of the netwtflg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNETWTFLG(String value) {
        this.netwtflg = value;
    }

    /**
     * Gets the value of the grosswtflg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGROSSWTFLG() {
        return grosswtflg;
    }

    /**
     * Sets the value of the grosswtflg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGROSSWTFLG(String value) {
        this.grosswtflg = value;
    }

}
