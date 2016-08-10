package com.lsh.wms.core.service.baseinfo;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoShelflifeRuleDao;
import com.lsh.wms.model.baseinfo.BaseinfoShelflifeRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/10.
 */
@Component
@Transactional(readOnly = true)
public class ShelfLifeService {
    private static final Logger logger = LoggerFactory.getLogger(ShelfLifeService.class);
    @Autowired
    private BaseinfoShelflifeRuleDao shelflifeRuleDao;

    public List<BaseinfoShelflifeRule> getShelflifeRuleList(Map<String,Object> mapQuery){
        return shelflifeRuleDao.getBaseinfoShelflifeRuleList(mapQuery);
    }
    public Integer getShelflifeRuleCount(Map<String,Object> mapQuery){
        return shelflifeRuleDao.countBaseinfoShelflifeRule(mapQuery);
    }

    public BaseinfoShelflifeRule getShelflifeRule(Long ruleId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ruleId",ruleId);
        List<BaseinfoShelflifeRule> list =
                shelflifeRuleDao.getBaseinfoShelflifeRuleList(mapQuery);
        BaseinfoShelflifeRule shelflifeRule = null;
        if(list.size() >0 ){
            shelflifeRule = list.get(0);
        }
        return shelflifeRule;
    }

    @Transactional(readOnly = false)
    public void updateShelflifeRule(BaseinfoShelflifeRule shelflifeRule){
        shelflifeRule.setUpdatedAt(DateUtils.getCurrentSeconds());
        shelflifeRuleDao.update(shelflifeRule);
    }
    @Transactional(readOnly = false)
    public void insertShelflifeRule(BaseinfoShelflifeRule shelflifeRule){
        shelflifeRule.setRuleId(RandomUtils.genId());
        shelflifeRule.setCreatedAt(DateUtils.getCurrentSeconds());
        shelflifeRule.setUpdatedAt(DateUtils.getCurrentSeconds());
        shelflifeRuleDao.insert(shelflifeRule);
    }

    @Transactional(readOnly = false)
    public void deleteShelflifeRule(BaseinfoShelflifeRule shelflifeRule){
        shelflifeRuleDao.delete(shelflifeRule);
    }

}
