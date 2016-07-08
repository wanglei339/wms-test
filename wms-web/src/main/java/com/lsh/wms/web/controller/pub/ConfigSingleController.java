package com.lsh.wms.web.controller.pub;

import com.google.common.collect.Lists;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.pub.PubConfigData;
import com.lsh.wms.model.pub.PubConfigDataFile;
import com.lsh.wms.model.pub.PubConfigPage;
import com.lsh.wms.model.pub.PubConfigShow;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.UploadConstant;
import com.lsh.wms.core.service.pub.PubConfigService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 配置页面（单内容）管理
 */
@Controller
@RequestMapping("/pub/conf/single")
public class ConfigSingleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigSingleController.class);

    @Autowired
    private PubConfigService pubConfigService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String index() {
        return "pub/conf/single";
    }

    /**
     * 页面列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param query_name
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "query_name", required = false) String query_name,
            HttpServletResponse response) {

        // 分页显示
        Integer num = pubConfigService.countPubConfigPage(query_name, BusiConstant.CONFIG_PAGE_TYPE_SINGLE);
        List<PubConfigPage> list = pubConfigService.getPubConfigPageList(query_name, BusiConstant.CONFIG_PAGE_TYPE_SINGLE, start, limit);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", num); //total
        result.put("recordsFiltered", num); //totalAfterFilter
        result.put("data", list.toArray());
        setResContent2Json(response);
        return result;
    }

    /**
     * 页面编辑
     *
     * @param id
     * @return
     */
    @RequestMapping("/page/edit")
    public ModelAndView edit(@RequestParam(value = "id", required = false) Integer id) {
        PubConfigPage pubConfigPage = null;
        PubConfigData pubConfigData = null;
        PubConfigShow pubConfigShow = null;
        List<PubConfigDataFile> pubConfigDataFileList = null;
        if (id == null) {
            pubConfigPage = new PubConfigPage();
            pubConfigData = new PubConfigData();
            pubConfigShow = pubConfigService.getSinglePubConfigShow(null);
            pubConfigDataFileList = Lists.newArrayList();
        } else {
            pubConfigPage = pubConfigService.getPubConfigPageById(id);
            pubConfigData = pubConfigService.getPubConfigDataByPageId(id);
            if (pubConfigData == null) {
                pubConfigData = new PubConfigData();
                pubConfigData.setPageId(id);
            }
            pubConfigShow = pubConfigService.getSinglePubConfigShow(pubConfigPage.getId());
            pubConfigDataFileList = pubConfigService.getPubConfigDataFileListByDataId(pubConfigData.getId());
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("page", pubConfigPage);
        modelAndView.addObject("data", pubConfigData);
        modelAndView.addObject("show", pubConfigShow);
        modelAndView.addObject("fileList", pubConfigDataFileList);
        modelAndView.setViewName("pub/conf/single_edit");
        return modelAndView;
    }

    @RequestMapping("/img/upload")
    @ResponseBody
    public String imgUpload(@RequestParam("imgUpload") MultipartFile fileUpload,
                            HttpServletResponse response) {

        if (fileUpload == null || fileUpload.isEmpty()) {
            setResContent2Text(response);
            return map2JsonString(getFailMap("参数不能为空！"));
        }
        // 处理上传文件
        String oldFileName = fileUpload.getOriginalFilename();
        String savePath = UploadConstant.getFilePath(null, UploadConstant.FILE_SOURCE_INNER, null, oldFileName);
        stream2File(fileUpload, UploadConstant.UPLOAD_PATH_ROOT + savePath);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("filePath", savePath);
        result.put("fileName", oldFileName);
        result.put("fileUrl", UploadConstant.UPLOAD_SERVER_HOST + savePath);
        setResContent2Text(response);
        return map2JsonString(result);
    }

    /**
     * 页面保存
     *
     * @param pubConfigPage
     * @param response
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(PubConfigPage pubConfigPage,
                                    PubConfigData pubConfigData,
                                    @RequestParam("pic_ary") String picAryStr,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        pubConfigData.setId(ObjUtils.toInteger(request.getParameter("dataId")));
        List<PubConfigDataFile> dataFileList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(picAryStr)) {
            String[] picIdAry = StringUtils.split(picAryStr, ",");
            int order = 1;
            for (String picId : picIdAry) {
                PubConfigDataFile dataFile = new PubConfigDataFile();
                String filePath = request.getParameter("pic_path_" + picId);
                String fileName = request.getParameter("pic_name_" + picId);
                if (StringUtils.isNotEmpty(filePath)) {
                    dataFile.setFilePath(filePath);
                    dataFile.setFileName(fileName);
                }
                if (!picId.startsWith("new_")) {
                    dataFile.setId(ObjUtils.toInteger(picId));
                }
                dataFile.setFileOrder(order);
                dataFileList.add(dataFile);
                order++;
            }
        }
        // 保存页面
        pubConfigService.savePubConfigPage(pubConfigPage, pubConfigData, dataFileList);
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/online")
    @ResponseBody
    public Map<String, Object> online(@RequestParam("id") Integer id, HttpServletResponse response) {

        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(id);
        PubConfigData pubConfigData = pubConfigService.getPubConfigDataByPageId(id);
        if (pubConfigPage == null || pubConfigData == null) {
            setResContent2Json(response);
            return getFailMap("上线失败，页面数据为空！");
        }
        List<PubConfigDataFile> fileList = pubConfigService.getPubConfigDataFileListByDataId(pubConfigData.getId());
        pubConfigData.setFileList(fileList);
        pubConfigService.singlePageOnline(pubConfigPage, pubConfigData);
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/page/show/edit")
    public ModelAndView showEdit(@RequestParam("pageId") Integer pageId) {

        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(pageId);
        PubConfigShow pubConfigShow = pubConfigService.getSinglePubConfigShow(pageId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("page", pubConfigPage);
        modelAndView.addObject("show", pubConfigShow);
        modelAndView.setViewName("pub/conf/single_show_edit");
        return modelAndView;
    }

    @RequestMapping("/show/save")
    @ResponseBody
    public Map<String, Object> showSave(PubConfigShow pubConfigShow, HttpServletResponse response) {

        pubConfigService.savePubConfigShow(pubConfigShow);
        setResContent2Json(response);
        return getSuccessMap();
    }

}
