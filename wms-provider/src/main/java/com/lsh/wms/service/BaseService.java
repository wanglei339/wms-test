package com.lsh.wms.service;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RpcResult;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.model.ClientInfo;
import com.lsh.wms.model.MessageContext;
import com.lsh.wms.model.MultipartFileInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BaseService {

    private static Logger logger = LoggerFactory.getLogger(BaseService.class);



    /**
     * 获取request数据
     * @param request
     * @return
     */
    public MessageContext getMessageContext(HttpServletRequest request){
        MessageContext messageContext = null;
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        Map<String, MultipartFileInfo> fileMap = new HashMap<String, MultipartFileInfo>();
        ClientInfo clientInfo = getHeadParam(request);
        if(isMultipart(request)){
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            try {
                List<FileItem> fileItems = upload.parseRequest(request);
                for(FileItem fileItem : fileItems){
                    if(fileItem.isFormField()){
                        String value =fileItem.getString("UTF-8");
                        String[] curParam = parameterMap.get(fileItem.getFieldName());
                        if (curParam == null) {
                            parameterMap.put(fileItem.getFieldName(), new String[]{value});
                        } else {
                            String[] newParam = StringUtils.addStringToArray(curParam, value);
                            parameterMap.put(fileItem.getFieldName(), newParam);
                        }
                    }else {
                        fileMap.put(fileItem.getFieldName(), new MultipartFileInfo(fileItem));
                    }
                }
            } catch (Exception e) {
                logger.error("--获取参数异常--",e);
                return new MessageContext();
            }
        }else{
            parameterMap = request.getParameterMap();
        }
        messageContext = new MessageContext(parameterMap,clientInfo,fileMap);

        return messageContext;
    }

    /**
     * 获取header中数据
     * @param request
     * @return
     */
    protected ClientInfo getHeadParam(HttpServletRequest request){
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUid(ObjUtils.toLong(request.getHeader("uid")));
        clientInfo.setUtoken(ObjUtils.toString(request.getHeader("utoken")));
        clientInfo.setAppkey(ObjUtils.toString(request.getHeader("appkey")));
        clientInfo.setAppversion(ObjUtils.toString(request.getHeader("appversion")));
        clientInfo.setOstype(ObjUtils.toInteger(request.getHeader("ostype")));
        clientInfo.setRandom(ObjUtils.toString(request.getHeader("random")));
        clientInfo.setSign(ObjUtils.toString(request.getHeader("sign")));

        return clientInfo;
    }

    protected boolean isMultipart(HttpServletRequest request){
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        return isMultipart;
    }


    public  byte[] toByteArray(InputStream result) {
        if (result == null) {
            return new byte[]{};
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] byteBuffer = new byte[1024 * 8];
        int count = 0;
        try {
            while ((count = result.read(byteBuffer)) > 0) {
                baos.write(byteBuffer, 0, count);
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            logger.error("", ex);
        } finally {
            IOUtils.closeQuietly(baos);
        }
        return new byte[]{};
    }

}
