
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Bapi2017GmHeadRet complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bapi2017GmHeadRet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MatDoc" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DocYear" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapi2017GmHeadRet", propOrder = {
    "matDoc",
    "docYear"
})
public class Bapi2017GmHeadRet {

    @XmlElement(name = "MatDoc", required = true)
    protected String matDoc;
    @XmlElement(name = "DocYear", required = true)
    protected String docYear;

    /**
     * 获取matDoc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatDoc() {
        return matDoc;
    }

    /**
     * 设置matDoc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatDoc(String value) {
        this.matDoc = value;
    }

    /**
     * 获取docYear属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocYear() {
        return docYear;
    }

    /**
     * 设置docYear属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocYear(String value) {
        this.docYear = value;
    }

}
