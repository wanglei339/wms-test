package com.lsh.wms.web.controller.pub;

import com.google.common.collect.Lists;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.pub.*;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.UploadConstant;
import com.lsh.wms.core.service.pub.PubConfigService;
import com.lsh.wms.core.service.pub.PubDictService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 配置页面（多内容）管理
 */
@Controller
@RequestMapping("/pub/conf/mult")
public class ConfigMultController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigMultController.class);

    @Autowired
    private PubConfigService pubConfigService;
    @Autowired
    private PubDictService pubDictService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String index() {
        return "pub/conf/mult_page";
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
    @RequestMapping(value = "/pg/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> pageList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "query_name", required = false) String query_name,
            HttpServletResponse response) {

        // 分页显示
        Integer num = pubConfigService.countPubConfigPage(query_name, BusiConstant.CONFIG_PAGE_TYPE_MULT);
        List<PubConfigPage> list = pubConfigService.getPubConfigPageList(query_name, BusiConstant.CONFIG_PAGE_TYPE_MULT, start, limit);
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
    @RequestMapping("/page/pg/edit")
    public ModelAndView pageEdit(@RequestParam(value = "id", required = false) Integer id) {
        PubConfigPage pubConfigPage = null;
        boolean isNew = false;
        if (id == null) {
            pubConfigPage = new PubConfigPage();
            isNew = true;
        } else {
            pubConfigPage = pubConfigService.getPubConfigPageById(id);
        }
        List<PubDict> dictList = pubDictService.getValidDictList(BusiConstant.DICT_TYPE_LIST);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("isNew", isNew);
        modelAndView.addObject("page", pubConfigPage);
        modelAndView.addObject("dictList", dictList);
        modelAndView.setViewName("pub/conf/mult_page_edit");
        return modelAndView;
    }

    /**
     * 页面保存
     *
     * @param pubConfigPage
     * @param response
     * @return
     */
    @RequestMapping("/pg/save")
    @ResponseBody
    public Map<String, Object> pageSave(PubConfigPage pubConfigPage,
                                        HttpServletResponse response) {

        if (pubConfigPage.getId() == null) {
            pubConfigPage.setPageType(BusiConstant.CONFIG_PAGE_TYPE_MULT);
            pubConfigPage.setStatus(BusiConstant.EFFECTIVE_YES);
            pubConfigPage.setCreatedTime(new Date());
            pubConfigService.insertPubConfigPage(pubConfigPage);
        } else {
            pubConfigPage.setUpdatedTime(new Date());
            pubConfigService.updatePubConfigPage(pubConfigPage);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    /**
     * 数据列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param query_name
     * @param response
     * @return
     */
    @RequestMapping(value = "/item/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> itemList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "pageId") Integer pageId,
            @RequestParam(value = "query_name", required = false) String query_name,
            @RequestParam(value = "query_contype", required = false) String query_contype,
            HttpServletResponse response) {

        // 分页显示
        Integer num = pubConfigService.countPubConfigData(pageId, query_name, query_contype);
        List<PubConfigData> list = pubConfigService.getPubConfigDataList(pageId, query_name, query_contype, start, limit);
        // 转换数据类型
        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(pageId);
        if (pubConfigPage != null && list != null) {
            Integer did = ObjUtils.toInteger(pubConfigPage.getConTypeCode());
            Map<String, PubDictItem> itemMap = pubDictService.getValidDictItemMapByDid(did);
            if (itemMap != null) {
                for (PubConfigData data : list) {
                    PubDictItem item = itemMap.get(ObjUtils.toString(data.getConType()));
                    if (item != null) {
                        data.setConTypeName(item.getItemValue() + " " + item.getItemName());
                    }
                }
            }
        }
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
     * 数据编辑
     *
     * @param pageId
     * @param dataId
     * @return
     */
    @RequestMapping("/page/item/edit")
    public ModelAndView itemEdit(
            @RequestParam(value = "pageId") Integer pageId,
            @RequestParam(value = "dataId", required = false) Integer dataId) {

        PubConfigData pubConfigData = null;
        List<PubConfigDataFile> pubConfigDataFileList = null;
        if (dataId == null) {
            pubConfigData = new PubConfigData();
            pubConfigData.setPageId(pageId);
            pubConfigData.setStatus(BusiConstant.EFFECTIVE_YES);
            pubConfigDataFileList = Lists.newArrayList();
        } else {
            pubConfigData = pubConfigService.getPubConfigDataById(dataId);
            pubConfigDataFileList = pubConfigService.getPubConfigDataFileListByDataId(dataId);
        }
        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(pageId);
        PubConfigShow pubConfigShow = pubConfigService.getMultPubConfigShow(pubConfigPage.getId());
        List<PubDictItem> dictItemList = pubDictService.getValidDictItemListByDid(ObjUtils.toInteger(pubConfigPage.getConTypeCode()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("data", pubConfigData);
        modelAndView.addObject("show", pubConfigShow);
        modelAndView.addObject("fileList", pubConfigDataFileList);
        modelAndView.addObject("dictItemList", dictItemList);
        modelAndView.setViewName("pub/conf/mult_item_edit");
        return modelAndView;
    }

    @RequestMapping("/img/upload")
    @ResponseBody
    public String imgUpload(@RequestParam("imgUpload") MultipartFile fileUpload,
                            HttpServletResponse response) throws Exception {

        if (fileUpload == null || fileUpload.isEmpty()) {
            setResContent2Text(response);
            return map2JsonString(getFailMap("参数不能为空！"));
        }
        // 处理上传文件
        String oldFileName = fileUpload.getOriginalFilename();
        String savePath = UploadConstant.getFilePath(null, UploadConstant.FILE_SOURCE_INNER, null, oldFileName);
        stream2File(fileUpload.getInputStream(), UploadConstant.UPLOAD_PATH_ROOT + savePath);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("filePath", savePath);
        result.put("fileName", oldFileName);
        result.put("fileUrl", UploadConstant.UPLOAD_SERVER_HOST + savePath);
        setResContent2Text(response);
        return map2JsonString(result);
    }

    /**
     * 数据保存
     *
     * @param pubConfigData
     * @param picAryStr
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/item/save")
    @ResponseBody
    public Map<String, Object> itemSave(PubConfigData pubConfigData,
                                        @RequestParam("pic_ary") String picAryStr,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {


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
        // 保存数据
        pubConfigService.savePubConfigData(pubConfigData, dataFileList);
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping(value = "/item/contype", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> itemContype(@RequestParam(value = "pageId") Integer pageId,
                                           HttpServletResponse response) {

        List<PubDictItem> dictItemList = null;
        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(pageId);
        if (pubConfigPage != null) {
            Integer did = ObjUtils.toInteger(pubConfigPage.getConTypeCode());
            dictItemList = pubDictService.getValidDictItemListByDid(did);
        }
        StringBuilder option = new StringBuilder();
        option.append("<option value=\"\">--请选择类型--<option>");
        if (dictItemList != null) {
            for (PubDictItem item : dictItemList) {
                option.append("<option value=\"");
                option.append(item.getItemValue());
                option.append("\">");
                option.append(item.getItemValue());
                option.append(" ");
                option.append(item.getItemName());
                option.append("<option>");
            }
        }
        Map<String, Object> result = getSuccessMap();
        result.put("data", option.toString());
        setResContent2Json(response);
        return result;
    }

    @RequestMapping("/online")
    @ResponseBody
    public Map<String, Object> online(@RequestParam("id") Integer id, HttpServletResponse response) {

        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(id);
        List<PubConfigData> configDataList = pubConfigService.getValidPubConfigDataList(id);
        if (pubConfigPage == null || configDataList == null || configDataList.isEmpty()) {
            setResContent2Json(response);
            return getFailMap("上线失败，页面数据为空！");
        }
        for (PubConfigData data : configDataList) {
            List<PubConfigDataFile> fileList = pubConfigService.getPubConfigDataFileListByDataId(data.getId());
            data.setFileList(fileList);
        }
        pubConfigService.multPageOnline(pubConfigPage, configDataList);
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/page/show/edit")
    public ModelAndView showEdit(@RequestParam("pageId") Integer pageId) {

        PubConfigPage pubConfigPage = pubConfigService.getPubConfigPageById(pageId);
        PubConfigShow pubConfigShow = pubConfigService.getMultPubConfigShow(pageId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("page", pubConfigPage);
        modelAndView.addObject("show", pubConfigShow);
        modelAndView.setViewName("pub/conf/mult_show_edit");
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
