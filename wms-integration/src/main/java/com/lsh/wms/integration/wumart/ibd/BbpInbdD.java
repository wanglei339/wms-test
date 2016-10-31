
package com.lsh.wms.integration.wumart.ibd;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BbpInbdD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BbpInbdD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Delivery" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DelivItem" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="Material" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="VendMat" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="MatlDesc" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="DelivQty" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="Unit" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="PoNumber" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="PoItem" type="{urn:sap-com:document:sap:rfc:functions}numeric5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BbpInbdD", propOrder = {
    "delivery",
    "delivItem",
    "material",
    "vendMat",
    "matlDesc",
    "delivQty",
    "unit",
    "poNumber",
    "poItem"
})
public class BbpInbdD {

    @XmlElement(name = "Delivery", required = true)
    protected String delivery;
    @XmlElement(name = "DelivItem", required = true)
    protected String delivItem;
    @XmlElement(name = "Material", required = true)
    protected String material;
    @XmlElement(name = "VendMat", required = true)
    protected String vendMat;
    @XmlElement(name = "MatlDesc", required = true)
    protected String matlDesc;
    @XmlElement(name = "DelivQty", required = true)
    protected BigDecimal delivQty;
    @XmlElement(name = "Unit", required = true)
    protected String unit;
    @XmlElement(name = "PoNumber", required = true)
    protected String poNumber;
    @XmlElement(name = "PoItem", required = true)
    protected String poItem;

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
     * Gets the value of the delivItem property.
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
     * Sets the value of the delivItem property.
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
     * Gets the value of the material property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Sets the value of the material property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterial(String value) {
        this.material = value;
    }

    /**
     * Gets the value of the vendMat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendMat() {
        return vendMat;
    }

    /**
     * Sets the value of the vendMat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendMat(String value) {
        this.vendMat = value;
    }

    /**
     * Gets the value of the matlDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlDesc() {
        return matlDesc;
    }

    /**
     * Sets the value of the matlDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlDesc(String value) {
        this.matlDesc = value;
    }

    /**
     * Gets the value of the delivQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDelivQty() {
        return delivQty;
    }

    /**
     * Sets the value of the delivQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDelivQty(BigDecimal value) {
        this.delivQty = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnit(String value) {
        this.unit = value;
    }

    /**
     * Gets the value of the poNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * Sets the value of the poNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoNumber(String value) {
        this.poNumber = value;
    }

    /**
     * Gets the value of the poItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoItem() {
        return poItem;
    }

    /**
     * Sets the value of the poItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoItem(String value) {
        this.poItem = value;
    }

}
