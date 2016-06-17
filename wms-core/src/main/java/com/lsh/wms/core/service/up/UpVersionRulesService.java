package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.up.UpRule;
import com.lsh.wms.api.model.up.UpVersionRules;
import com.lsh.wms.api.model.up.UpVersionRulesCons;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.UpgradeConstant;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import com.lsh.wms.core.dao.redis.RedisSetDao;
import com.lsh.wms.core.dao.up.UpRuleDao;
import com.lsh.wms.core.dao.up.UpVersionRulesConsDao;
import com.lsh.wms.core.dao.up.UpVersionRulesDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则
 */
@Component
@Transactional(readOnly = true)
public class UpVersionRulesService {

    private static Logger logger = LoggerFactory.getLogger(UpVersionRulesService.class);

    @Autowired
    private UpRuleDao ruleDao;
    @Autowired
    private UpVersionRulesDao versionRulesDao;
    @Autowired
    private UpVersionRulesConsDao versionRulesConsDao;

    @Autowired
    private RedisHashDao hashDao;
    @Autowired
    private RedisSetDao setDao;

    public UpVersionRules getVersionRules(Long verId, Integer ruleId) {
        return getVersionRules(verId, ruleId, UpgradeConstant.RULE_TYPE_BLACK);
    }

    public UpVersionRules getVersionRules(Long verId, Integer ruleId, Integer ruleType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("verId", verId);
        params.put("ruleId", ruleId);
        params.put("ruleType", ruleType);
        List<UpVersionRules> verRuleList = versionRulesDao.getVersionRulesList(params);
        if (verRuleList == null || verRuleList.isEmpty()) {
            return null;
        }
        return verRuleList.get(0);
    }

    public UpVersionRules getVersionRulesById(Long id) {
        if (id == null) {
            return null;
        }
        return versionRulesDao.getVersionRulesById(id);
    }

    @Transactional(readOnly = false)
    public void insert(UpVersionRules versionRules) {
        if (versionRules == null) {
            return;
        }
        versionRules.setCreatedTime(new Date());
        versionRulesDao.insert(versionRules);
        // 更新缓存
        cacheVerRule(versionRules.getId());
    }

    @Transactional(readOnly = false)
    public void update(UpVersionRules versionRules) {
        versionRules.setUpdatedTime(new Date());
        versionRulesDao.update(versionRules);
        // 更新缓存
        cacheVerRule(versionRules.getId());
    }

    public Integer countVersionRulesCons(Long verRuleId, String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("verRuleId", verRuleId);
        return versionRulesConsDao.countVersionRulesCons(params);
    }

    public List<UpVersionRulesCons> getVersionRulesConsList(Long verRuleId, String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("verRuleId", verRuleId);
        params.put("start", start);
        params.put("limit", limit);
        return versionRulesConsDao.getVersionRulesConsList(params);
    }


    public UpVersionRulesCons getVersionRulesConsById(Long id) {
        if (id == null) {
            return null;
        }
        return versionRulesConsDao.getVersionRulesConsById(id);
    }

    @Transactional(readOnly = false)
    public void insert(UpVersionRulesCons versionRulesCons) {
        if (versionRulesCons == null) {
            return;
        }
        versionRulesCons.setCreatedTime(new Date());
        versionRulesConsDao.insert(versionRulesCons);
        // 更新缓存
        cacheVerRuleCon(versionRulesCons.getId());
    }

    @Transactional(readOnly = false)
    public void update(UpVersionRulesCons versionRulesCons) {
        if (versionRulesCons == null) {
            return;
        }
        versionRulesCons.setUpdatedTime(new Date());
        versionRulesConsDao.update(versionRulesCons);
        // 更新缓存
        cacheVerRuleCon(versionRulesCons.getId());
    }

    @Transactional(readOnly = false)
    public void delete(Long conId) {
        if (conId == null) {
            return;
        }
        // 注意需在删除前查出来
        UpVersionRulesCons versionRulesCons = getVersionRulesConsById(conId);
        // 删除
        versionRulesConsDao.delete(conId);
        // 更新缓存
        delCacheVerRuleCon(versionRulesCons);
    }

    public void cacheRule() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", BusiConstant.EFFECTIVE_YES);
        List<UpRule> ruleList = ruleDao.getRuleList(params);
        if (ruleList == null || ruleList.isEmpty()) {
            return;
        }
        for (UpRule rule : ruleList) {
            Map<String, Object> hash = Maps.newLinkedHashMap();
            hash.put("rule_name", ObjUtils.toString(rule.getRuleName(), ""));
            hash.put("compare_type", ObjUtils.toString(rule.getCompareType(), "1"));
            hash.put("compare_col", ObjUtils.toString(rule.getCompareCol(), ""));
            hash.put("return_type", ObjUtils.toString(rule.getReturnType(), "1"));
            String key = StrUtils.formatString(RedisKeyConstant.UP_RULE, rule.getId());
            hashDao.putAll(key, hash);
        }
    }

    public void cacheVerRule(Long id) {
        UpVersionRules verRule = getVersionRulesById(id);
        if (verRule == null) {
            return;
        }
        String key_ver_rule_list = StrUtils.formatString(RedisKeyConstant.UP_VER_RULE_LIST, verRule.getVerId());
        setDao.add(key_ver_rule_list, verRule.getId().toString());
        Map<String, String> hash = new LinkedHashMap<String, String>();
        hash.put("rule_id", ObjUtils.toString(verRule.getRuleId(), "-1"));
        hash.put("rule_type", ObjUtils.toString(verRule.getRuleType(), "-1"));
        hash.put("judge_way", ObjUtils.toString(verRule.getJudgeWay(), "-1"));
        String key_ver_rule_info = StrUtils.formatString(RedisKeyConstant.UP_VER_RULE_INFO, verRule.getId());
        hashDao.putAll(key_ver_rule_info, hash);
    }

    public void cacheVerRuleCon(Long id) {
        UpVersionRulesCons con = getVersionRulesConsById(id);
        if (con == null) {
            return;
        }
        String key_ver_rule_conlist = StrUtils.formatString(RedisKeyConstant.UP_VER_RULE_CONLIST, con.getVerRuleId());
        setDao.add(key_ver_rule_conlist, con.getId().toString());
        Map<String, String> hash = new LinkedHashMap<String, String>();
        hash.put("eq_value", ObjUtils.toString(con.getEqValue(), "-1"));
        hash.put("min_value", ObjUtils.toString(con.getMinValue(), "-1"));
        hash.put("max_value", ObjUtils.toString(con.getMaxValue(), "-1"));
        String key_ver_rule_coninfo = StrUtils.formatString(RedisKeyConstant.UP_VER_RULE_CONINFO, con.getId());
        hashDao.putAll(key_ver_rule_coninfo, hash);
    }

    public void delCacheVerRuleCon(UpVersionRulesCons con) {
        if (con == null) {
            return;
        }
        String key_ver_rule_conlist = StrUtils.formatString(RedisKeyConstant.UP_VER_RULE_CONLIST, con.getVerRuleId());
        setDao.remove(key_ver_rule_conlist, con.getId().toString());
        String key_ver_rule_coninfo = StrUtils.formatString(RedisKeyConstant.UP_VER_RULE_CONINFO, con.getId());
        hashDao.delete(key_ver_rule_coninfo);
    }

}
