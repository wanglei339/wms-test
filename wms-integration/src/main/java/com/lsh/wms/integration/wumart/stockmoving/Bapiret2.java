
package com.lsh.wms.integration.wumart.stockmoving;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Bapiret2 complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bapiret2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Id" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="Number" type="{urn:sap-com:document:sap:rfc:functions}numeric3"/>
 *         &lt;element name="Message" type="{urn:sap-com:document:sap:rfc:functions}char220"/>
 *         &lt;element name="LogNo" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="LogMsgNo" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="MessageV1" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MessageV2" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MessageV3" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MessageV4" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="Parameter" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *         &lt;element name="Row" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Field" type="{urn:sap-com:document:sap:rfc:functions}char30"/>
 *         &lt;element name="System" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapiret2", propOrder = {
    "type",
    "id",
    "number",
    "message",
    "logNo",
    "logMsgNo",
    "messageV1",
    "messageV2",
    "messageV3",
    "messageV4",
    "parameter",
    "row",
    "field",
    "system"
})
public class Bapiret2 {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "Number", required = true)
    protected String number;
    @XmlElement(name = "Message", required = true)
    protected String message;
    @XmlElement(name = "LogNo", required = true)
    protected String logNo;
    @XmlElement(name = "LogMsgNo", required = true)
    protected String logMsgNo;
    @XmlElement(name = "MessageV1", required = true)
    protected String messageV1;
    @XmlElement(name = "MessageV2", required = true)
    protected String messageV2;
    @XmlElement(name = "MessageV3", required = true)
    protected String messageV3;
    @XmlElement(name = "MessageV4", required = true)
    protected String messageV4;
    @XmlElement(name = "Parameter", required = true)
    protected String parameter;
    @XmlElement(name = "Row")
    protected int row;
    @XmlElement(name = "Field", required = true)
    protected String field;
    @XmlElement(name = "System", required = true)
    protected String system;

    /**
     * 获取type属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * 设置type属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * 获取id属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * 获取number属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * 设置number属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * 获取message属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取logNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogNo() {
        return logNo;
    }

    /**
     * 设置logNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogNo(String value) {
        this.logNo = value;
    }

    /**
     * 获取logMsgNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogMsgNo() {
        return logMsgNo;
    }

    /**
     * 设置logMsgNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogMsgNo(String value) {
        this.logMsgNo = value;
    }

    /**
     * 获取messageV1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageV1() {
        return messageV1;
    }

    /**
     * 设置messageV1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageV1(String value) {
        this.messageV1 = value;
    }

    /**
     * 获取messageV2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageV2() {
        return messageV2;
    }

    /**
     * 设置messageV2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageV2(String value) {
        this.messageV2 = value;
    }

    /**
     * 获取messageV3属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageV3() {
        return messageV3;
    }

    /**
     * 设置messageV3属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageV3(String value) {
        this.messageV3 = value;
    }

    /**
     * 获取messageV4属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageV4() {
        return messageV4;
    }

    /**
     * 设置messageV4属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageV4(String value) {
        this.messageV4 = value;
    }

    /**
     * 获取parameter属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * 设置parameter属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameter(String value) {
        this.parameter = value;
    }

    /**
     * 获取row属性的值。
     * 
     */
    public int getRow() {
        return row;
    }

    /**
     * 设置row属性的值。
     * 
     */
    public void setRow(int value) {
        this.row = value;
    }

    /**
     * 获取field属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField() {
        return field;
    }

    /**
     * 设置field属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField(String value) {
        this.field = value;
    }

    /**
     * 获取system属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystem() {
        return system;
    }

    /**
     * 设置system属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystem(String value) {
        this.system = value;
    }

}
