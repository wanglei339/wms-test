
package com.lsh.wms.integration.wumart.ibdback;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ZBAPI_GOODSMVT_CANCEL", targetNamespace = "urn:sap-com:document:sap:rfc:functions", wsdlLocation = "file:/home/work/wumart/ibdback.wsdl")
public class ZBAPIGOODSMVTCANCEL_Service
    extends Service
{

    private final static URL ZBAPIGOODSMVTCANCEL_WSDL_LOCATION;
    private final static WebServiceException ZBAPIGOODSMVTCANCEL_EXCEPTION;
    private final static QName ZBAPIGOODSMVTCANCEL_QNAME = new QName("urn:sap-com:document:sap:rfc:functions", "ZBAPI_GOODSMVT_CANCEL");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/home/work/wumart/ibdback.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ZBAPIGOODSMVTCANCEL_WSDL_LOCATION = url;
        ZBAPIGOODSMVTCANCEL_EXCEPTION = e;
    }

    public ZBAPIGOODSMVTCANCEL_Service() {
        super(__getWsdlLocation(), ZBAPIGOODSMVTCANCEL_QNAME);
    }

    public ZBAPIGOODSMVTCANCEL_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), ZBAPIGOODSMVTCANCEL_QNAME, features);
    }

    public ZBAPIGOODSMVTCANCEL_Service(URL wsdlLocation) {
        super(wsdlLocation, ZBAPIGOODSMVTCANCEL_QNAME);
    }

    public ZBAPIGOODSMVTCANCEL_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ZBAPIGOODSMVTCANCEL_QNAME, features);
    }

    public ZBAPIGOODSMVTCANCEL_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ZBAPIGOODSMVTCANCEL_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ZBAPIGOODSMVTCANCEL
     */
    @WebEndpoint(name = "binding")
    public ZBAPIGOODSMVTCANCEL getBinding() {
        return super.getPort(new QName("urn:sap-com:document:sap:rfc:functions", "binding"), ZBAPIGOODSMVTCANCEL.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ZBAPIGOODSMVTCANCEL
     */
    @WebEndpoint(name = "binding")
    public ZBAPIGOODSMVTCANCEL getBinding(WebServiceFeature... features) {
        return super.getPort(new QName("urn:sap-com:document:sap:rfc:functions", "binding"), ZBAPIGOODSMVTCANCEL.class, features);
    }

    /**
     * 
     * @return
     *     returns ZBAPIGOODSMVTCANCEL
     */
    @WebEndpoint(name = "binding_SOAP12")
    public ZBAPIGOODSMVTCANCEL getBindingSOAP12() {
        return super.getPort(new QName("urn:sap-com:document:sap:rfc:functions", "binding_SOAP12"), ZBAPIGOODSMVTCANCEL.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ZBAPIGOODSMVTCANCEL
     */
    @WebEndpoint(name = "binding_SOAP12")
    public ZBAPIGOODSMVTCANCEL getBindingSOAP12(WebServiceFeature... features) {
        return super.getPort(new QName("urn:sap-com:document:sap:rfc:functions", "binding_SOAP12"), ZBAPIGOODSMVTCANCEL.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ZBAPIGOODSMVTCANCEL_EXCEPTION!= null) {
            throw ZBAPIGOODSMVTCANCEL_EXCEPTION;
        }
        return ZBAPIGOODSMVTCANCEL_WSDL_LOCATION;
    }

}
