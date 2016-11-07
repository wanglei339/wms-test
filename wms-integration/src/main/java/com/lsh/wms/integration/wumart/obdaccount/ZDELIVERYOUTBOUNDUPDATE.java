
package com.lsh.wms.integration.wumart.obdaccount;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Z_DELIVERY_OUTBOUND_UPDATE", targetNamespace = "urn:sap-com:document:sap:rfc:functions")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ZDELIVERYOUTBOUNDUPDATE {


    /**
     * 
     * @param pZIMPORT
     * @param pZEXPORT
     * @param itemCONTROL
     * @param itemDATA
     * @param vbpokTAB
     * @param _return
     * @param prot
     * @param return1
     * @return
     *     returns com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2
     */
    @WebMethod(operationName = "Z_DELIVERY_OUTBOUND_UPDATE")
    @WebResult(name = "RETURN", targetNamespace = "")
    @RequestWrapper(localName = "Z_DELIVERY_OUTBOUND_UPDATE", targetNamespace = "urn:sap-com:document:sap:rfc:functions", className = "com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYOUTBOUNDUPDATE_Type")
    @ResponseWrapper(localName = "Z_DELIVERY_OUTBOUND_UPDATEResponse", targetNamespace = "urn:sap-com:document:sap:rfc:functions", className = "com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYOUTBOUNDUPDATEResponse")
    public TABLEOFBAPIRET2 zDELIVERYOUTBOUNDUPDATE(
        @WebParam(name = "ITEM_CONTROL", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFBAPIOBDLVITEMCTRLCHG> itemCONTROL,
        @WebParam(name = "ITEM_DATA", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFBAPIOBDLVITEMCHG> itemDATA,
        @WebParam(name = "PROT", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFPROTT> prot,
        @WebParam(name = "P_ZEXPORT", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFZDELIVERYEXPORT> pZEXPORT,
        @WebParam(name = "P_ZIMPORT", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFZDELIVERYIMPORT> pZIMPORT,
        @WebParam(name = "RETURN", targetNamespace = "")
        TABLEOFBAPIRET2 _return,
        @WebParam(name = "RETURN1", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFBAPIRET2> return1,
        @WebParam(name = "VBPOK_TAB", targetNamespace = "", mode = WebParam.Mode.INOUT)
        Holder<TABLEOFVBPOK> vbpokTAB);

}
