package com.lsh.wms.core.constant;

import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;


public class UploadConstant {

    /**
     * 最大上传文件大小
     */
    public static Integer MAX_UPLOAD_SIZE = PropertyUtils.getInt("maxChunksize");

    /**
     * 上传文件存储-根路径
     */
    public static String UPLOAD_PATH_ROOT = PropertyUtils.getString("upload.path.root");
    /**
     * 静态文件host
     */
    public static String UPLOAD_SERVER_HOST = PropertyUtils.getString("upload.server.host");
    /**
     * 文件来源，内部或外部，CMS 还是 APP
     */
    public static String FILE_SOURCE_INNER = "inner";
    public static String FILE_SOURCE_OUTER = "outer";

    /**
     * 上传文件存储路径
     *
     * @param fileName
     * @return
     */
    public static String getFilePath(String appkey,String source,String busicode, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotBlank(appkey)){
            sb.append(File.separator).append(appkey);
        }
        if(StringUtils.isNotBlank(source)){
            sb.append(File.separator).append(source);
        }
        if(StringUtils.isNotBlank(busicode)){
            sb.append(File.separator).append(busicode);
        }
        sb.append(File.separator).append(DateUtils.getDateFilePath(new Date()));
        sb.append(File.separator).append(RandomUtils.uuid2());
        String extName = "";
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            extName = fileName.substring(index);
            sb.append(extName.toLowerCase());
        }
        return sb.toString();
    }
}
