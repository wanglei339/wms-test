package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationRegionDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;
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
 * @Date 16/7/23 下午5:10
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationRegionService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    //增删改查
    @Autowired
    private BaseinfoLocationRegionDao baseinfoLocationRegionDao;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationRegionDao.insert((BaseinfoLocationRegion) baseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationRegionDao.update((BaseinfoLocationRegion) baseinfoLocaltionModel);
    }

    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long locationId) {
        Map<String,Object> mapQuery = new HashMap<String,Object>();
        mapQuery.put("locationId",locationId);
        List<BaseinfoLocationRegion> lists =
                baseinfoLocationRegionDao.getBaseinfoLocationRegionList(mapQuery);
        BaseinfoLocationRegion baseinfoLocationRegion = null;
        if(lists.size()>0){
            baseinfoLocationRegion = lists.get(0);
        }
        return baseinfoLocationRegion;

//        return baseinfoLocationRegionDao.getBaseinfoLocationRegionById(locationId);
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationRegionDao.countBaseinfoLocationRegion(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocationRegion> list = baseinfoLocationRegionDao.getBaseinfoLocationRegionList(params);
        List<IBaseinfoLocaltionModel> resList = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocationRegion baseinfoLocationRegion : list) {
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocationRegion;
            resList.add(iBaseinfoLocaltionModel);
        }
        return resList;
    }
}
