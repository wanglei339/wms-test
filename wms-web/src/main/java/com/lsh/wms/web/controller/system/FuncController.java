package com.lsh.wms.web.controller.system;


import com.lsh.wms.model.system.SysFunction;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.system.SysFunctionService;
import com.lsh.wms.web.constant.MediaTypes;
import com.lsh.wms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 功能管理
 */
@Controller
@RequestMapping("/system/func")
public class FuncController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FuncController.class);

    @Autowired
    private SysFunctionService sysFunctionService;

    @RequestMapping("")
    public String index() {
        return "system/func/func";
    }

    @RequestMapping(value = "/tree", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> getTreeData(HttpServletResponse response) {
        Map<String, Object> treeMap = sysFunctionService.getFunctionTreeMap();
        setResContent2Json(response);
        return treeMap;
    }

    @RequestMapping("/page/edit")
    public ModelAndView edit(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "parentId") Integer parentId) {

        SysFunction func = null;
        boolean isNew = false;
        boolean isRoot = false;
        if (id == null) {
            func = new SysFunction();
            func.setParentFuncId(parentId);
            func.setIsLeaf(BusiConstant.EFFECTIVE_YES);
            func.setIsEffective(BusiConstant.EFFECTIVE_YES);
            isNew = true;
        } else if (id == 0) {
            func = new SysFunction();
            func.setId(0);
            func.setParentFuncId(0);
            func.setFuncName("功能菜单(根节点)");
            func.setIsLeaf(BusiConstant.EFFECTIVE_NO);
            func.setIsEffective(BusiConstant.EFFECTIVE_YES);
            isRoot = true;
        } else {
            func = sysFunctionService.getSysFunctionById(id);
            if (func != null){
                parentId = func.getParentFuncId();
            }
        }
        SysFunction parentFunc = sysFunctionService.getSysFunctionById(parentId);
        if (parentFunc == null){
            parentFunc = new SysFunction();
            parentFunc.setFuncName("功能菜单(根节点)");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("func", func);
        modelAndView.addObject("isNew", isNew);
        modelAndView.addObject("isRoot", isRoot);
        modelAndView.addObject("parentFuncName", parentFunc.getFuncName());
        modelAndView.setViewName("system/func/func_edit");
        return modelAndView;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(SysFunction func, HttpServletResponse response) {

        Integer id = func.getId();
        if (id == null) {
            sysFunctionService.insert(func);
        } else {
            sysFunctionService.update(func);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/move")
    @ResponseBody
    public Map<String, Object> move(@RequestParam(value = "funcId") Integer funcId,
                                   @RequestParam(value = "parentId") Integer parentId,
                                   @RequestParam(value = "old_parent") Integer old_parent,
                                   @RequestParam(value = "position") Integer position,
                                   HttpServletResponse response) {

        sysFunctionService.move(funcId, parentId, old_parent, position);
        setResContent2Json(response);
        return getSuccessMap();
    }

}
