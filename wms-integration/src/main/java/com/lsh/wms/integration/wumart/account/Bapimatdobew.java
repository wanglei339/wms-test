
package com.lsh.wms.integration.wumart.account;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapimatdobew complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapimatdobew">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PriceCtrl" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MovingPr" type="{urn:sap-com:document:sap:rfc:functions}decimal23.4"/>
 *         &lt;element name="StdPrice" type="{urn:sap-com:document:sap:rfc:functions}decimal23.4"/>
 *         &lt;element name="PriceUnit" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="Currency" type="{urn:sap-com:document:sap:rfc:functions}cuky5"/>
 *         &lt;element name="CurrencyIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapimatdobew", propOrder = {
    "priceCtrl",
    "movingPr",
    "stdPrice",
    "priceUnit",
    "currency",
    "currencyIso"
})
public class Bapimatdobew {

    @XmlElement(name = "PriceCtrl", required = true)
    protected String priceCtrl;
    @XmlElement(name = "MovingPr", required = true)
    protected BigDecimal movingPr;
    @XmlElement(name = "StdPrice", required = true)
    protected BigDecimal stdPrice;
    @XmlElement(name = "PriceUnit", required = true)
    protected BigDecimal priceUnit;
    @XmlElement(name = "Currency", required = true)
    protected String currency;
    @XmlElement(name = "CurrencyIso", required = true)
    protected String currencyIso;

    /**
     * Gets the value of the priceCtrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceCtrl() {
        return priceCtrl;
    }

    /**
     * Sets the value of the priceCtrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceCtrl(String value) {
        this.priceCtrl = value;
    }

    /**
     * Gets the value of the movingPr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMovingPr() {
        return movingPr;
    }

    /**
     * Sets the value of the movingPr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMovingPr(BigDecimal value) {
        this.movingPr = value;
    }

    /**
     * Gets the value of the stdPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStdPrice() {
        return stdPrice;
    }

    /**
     * Sets the value of the stdPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStdPrice(BigDecimal value) {
        this.stdPrice = value;
    }

    /**
     * Gets the value of the priceUnit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPriceUnit() {
        return priceUnit;
    }

    /**
     * Sets the value of the priceUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPriceUnit(BigDecimal value) {
        this.priceUnit = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the currencyIso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyIso() {
        return currencyIso;
    }

    /**
     * Sets the value of the currencyIso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyIso(String value) {
        this.currencyIso = value;
    }

}
