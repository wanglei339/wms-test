
package com.lsh.wms.integration.wumart.obd;

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
 *         &lt;element name="CreatedItems" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapidlvitemcreated"/>
 *         &lt;element name="Deliveries" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapishpdelivnumb"/>
 *         &lt;element name="Delivery" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="ExtensionIn" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiparex"/>
 *         &lt;element name="ExtensionOut" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiparex"/>
 *         &lt;element name="NumDeliveries" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="Return" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapiret2"/>
 *         &lt;element name="SerialNumbers" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapidlvserialnumber"/>
 *         &lt;element name="StockTransItems" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapidlvreftosto"/>
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
    "createdItems",
    "deliveries",
    "delivery",
    "extensionIn",
    "extensionOut",
    "numDeliveries",
    "_return",
    "serialNumbers",
    "stockTransItems"
})
@XmlRootElement(name = "ZBapiOutbCreateObdResponse")
public class ZBapiOutbCreateObdResponse {

    @XmlElement(name = "CreatedItems", required = true)
    protected TableOfBapidlvitemcreated createdItems;
    @XmlElement(name = "Deliveries", required = true)
    protected TableOfBapishpdelivnumb deliveries;
    @XmlElement(name = "Delivery", required = true)
    protected String delivery;
    @XmlElement(name = "ExtensionIn", required = true)
    protected TableOfBapiparex extensionIn;
    @XmlElement(name = "ExtensionOut", required = true)
    protected TableOfBapiparex extensionOut;
    @XmlElement(name = "NumDeliveries", required = true)
    protected String numDeliveries;
    @XmlElement(name = "Return", required = true)
    protected TableOfBapiret2 _return;
    @XmlElement(name = "SerialNumbers", required = true)
    protected TableOfBapidlvserialnumber serialNumbers;
    @XmlElement(name = "StockTransItems", required = true)
    protected TableOfBapidlvreftosto stockTransItems;

    /**
     * Gets the value of the createdItems property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapidlvitemcreated }
     *     
     */
    public TableOfBapidlvitemcreated getCreatedItems() {
        return createdItems;
    }

    /**
     * Sets the value of the createdItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapidlvitemcreated }
     *     
     */
    public void setCreatedItems(TableOfBapidlvitemcreated value) {
        this.createdItems = value;
    }

    /**
     * Gets the value of the deliveries property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapishpdelivnumb }
     *     
     */
    public TableOfBapishpdelivnumb getDeliveries() {
        return deliveries;
    }

    /**
     * Sets the value of the deliveries property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapishpdelivnumb }
     *     
     */
    public void setDeliveries(TableOfBapishpdelivnumb value) {
        this.deliveries = value;
    }

    /**
     * Gets the value of the delivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivery() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivery(String value) {
        this.delivery = value;
    }

    /**
     * Gets the value of the extensionIn property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapiparex }
     *     
     */
    public TableOfBapiparex getExtensionIn() {
        return extensionIn;
    }

    /**
     * Sets the value of the extensionIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapiparex }
     *     
     */
    public void setExtensionIn(TableOfBapiparex value) {
        this.extensionIn = value;
    }

    /**
     * Gets the value of the extensionOut property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapiparex }
     *     
     */
    public TableOfBapiparex getExtensionOut() {
        return extensionOut;
    }

    /**
     * Sets the value of the extensionOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapiparex }
     *     
     */
    public void setExtensionOut(TableOfBapiparex value) {
        this.extensionOut = value;
    }

    /**
     * Gets the value of the numDeliveries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumDeliveries() {
        return numDeliveries;
    }

    /**
     * Sets the value of the numDeliveries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumDeliveries(String value) {
        this.numDeliveries = value;
    }

    /**
     * Gets the value of the return property.
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
     * Sets the value of the return property.
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
     * Gets the value of the serialNumbers property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapidlvserialnumber }
     *     
     */
    public TableOfBapidlvserialnumber getSerialNumbers() {
        return serialNumbers;
    }

    /**
     * Sets the value of the serialNumbers property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapidlvserialnumber }
     *     
     */
    public void setSerialNumbers(TableOfBapidlvserialnumber value) {
        this.serialNumbers = value;
    }

    /**
     * Gets the value of the stockTransItems property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapidlvreftosto }
     *     
     */
    public TableOfBapidlvreftosto getStockTransItems() {
        return stockTransItems;
    }

    /**
     * Sets the value of the stockTransItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapidlvreftosto }
     *     
     */
    public void setStockTransItems(TableOfBapidlvreftosto value) {
        this.stockTransItems = value;
    }

}
