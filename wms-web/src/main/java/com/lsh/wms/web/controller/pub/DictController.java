package com.lsh.wms.web.controller.pub;


import com.lsh.wms.model.pub.PubDict;
import com.lsh.wms.model.pub.PubDictItem;
import com.lsh.wms.core.constant.BusiConstant;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 词典管理
 */
@Controller
@RequestMapping("/pub/dict")
public class DictController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private PubDictService pubDictService;

    @RequestMapping("")
    public String index() {
        return "pub/dict/dict";
    }

    /**
     * 词典列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param query_dictName
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> dictList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "query_dictName", required = false) String query_dictName,
            HttpServletResponse response) {

        // 分页显示
        Integer num = pubDictService.countPubDict(query_dictName);
        List<PubDict> list = pubDictService.getPubDictList(query_dictName, start, limit);
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
     * 词典编辑
     *
     * @param id
     * @return
     */
    @RequestMapping("/page/dict/edit")
    public ModelAndView dictEdit(@RequestParam(value = "id", required = false) Integer id) {
        PubDict dict = null;
        boolean isNew = false;
        if (id == null) {
            dict = new PubDict();
            dict.setStatus(BusiConstant.EFFECTIVE_YES);
            isNew = true;
        } else {
            dict = pubDictService.getPubDictById(id);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dict", dict);
        modelAndView.addObject("isNew", isNew);
        modelAndView.setViewName("pub/dict/dict_edit");
        return modelAndView;
    }

    /**
     * 词典保存
     *
     * @param pubDict
     * @param response
     * @return
     */
    @RequestMapping("/dict/save")
    @ResponseBody
    public Map<String, Object> dictSave(PubDict pubDict, HttpServletResponse response) {

        if (StringUtils.isBlank(pubDict.getDictCode())) {
            setResContent2Json(response);
            return getFailMap("编码不能为空！");
        }
        if (StringUtils.isBlank(pubDict.getDictName())) {
            setResContent2Json(response);
            return getFailMap("名称不能为空！");
        }
        if (pubDict.getId() == null) {
            // 保存
            pubDictService.insertPubDict(pubDict);
        } else {
            // 保存
            pubDictService.updatePubDict(pubDict);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping(value = "/item/tree", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> itemTree(@RequestParam(value = "dictId") Integer dictId,
                                        HttpServletResponse response) {

        Map<String, Object> treeMap = pubDictService.getPubDictTreeMap(dictId);
        setResContent2Json(response);
        return treeMap;
    }

    @RequestMapping("/page/item/edit")
    public ModelAndView itemEdit(@RequestParam(value = "id", required = false) Integer id,
                                 @RequestParam(value = "did") Integer did,
                                 @RequestParam(value = "pid", required = false) Integer pid) {

        PubDict pubDict = pubDictService.getPubDictById(did);
        if (pubDict == null) {
            pubDict = new PubDict();
            pubDict.setDictType(1);
            pubDict.setDictName("词典不存在");
        }
        boolean isNew = false;
        boolean isRoot = false;
        PubDictItem item = null;
        if (id == null) {
            item = new PubDictItem();
            item.setDid(did);
            item.setPid(pid);
            item.setStatus(BusiConstant.EFFECTIVE_YES);
            isNew = true;
        } else if (id == 0) {
            item = new PubDictItem();
            item.setId(id);
            item.setDid(did);
            item.setPid(0);
            item.setItemName("根节点");
            item.setStatus(BusiConstant.EFFECTIVE_YES);
            isRoot = true;
        } else {
            item = pubDictService.getPubDictItemById(id);
            if (item != null) {
                pid = item.getPid();
            }
        }
        PubDictItem parentItem = pubDictService.getPubDictItemById(pid);
        if (parentItem == null) {
            parentItem = new PubDictItem();
            parentItem.setItemName("根节点");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dict", pubDict);
        modelAndView.addObject("item", item);
        modelAndView.addObject("isNew", isNew);
        modelAndView.addObject("isRoot", isRoot);
        modelAndView.addObject("parentItemName", parentItem.getItemName());
        modelAndView.setViewName("pub/dict/item_edit");
        return modelAndView;
    }

    @RequestMapping("/item/save")
    @ResponseBody
    public Map<String, Object> itemSave(PubDictItem item, HttpServletResponse response) {

        Integer id = item.getId();
        if (id == null) {
            pubDictService.insertPubDictItem(item);
        } else {
            pubDictService.updatePubDictItem(item);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/item/move")
    @ResponseBody
    public Map<String, Object> itemMove(@RequestParam(value = "itemId") Integer itemId,
                                        @RequestParam(value = "pid") Integer pid,
                                        @RequestParam(value = "old_pid") Integer old_pid,
                                        @RequestParam(value = "position") Integer position,
                                        HttpServletResponse response) {

        pubDictService.moveItem(itemId, pid, old_pid, position);
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/online")
    @ResponseBody
    public Map<String, Object> online(@RequestParam(value = "id") Integer id,
                                      HttpServletResponse response) {


        PubDict dict = pubDictService.getPubDictById(id);
        List<PubDictItem> dictItemList = pubDictService.getValidDictItemListByDid(id);
        if (dict == null || dictItemList == null || dictItemList.isEmpty()) {
            setResContent2Json(response);
            return getFailMap("上线失败，词典数据为空！");
        }
        pubDictService.online(dict, dictItemList);
        setResContent2Json(response);
        return getSuccessMap();
    }
}
