
package com.lsh.wms.integration.wumart.obd;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapidlvreftosto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapidlvreftosto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RefDoc" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="RefItem" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="DlvQty" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="SalesUnit" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="SalesUnitIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="DelivNumb" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapidlvreftosto", propOrder = {
    "refDoc",
    "refItem",
    "dlvQty",
    "salesUnit",
    "salesUnitIso",
    "delivNumb"
})
public class Bapidlvreftosto {

    @XmlElement(name = "RefDoc", required = true)
    protected String refDoc;
    @XmlElement(name = "RefItem", required = true)
    protected String refItem;
    @XmlElement(name = "DlvQty", required = true)
    protected BigDecimal dlvQty;
    @XmlElement(name = "SalesUnit", required = true)
    protected String salesUnit;
    @XmlElement(name = "SalesUnitIso", required = true)
    protected String salesUnitIso;
    @XmlElement(name = "DelivNumb", required = true)
    protected String delivNumb;

    /**
     * Gets the value of the refDoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDoc() {
        return refDoc;
    }

    /**
     * Sets the value of the refDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDoc(String value) {
        this.refDoc = value;
    }

    /**
     * Gets the value of the refItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefItem() {
        return refItem;
    }

    /**
     * Sets the value of the refItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefItem(String value) {
        this.refItem = value;
    }

    /**
     * Gets the value of the dlvQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDlvQty() {
        return dlvQty;
    }

    /**
     * Sets the value of the dlvQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDlvQty(BigDecimal value) {
        this.dlvQty = value;
    }

    /**
     * Gets the value of the salesUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesUnit() {
        return salesUnit;
    }

    /**
     * Sets the value of the salesUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesUnit(String value) {
        this.salesUnit = value;
    }

    /**
     * Gets the value of the salesUnitIso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesUnitIso() {
        return salesUnitIso;
    }

    /**
     * Sets the value of the salesUnitIso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesUnitIso(String value) {
        this.salesUnitIso = value;
    }

    /**
     * Gets the value of the delivNumb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivNumb() {
        return delivNumb;
    }

    /**
     * Sets the value of the delivNumb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivNumb(String value) {
        this.delivNumb = value;
    }

}
