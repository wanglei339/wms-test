
package com.lsh.wms.integration.wumart.ibd;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BbpInbdL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BbpInbdL">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Delivery" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DelivExt" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="DelivDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="DelivTime" type="{urn:sap-com:document:sap:rfc:functions}time"/>
 *         &lt;element name="Route" type="{urn:sap-com:document:sap:rfc:functions}char6"/>
 *         &lt;element name="ShipPoint" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="WhseNo" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="TransCat" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="TranspId" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="Billoflad" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="TotalWght" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="UnitOfWt" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="NetWeight" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="Volume" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="Volumeunit" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="Noshpunits" type="{urn:sap-com:document:sap:rfc:functions}numeric5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BbpInbdL", propOrder = {
    "delivery",
    "delivExt",
    "delivDate",
    "delivTime",
    "route",
    "shipPoint",
    "whseNo",
    "transCat",
    "transpId",
    "billoflad",
    "totalWght",
    "unitOfWt",
    "netWeight",
    "volume",
    "volumeunit",
    "noshpunits"
})
public class BbpInbdL {

    @XmlElement(name = "Delivery", required = true)
    protected String delivery;
    @XmlElement(name = "DelivExt", required = true)
    protected String delivExt;
    @XmlElement(name = "DelivDate", required = true)
    protected XMLGregorianCalendar delivDate;
    @XmlElement(name = "DelivTime", required = true)
    protected XMLGregorianCalendar delivTime;
    @XmlElement(name = "Route", required = true)
    protected String route;
    @XmlElement(name = "ShipPoint", required = true)
    protected String shipPoint;
    @XmlElement(name = "WhseNo", required = true)
    protected String whseNo;
    @XmlElement(name = "TransCat", required = true)
    protected String transCat;
    @XmlElement(name = "TranspId", required = true)
    protected String transpId;
    @XmlElement(name = "Billoflad", required = true)
    protected String billoflad;
    @XmlElement(name = "TotalWght", required = true)
    protected BigDecimal totalWght;
    @XmlElement(name = "UnitOfWt", required = true)
    protected String unitOfWt;
    @XmlElement(name = "NetWeight", required = true)
    protected BigDecimal netWeight;
    @XmlElement(name = "Volume", required = true)
    protected BigDecimal volume;
    @XmlElement(name = "Volumeunit", required = true)
    protected String volumeunit;
    @XmlElement(name = "Noshpunits", required = true)
    protected String noshpunits;

    /**
     * Gets the value of the delivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivery() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivery(String value) {
        this.delivery = value;
    }

    /**
     * Gets the value of the delivExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivExt() {
        return delivExt;
    }

    /**
     * Sets the value of the delivExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivExt(String value) {
        this.delivExt = value;
    }

    /**
     * Gets the value of the delivDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDelivDate() {
        return delivDate;
    }

    /**
     * Sets the value of the delivDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDelivDate(XMLGregorianCalendar value) {
        this.delivDate = value;
    }

    /**
     * Gets the value of the delivTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDelivTime() {
        return delivTime;
    }

    /**
     * Sets the value of the delivTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDelivTime(XMLGregorianCalendar value) {
        this.delivTime = value;
    }

    /**
     * Gets the value of the route property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoute() {
        return route;
    }

    /**
     * Sets the value of the route property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoute(String value) {
        this.route = value;
    }

    /**
     * Gets the value of the shipPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipPoint() {
        return shipPoint;
    }

    /**
     * Sets the value of the shipPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipPoint(String value) {
        this.shipPoint = value;
    }

    /**
     * Gets the value of the whseNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWhseNo() {
        return whseNo;
    }

    /**
     * Sets the value of the whseNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWhseNo(String value) {
        this.whseNo = value;
    }

    /**
     * Gets the value of the transCat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransCat() {
        return transCat;
    }

    /**
     * Sets the value of the transCat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransCat(String value) {
        this.transCat = value;
    }

    /**
     * Gets the value of the transpId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranspId() {
        return transpId;
    }

    /**
     * Sets the value of the transpId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranspId(String value) {
        this.transpId = value;
    }

    /**
     * Gets the value of the billoflad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBilloflad() {
        return billoflad;
    }

    /**
     * Sets the value of the billoflad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBilloflad(String value) {
        this.billoflad = value;
    }

    /**
     * Gets the value of the totalWght property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalWght() {
        return totalWght;
    }

    /**
     * Sets the value of the totalWght property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalWght(BigDecimal value) {
        this.totalWght = value;
    }

    /**
     * Gets the value of the unitOfWt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfWt() {
        return unitOfWt;
    }

    /**
     * Sets the value of the unitOfWt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfWt(String value) {
        this.unitOfWt = value;
    }

    /**
     * Gets the value of the netWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetWeight() {
        return netWeight;
    }

    /**
     * Sets the value of the netWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetWeight(BigDecimal value) {
        this.netWeight = value;
    }

    /**
     * Gets the value of the volume property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVolume() {
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
    public void setVolume(BigDecimal value) {
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
    public String getVolumeunit() {
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
    public void setVolumeunit(String value) {
        this.volumeunit = value;
    }

    /**
     * Gets the value of the noshpunits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoshpunits() {
        return noshpunits;
    }

    /**
     * Sets the value of the noshpunits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoshpunits(String value) {
        this.noshpunits = value;
    }

}
