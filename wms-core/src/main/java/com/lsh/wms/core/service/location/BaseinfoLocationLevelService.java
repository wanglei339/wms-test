package com.lsh.wms.core.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 层级的service,为以后可能扩展,增加方法
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/17 上午11:12
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationLevelService implements IStrategy{
    private static final Logger logger = LoggerFactory.getLogger(BaseinfoLocationLevelService.class);
    @Autowired
    private LocationService locationService;
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        locationService.insertLocation((BaseinfoLocation) iBaseinfoLocaltionModel);
    }
    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        locationService.updateLocation(iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        return locationService.getLocation(id);
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return locationService.countLocation(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        return locationService.getBaseinfoLocationList(params);
    }
}
