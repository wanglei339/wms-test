package com.lsh.wms.core.service.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 2016/11/15.
 */
public class HttpUtils {

    private static Log log = LogFactory.getLog(HttpUtils.class);

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url         请求的URL地址
     * @param params    请求的查询参数,可以为null
     * @return 返回请求响应的HTML
     */
    public static String doPostByForm(String url, Map<String, Object> params) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        //设置Http Post数据
        if (params != null) {
            try {
                method.setRequestEntity(new MultipartRequestEntity(getParts(params), method.getParams()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (true)
                        response.append(line).append(System.getProperty("line.separator"));
                    else
                        response.append(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }
    public static Part[] getParts(Map<String,Object> params) {
        List<Part> partList = new ArrayList<Part>();
        for(String key:params.keySet()){
            partList.add(new StringPart(key, JSON.toJSONString(params.get(key)),"UTF-8"));
        }
        int size = partList.size();
        return partList.toArray(new Part[size]);
    }
}
