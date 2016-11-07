
package com.lsh.wms.integration.wumart.ibdaccount;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BAPIIBDLVITEMCHG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BAPIIBDLVITEMCHG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DELIV_NUMB" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DELIV_ITEM" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="MATERIAL" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="BATCH" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="HIERARITEM" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="USEHIERITM" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="DLV_QTY" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="DLV_QTY_IMUNIT" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="FACT_UNIT_NOM" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="FACT_UNIT_DENOM" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="CONV_FACT" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="GROSS_WT" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="NET_WEIGHT" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="UNIT_OF_WT" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="UNIT_OF_WT_ISO" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="VOLUME" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="VOLUMEUNIT" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="VOLUMEUNIT_ISO" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="SALES_UNIT" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="SALES_UNIT_ISO" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="BASE_UOM" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="BASE_UOM_ISO" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="DEL_QTY_FLO" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="DLV_QTY_ST_FLO" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="STOCK_TYPE" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="VAL_TYPE" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MATERIAL_EXTERNAL" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="MATERIAL_GUID" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *         &lt;element name="MATERIAL_VERSION" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="INSPLOT" type="{urn:sap-com:document:sap:rfc:functions}numeric12"/>
 *         &lt;element name="EXPIRYDATE" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="PART_GR_QTY" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BAPIIBDLVITEMCHG", propOrder = {
    "delivnumb",
    "delivitem",
    "material",
    "batch",
    "hieraritem",
    "usehieritm",
    "dlvqty",
    "dlvqtyimunit",
    "factunitnom",
    "factunitdenom",
    "convfact",
    "grosswt",
    "netweight",
    "unitofwt",
    "unitofwtiso",
    "volume",
    "volumeunit",
    "volumeunitiso",
    "salesunit",
    "salesunitiso",
    "baseuom",
    "baseuomiso",
    "delqtyflo",
    "dlvqtystflo",
    "stocktype",
    "valtype",
    "materialexternal",
    "materialguid",
    "materialversion",
    "insplot",
    "expirydate",
    "partgrqty"
})
public class BAPIIBDLVITEMCHG {

    @XmlElement(name = "DELIV_NUMB", required = true)
    protected String delivnumb;
    @XmlElement(name = "DELIV_ITEM", required = true)
    protected String delivitem;
    @XmlElement(name = "MATERIAL", required = true)
    protected String material;
    @XmlElement(name = "BATCH", required = true)
    protected String batch;
    @XmlElement(name = "HIERARITEM", required = true)
    protected String hieraritem;
    @XmlElement(name = "USEHIERITM", required = true)
    protected String usehieritm;
    @XmlElement(name = "DLV_QTY", required = true)
    protected BigDecimal dlvqty;
    @XmlElement(name = "DLV_QTY_IMUNIT", required = true)
    protected BigDecimal dlvqtyimunit;
    @XmlElement(name = "FACT_UNIT_NOM", required = true)
    protected BigDecimal factunitnom;
    @XmlElement(name = "FACT_UNIT_DENOM", required = true)
    protected BigDecimal factunitdenom;
    @XmlElement(name = "CONV_FACT")
    protected float convfact;
    @XmlElement(name = "GROSS_WT", required = true)
    protected BigDecimal grosswt;
    @XmlElement(name = "NET_WEIGHT", required = true)
    protected BigDecimal netweight;
    @XmlElement(name = "UNIT_OF_WT", required = true)
    protected String unitofwt;
    @XmlElement(name = "UNIT_OF_WT_ISO", required = true)
    protected String unitofwtiso;
    @XmlElement(name = "VOLUME", required = true)
    protected BigDecimal volume;
    @XmlElement(name = "VOLUMEUNIT", required = true)
    protected String volumeunit;
    @XmlElement(name = "VOLUMEUNIT_ISO", required = true)
    protected String volumeunitiso;
    @XmlElement(name = "SALES_UNIT", required = true)
    protected String salesunit;
    @XmlElement(name = "SALES_UNIT_ISO", required = true)
    protected String salesunitiso;
    @XmlElement(name = "BASE_UOM", required = true)
    protected String baseuom;
    @XmlElement(name = "BASE_UOM_ISO", required = true)
    protected String baseuomiso;
    @XmlElement(name = "DEL_QTY_FLO")
    protected float delqtyflo;
    @XmlElement(name = "DLV_QTY_ST_FLO")
    protected float dlvqtystflo;
    @XmlElement(name = "STOCK_TYPE", required = true)
    protected String stocktype;
    @XmlElement(name = "VAL_TYPE", required = true)
    protected String valtype;
    @XmlElement(name = "MATERIAL_EXTERNAL", required = true)
    protected String materialexternal;
    @XmlElement(name = "MATERIAL_GUID", required = true)
    protected String materialguid;
    @XmlElement(name = "MATERIAL_VERSION", required = true)
    protected String materialversion;
    @XmlElement(name = "INSPLOT", required = true)
    protected String insplot;
    @XmlElement(name = "EXPIRYDATE", required = true)
    protected XMLGregorianCalendar expirydate;
    @XmlElement(name = "PART_GR_QTY", required = true)
    protected BigDecimal partgrqty;

