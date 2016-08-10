package com.lsh.wms.rpc.service.rule;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.baseinfo.IShelfLifeRpcService;
import com.lsh.wms.core.service.baseinfo.ShelfLifeService;
import com.lsh.wms.model.baseinfo.BaseinfoShelflifeRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/10.
 */
@Service(protocol = "dubbo")
public class ShelfLifeRpcService implements IShelfLifeRpcService{

    @Autowired
    private ShelfLifeService shelfLifeService;

    public List<BaseinfoShelflifeRule> getShelflifeRuleList(Map<String, Object> mapQuery) {
        return shelfLifeService.getShelflifeRuleList(mapQuery);
    }

    public Integer getShelflifeRuleCount(Map<String, Object> mapQuery) {
        return shelfLifeService.getShelflifeRuleCount(mapQuery);
    }

    public void updateShelflifeRule(BaseinfoShelflifeRule shelflifeRule) {
        shelfLifeService.updateShelflifeRule(shelflifeRule);
    }

    public void insertShelflifeRule(BaseinfoShelflifeRule shelflifeRule) {
        shelfLifeService.insertShelflifeRule(shelflifeRule);
    }

    public void deleteShelflifeRule(BaseinfoShelflifeRule shelflifeRule) {
        shelfLifeService.deleteShelflifeRule(shelflifeRule);
    }
}
