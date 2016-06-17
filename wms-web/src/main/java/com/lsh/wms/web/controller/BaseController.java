package com.lsh.wms.web.controller;

import com.google.common.collect.Maps;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.web.constant.MediaTypes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

public abstract class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected Map<String, Object> getSuccessMap() {
        Map<String, Object> result = Maps.newTreeMap();
        result.put("rsCode", "1");
        result.put("rsMsg", "成功！");
        return result;
    }

    protected Map<String, Object> getSuccessMap(String rsMsg) {
        Map<String, Object> result = Maps.newTreeMap();
        result.put("rsCode", "1");
        result.put("rsMsg", rsMsg);
        return result;
    }

    protected Map<String, Object> getFailMap(String rsMsg) {
        Map<String, Object> result = Maps.newTreeMap();
        result.put("rsCode", "0");
        result.put("rsMsg", rsMsg);
        return result;
    }

    protected void setResContent2Json(HttpServletResponse response) {
        response.setContentType(MediaTypes.JSON_UTF_8);
        response.setHeader("Cache-Control", "no-store");
    }

    protected void setResContent2Text(HttpServletResponse response) {
        response.setContentType(MediaTypes.TEXT_PLAIN_UTF_8);
        response.setHeader("Cache-Control", "no-store");
    }

    protected void stream2File(InputStream in, String filePath) {
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

    protected void stream2File(MultipartFile fileUpload, String filePath) {
        try {
            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                FileUtils.deleteQuietly(targetFile);
            }
            File tmpFile = new File(targetFile.getAbsolutePath());
            FileUtils.copyInputStreamToFile(fileUpload.getInputStream(), tmpFile);
            tmpFile.renameTo(targetFile);
        } catch (Exception e) {
            logger.error("写入文件失败：" + filePath, e);
        }
    }

    protected String map2JsonString(Map<String, Object> map) {
        return JsonUtils.obj2Json(map);
    }

    public void put(Map<String, Object> params, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return;
            }
        }
        params.put(key, value);
    }

}