    /**
     * Gets the value of the delivnumb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVNUMB() {
        return delivnumb;
    }

    /**
     * Sets the value of the delivnumb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVNUMB(String value) {
        this.delivnumb = value;
    }

    /**
     * Gets the value of the delivitem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVITEM() {
        return delivitem;
    }

    /**
     * Sets the value of the delivitem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVITEM(String value) {
        this.delivitem = value;
    }

    /**
     * Gets the value of the material property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATERIAL() {
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
    public void setMATERIAL(String value) {
        this.material = value;
    }

    /**
     * Gets the value of the batch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBATCH() {
        return batch;
    }

    /**
     * Sets the value of the batch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBATCH(String value) {
        this.batch = value;
    }

    /**
     * Gets the value of the hieraritem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHIERARITEM() {
        return hieraritem;
    }

    /**
     * Sets the value of the hieraritem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHIERARITEM(String value) {
        this.hieraritem = value;
    }

    /**
     * Gets the value of the usehieritm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSEHIERITM() {
        return usehieritm;
    }

    /**
     * Sets the value of the usehieritm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSEHIERITM(String value) {
        this.usehieritm = value;
    }

    /**
     * Gets the value of the dlvqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDLVQTY() {
        return dlvqty;
    }

    /**
     * Sets the value of the dlvqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDLVQTY(BigDecimal value) {
        this.dlvqty = value;
    }

    /**
     * Gets the value of the dlvqtyimunit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDLVQTYIMUNIT() {
        return dlvqtyimunit;
    }

    /**
     * Sets the value of the dlvqtyimunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDLVQTYIMUNIT(BigDecimal value) {
        this.dlvqtyimunit = value;
    }

    /**
     * Gets the value of the factunitnom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFACTUNITNOM() {
        return factunitnom;
    }

    /**
     * Sets the value of the factunitnom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFACTUNITNOM(BigDecimal value) {
        this.factunitnom = value;
    }

    /**
     * Gets the value of the factunitdenom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFACTUNITDENOM() {
        return factunitdenom;
    }

    /**
     * Sets the value of the factunitdenom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFACTUNITDENOM(BigDecimal value) {
        this.factunitdenom = value;
    }

    /**
     * Gets the value of the convfact property.
     * 
     */
    public float getCONVFACT() {
        return convfact;
    }

    /**
     * Sets the value of the convfact property.
     * 
     */
    public void setCONVFACT(float value) {
        this.convfact = value;
    }

