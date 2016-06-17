package com.lsh.wms.web.controller.up;


import com.lsh.wms.api.model.up.UpApp;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.up.UpAppService;
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
@RequestMapping("/up/app")
public class AppController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private UpAppService appService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String appIndex() {
        return "up/app/app";
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
    public Map<String, Object> appList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletResponse response) {

        // 分页显示
        Integer num = appService.countApp(keyword);
        List<UpApp> list = appService.getAppList(keyword, start, limit);
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
    public ModelAndView appEdit(@RequestParam(value = "id", required = false) Integer id) {
        UpApp app = null;
        if (id == null) {
            app = new UpApp();
            app.setStatus(BusiConstant.EFFECTIVE_YES);
        } else {
            app = appService.getAppById(id);
        }
        logger.info("app info ={}",app.toString());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("app", app);
        modelAndView.setViewName("up/app/app_edit");
        return modelAndView;
    }

    /**
     * 保存
     *
     * @param app
     * @param response
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> appSave(UpApp app, HttpServletResponse response) {

        if (StringUtils.isBlank(app.getAppCode())) {
            setResContent2Json(response);
            return getFailMap("应用编码不能为空！");
        }
        if (appService.existApp(app.getAppCode(), app.getId())) {
            return getFailMap("应用编码已存在！");
        }
        if (app.getId() == null) {
            // 新增
            app.setStatus(BusiConstant.EFFECTIVE_YES);
            // 保存
            appService.insert(app);
        } else {
            appService.update(app);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

}

