package com.lsh.wms.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class MessageContext {

    private static final Logger logger = LoggerFactory.getLogger(MessageContext.class);

    private Map<String, String[]> parameterMap;
    private ClientInfo header;
    private Map<String, MultipartFileInfo> fileMap;

//    private InputStream inputStream;

    public MessageContext() {
        this.parameterMap = new HashMap<String, String[]>();
        this.header = new ClientInfo();
        this.fileMap = new HashMap<String, MultipartFileInfo>();
    }

    public MessageContext(Map<String, String[]> parameters, ClientInfo clientInfo,Map<String, MultipartFileInfo> fileMap) {
        this.parameterMap = parameters;
        this.header = clientInfo;
        this.fileMap = fileMap;
    }

    public ClientInfo getClientInfo(){
        if(header == null){
            return null;
        }
        return this.header;
    }

    public String getParameter(String name) {
        if (parameterMap == null) {
            return null;
        }
        String[] parameterValues = parameterMap.get(name);
        if (parameterValues != null && parameterValues.length > 0) {
            return parameterValues[0];
        }
        return null;
    }

    public Integer getParameterInt(String name) {
        try {
            return Integer.valueOf(getParameter(name));
        } catch (Exception ex) {
            // DO NOTHING
        }
        return null;
    }

    public Integer getParameterInt(String name, Integer defaultValue) {
        try {
            return Integer.valueOf(getParameter(name));
        } catch (Exception ex) {
            // DO NOTHING
        }
        return defaultValue;
    }

    public Long getParameterLong(String name) {
        try {
            return Long.valueOf(getParameter(name));
        } catch (Exception ex) {
            // DO NOTHING
        }
        return null;
    }

    public Long getParameterLong(String name, Long defaultValue) {
        try {
            return Long.valueOf(getParameter(name));
        } catch (Exception ex) {
            // DO NOTHING
        }
        return defaultValue;
    }

    public String[] getParameterValues(String name) {
        if (parameterMap == null) {
            return null;
        }
        return parameterMap.get(name);
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Map<String, MultipartFileInfo> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, MultipartFileInfo> fileMap) {
        this.fileMap = fileMap;
    }

    public MultipartFileInfo getFile(String fieldName) {
        if (fileMap == null) {
            return null;
        }
        return fileMap.get(fieldName);
    }

}
