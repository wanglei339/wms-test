package com.lsh.wms.web.controller.up;


import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.model.up.*;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.up.*;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/up/pkg")
public class PackageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PackageController.class);

    @Autowired
    private UpAppService appService;
    @Autowired
    private UpChannelService channelService;
    @Autowired
    private UpModelService modelService;
    @Autowired
    private UpOpsystemService opsystemService;
    @Autowired
    private UpPackageService packageService;

    @RequestMapping("")
    public ModelAndView index() {
        List<UpApp> appList = appService.getAllValidAppList();
        List<UpOpsystem> osList = opsystemService.getAllValidOpsystemlList();
        List<UpChannel> chnList = channelService.getAllValidChannelList();
        List<UpModel> modList = modelService.getAllValidModelList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("appList", appList);
        modelAndView.addObject("osList", osList);
        modelAndView.addObject("chnList", chnList);
        modelAndView.addObject("modList", modList);
        modelAndView.setViewName("up/pkg/pkg");
        return modelAndView;
    }

    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "osId", required = false) Integer osId,
            @RequestParam(value = "chnId", required = false) Integer chnId,
            @RequestParam(value = "modId", required = false) Integer modId,
            HttpServletResponse response) {

        Integer total = packageService.countUpPackage(keyword, appId, osId, chnId, modId);
        List<UpPackage> list = packageService.getUpPackageList(keyword, appId, osId, chnId, modId, start, limit);
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw);
        result.put("recordsTotal", total);
        result.put("recordsFiltered", total);
        result.put("data", list.toArray());
        setResContent2Json(response);
        return result;
    }

    @RequestMapping("/page/edit")
    public ModelAndView edit(@RequestParam(value = "id") Long id) {

        UpPackage pkg = null;
        if (id == null) {
            pkg = new UpPackage();
            pkg.setStatus(BusiConstant.EFFECTIVE_YES);
        } else {
            pkg = packageService.getPackageById(id);
        }
        List<UpApp> appList = appService.getAllValidAppList();
        List<UpOpsystem> osList = opsystemService.getAllValidOpsystemlList();
        List<UpChannel> chnList = channelService.getAllValidChannelList();
        List<UpModel> modList = modelService.getAllValidModelList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pkg", pkg);
        modelAndView.addObject("appList", appList);
        modelAndView.addObject("osList", osList);
        modelAndView.addObject("chnList", chnList);
        modelAndView.addObject("modList", modList);
        modelAndView.setViewName("up/pkg/pkg_edit");
        return modelAndView;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(UpPackage pkg, HttpServletResponse response) {

        if (pkg.getId() == null) {
            String appKey = RandomUtils.randomStr(20);
            // 检查appKey是否有重复
            if (packageService.existAppkey(appKey, pkg.getId())) {
                setResContent2Json(response);
                return getFailMap("产生的appKey[" + appKey + "]重复，请重新操作！");
            }
            // 检查五个纬度信息是否重复
            if (packageService.existPackage(pkg.getAppId(), pkg.getOsId(), pkg.getChnId(), pkg.getModId(), pkg.getPkgType(), pkg.getId())) {
                setResContent2Json(response);
                return getFailMap("已存在相同[应用][操作系统][渠道][型号][类型]的包！");
            }
            pkg.setAppKey(appKey);
            pkg.setCreatedTime(new Date());
            pkg.setUpdatedTime(new Date());
            packageService.insert(pkg);
        } else {
            // 检查五个纬度信息是否重复
            if (packageService.existPackage(pkg.getAppId(), pkg.getOsId(), pkg.getChnId(), pkg.getModId(), pkg.getPkgType(), pkg.getId())) {
                setResContent2Json(response);
                return getFailMap("已存在相同[应用][操作系统][渠道][型号][类型]的包！");
            }
            pkg.setUpdatedTime(new Date());
            packageService.update(pkg);
        }
        setResContent2Json(response);
        return getSuccessMap();
    }

}

