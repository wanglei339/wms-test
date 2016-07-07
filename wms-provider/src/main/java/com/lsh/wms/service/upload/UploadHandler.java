package com.lsh.wms.service.upload;

import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.wms.model.upload.UploadForm;
import com.lsh.wms.core.constant.UploadConstant;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

/**
 * Created by xiaoma on 16/1/29.
 */
public class UploadHandler {

    private static Logger logger = LoggerFactory.getLogger(UploadHandler.class);

    public static void stream2File(InputStream in, String filePath) {
        try {
            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                FileUtils.deleteQuietly(targetFile);
            }
            File tmpFile = new File(targetFile.getAbsolutePath());
            FileUtils.copyInputStreamToFile(in, tmpFile);
            tmpFile.renameTo(targetFile);
        } catch (Exception e) {
            logger.error("写入文件失败：" + filePath, e);
        }
    }

    public static byte[] toByteArray(InputStream result) {
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

    /**
     * 通过图片URL先下载图片，然后上传到阿里
     * @return
     * @throws IOException
     */
    public static HashMap<String,Object> uploadFromUrl(UploadForm uploadForm) throws IOException {
        String picurl = uploadForm.getIcon_url();
        logger.info("uploadFromUrl picurl={}", picurl);
        if(StringUtils.isBlank(picurl))return null;
        String fileName = "";
        if(picurl.contains("@")){
            fileName = picurl.substring(picurl.lastIndexOf("/") + 1,picurl.lastIndexOf("@"));
            String[] urlarr = picurl.split("@");
            picurl = urlarr[0] + EncodeUtils.urlEncode(picurl.substring(picurl.lastIndexOf("@"), picurl.length()));
        }else{
            fileName = picurl.substring(picurl.lastIndexOf("/") + 1);
        }
        // todo 暂时不出来名字
        if(fileName.indexOf("\\.") == -1 ) fileName = fileName + ".png";
        byte[] bytes = HttpClientUtils.getByte(picurl);
        if(bytes == null){
            logger.info("download image failure!picurl={}",picurl);
            return null;
        }
        InputStream in = new ByteArrayInputStream(bytes);
        final String path_key = UploadConstant.getFilePath(uploadForm.getAppkey(), uploadForm.getSource(), uploadForm.getBusicode(), fileName);
        final String filename = fileName;
        String topath = UploadConstant.UPLOAD_PATH_ROOT + path_key;
        FileUtils.copyInputStreamToFile(in, new File(topath));
        return new HashMap<String,Object>(){{put("key", path_key);put("fileName", filename);}};
    }


}
