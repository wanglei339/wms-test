
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>_-spe_-bapi2017GmRefEwm complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="_-spe_-bapi2017GmRefEwm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RefDocEwm" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="Logsys" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="GtsScrapNo" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "_-spe_-bapi2017GmRefEwm", propOrder = {
    "refDocEwm",
    "logsys",
    "gtsScrapNo"
})
public class SpeBapi2017GmRefEwm {

    @XmlElement(name = "RefDocEwm", required = true)
    protected String refDocEwm;
    @XmlElement(name = "Logsys", required = true)
    protected String logsys;
    @XmlElement(name = "GtsScrapNo", required = true)
    protected String gtsScrapNo;

    /**
     * 获取refDocEwm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDocEwm() {
        return refDocEwm;
    }

    /**
     * 设置refDocEwm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDocEwm(String value) {
        this.refDocEwm = value;
    }

    /**
     * 获取logsys属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogsys() {
        return logsys;
    }

    /**
     * 设置logsys属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogsys(String value) {
        this.logsys = value;
    }

    /**
     * 获取gtsScrapNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGtsScrapNo() {
        return gtsScrapNo;
    }

    /**
     * 设置gtsScrapNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGtsScrapNo(String value) {
        this.gtsScrapNo = value;
    }

}
