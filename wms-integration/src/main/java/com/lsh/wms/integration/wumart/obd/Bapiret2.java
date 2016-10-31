
package com.lsh.wms.integration.wumart.obd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapiret2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the type property.
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
     * Sets the value of the type property.
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
     * Gets the value of the id property.
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
     * Sets the value of the id property.
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
     * Gets the value of the number property.
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
     * Sets the value of the number property.
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
     * Gets the value of the message property.
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
     * Sets the value of the message property.
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
     * Gets the value of the logNo property.
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
     * Sets the value of the logNo property.
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
     * Gets the value of the logMsgNo property.
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
     * Sets the value of the logMsgNo property.
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
     * Gets the value of the messageV1 property.
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
     * Sets the value of the messageV1 property.
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
     * Gets the value of the messageV2 property.
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
     * Sets the value of the messageV2 property.
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
     * Gets the value of the messageV3 property.
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
     * Sets the value of the messageV3 property.
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
     * Gets the value of the messageV4 property.
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
     * Sets the value of the messageV4 property.
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
     * Gets the value of the parameter property.
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
     * Sets the value of the parameter property.
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
     * Gets the value of the row property.
     * 
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the value of the row property.
     * 
     */
    public void setRow(int value) {
        this.row = value;
    }

    /**
     * Gets the value of the field property.
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
     * Sets the value of the field property.
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
     * Gets the value of the system property.
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
     * Sets the value of the system property.
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
