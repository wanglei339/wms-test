
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Bapi2017GmSerialnumber complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bapi2017GmSerialnumber">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MatdocItm" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="Serialno" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="Uii" type="{urn:sap-com:document:sap:rfc:functions}char72"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapi2017GmSerialnumber", propOrder = {
    "matdocItm",
    "serialno",
    "uii"
})
public class Bapi2017GmSerialnumber {

    @XmlElement(name = "MatdocItm", required = true)
    protected String matdocItm;
    @XmlElement(name = "Serialno", required = true)
    protected String serialno;
    @XmlElement(name = "Uii", required = true)
    protected String uii;

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
     * 获取serialno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialno() {
        return serialno;
    }

    /**
     * 设置serialno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialno(String value) {
        this.serialno = value;
    }

    /**
     * 获取uii属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUii() {
        return uii;
    }

    /**
     * 设置uii属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUii(String value) {
        this.uii = value;
    }

}
