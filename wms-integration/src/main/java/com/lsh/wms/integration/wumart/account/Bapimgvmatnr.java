
package com.lsh.wms.integration.wumart.account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapimgvmatnr complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapimgvmatnr">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MaterialExt" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="MaterialVers" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MaterialGuid" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapimgvmatnr", propOrder = {
    "materialExt",
    "materialVers",
    "materialGuid"
})
public class Bapimgvmatnr {

    @XmlElement(name = "MaterialExt", required = true)
    protected String materialExt;
    @XmlElement(name = "MaterialVers", required = true)
    protected String materialVers;
    @XmlElement(name = "MaterialGuid", required = true)
    protected String materialGuid;

    /**
     * Gets the value of the materialExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialExt() {
        return materialExt;
    }

    /**
     * Sets the value of the materialExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialExt(String value) {
        this.materialExt = value;
    }

    /**
     * Gets the value of the materialVers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialVers() {
        return materialVers;
    }

    /**
     * Sets the value of the materialVers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialVers(String value) {
        this.materialVers = value;
    }

    /**
     * Gets the value of the materialGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialGuid() {
        return materialGuid;
    }

    /**
     * Sets the value of the materialGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialGuid(String value) {
        this.materialGuid = value;
    }

}
