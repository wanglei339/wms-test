package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationPassageDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocationPassage;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午7:17
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationPassageService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private BaseinfoLocationPassageDao baseinfoLocationPassageDao;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationPassageDao.insert((BaseinfoLocationPassage) baseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationPassageDao.update((BaseinfoLocationPassage) baseinfoLocaltionModel);

    }

    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long locationId) {
        Map<String,Object> mapQuery = new HashMap<String,Object>();
        mapQuery.put("locationId",locationId);
        List<BaseinfoLocationPassage> lists = baseinfoLocationPassageDao.getBaseinfoLocationPassageList(mapQuery);
        BaseinfoLocationPassage  baseinfoLocationPassage = null;
        if(lists.size()>0){
            baseinfoLocationPassage = lists.get(0);
        }

        return baseinfoLocationPassage;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationPassageDao.countBaseinfoLocationPassage(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocationPassage> list = baseinfoLocationPassageDao.getBaseinfoLocationPassageList(params);
        List<IBaseinfoLocaltionModel> resList = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocationPassage baseinfoLocationPassage : list) {
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocationPassage;
            resList.add(iBaseinfoLocaltionModel);
        }
        return resList;
    }
}
