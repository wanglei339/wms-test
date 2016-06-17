package com.lsh.wms.web.controller.up;


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
import com.lsh.wms.web.controller.BaseController;
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
@RequestMapping("/up/ver")
public class VersionController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private UpAppService appService;
    @Autowired
    private UpChannelService channelService;
    @Autowired
    private UpModelService modelService;
    @Autowired
    private UpOpsystemService osService;
    @Autowired
    private UpPackageService packageService;
    @Autowired
    private UpVersionService versionService;

    @RequestMapping("")
    public ModelAndView index() {
        List<UpApp> appList = appService.getAllValidAppList();
        List<UpOpsystem> osList = osService.getAllValidOpsystemlList();
        List<UpChannel> chnList = channelService.getAllValidChannelList();
        List<UpModel> modList = modelService.getAllValidModelList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("appList", appList);
        modelAndView.addObject("osList", osList);
        modelAndView.addObject("chnList", chnList);
        modelAndView.addObject("modList", modList);
        modelAndView.setViewName("up/ver/ver");
        return modelAndView;
    }

    @RequestMapping(value = "/no/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> verNoList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "osId", required = false) Integer osId,
            HttpServletResponse response) {
        // 分页显示
        Integer num = versionService.countVersionNo(appId, osId);
        List<UpVersion> list = versionService.getVersionNoList(appId, osId, start, limit);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", num); //total
        result.put("recordsFiltered", num); //totalAfterFilter
        result.put("data", list.toArray());
        setResContent2Json(response);
        return result;
    }

    @RequestMapping(value = "/ver/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> verList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "osId", required = false) Integer osId,
            @RequestParam(value = "chnId", required = false) Integer chnId,
            @RequestParam(value = "modId", required = false) Integer modId,
            @RequestParam(value = "pkgType", required = false) Integer pkgType,
            @RequestParam(value = "verStr", required = false) String verStr,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletResponse response) {

        Integer total = versionService.countVersion(keyword, appId, osId, chnId, modId, pkgType, verStr);
        List<UpVersion> list = versionService.getVersionList(keyword, appId, osId, chnId, modId, pkgType, verStr, start, limit);
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", total);
        result.put("recordsFiltered", total);
        result.put("data", list.toArray());
        setResContent2Json(response);
        return result;
    }

    @RequestMapping("/pkg/query")
    @ResponseBody
    public Map<String, Object> pkgQuery(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "appId") Integer appId,
            @RequestParam(value = "osId") Integer osId,
            HttpServletResponse response) {

        List<UpPackage> pkgList = packageService.getValidPackageList(keyword, appId, osId);
        Map<String, Object> result = getSuccessMap();
        result.put("pkgList", pkgList);
        setResContent2Json(response);
        return result;
    }

    @RequestMapping("/page/new")
    public ModelAndView verNew(@RequestParam(value = "appId") Integer appId,
                               @RequestParam(value = "osId") Integer osId) {

        UpVersion version = new UpVersion();
        version.setUpType(1);
        version.setSilentDownload(0);
        version.setSilentInstall(0);
        version.setPromptUp(1);
        version.setPromptAlways(1);
        version.setAppId(appId);
        version.setOsId(osId);
        List<UpPackage> pkgList = packageService.getValidPackageList(null, appId, osId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ver", version);
        modelAndView.addObject("pkgList", pkgList);
        modelAndView.setViewName("up/ver/ver_new");
        return modelAndView;
    }

    @RequestMapping("/page/edit")
    public ModelAndView verEdit(@RequestParam(value = "verId") Long verId) {

        UpVersion version = versionService.getVersionById(verId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("up/ver/ver_edit");
        return modelAndView;
    }

    @RequestMapping("/page/edit/batch")
    public ModelAndView verEditBatch(@RequestParam(value = "appId") Integer appId,
                                     @RequestParam(value = "osId") Integer osId,
                                     @RequestParam(value = "verStr") String verStr) {

        UpVersion version = null;
        Integer verCount = 1;
        List<UpVersion> verList = versionService.getVersionList(appId, osId, verStr);
        if (verList == null || verList.isEmpty()) {
            version = new UpVersion();
            version.setAppId(appId);
            version.setOsId(osId);
            version.setVerStr(verStr);
        } else {
            version = verList.get(0);
            verCount = verList.size();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ver", version);
        modelAndView.addObject("verCount", verCount);
        modelAndView.setViewName("up/ver/ver_edit_batch");
        return modelAndView;
    }

    @RequestMapping("upload")
    @ResponseBody
    public String uploadVerFile(@RequestParam(value = "file") MultipartFile file,
                                HttpServletResponse response) {
        try {
            if (file == null || file.isEmpty()) {
                setResContent2Text(response);
                return map2JsonString(getFailMap("文件内容不能为空!"));
            }
            String fileName = file.getOriginalFilename();
            String filePath = UploadConstant.getFilePath(null, UploadConstant.FILE_SOURCE_INNER, "upgrade", fileName);
            String savePath = UploadConstant.UPLOAD_PATH_ROOT + filePath;
            logger.info("文件存储路径={}", savePath);
            stream2File(file.getInputStream(), savePath);
            Map<String, Object> result = getSuccessMap();
            String fileUrl = UploadConstant.UPLOAD_SERVER_HOST + filePath;
            result.put("filePath", filePath);
            result.put("fileUrl", fileUrl);
            setResContent2Text(response);
            return map2JsonString(result);
        } catch (Exception e) {
            logger.error("文件上传异常：", e);
            setResContent2Text(response);
            return map2JsonString(getFailMap("文件上传异常!"));
        }
    }

    @RequestMapping("/new/save")
    @ResponseBody
    public Map<String, Object> newSave(UpVersion version,
                                       @RequestParam(value = "pkgIdAryStr") String pkgIdAryStr,
                                       HttpServletResponse response) {

        if (StringUtils.isEmpty(pkgIdAryStr)) {
            setResContent2Json(response);
            return getFailMap("请选择应用包！");
        }
        Integer verInt = UpgradeConstant.verStr2Int(version.getVerStr());
        if (verInt == 0) {
            setResContent2Json(response);
            return getFailMap("版本号不符合规则(1~3位数字.1~3位数字.1~3位数字)！");
        }
        Long[] pkgIdAry = ObjUtils.string2LongAry(pkgIdAryStr, ",", 0L);
        List<String> msgList = Lists.newArrayList();
        for (Long pkgId : pkgIdAry) {
            if (versionService.existVersion(pkgId, version.getVerStr())) {
                UpPackage tmpPkg = packageService.getPackageById(pkgId);
                msgList.add(StrUtils.formatString("产品包[{0}({1})]的版本[{2}]已存在！", tmpPkg.getAppName(), tmpPkg.getAppKey(), version.getVerStr()));
                continue;
            }
            version.setId(null);
            version.setPkgId(pkgId);
            version.setVerInt(verInt);
            version.setFileStatus(UpgradeConstant.UP_FILE_STATUS_INIT);
            version.setStatus(BusiConstant.EFFECTIVE_NO);
            versionService.insert(version);
        }
        setResContent2Json(response);
        if (msgList.size() > 0) {
            // return getFailMap(StringUtils.join(msgList, "</br>"));
            return getSuccessMap("保存成功，部分产品包已存在。");
        }
        return getSuccessMap("保存成功。");
    }

    @RequestMapping("/edit/save")
    @ResponseBody
    public Map<String, Object> editSave(UpVersion version,
                                        HttpServletResponse response) {

        if (StringUtils.isBlank(version.getFilePath())) {
            version.setFilePath(null); //置为null，确保原有值不被更新成空串
            if (StringUtils.isNotBlank(version.getFileUrl())){
                version.setFileStatus(UpgradeConstant.UP_FILE_STATUS_SUCCESS);
            }
            versionService.update(version);
            setResContent2Json(response);
            return getSuccessMap();
        }
        String localPath = version.getFilePath();
        File upFile = new File(UploadConstant.UPLOAD_PATH_ROOT + localPath);
        String fileMd5 = EncodeUtils.fileMd5(upFile);
        if (StringUtils.isBlank(version.getFileMd5())
                || !version.getFileMd5().equalsIgnoreCase(fileMd5)) {
            logger.info("fileMd5={}", fileMd5);
            setResContent2Json(response);
            return getFailMap("文件MD5校验不匹配！");
        }
        version.setFilePath(localPath);
        String ossUrl = OssClientUtils.uploadFile(localPath, upFile);
        if (ossUrl == null) {
            // ossUrl = UploadConstant.UPLOAD_SERVER_HOST + localPath;
            version.setFileUrl(null);
            version.setFileStatus(UpgradeConstant.UP_FILE_STATUS_FAIL);
            versionService.update(version);
            setResContent2Json(response);
            return getFailMap("文件上传到CDN失败！");
        }
        version.setFileUrl(ossUrl);
        version.setFileStatus(UpgradeConstant.UP_FILE_STATUS_SUCCESS);
        versionService.update(version);
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/edit/batch/save")
    @ResponseBody
    public Map<String, Object> editBatchSave(UpVersion version,
                                        HttpServletResponse response) {

        if (version.getAppId() == null
                || version.getAppId() == null
                || StringUtils.isBlank(version.getVerStr())) {
            setResContent2Json(response);
            return getFailMap("参数错误！");
        }
        List<UpVersion> verList = versionService.getVersionList(version.getAppId(), version.getAppId(), version.getVerStr());
        if (verList == null || verList.isEmpty()) {
            setResContent2Json(response);
            return getFailMap("版本列表为空！");
        }
        for (UpVersion ver : verList) {
            version.setId(ver.getId());
            versionService.update(version);
        }
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/status/edit")
    @ResponseBody
    public Map<String, Object> statusEdit(@RequestParam(value = "verId") Long verId,
                                          HttpServletResponse response) {

        UpVersion version = versionService.getVersionById(verId);
        if (version.getStatus() == BusiConstant.EFFECTIVE_NO
            && version.getFileStatus() != UpgradeConstant.UP_FILE_STATUS_SUCCESS){
            setResContent2Json(response);
            return getFailMap("请上传升级文件或填写升级文件链接后再启用！");
        }
        UpVersion upVer = new UpVersion();
        upVer.setId(verId);
        if (version.getStatus() == BusiConstant.EFFECTIVE_YES) {
            upVer.setStatus(BusiConstant.EFFECTIVE_NO);
        } else {
            upVer.setStatus(BusiConstant.EFFECTIVE_YES);
        }
        versionService.update(upVer);
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/status/enable/batch")
    @ResponseBody
    public Map<String, Object> statusEnableBatch(@RequestParam(value = "appId") Integer appId,
                                                 @RequestParam(value = "osId") Integer osId,
                                                 @RequestParam(value = "verStr") String verStr,
                                                 HttpServletResponse response) {

        if (appId == null
                || osId == null
                || StringUtils.isBlank(verStr)) {
            setResContent2Json(response);
            return getFailMap("参数错误！");
        }
        List<UpVersion> verList = versionService.getVersionList(appId, osId, verStr);
        if (verList == null || verList.isEmpty()) {
            setResContent2Json(response);
            return getFailMap("版本列表为空！");
        }
        List<String> msgList = Lists.newArrayList();
        for(UpVersion ver: verList){
            if (ver.getStatus() == BusiConstant.EFFECTIVE_YES) {
                continue;
            }
            if (ver.getFileStatus() != UpgradeConstant.UP_FILE_STATUS_SUCCESS) {
                msgList.add(StrUtils.formatString("版本[{0}({1})]没有文件链接，不能启用。", ver.getPkgName(), ver.getAppKey()));
                continue;
            }
            UpVersion upVer = new UpVersion();
            upVer.setId(ver.getId());
            upVer.setStatus(BusiConstant.EFFECTIVE_YES);
            versionService.update(upVer);
        }
        setResContent2Json(response);
        if (msgList.size() > 0) {
            // return getFailMap(StringUtils.join(msgList, "</br>"));
            return getSuccessMap("启用成功，部分版本没有文件链接不能启用。");
        }
        return getSuccessMap("启用成功。");
    }

    @RequestMapping("/status/disable/batch")
    @ResponseBody
    public Map<String, Object> statusDisableBatch(@RequestParam(value = "appId") Integer appId,
                                                  @RequestParam(value = "osId") Integer osId,
                                                  @RequestParam(value = "verStr") String verStr,
                                                  HttpServletResponse response) {

        if (appId == null
                || osId == null
                || StringUtils.isBlank(verStr)) {
            setResContent2Json(response);
            return getFailMap("参数错误！");
        }
        List<UpVersion> verList = versionService.getVersionList(appId, osId, verStr);
        if (verList == null || verList.isEmpty()) {
            setResContent2Json(response);
            return getFailMap("版本列表为空！");
        }
        for(UpVersion ver: verList){
            if (ver.getStatus() == BusiConstant.EFFECTIVE_NO) {
                continue;
            }
            UpVersion upVer = new UpVersion();
            upVer.setId(ver.getId());
            upVer.setStatus(BusiConstant.EFFECTIVE_NO);
            versionService.update(upVer);
        }
        setResContent2Json(response);
        return getSuccessMap();
    }

}

