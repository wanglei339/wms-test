
package com.lsh.wms.integration.wumart.account;

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
 *         &lt;element name="Material" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="MaterialEvg" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapimgvmatnr" minOccurs="0"/>
 *         &lt;element name="Plant" type="{urn:sap-com:document:sap:rfc:functions}char4" minOccurs="0"/>
 *         &lt;element name="Valuationarea" type="{urn:sap-com:document:sap:rfc:functions}char4" minOccurs="0"/>
 *         &lt;element name="Valuationtype" type="{urn:sap-com:document:sap:rfc:functions}char10" minOccurs="0"/>
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
    "material",
    "materialEvg",
    "plant",
    "valuationarea",
    "valuationtype"
})
@XmlRootElement(name = "MaterialGetDetail")
public class MaterialGetDetail {

    @XmlElement(name = "Material", required = true)
    protected String material;
    @XmlElement(name = "MaterialEvg")
    protected Bapimgvmatnr materialEvg;
    @XmlElement(name = "Plant")
    protected String plant;
    @XmlElement(name = "Valuationarea")
    protected String valuationarea;
    @XmlElement(name = "Valuationtype")
    protected String valuationtype;

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
     * Gets the value of the materialEvg property.
     * 
     * @return
     *     possible object is
     *     {@link Bapimgvmatnr }
     *     
     */
    public Bapimgvmatnr getMaterialEvg() {
        return materialEvg;
    }

    /**
     * Sets the value of the materialEvg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bapimgvmatnr }
     *     
     */
    public void setMaterialEvg(Bapimgvmatnr value) {
        this.materialEvg = value;
    }

    /**
     * Gets the value of the plant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlant() {
        return plant;
    }

    /**
     * Sets the value of the plant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlant(String value) {
        this.plant = value;
    }

    /**
     * Gets the value of the valuationarea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuationarea() {
        return valuationarea;
    }

    /**
     * Sets the value of the valuationarea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuationarea(String value) {
        this.valuationarea = value;
    }

    /**
     * Gets the value of the valuationtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuationtype() {
        return valuationtype;
    }

    /**
     * Sets the value of the valuationtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuationtype(String value) {
        this.valuationtype = value;
    }

}
