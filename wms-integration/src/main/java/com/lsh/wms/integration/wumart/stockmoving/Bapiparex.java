
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Bapiparex complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bapiparex">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Structure" type="{urn:sap-com:document:sap:rfc:functions}char30"/>
 *         &lt;element name="Valuepart1" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *         &lt;element name="Valuepart2" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *         &lt;element name="Valuepart3" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *         &lt;element name="Valuepart4" type="{urn:sap-com:document:sap:rfc:functions}char240"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapiparex", propOrder = {
    "structure",
    "valuepart1",
    "valuepart2",
    "valuepart3",
    "valuepart4"
})
public class Bapiparex {

    @XmlElement(name = "Structure", required = true)
    protected String structure;
    @XmlElement(name = "Valuepart1", required = true)
    protected String valuepart1;
    @XmlElement(name = "Valuepart2", required = true)
    protected String valuepart2;
    @XmlElement(name = "Valuepart3", required = true)
    protected String valuepart3;
    @XmlElement(name = "Valuepart4", required = true)
    protected String valuepart4;

    /**
     * 获取structure属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStructure() {
        return structure;
    }

    /**
     * 设置structure属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStructure(String value) {
        this.structure = value;
    }

    /**
     * 获取valuepart1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart1() {
        return valuepart1;
    }

    /**
     * 设置valuepart1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart1(String value) {
        this.valuepart1 = value;
    }

    /**
     * 获取valuepart2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart2() {
        return valuepart2;
    }

    /**
     * 设置valuepart2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart2(String value) {
        this.valuepart2 = value;
    }

    /**
     * 获取valuepart3属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart3() {
        return valuepart3;
    }

    /**
     * 设置valuepart3属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart3(String value) {
        this.valuepart3 = value;
    }

    /**
     * 获取valuepart4属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValuepart4() {
        return valuepart4;
    }

    /**
     * 设置valuepart4属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValuepart4(String value) {
        this.valuepart4 = value;
    }

}
