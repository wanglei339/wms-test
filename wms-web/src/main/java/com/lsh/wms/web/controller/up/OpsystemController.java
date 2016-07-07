package com.lsh.wms.web.controller.up;


import com.lsh.wms.core.model.up.UpOpsystem;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.up.UpOpsystemService;
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

@Controller
@RequestMapping("/up/os")
public class OpsystemController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OpsystemController.class);


    @Autowired
    private UpOpsystemService opsystemService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String osIndex() {
        return "up/os/os";
    }

    /**
     * 列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param keyword
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> osList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletResponse response) {

        // 分页显示
        Integer num = opsystemService.countOpsystem(keyword);
        List<UpOpsystem> list = opsystemService.getOpsystemList(keyword, start, limit);
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
     * 编辑
     *
     * @param id
     * @return
     */
    @RequestMapping("/page/edit")
    public ModelAndView osEdit(@RequestParam(value = "id", required = false) Integer id) {
        UpOpsystem os = null;
        if (id == null) {
            os = new UpOpsystem();
            os.setStatus(BusiConstant.EFFECTIVE_YES);
        } else {
            os = opsystemService.getOpsystemById(id);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("os", os);
        modelAndView.setViewName("up/os/os_edit");
        return modelAndView;
    }

    /**
     * 保存
     *
     * @param os
     * @param response
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> osSave(UpOpsystem os, HttpServletResponse response) {

        if (StringUtils.isBlank(os.getOsCode())) {
            setResContent2Json(response);
            return getFailMap("系统编码不能为空！");
        }
        if (opsystemService.existOpsystem(os.getOsCode(), os.getId())) {
            return getFailMap("系统编码已存在！");
        }
        if (os.getId() == null) {
            // 新增
            os.setStatus(BusiConstant.EFFECTIVE_YES);
            // 保存
            opsystemService.insert(os);
        } else {
            opsystemService.update(os);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }


}

