
package com.lsh.wms.integration.wumart.ibd;

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
@WebServiceClient(name = "ZMM_INB_IBD", targetNamespace = "urn:sap-com:document:sap:soap:functions:mc-style", wsdlLocation = "file:/home/work/aa.wsdl")
public class ZMMINBIBD_Service
    extends Service
{

    private final static URL ZMMINBIBD_WSDL_LOCATION;
    private final static WebServiceException ZMMINBIBD_EXCEPTION;
    private final static QName ZMMINBIBD_QNAME = new QName("urn:sap-com:document:sap:soap:functions:mc-style", "ZMM_INB_IBD");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/home/work/aa.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ZMMINBIBD_WSDL_LOCATION = url;
        ZMMINBIBD_EXCEPTION = e;
    }

    public ZMMINBIBD_Service() {
        super(__getWsdlLocation(), ZMMINBIBD_QNAME);
    }

    public ZMMINBIBD_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), ZMMINBIBD_QNAME, features);
    }

    public ZMMINBIBD_Service(URL wsdlLocation) {
        super(wsdlLocation, ZMMINBIBD_QNAME);
    }

    public ZMMINBIBD_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ZMMINBIBD_QNAME, features);
    }

    public ZMMINBIBD_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ZMMINBIBD_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ZMMINBIBD
     */
    @WebEndpoint(name = "binding")
    public ZMMINBIBD getBinding() {
        return super.getPort(new QName("urn:sap-com:document:sap:soap:functions:mc-style", "binding"), ZMMINBIBD.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ZMMINBIBD
     */
    @WebEndpoint(name = "binding")
    public ZMMINBIBD getBinding(WebServiceFeature... features) {
        return super.getPort(new QName("urn:sap-com:document:sap:soap:functions:mc-style", "binding"), ZMMINBIBD.class, features);
    }

    /**
     * 
     * @return
     *     returns ZMMINBIBD
     */
    @WebEndpoint(name = "binding_SOAP12")
    public ZMMINBIBD getBindingSOAP12() {
        return super.getPort(new QName("urn:sap-com:document:sap:soap:functions:mc-style", "binding_SOAP12"), ZMMINBIBD.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ZMMINBIBD
     */
    @WebEndpoint(name = "binding_SOAP12")
    public ZMMINBIBD getBindingSOAP12(WebServiceFeature... features) {
        return super.getPort(new QName("urn:sap-com:document:sap:soap:functions:mc-style", "binding_SOAP12"), ZMMINBIBD.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ZMMINBIBD_EXCEPTION!= null) {
            throw ZMMINBIBD_EXCEPTION;
        }
        return ZMMINBIBD_WSDL_LOCATION;
    }

}
