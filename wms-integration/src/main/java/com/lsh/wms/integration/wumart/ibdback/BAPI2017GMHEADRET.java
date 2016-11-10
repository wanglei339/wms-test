
package com.lsh.wms.integration.wumart.ibdback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BAPI2017_GM_HEAD_RET complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BAPI2017_GM_HEAD_RET">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MAT_DOC" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DOC_YEAR" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BAPI2017_GM_HEAD_RET", propOrder = {
    "matdoc",
    "docyear"
})
public class BAPI2017GMHEADRET {

    @XmlElement(name = "MAT_DOC", required = true)
    protected String matdoc;
    @XmlElement(name = "DOC_YEAR", required = true)
    protected String docyear;

    /**
     * Gets the value of the matdoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDOC() {
        return matdoc;
    }

    /**
     * Sets the value of the matdoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDOC(String value) {
        this.matdoc = value;
    }

    /**
     * Gets the value of the docyear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOCYEAR() {
        return docyear;
    }

    /**
     * Sets the value of the docyear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOCYEAR(String value) {
        this.docyear = value;
    }

}
