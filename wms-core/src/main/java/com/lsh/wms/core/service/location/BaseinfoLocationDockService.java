package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDockDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
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
 * @Date 16/7/23 下午6:58
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationDockService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private BaseinfoLocationDockDao baseinfoLocationDockDao;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationDockDao.insert((BaseinfoLocationDock) baseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationDockDao.update((BaseinfoLocationDock) baseinfoLocaltionModel);
    }

    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long locationId) {
        Map<String,Object> mapQuery = new HashMap<String,Object>();
        mapQuery.put("locationId",locationId);
        List<BaseinfoLocationDock> lists =
                baseinfoLocationDockDao.getBaseinfoLocationDockList(mapQuery);
        BaseinfoLocationDock baseinfoLocationDock = null;
        if(lists.size()>0){
            baseinfoLocationDock = lists.get(0);
        }
        return baseinfoLocationDock;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationDockDao.countBaseinfoLocationDock(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocationDock> list = baseinfoLocationDockDao.getBaseinfoLocationDockList(params);
        List<IBaseinfoLocaltionModel> reslist = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocationDock baseinfoLocationDock:list){
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocationDock;
            reslist.add(iBaseinfoLocaltionModel);
        }
        return reslist;
    }
}
