
package com.lsh.wms.integration.wumart.ibdback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="P_DATE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="P_DOCITEM" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPI2017_GM_ITEM_04" minOccurs="0"/>
 *         &lt;element name="P_DOCUMENT" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="P_DOCYEAR" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="P_UNAME" type="{urn:sap-com:document:sap:rfc:functions}char12" minOccurs="0"/>
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
    "pdate",
    "pdocitem",
    "pdocument",
    "pdocyear",
    "puname",
    "_return"
})
@XmlRootElement(name = "ZBAPI_GOODSMVT_CANCEL")
public class ZBAPIGOODSMVTCANCEL_Type {

    @XmlElement(name = "P_DATE")
    protected XMLGregorianCalendar pdate;
    @XmlElement(name = "P_DOCITEM")
    protected TABLEOFBAPI2017GMITEM04 pdocitem;
    @XmlElement(name = "P_DOCUMENT", required = true)
    protected String pdocument;
    @XmlElement(name = "P_DOCYEAR", required = true)
    protected String pdocyear;
    @XmlElement(name = "P_UNAME")
    protected String puname;
    @XmlElement(name = "RETURN", required = true)
    protected TABLEOFBAPIRET2 _return;

    /**
     * Gets the value of the pdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPDATE() {
        return pdate;
    }

    /**
     * Sets the value of the pdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPDATE(XMLGregorianCalendar value) {
        this.pdate = value;
    }

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
     * Gets the value of the pdocument property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDOCUMENT() {
        return pdocument;
    }

    /**
     * Sets the value of the pdocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDOCUMENT(String value) {
        this.pdocument = value;
    }

    /**
     * Gets the value of the pdocyear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDOCYEAR() {
        return pdocyear;
    }

    /**
     * Sets the value of the pdocyear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDOCYEAR(String value) {
        this.pdocyear = value;
    }

    /**
     * Gets the value of the puname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPUNAME() {
        return puname;
    }

    /**
     * Sets the value of the puname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPUNAME(String value) {
        this.puname = value;
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
