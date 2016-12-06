
package com.lsh.wms.integration.wumart.account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapimatdoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapimatdoc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PurGroup" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="IssueUnit" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapimatdoc", propOrder = {
    "purGroup",
    "issueUnit"
})
public class Bapimatdoc {

    @XmlElement(name = "PurGroup", required = true)
    protected String purGroup;
    @XmlElement(name = "IssueUnit", required = true)
    protected String issueUnit;

    /**
     * Gets the value of the purGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurGroup() {
        return purGroup;
    }

    /**
     * Sets the value of the purGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurGroup(String value) {
        this.purGroup = value;
    }

    /**
     * Gets the value of the issueUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueUnit() {
        return issueUnit;
    }

    /**
     * Sets the value of the issueUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueUnit(String value) {
        this.issueUnit = value;
    }

}
