package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/23
 * Time: 16/7/23.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.location.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private BaseinfoLocationDao baseinfoLocationDao;
    @Autowired
    private LocationService locationService;


    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationDao.insert((BaseinfoLocation) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationDao.update((BaseinfoLocation) iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        List<BaseinfoLocation> baseinfoLocationList = baseinfoLocationDao.getBaseinfoLocationList(mapQuery);
//        BaseinfoLocation baseinfoLocation = baseinfoLocationList.get(0);
        return baseinfoLocationList.size() > 0 ? baseinfoLocationList.get(0) : null;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationDao.countBaseinfoLocation(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocation> baseinfoLocationList = locationService.getBaseinfoLocationList(params);
        return baseinfoLocationList;
    }

    public IBaseinfoLocaltionModel removeLocation(Long locationId) {
        return null;
    }
}
