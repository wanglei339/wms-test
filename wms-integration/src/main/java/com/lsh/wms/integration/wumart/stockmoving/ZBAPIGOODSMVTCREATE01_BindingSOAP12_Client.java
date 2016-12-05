
package com.lsh.wms.integration.wumart.stockmoving;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.3
 * 2016-12-01T16:06:25.208+08:00
 * Generated source version: 3.0.3
 * 
 */
public final class ZBAPIGOODSMVTCREATE01_BindingSOAP12_Client {

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:soap:functions:mc-style", "ZBAPI_GOODSMVT_CREATE_01");

    private ZBAPIGOODSMVTCREATE01_BindingSOAP12_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = ZBAPIGOODSMVTCREATE01_Service.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ZBAPIGOODSMVTCREATE01_Service ss = new ZBAPIGOODSMVTCREATE01_Service(wsdlURL, SERVICE_NAME);
        ZBAPIGOODSMVTCREATE01 port = ss.getBindingSOAP12();  
        
        {
        System.out.println("Invoking zbapiGoodsmvtCreate01...");
        TableOfBapiparex _zbapiGoodsmvtCreate01_extensioninVal = null;
        javax.xml.ws.Holder<TableOfBapiparex> _zbapiGoodsmvtCreate01_extensionin = new javax.xml.ws.Holder<TableOfBapiparex>(_zbapiGoodsmvtCreate01_extensioninVal);
        Bapi2017GmCode _zbapiGoodsmvtCreate01_goodsmvtCode = null;
        Bapi2017GmHead01 _zbapiGoodsmvtCreate01_goodsmvtHeader = null;
        TableOfBapi2017GmItemCreate _zbapiGoodsmvtCreate01_goodsmvtItemVal = null;
        javax.xml.ws.Holder<TableOfBapi2017GmItemCreate> _zbapiGoodsmvtCreate01_goodsmvtItem = new javax.xml.ws.Holder<TableOfBapi2017GmItemCreate>(_zbapiGoodsmvtCreate01_goodsmvtItemVal);
        TableOfCwmBapi2017GmItemCreate _zbapiGoodsmvtCreate01_goodsmvtItemCwmVal = null;
        javax.xml.ws.Holder<TableOfCwmBapi2017GmItemCreate> _zbapiGoodsmvtCreate01_goodsmvtItemCwm = new javax.xml.ws.Holder<TableOfCwmBapi2017GmItemCreate>(_zbapiGoodsmvtCreate01_goodsmvtItemCwmVal);
        SpeBapi2017GmRefEwm _zbapiGoodsmvtCreate01_goodsmvtRefEwm = null;
        TableOfBapi2017GmSerialnumber _zbapiGoodsmvtCreate01_goodsmvtSerialnumberVal = null;
        javax.xml.ws.Holder<TableOfBapi2017GmSerialnumber> _zbapiGoodsmvtCreate01_goodsmvtSerialnumber = new javax.xml.ws.Holder<TableOfBapi2017GmSerialnumber>(_zbapiGoodsmvtCreate01_goodsmvtSerialnumberVal);
        TableOfSpeBapi2017ServicepartData _zbapiGoodsmvtCreate01_goodsmvtServPartDataVal = null;
        javax.xml.ws.Holder<TableOfSpeBapi2017ServicepartData> _zbapiGoodsmvtCreate01_goodsmvtServPartData = new javax.xml.ws.Holder<TableOfSpeBapi2017ServicepartData>(_zbapiGoodsmvtCreate01_goodsmvtServPartDataVal);
        TableOfBapiret2 _zbapiGoodsmvtCreate01__returnVal = null;
        javax.xml.ws.Holder<TableOfBapiret2> _zbapiGoodsmvtCreate01__return = new javax.xml.ws.Holder<TableOfBapiret2>(_zbapiGoodsmvtCreate01__returnVal);
        String _zbapiGoodsmvtCreate01_testrun = "";
        javax.xml.ws.Holder<Bapi2017GmHeadRet> _zbapiGoodsmvtCreate01_goodsmvtHeadret = new javax.xml.ws.Holder<Bapi2017GmHeadRet>();
        javax.xml.ws.Holder<String> _zbapiGoodsmvtCreate01_matdocumentyear = new javax.xml.ws.Holder<String>();
        javax.xml.ws.Holder<String> _zbapiGoodsmvtCreate01_materialdocument = new javax.xml.ws.Holder<String>();
        port.zbapiGoodsmvtCreate01(_zbapiGoodsmvtCreate01_extensionin, _zbapiGoodsmvtCreate01_goodsmvtCode, _zbapiGoodsmvtCreate01_goodsmvtHeader, _zbapiGoodsmvtCreate01_goodsmvtItem, _zbapiGoodsmvtCreate01_goodsmvtItemCwm, _zbapiGoodsmvtCreate01_goodsmvtRefEwm, _zbapiGoodsmvtCreate01_goodsmvtSerialnumber, _zbapiGoodsmvtCreate01_goodsmvtServPartData, _zbapiGoodsmvtCreate01__return, _zbapiGoodsmvtCreate01_testrun, _zbapiGoodsmvtCreate01_goodsmvtHeadret, _zbapiGoodsmvtCreate01_matdocumentyear, _zbapiGoodsmvtCreate01_materialdocument);

        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_extensionin=" + _zbapiGoodsmvtCreate01_extensionin.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_goodsmvtItem=" + _zbapiGoodsmvtCreate01_goodsmvtItem.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_goodsmvtItemCwm=" + _zbapiGoodsmvtCreate01_goodsmvtItemCwm.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_goodsmvtSerialnumber=" + _zbapiGoodsmvtCreate01_goodsmvtSerialnumber.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_goodsmvtServPartData=" + _zbapiGoodsmvtCreate01_goodsmvtServPartData.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01__return=" + _zbapiGoodsmvtCreate01__return.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_goodsmvtHeadret=" + _zbapiGoodsmvtCreate01_goodsmvtHeadret.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_matdocumentyear=" + _zbapiGoodsmvtCreate01_matdocumentyear.value);
        System.out.println("zbapiGoodsmvtCreate01._zbapiGoodsmvtCreate01_materialdocument=" + _zbapiGoodsmvtCreate01_materialdocument.value);

        }

        System.exit(0);
    }

}
