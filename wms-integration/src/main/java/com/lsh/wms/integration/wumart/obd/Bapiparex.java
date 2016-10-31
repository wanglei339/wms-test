
package com.lsh.wms.integration.wumart.obd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapiparex complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapiparex">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Structure" type="{urn:sap-com:document:sap:rfc:functions}char30"/>
 *         &lt;element name="Valuepart1" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *         &lt;element name="Valuepart2" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *         &lt;element name="Valuepart3" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *         &lt;element name="Valuepart4" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapiparex", propOrder = {
    "structure",
    "valuepart1",
    "valuepart2",
    "valuepart3",
    "valuepart4"
})
public class Bapiparex {

    @XmlElement(name = "Structure", required = true)
    protected String structure;
    @XmlElement(name = "Valuepart1", required = true)
    protected String valuepart1;
    @XmlElement(name = "Valuepart2", required = true)
    protected String valuepart2;
    @XmlElement(name = "Valuepart3", required = true)
    protected String valuepart3;
    @XmlElement(name = "Valuepart4", required = true)
    protected String valuepart4;

    /**
     * Gets the value of the structure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStructure() {
        return structure;
    }

    /**
     * Sets the value of the structure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStructure(String value) {
        this.structure = value;
    }

    /**
     * Gets the value of the valuepart1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart1() {
        return valuepart1;
    }

    /**
     * Sets the value of the valuepart1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart1(String value) {
        this.valuepart1 = value;
    }

    /**
     * Gets the value of the valuepart2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart2() {
        return valuepart2;
    }

    /**
     * Sets the value of the valuepart2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart2(String value) {
        this.valuepart2 = value;
    }

    /**
     * Gets the value of the valuepart3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart3() {
        return valuepart3;
    }

    /**
     * Sets the value of the valuepart3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart3(String value) {
        this.valuepart3 = value;
    }

    /**
     * Gets the value of the valuepart4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart4() {
        return valuepart4;
    }

    /**
     * Sets the value of the valuepart4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart4(String value) {
        this.valuepart4 = value;
    }

}
