
package com.lsh.wms.integration.wumart.account;

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
 *         &lt;element name="MaterialGeneralData" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapimatdoa"/>
 *         &lt;element name="Materialplantdata" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapimatdoc"/>
 *         &lt;element name="Materialvaluationdata" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapimatdobew"/>
 *         &lt;element name="Return" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapireturn"/>
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
    "materialGeneralData",
    "materialplantdata",
    "materialvaluationdata",
    "_return"
})
@XmlRootElement(name = "MaterialGetDetailResponse")
public class MaterialGetDetailResponse {

    @XmlElement(name = "MaterialGeneralData", required = true)
    protected Bapimatdoa materialGeneralData;
    @XmlElement(name = "Materialplantdata", required = true)
    protected Bapimatdoc materialplantdata;
    @XmlElement(name = "Materialvaluationdata", required = true)
    protected Bapimatdobew materialvaluationdata;
    @XmlElement(name = "Return", required = true)
    protected Bapireturn _return;

    /**
     * Gets the value of the materialGeneralData property.
     * 
     * @return
     *     possible object is
     *     {@link Bapimatdoa }
     *     
     */
    public Bapimatdoa getMaterialGeneralData() {
        return materialGeneralData;
    }

    /**
     * Sets the value of the materialGeneralData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bapimatdoa }
     *     
     */
    public void setMaterialGeneralData(Bapimatdoa value) {
        this.materialGeneralData = value;
    }

    /**
     * Gets the value of the materialplantdata property.
     * 
     * @return
     *     possible object is
     *     {@link Bapimatdoc }
     *     
     */
    public Bapimatdoc getMaterialplantdata() {
        return materialplantdata;
    }

    /**
     * Sets the value of the materialplantdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bapimatdoc }
     *     
     */
    public void setMaterialplantdata(Bapimatdoc value) {
        this.materialplantdata = value;
    }

    /**
     * Gets the value of the materialvaluationdata property.
     * 
     * @return
     *     possible object is
     *     {@link Bapimatdobew }
     *     
     */
    public Bapimatdobew getMaterialvaluationdata() {
        return materialvaluationdata;
    }

    /**
     * Sets the value of the materialvaluationdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bapimatdobew }
     *     
     */
    public void setMaterialvaluationdata(Bapimatdobew value) {
        this.materialvaluationdata = value;
    }

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link Bapireturn }
     *     
     */
    public Bapireturn getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bapireturn }
     *     
     */
    public void setReturn(Bapireturn value) {
        this._return = value;
    }

}
