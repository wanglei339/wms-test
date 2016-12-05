
package com.lsh.wms.integration.wumart.stockmoving;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>_-cwm_-bapi2017GmItemCreate complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="_-cwm_-bapi2017GmItemCreate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MatdocItm" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="QuantityPme" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="BaseUomPme" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="EntryQntPme" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="EntryUomPme" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_-cwm_-bapi2017GmItemCreate", propOrder = {
    "matdocItm",
    "quantityPme",
    "baseUomPme",
    "entryQntPme",
    "entryUomPme"
})
public class CwmBapi2017GmItemCreate {

    @XmlElement(name = "MatdocItm", required = true)
    protected String matdocItm;
    @XmlElement(name = "QuantityPme", required = true)
    protected BigDecimal quantityPme;
    @XmlElement(name = "BaseUomPme", required = true)
    protected String baseUomPme;
    @XmlElement(name = "EntryQntPme", required = true)
    protected BigDecimal entryQntPme;
    @XmlElement(name = "EntryUomPme", required = true)
    protected String entryUomPme;

    /**
     * 获取matdocItm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatdocItm() {
        return matdocItm;
    }

    /**
     * 设置matdocItm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatdocItm(String value) {
        this.matdocItm = value;
    }

    /**
     * 获取quantityPme属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantityPme() {
        return quantityPme;
    }

    /**
     * 设置quantityPme属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantityPme(BigDecimal value) {
        this.quantityPme = value;
    }

    /**
     * 获取baseUomPme属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseUomPme() {
        return baseUomPme;
    }

    /**
     * 设置baseUomPme属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseUomPme(String value) {
        this.baseUomPme = value;
    }

    /**
     * 获取entryQntPme属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEntryQntPme() {
        return entryQntPme;
    }

    /**
     * 设置entryQntPme属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEntryQntPme(BigDecimal value) {
        this.entryQntPme = value;
    }

    /**
     * 获取entryUomPme属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryUomPme() {
        return entryUomPme;
    }

    /**
     * 设置entryUomPme属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryUomPme(String value) {
        this.entryUomPme = value;
    }

}
