package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationShelfDao;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午7:22
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationShelfService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(BaseinfoLocationShelfService.class);
    @Autowired
    private BaseinfoLocationShelfDao baseinfoLocationShelfDao;
    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationShelfDao.insert((BaseinfoLocationShelf) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationShelfDao.update((BaseinfoLocationShelf) iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        List<BaseinfoLocationShelf> shelfList = baseinfoLocationShelfDao.getBaseinfoLocationShelfList(mapQuery);
//        BaseinfoLocationShelf shelf =  shelfList.get(0);
        return shelfList.size() > 0 ? shelfList.get(0) : null;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationShelfDao.countBaseinfoLocationShelf(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        return (List<BaseinfoLocation>) (List<?>) baseinfoLocationShelfDao.getBaseinfoLocationShelfList(params);
    }
}
