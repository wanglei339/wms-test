package com.lsh.wms.integration.service.Demo;

import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.util.*;
import com.alibaba.fastjson.JSON;

public class Demo {
    final static String url = "http://115.182.215.119",
              db = "lsh-odoo-test",
        username = "yg-rd@lsh123.com",
        password = "YgRd@Lsh123",
        uid = "7";

    public static void main(String[] args) {
        try {
            
            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                }});
            }};
            final Object[] ret  = (Object[])models.execute("execute_kw", Arrays.asList(
                db, uid, password,
                "res.partner", "search_read",
                Arrays.asList(Arrays.asList(
                    Arrays.asList("is_company", "=", true),
                    Arrays.asList("supplier", "=", true),
                    Arrays.asList("category_id", "=", false)
                )),
                new HashMap<String, Object>() {{
                    put("fields", Arrays.asList("name", "ref", "phone", "comment"));
                    put("limit", 1);
                }}
            ));
            System.out.println(JSON.toJSONString(ret));

            final Object[] ret1  = (Object[])models.execute("execute_kw", Arrays.asList(
                db, uid, password,
                "product.product", "search_read",
                Arrays.asList(Arrays.asList(
                    Arrays.asList("sale_ok", "=", true),
                    Arrays.asList("purchase_ok", "=", true),
                    Arrays.asList("type", "=", "product"),
                    Arrays.asList("default_code", "!=", false)
                )),
                new HashMap<String, Object>() {{
                    put("limit", 1);
                    put("fields", Arrays.asList("name", "barcode", "default_code"/*, "taxes_id", "supplier_taxes_id"*/));
                }}
            ));
            System.out.println(JSON.toJSONString(ret1));

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
}


/*

javac -cp '.:/Users/ne/workspace/java/org-apache-xmlrpc.jar:/Users/ne/workspace/java/xmlrpc-common-3.1.3.jar:/Users/ne/workspace/java/ws-commons-util-1.0.2.jar:/Users/ne/workspace/java/fastjson-1.2.13.jar' Demo.java


java -cp '.:/Users/ne/workspace/java/org-apache-xmlrpc.jar:/Users/ne/workspace/java/xmlrpc-common-3.1.3.jar:/Users/ne/workspace/java/ws-commons-util-1.0.2.jar:/Users/ne/workspace/java/fastjson-1.2.13.jar' Demo
*/
