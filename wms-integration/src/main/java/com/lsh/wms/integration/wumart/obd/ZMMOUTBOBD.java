
package com.lsh.wms.integration.wumart.obd;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ZMM_OUTB_OBD", targetNamespace = "urn:sap-com:document:sap:soap:functions:mc-style")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ZMMOUTBOBD {


    /**
     * 
     * @param stockTransItems
     * @param noDequeue
     * @param _return
     * @param serialNumbers
     * @param shipPoint
     * @param extensionOut
     * @param debugFlg
     * @param createdItems
     * @param numDeliveries
     * @param delivery
     * @param dueDate
     * @param extensionIn
     * @param deliveries
     * @return
     *     returns com.lsh.wms.integration.wumart.obd.TableOfBapiret2
     */
    @WebMethod(operationName = "ZBapiOutbCreateObd")
    @WebResult(name = "Return", targetNamespace = "")
    @RequestWrapper(localName = "ZBapiOutbCreateObd", targetNamespace = "urn:sap-com:document:sap:soap:functions:mc-style", className = "com.lsh.wms.integration.wumart.obd.ZBapiOutbCreateObd")
    @ResponseWrapper(localName = "ZBapiOutbCreateObdResponse", targetNamespace = "urn:sap-com:document:sap:soap:functions:mc-style", className = "com.lsh.wms.integration.wumart.obd.ZBapiOutbCreateObdResponse")
    public TableOfBapiret2 zBapiOutbCreateObd(
        @WebParam(name = "CreatedItems", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TableOfBapidlvitemcreated> createdItems,
        @WebParam(name = "DebugFlg", targetNamespace = "")
        String debugFlg,
        @WebParam(name = "Deliveries", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TableOfBapishpdelivnumb> deliveries,
        @WebParam(name = "DueDate", targetNamespace = "")
        XMLGregorianCalendar dueDate,
        @WebParam(name = "ExtensionIn", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TableOfBapiparex> extensionIn,
        @WebParam(name = "ExtensionOut", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TableOfBapiparex> extensionOut,
        @WebParam(name = "NoDequeue", targetNamespace = "")
        String noDequeue,
        @WebParam(name = "Return", targetNamespace = "")
        TableOfBapiret2 _return,
        @WebParam(name = "SerialNumbers", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TableOfBapidlvserialnumber> serialNumbers,
        @WebParam(name = "ShipPoint", targetNamespace = "")
        String shipPoint,
        @WebParam(name = "StockTransItems", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TableOfBapidlvreftosto> stockTransItems,
        @WebParam(name = "Delivery", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> delivery,
        @WebParam(name = "NumDeliveries", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> numDeliveries);

}
