package com.lsh.wms.api.service.baseinfo;

import com.lsh.wms.model.baseinfo.BaseinfoShelflifeRule;

import java.util.Map;

/**
 * Created by lixin-mac on 16/8/10.
 */
public interface IShelfLifeRestService {
    String getShelflifeRuleList(Map<String,Object> mapQuery);
    String getShelflifeRuleCount(Map<String,Object> mapQuery);
    String updateShelflifeRule(BaseinfoShelflifeRule shelflifeRule);
    String insertShelflifeRule(BaseinfoShelflifeRule shelflifeRule);
    String deleteShelflifeRule(BaseinfoShelflifeRule shelflifeRule);
    String getShelflifeRule(Long ruleId);
}