    /**
     * Gets the value of the grosswt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGROSSWT() {
        return grosswt;
    }

    /**
     * Sets the value of the grosswt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGROSSWT(BigDecimal value) {
        this.grosswt = value;
    }

    /**
     * Gets the value of the netweight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNETWEIGHT() {
        return netweight;
    }

    /**
     * Sets the value of the netweight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNETWEIGHT(BigDecimal value) {
        this.netweight = value;
    }

    /**
     * Gets the value of the unitofwt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNITOFWT() {
        return unitofwt;
    }

    /**
     * Sets the value of the unitofwt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNITOFWT(String value) {
        this.unitofwt = value;
    }

    /**
     * Gets the value of the unitofwtiso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNITOFWTISO() {
        return unitofwtiso;
    }

    /**
     * Sets the value of the unitofwtiso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNITOFWTISO(String value) {
        this.unitofwtiso = value;
    }

    /**
     * Gets the value of the volume property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVOLUME() {
        return volume;
    }

    /**
     * Sets the value of the volume property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVOLUME(BigDecimal value) {
        this.volume = value;
    }

    /**
     * Gets the value of the volumeunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOLUMEUNIT() {
        return volumeunit;
    }

    /**
     * Sets the value of the volumeunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOLUMEUNIT(String value) {
        this.volumeunit = value;
    }

    /**
     * Gets the value of the volumeunitiso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOLUMEUNITISO() {
        return volumeunitiso;
    }

    /**
     * Sets the value of the volumeunitiso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOLUMEUNITISO(String value) {
        this.volumeunitiso = value;
    }

    /**
     * Gets the value of the salesunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSALESUNIT() {
        return salesunit;
    }

    /**
     * Sets the value of the salesunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSALESUNIT(String value) {
        this.salesunit = value;
    }

    /**
     * Gets the value of the salesunitiso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSALESUNITISO() {
        return salesunitiso;
    }

    /**
     * Sets the value of the salesunitiso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSALESUNITISO(String value) {
        this.salesunitiso = value;
    }

    /**
     * Gets the value of the baseuom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBASEUOM() {
        return baseuom;
    }

    /**
     * Sets the value of the baseuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBASEUOM(String value) {
        this.baseuom = value;
    }

    /**
     * Gets the value of the baseuomiso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBASEUOMISO() {
        return baseuomiso;
    }

    /**
     * Sets the value of the baseuomiso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBASEUOMISO(String value) {
        this.baseuomiso = value;
    }

    /**
     * Gets the value of the delqtyflo property.
     * 
     */
    public float getDELQTYFLO() {
        return delqtyflo;
    }

    /**
     * Sets the value of the delqtyflo property.
     * 
     */
    public void setDELQTYFLO(float value) {
        this.delqtyflo = value;
    }

    /**
     * Gets the value of the dlvqtystflo property.
     * 
     */
    public float getDLVQTYSTFLO() {
        return dlvqtystflo;
    }

    /**
     * Sets the value of the dlvqtystflo property.
     * 
     */
    public void setDLVQTYSTFLO(float value) {
        this.dlvqtystflo = value;
    }

    /**
     * Gets the value of the stocktype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTOCKTYPE() {
        return stocktype;
    }

    /**
     * Sets the value of the stocktype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTOCKTYPE(String value) {
        this.stocktype = value;
    }

    /**
     * Gets the value of the valtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALTYPE() {
        return valtype;
    }

    /**
     * Sets the value of the valtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALTYPE(String value) {
        this.valtype = value;
    }

    /**
     * Gets the value of the materialexternal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATERIALEXTERNAL() {
        return materialexternal;
    }

    /**
     * Sets the value of the materialexternal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATERIALEXTERNAL(String value) {
        this.materialexternal = value;
    }

    /**
     * Gets the value of the materialguid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATERIALGUID() {
        return materialguid;
    }

    /**
     * Sets the value of the materialguid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATERIALGUID(String value) {
        this.materialguid = value;
    }

    /**
     * Gets the value of the materialversion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATERIALVERSION() {
        return materialversion;
    }

    /**
     * Sets the value of the materialversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATERIALVERSION(String value) {
        this.materialversion = value;
    }

    /**
     * Gets the value of the insplot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSPLOT() {
        return insplot;
    }

    /**
     * Sets the value of the insplot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSPLOT(String value) {
        this.insplot = value;
    }

    /**
     * Gets the value of the expirydate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEXPIRYDATE() {
        return expirydate;
    }

    /**
     * Sets the value of the expirydate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEXPIRYDATE(XMLGregorianCalendar value) {
        this.expirydate = value;
    }

    /**
     * Gets the value of the partgrqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPARTGRQTY() {
        return partgrqty;
    }

    /**
     * Sets the value of the partgrqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPARTGRQTY(BigDecimal value) {
        this.partgrqty = value;
    }

}
