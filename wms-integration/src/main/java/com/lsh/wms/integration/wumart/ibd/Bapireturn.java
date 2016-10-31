
package com.lsh.wms.integration.wumart.ibd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bapireturn complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bapireturn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Code" type="{urn:sap-com:document:sap:rfc:functions}char5"/>
 *         &lt;element name="Message" type="{urn:sap-com:document:sap:rfc:functions}char220"/>
 *         &lt;element name="LogNo" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="LogMsgNo" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="MessageV1" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MessageV2" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MessageV3" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="MessageV4" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapireturn", propOrder = {
    "type",
    "code",
    "message",
    "logNo",
    "logMsgNo",
    "messageV1",
    "messageV2",
    "messageV3",
    "messageV4"
})
public class Bapireturn {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Code", required = true)
    protected String code;
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
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
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

}
