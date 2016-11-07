
package com.lsh.wms.integration.wumart.obdaccount;

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
 *         &lt;element name="ITEM_CONTROL" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPIOBDLVITEMCTRLCHG"/>
 *         &lt;element name="ITEM_DATA" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPIOBDLVITEMCHG"/>
 *         &lt;element name="PROT" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_PROTT"/>
 *         &lt;element name="P_ZEXPORT" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_ZDELIVERYEXPORT"/>
 *         &lt;element name="P_ZIMPORT" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_ZDELIVERYIMPORT"/>
 *         &lt;element name="RETURN" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPIRET2"/>
 *         &lt;element name="RETURN1" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_BAPIRET2"/>
 *         &lt;element name="VBPOK_TAB" type="{urn:sap-com:document:sap:rfc:functions}TABLE_OF_VBPOK"/>
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
    "itemcontrol",
    "itemdata",
    "prot",
    "pzexport",
    "pzimport",
    "_return",
    "return1",
    "vbpoktab"
})
@XmlRootElement(name = "Z_DELIVERY_OUTBOUND_UPDATEResponse")
public class ZDELIVERYOUTBOUNDUPDATEResponse {

    @XmlElement(name = "ITEM_CONTROL", required = true)
    protected TABLEOFBAPIOBDLVITEMCTRLCHG itemcontrol;
    @XmlElement(name = "ITEM_DATA", required = true)
    protected TABLEOFBAPIOBDLVITEMCHG itemdata;
    @XmlElement(name = "PROT", required = true)
    protected TABLEOFPROTT prot;
    @XmlElement(name = "P_ZEXPORT", required = true)
    protected TABLEOFZDELIVERYEXPORT pzexport;
    @XmlElement(name = "P_ZIMPORT", required = true)
    protected TABLEOFZDELIVERYIMPORT pzimport;
    @XmlElement(name = "RETURN", required = true)
    protected TABLEOFBAPIRET2 _return;
    @XmlElement(name = "RETURN1", required = true)
    protected TABLEOFBAPIRET2 return1;
    @XmlElement(name = "VBPOK_TAB", required = true)
    protected TABLEOFVBPOK vbpoktab;

    /**
     * Gets the value of the itemcontrol property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFBAPIOBDLVITEMCTRLCHG }
     *     
     */
    public TABLEOFBAPIOBDLVITEMCTRLCHG getITEMCONTROL() {
        return itemcontrol;
    }

    /**
     * Sets the value of the itemcontrol property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFBAPIOBDLVITEMCTRLCHG }
     *     
     */
    public void setITEMCONTROL(TABLEOFBAPIOBDLVITEMCTRLCHG value) {
        this.itemcontrol = value;
    }

    /**
     * Gets the value of the itemdata property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFBAPIOBDLVITEMCHG }
     *     
     */
    public TABLEOFBAPIOBDLVITEMCHG getITEMDATA() {
        return itemdata;
    }

    /**
     * Sets the value of the itemdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFBAPIOBDLVITEMCHG }
     *     
     */
    public void setITEMDATA(TABLEOFBAPIOBDLVITEMCHG value) {
        this.itemdata = value;
    }

    /**
     * Gets the value of the prot property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFPROTT }
     *     
     */
    public TABLEOFPROTT getPROT() {
        return prot;
    }

    /**
     * Sets the value of the prot property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFPROTT }
     *     
     */
    public void setPROT(TABLEOFPROTT value) {
        this.prot = value;
    }

    /**
     * Gets the value of the pzexport property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFZDELIVERYEXPORT }
     *     
     */
    public TABLEOFZDELIVERYEXPORT getPZEXPORT() {
        return pzexport;
    }

    /**
     * Sets the value of the pzexport property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFZDELIVERYEXPORT }
     *     
     */
    public void setPZEXPORT(TABLEOFZDELIVERYEXPORT value) {
        this.pzexport = value;
    }

    /**
     * Gets the value of the pzimport property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFZDELIVERYIMPORT }
     *     
     */
    public TABLEOFZDELIVERYIMPORT getPZIMPORT() {
        return pzimport;
    }

    /**
     * Sets the value of the pzimport property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFZDELIVERYIMPORT }
     *     
     */
    public void setPZIMPORT(TABLEOFZDELIVERYIMPORT value) {
        this.pzimport = value;
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

    /**
     * Gets the value of the return1 property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFBAPIRET2 }
     *     
     */
    public TABLEOFBAPIRET2 getRETURN1() {
        return return1;
    }

    /**
     * Sets the value of the return1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFBAPIRET2 }
     *     
     */
    public void setRETURN1(TABLEOFBAPIRET2 value) {
        this.return1 = value;
    }

    /**
     * Gets the value of the vbpoktab property.
     * 
     * @return
     *     possible object is
     *     {@link TABLEOFVBPOK }
     *     
     */
    public TABLEOFVBPOK getVBPOKTAB() {
        return vbpoktab;
    }

    /**
     * Sets the value of the vbpoktab property.
     * 
     * @param value
     *     allowed object is
     *     {@link TABLEOFVBPOK }
     *     
     */
    public void setVBPOKTAB(TABLEOFVBPOK value) {
        this.vbpoktab = value;
    }

}
