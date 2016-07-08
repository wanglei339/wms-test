package com.lsh.wms.web.controller.up;


import com.google.common.collect.Lists;
import com.lsh.wms.model.up.UpVersion;
import com.lsh.wms.model.up.UpVersionRules;
import com.lsh.wms.model.up.UpVersionRulesCons;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.UpgradeConstant;
import com.lsh.wms.core.service.up.UpVersionRulesService;
import com.lsh.wms.core.service.up.UpVersionService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/up/rule")
public class RuleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    private UpVersionService versionService;
    @Autowired
    private UpVersionRulesService versionRulesService;

    @RequestMapping("/page/rule")
    public ModelAndView packageVersionRule(@RequestParam(value = "verId") Long verId) {

        UpVersion version = versionService.getVersionById(verId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("up/rule/rule");
        return modelAndView;
    }

    @RequestMapping("/verrule")
    @ResponseBody
    public Map<String, Object> verRule(@RequestParam(value = "ruleId") Integer ruleId,
                                       @RequestParam(value = "verId") Long verId,
                                       HttpServletResponse response) {

        UpVersionRules versionRules = versionRulesService.getVersionRules(verId, ruleId);
        if (versionRules == null) {
            versionRules = new UpVersionRules();
            versionRules.setRuleType(UpgradeConstant.RULE_TYPE_BLACK);
            versionRules.setJudgeWay(UpgradeConstant.JUDGE_WAY_IN);
        }
        Map<String, Object> result = getSuccessMap();
        result.put("versionRules", versionRules);
        setResContent2Json(response);
        return result;
    }

    @RequestMapping(value = "/con/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> verRuleConList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "verId") Long verId,
            @RequestParam(value = "ruleId") Integer ruleId,
            HttpServletResponse response) {

        Integer total = 0;
        List<UpVersionRulesCons> list = Lists.newArrayList();
        UpVersionRules versionRules = versionRulesService.getVersionRules(verId, ruleId);
        if (versionRules != null) {
            total = versionRulesService.countVersionRulesCons(versionRules.getId(), null);
            list = versionRulesService.getVersionRulesConsList(versionRules.getId(), null, start, limit);
        }
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", total);
        result.put("recordsFiltered", total);
        result.put("data", list.toArray());
        setResContent2Json(response);
        return result;
    }

    @RequestMapping("/verrule/save")
    @ResponseBody
    public Map<String, Object> verRuleSave(@RequestParam(value = "verId") Long verId,
                                           @RequestParam(value = "ruleId") Integer ruleId,
                                           @RequestParam(value = "judgeWay") Integer judgeWay,
                                           HttpServletResponse response) {

        if (verId == null || ruleId == null || judgeWay == null) {
            setResContent2Json(response);
            return getFailMap("参数错误！");
        }
        UpVersionRules versionRules = versionRulesService.getVersionRules(verId, ruleId);
        if (versionRules == null) {
            versionRules = new UpVersionRules();
            versionRules.setVerId(verId);
            versionRules.setRuleId(ruleId);
            versionRules.setRuleType(UpgradeConstant.RULE_TYPE_BLACK);
            versionRules.setJudgeWay(judgeWay);
            versionRules.setStatus(BusiConstant.EFFECTIVE_YES);
            versionRulesService.insert(versionRules);
        } else {
            UpVersionRules upVerRule = new UpVersionRules();
            upVerRule.setId(versionRules.getId());
            upVerRule.setJudgeWay(judgeWay);
            versionRulesService.update(upVerRule);
        }
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/page/con/edit")
    public ModelAndView verRuleConEdit(@RequestParam(value = "verId") Long verId,
                                       @RequestParam(value = "ruleId") Integer ruleId,
                                       @RequestParam(value = "conId", required = false) Long conId) {

        UpVersionRulesCons versionRulesCons = null;
        if (conId == null) {
            versionRulesCons = new UpVersionRulesCons();
            UpVersionRules versionRules = versionRulesService.getVersionRules(verId, ruleId);
            if (versionRules != null) {
                versionRulesCons.setVerRuleId(versionRules.getId());
            }
        } else {
            versionRulesCons = versionRulesService.getVersionRulesConsById(conId);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("verId", verId);
        modelAndView.addObject("ruleId", ruleId);
        modelAndView.addObject("con", versionRulesCons);
        modelAndView.setViewName("up/rule/rule_01");
        return modelAndView;
    }

    @RequestMapping("/con/save")
    @ResponseBody
    public Map<String, Object> verRuleConSave(@RequestParam(value = "verId") Long verId,
                                              @RequestParam(value = "ruleId") Integer ruleId,
                                              UpVersionRulesCons con,
                                              HttpServletResponse response) {

        // 参数校验及值转换
        if (ruleId == UpgradeConstant.RULE_ID_FORCEUP) {
            // 强制升级
            if (StringUtils.isBlank(con.getMinValueName())
                    || StringUtils.isBlank(con.getMaxValueName())) {
                setResContent2Json(response);
                return getFailMap("最小值和最大值不能为空！");
            }
            Integer minValue = UpgradeConstant.verStr2Int(con.getMinValueName());
            Integer maxValue = UpgradeConstant.verStr2Int(con.getMaxValueName());
            if (minValue == 0 || maxValue == 0 || minValue > maxValue) {
                setResContent2Json(response);
                return getFailMap("最小值和最大值格式不正确！");
            }
            con.setMinValue(minValue.longValue());
            con.setMaxValue(maxValue.longValue());
        } else if (ruleId == UpgradeConstant.RULE_ID_AREA) {
            // todo
        } else {
            setResContent2Json(response);
            return getFailMap("此规则无效！");
        }
        // 设置版本与规则的关联关系
        if (con.getVerRuleId() == null) {
            UpVersionRules versionRules = versionRulesService.getVersionRules(verId, ruleId);
            if (versionRules != null) {
                con.setVerRuleId(versionRules.getId());
            } else {
                // 如果版本与规则关联关系不存在，新增
                versionRules = new UpVersionRules();
                versionRules.setVerId(verId);
                versionRules.setRuleId(ruleId);
                versionRules.setRuleType(UpgradeConstant.RULE_TYPE_BLACK);
                versionRules.setJudgeWay(UpgradeConstant.JUDGE_WAY_IN);
                versionRules.setStatus(BusiConstant.EFFECTIVE_YES);
                versionRulesService.insert(versionRules);
                con.setVerRuleId(versionRules.getId());
            }
        }
        // 保存条件
        if (con.getId() == null) {
            con.setStatus(BusiConstant.EFFECTIVE_YES);
            con.setCreatedTime(new Date());
            con.setUpdatedTime(new Date());
            versionRulesService.insert(con);
        } else {
            versionRulesService.update(con);
        }
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/con/del")
    @ResponseBody
    public Map<String, Object> verRuleConDel(@RequestParam(value = "conId") Long conId, HttpServletResponse response) {
        versionRulesService.delete(conId);
        setResContent2Json(response);
        return getSuccessMap();
    }

    @RequestMapping("/cache")
    @ResponseBody
    public Map<String, Object> ruleCache(HttpServletResponse response) {
        versionRulesService.cacheRule();
        setResContent2Json(response);
        return getSuccessMap();
    }

}

