
package com.lsh.wms.integration.wumart.ibdback;

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
 *         &lt;element name="P_DOCITEM" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPI2017_GM_ITEM_04"/>
 *         &lt;element name="P_HEADRET" type="{urn:sap-com:document:sap:rfc:functions}BAPI2017_GM_HEAD_RET"/>
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
    "pdocitem",
    "pheadret",
    "_return"
})
@XmlRootElement(name = "ZBAPI_GOODSMVT_CANCELResponse")
public class ZBAPIGOODSMVTCANCELResponse {

    @XmlElement(name = "P_DOCITEM", required = true)
    protected TABLEOFBAPI2017GMITEM04 pdocitem;
    @XmlElement(name = "P_HEADRET", required = true)
    protected BAPI2017GMHEADRET pheadret;
    @XmlElement(name = "RETURN", required = true)
    protected TABLEOFBAPIRET2 _return;

    /**
     * Gets the value of the pdocitem property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFBAPI2017GMITEM04 }
     *     
     */
    public TABLEOFBAPI2017GMITEM04 getPDOCITEM() {
        return pdocitem;
    }

    /**
     * Sets the value of the pdocitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFBAPI2017GMITEM04 }
     *     
     */
    public void setPDOCITEM(TABLEOFBAPI2017GMITEM04 value) {
        this.pdocitem = value;
    }

    /**
     * Gets the value of the pheadret property.
     * 
     * @return
     *     possible object is
     *     {@link BAPI2017GMHEADRET }
     *     
     */
    public BAPI2017GMHEADRET getPHEADRET() {
        return pheadret;
    }

    /**
     * Sets the value of the pheadret property.
     * 
     * @param value
     *     allowed object is
     *     {@link BAPI2017GMHEADRET }
     *     
     */
    public void setPHEADRET(BAPI2017GMHEADRET value) {
        this.pheadret = value;
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
