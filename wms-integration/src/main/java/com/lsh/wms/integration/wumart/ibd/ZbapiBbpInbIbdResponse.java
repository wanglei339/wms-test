
package com.lsh.wms.integration.wumart.ibd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EfDelivery" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="ItInbDeliveryDetail" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBbpInbdD"/>
 *         &lt;element name="Return" type="{urn:sap-com:document:sap:soap:functions:mc-style}TableOfBapireturn"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "efDelivery",
    "itInbDeliveryDetail",
    "_return"
})
@XmlRootElement(name = "ZbapiBbpInbIbdResponse")
public class ZbapiBbpInbIbdResponse {

    @XmlElement(name = "EfDelivery", required = true)
    protected String efDelivery;
    @XmlElement(name = "ItInbDeliveryDetail", required = true)
    protected TableOfBbpInbdD itInbDeliveryDetail;
    @XmlElement(name = "Return", required = true)
    protected TableOfBapireturn _return;

    /**
     * Gets the value of the efDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEfDelivery() {
        return efDelivery;
    }

    /**
     * Sets the value of the efDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEfDelivery(String value) {
        this.efDelivery = value;
    }

    /**
     * Gets the value of the itInbDeliveryDetail property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBbpInbdD }
     *     
     */
    public TableOfBbpInbdD getItInbDeliveryDetail() {
        return itInbDeliveryDetail;
    }

    /**
     * Sets the value of the itInbDeliveryDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBbpInbdD }
     *     
     */
    public void setItInbDeliveryDetail(TableOfBbpInbdD value) {
        this.itInbDeliveryDetail = value;
    }

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link TableOfBapireturn }
     *     
     */
    public TableOfBapireturn getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableOfBapireturn }
     *     
     */
    public void setReturn(TableOfBapireturn value) {
        this._return = value;
    }

}
