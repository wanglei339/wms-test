package com.lsh.wms.web.controller.up;


import com.lsh.wms.core.model.up.UpModel;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.up.UpModelService;
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
@RequestMapping("/up/mod")
public class ModelController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private UpModelService modelService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("")
    public String modIndex() {
        return "up/mod/mod";
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
    public Map<String, Object> modList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletResponse response) {

        // 分页显示
        Integer num = modelService.countModel(keyword);
        List<UpModel> list = modelService.getModelList(keyword, start, limit);
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
    public ModelAndView modEdit(@RequestParam(value = "id", required = false) Integer id) {
        UpModel model = null;
        if (id == null) {
            model = new UpModel();
            model.setStatus(BusiConstant.EFFECTIVE_YES);
        } else {
            model = modelService.getModelById(id);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("upMod", model);
        modelAndView.setViewName("up/mod/mod_edit");
        return modelAndView;
    }

    /**
     * 保存
     *
     * @param model
     * @param response
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> modSave(UpModel model, HttpServletResponse response) {

        if (StringUtils.isBlank(model.getModCode())) {
            setResContent2Json(response);
            return getFailMap("型号编码不能为空！");
        }
        if (modelService.existModel(model.getModCode(), model.getId())) {
            return getFailMap("型号编码已存在！");
        }
        if (model.getId() == null) {
            // 新增
            model.setStatus(BusiConstant.EFFECTIVE_YES);
            // 保存
            modelService.insert(model);
        } else {
            modelService.update(model);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

}

