
package com.lsh.wms.integration.wumart.soobd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OBDHEADER" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_ZBAPI_R2_DELIVERYHEAD"/>
 *         &lt;element name="OBDITEM" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_ZBAPI_R2_DELIVERYITEM"/>
 *         &lt;element name="RETURN" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPIRET2"/>
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
    "obdheader",
    "obditem",
    "_return"
})
@XmlRootElement(name = "Z_BAPI_R2_DELIVERY_SOResponse")
public class ZBAPIR2DELIVERYSOResponse {

    @XmlElement(name = "OBDHEADER", required = true)
    protected TABLEOFZBAPIR2DELIVERYHEAD obdheader;
    @XmlElement(name = "OBDITEM", required = true)
    protected TABLEOFZBAPIR2DELIVERYITEM obditem;
    @XmlElement(name = "RETURN", required = true)
    protected TABLEOFBAPIRET2 _return;

    /**
     * Gets the value of the obdheader property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFZBAPIR2DELIVERYHEAD }
     *     
     */
    public TABLEOFZBAPIR2DELIVERYHEAD getOBDHEADER() {
        return obdheader;
    }

    /**
     * Sets the value of the obdheader property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFZBAPIR2DELIVERYHEAD }
     *     
     */
    public void setOBDHEADER(TABLEOFZBAPIR2DELIVERYHEAD value) {
        this.obdheader = value;
    }

    /**
     * Gets the value of the obditem property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFZBAPIR2DELIVERYITEM }
     *     
     */
    public TABLEOFZBAPIR2DELIVERYITEM getOBDITEM() {
        return obditem;
    }

    /**
     * Sets the value of the obditem property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFZBAPIR2DELIVERYITEM }
     *     
     */
    public void setOBDITEM(TABLEOFZBAPIR2DELIVERYITEM value) {
        this.obditem = value;
    }

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFBAPIRET2 }
     *     
     */
    public TABLEOFBAPIRET2 getRETURN() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFBAPIRET2 }
     *     
     */
    public void setRETURN(TABLEOFBAPIRET2 value) {
        this._return = value;
    }

}
