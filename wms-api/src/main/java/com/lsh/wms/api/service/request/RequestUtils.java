package com.lsh.wms.api.service.request;

import com.alibaba.dubbo.rpc.RpcContext;
import com.google.common.io.CharStreams;
import com.lsh.base.common.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/19
 * Time: 16/7/19.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.request.
 * desc:类功能描述
 */
public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static Map<String, Object> getRequest() {
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        Map<String, Object> requestMap = new HashMap<String, Object>();
        Map<String, String[]> paramMap = new HashMap<String, String[]>();

        String contentType = request.getContentType();
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // Map<String, MultipartFileInfo> fileMap = new HashMap<String, MultipartFileInfo>();  // TODO: 16/7/30  文件上传需求再改
            if(isMultipart(request)){
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                try {
                    List<FileItem> fileItems = upload.parseRequest(request);
                    for(FileItem fileItem : fileItems){
                        if(fileItem.isFormField()){
                            String value =fileItem.getString("UTF-8");
                            String[] curParam = paramMap.get(fileItem.getFieldName());
                            if (curParam == null) {
                                paramMap.put(fileItem.getFieldName(), new String[]{value});
                            } else {
                                String[] newParam = StringUtils.addStringToArray(curParam, value);
                                paramMap.put(fileItem.getFieldName(), newParam);
                            }
                        }else {
                            requestMap.put(fileItem.getFieldName(), new MultipartFileInfo(fileItem));
                        }
                    }
                } catch (Exception e) {
                    logger.error("--获取参数异常--",e);
                    return  requestMap;
                }
            }else if(contentType.contains("application/json")){
                String req = null;
                try{
                    req = CharStreams.toString(request.getReader());
                    logger.debug(req);
                    requestMap = JsonUtils.json2Obj(req, Map.class);
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }else {
                paramMap = request.getParameterMap();
            }
        } else {
            paramMap = request.getParameterMap();
        }

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            logger.debug("key= " + entry.getKey() + " and value= " + entry.getValue());
            String[] parameterValues = entry.getValue();
            if (parameterValues != null && parameterValues.length > 0) {
                requestMap.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        return requestMap;
    }

    public static HttpSession getSession() {
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        return request.getSession();
    }

    public static String getHeader(String key) {
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        return request.getHeader(key);
    }

    public static Cookie[] getCookie() {
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        return request.getCookies();
    }

    protected static boolean isMultipart(HttpServletRequest request){
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        return isMultipart;
    }

    public static void destroySession(){
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        request.getSession().invalidate();
    }
}
