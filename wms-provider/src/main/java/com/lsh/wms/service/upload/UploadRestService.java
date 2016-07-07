package com.lsh.wms.service.upload;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.upload.UploadForm;
import com.lsh.wms.api.service.upload.IUploadRestService;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Created by xiaoma on 16/1/29.
 */
@Service(protocol = "rest")
@Path("upload")
public class UploadRestService implements IUploadRestService {

    @Autowired
    private UploadService uploadService;

    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }
    @POST
    @Path("fileUpload")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.MULTIPART_FORM_DATA})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fileUpload() {
        HttpServletRequest request = (HttpServletRequest) RpcContext.getContext().getRequest();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        UploadForm uploadForm = new UploadForm();
        if(isMultipart){
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            // Parse the request
            try {
                FileItemIterator iter = upload.getItemIterator(request);
                byte[] fileByte =null;
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    if (item.isFormField()) {
                        System.out.println("Form field " + name );
                        if(name.equals("type"))
                            uploadForm.setType(ObjUtils.toInteger(Streams.asString(stream)));
                        if(name.equals("appkey"))
                            uploadForm.setAppkey(Streams.asString(stream));
                        if(name.equals("source"))
                            uploadForm.setSource(Streams.asString(stream));
                        if(name.equals("busicode"))
                            uploadForm.setBusicode(Streams.asString(stream));
                    } else {
                        System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
                        //System.out.println(Streams.asString(stream));
                        uploadForm.setName(item.getName());
                        fileByte = UploadHandler.toByteArray(stream);
                    }
                }
                return uploadService.fileUpload(uploadForm,fileByte);
            } catch (Exception e) {
                e.printStackTrace();
                return JsonUtils.EXCEPTION_ERROR();
            }
        }else{
            String type = request.getParameter("type");
            uploadForm.setType(ObjUtils.toInteger(type));
            String appkey = request.getParameter("appkey");
            uploadForm.setAppkey(appkey);
            String source = request.getParameter("source");
            uploadForm.setSource(source);
            String busicode = request.getParameter("busicode");
            uploadForm.setBusicode(busicode);
            String icon_url = request.getParameter("icon_url");
            uploadForm.setIcon_url(icon_url);
            String img_base64 = request.getParameter("img_base64");
            uploadForm.setImg_base64(img_base64);
            if(type == null || appkey==null || source==null || busicode==null || icon_url==null){
                return JsonUtils.PARAMETER_ERROR();
            }
            return uploadService.urlUpload(uploadForm);
        }

    }

}
