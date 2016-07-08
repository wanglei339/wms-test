package com.lsh.wms.service.upload;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.ali.OssClientUtils;
import com.lsh.base.common.json.JsonUtils2;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.upload.UploadForm;
import com.lsh.wms.api.service.upload.IUploadService;
import com.lsh.wms.core.constant.UploadConstant;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

/**
 * Created by xiaoma on 16/1/29.
 */
@Service(protocol = "dubbo")
public class UploadService implements IUploadService {
    private static Logger logger = LoggerFactory.getLogger(UploadService.class);

    private final  Integer IMGETYPE = 1; //图片文件
    private final  Integer FILETYPE = 2; //其他文件
    private final  Integer THIRDTYPE = 3; //第三方url地址
    private final  Integer IMGESTRTYPE = 4; //图片img_base64

    /**
     * 文件上传
     * @param uploadForm
     * @return
     */
    public String fileUpload(UploadForm uploadForm,byte[] item) {

        try{
            Integer type = uploadForm.getType();
            String appkey = uploadForm.getAppkey();
            String source = uploadForm.getSource();
            String busicode = uploadForm.getBusicode();
            if(type == null || appkey==null || source==null || busicode==null){
                return JsonUtils2.PARAMETER_ERROR();
            }

            //name
            String name = uploadForm.getName();
            if(item ==null){
                return JsonUtils2.PARAMETER_ERROR();
            }
            //file
            //InputStream fileInputStream = item.openStream();
            HashMap<String,Object> data = new HashMap<String, Object>();
            String filePath = "";
            String url = null;
            //String md5 = null;
            if(IMGETYPE.intValue() == ObjUtils.toInteger(type).intValue()){
                if(name ==null ){
                    return JsonUtils2.PARAMETER_ERROR();
                }
                filePath = UploadConstant.getFilePath(appkey, source, busicode, name);

                if(filePath != null) FileUtils.writeByteArrayToFile(new File(UploadConstant.UPLOAD_PATH_ROOT + filePath),item);
                //UploadHandler.stream2File(fileInputStream, UploadConstant.UPLOAD_PATH_ROOT + filePath);
                // 上传到阿里云oss
                url = OssClientUtils.uploadImage(filePath, new File(UploadConstant.UPLOAD_PATH_ROOT + filePath));
            }else if(FILETYPE.intValue() == ObjUtils.toInteger(type).intValue()){
                if(name ==null){
                    return JsonUtils2.PARAMETER_ERROR();
                }
                filePath += UploadConstant.getFilePath(appkey,source,busicode,name);
                if(filePath != null) FileUtils.writeByteArrayToFile(new File(UploadConstant.UPLOAD_PATH_ROOT + filePath),item);
                //UploadHandler.stream2File(fileInputStream, UploadConstant.UPLOAD_PATH_ROOT + filePath);
                // 上传到阿里云oss
                url = OssClientUtils.uploadFile(filePath, new File(UploadConstant.UPLOAD_PATH_ROOT + filePath));
            }
            data.put("fileUrl",url);
            data.put("filePath",filePath);
            data.put("name",name);

            if(data.isEmpty()) return JsonUtils2.EXCEPTION_ERROR();
            return JsonUtils2.SUCCESS(data);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtils2.EXCEPTION_ERROR();
        }
    }
    public String urlUpload(UploadForm uploadForm) {
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(File.separator).append(uploadForm.getAppkey());
            sb.append(File.separator).append(uploadForm.getSource());
            sb.append(File.separator).append(uploadForm.getBusicode());

            //name
            String name = null;
            String filePath = "";
            String url = null;
            HashMap<String,Object> result = new HashMap<String, Object>();

            if(THIRDTYPE.intValue() == uploadForm.getType().intValue()){
                if(uploadForm.getIcon_url() == null){
                    return JsonUtils2.PARAMETER_ERROR();
                }
                HashMap<String,Object> res = UploadHandler.uploadFromUrl(uploadForm);
                name = ObjectUtils.toString(res.get("fileName"));
                filePath = ObjectUtils.toString(res.get("key"));
                url = OssClientUtils.uploadImage(filePath, new File(UploadConstant.UPLOAD_PATH_ROOT + filePath));
            }else if(IMGESTRTYPE.intValue() == uploadForm.getType().intValue()){
                if(uploadForm.getIcon_url() == null){
                    return JsonUtils2.PARAMETER_ERROR();
                }
                // todo 暂时先不处理

            }
            result.put("fileUrl",url);
            result.put("filePath",filePath);
            result.put("name",name);

            if(result.isEmpty()) return JsonUtils2.EXCEPTION_ERROR();
            return JsonUtils2.SUCCESS(result);
        }catch (Exception e){
            return JsonUtils2.EXCEPTION_ERROR();
        }
    }


}
