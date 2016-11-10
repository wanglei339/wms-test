
package com.lsh.wms.integration.wumart.ibdback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BAPI2017_GM_ITEM_04 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BAPI2017_GM_ITEM_04">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MATDOC_ITEM" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BAPI2017_GM_ITEM_04", propOrder = {
    "matdocitem"
})
public class BAPI2017GMITEM04 {

    @XmlElement(name = "MATDOC_ITEM", required = true)
    protected String matdocitem;

    /**
     * Gets the value of the matdocitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDOCITEM() {
        return matdocitem;
    }

    /**
     * Sets the value of the matdocitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDOCITEM(String value) {
        this.matdocitem = value;
    }

}
