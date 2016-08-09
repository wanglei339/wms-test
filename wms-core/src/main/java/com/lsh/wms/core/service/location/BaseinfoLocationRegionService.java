package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationRegionDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;
import com.lsh.wms.model.baseinfo.BaseinfoLocationRegion;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
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
    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationRegionDao.insert((BaseinfoLocationRegion) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationRegionDao.update((BaseinfoLocationRegion) iBaseinfoLocaltionModel);
    }


    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        List<BaseinfoLocationRegion> regionList = baseinfoLocationRegionDao.getBaseinfoLocationRegionList(mapQuery);
//        BaseinfoLocationRegion region =  regionList.get(0);
        return regionList.size() > 0 ? null : regionList.get(0);

    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationRegionDao.countBaseinfoLocationRegion(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params)  {
        return (List<BaseinfoLocation>) (List<?>) baseinfoLocationRegionDao.getBaseinfoLocationRegionList(params);

    }
}
