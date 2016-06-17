package com.lsh.wms.web.controller;


import com.google.common.collect.Lists;
import com.lsh.base.ali.OssClientUtils;
import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.up.*;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.UpgradeConstant;
import com.lsh.wms.core.constant.UploadConstant;
import com.lsh.wms.core.service.up.*;
import com.lsh.wms.web.constant.MediaTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/util")
public class UtilController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UtilController.class);

    @RequestMapping("upload/file")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "busicode", required = false) String busicode,
                                HttpServletResponse response) {
        try {
            if (file == null || file.isEmpty()) {
                setResContent2Text(response);
                return map2JsonString(getFailMap("文件内容不能为空!"));
            }
            String fileName = file.getOriginalFilename();
            String filePath = UploadConstant.getFilePath(appkey, UploadConstant.FILE_SOURCE_INNER, busicode, fileName);
            String savePath = UploadConstant.UPLOAD_PATH_ROOT + filePath;
            logger.info("文件存储路径={}", savePath);
            stream2File(file.getInputStream(), savePath);
            Map<String, Object> result = getSuccessMap();
            String fileUrl = UploadConstant.UPLOAD_SERVER_HOST + filePath;
            result.put("filePath", filePath);
            result.put("fileUrl", fileUrl);
            logger.info("fileUrl={}",fileUrl);
            setResContent2Text(response);
            return map2JsonString(result);
        } catch (Exception e) {
            logger.error("文件上传异常：", e);
            setResContent2Text(response);
            return map2JsonString(getFailMap("文件上传异常!"));
        }
    }


}

