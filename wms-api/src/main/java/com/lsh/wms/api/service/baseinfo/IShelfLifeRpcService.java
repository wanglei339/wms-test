package com.lsh.wms.api.service.baseinfo;

import com.lsh.wms.model.baseinfo.BaseinfoShelflifeRule;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/10.
 */
public interface IShelfLifeRpcService {
    List<BaseinfoShelflifeRule> getShelflifeRuleList(Map<String,Object> mapQuery);
    Integer getShelflifeRuleCount(Map<String,Object> mapQuery);
    void updateShelflifeRule(BaseinfoShelflifeRule shelflifeRule);
    void insertShelflifeRule(BaseinfoShelflifeRule shelflifeRule);
    void deleteShelflifeRule(BaseinfoShelflifeRule shelflifeRule);
}
