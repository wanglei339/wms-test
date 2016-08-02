package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDockDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    @Autowired
    private LocationService locationService;

    /**
     * 只完成插入服务
     *
     * @param
     */
    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationDockDao.insert((BaseinfoLocationDock) iBaseinfoLocaltionModel);

    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationDockDao.update((BaseinfoLocationDock) iBaseinfoLocaltionModel);

    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        List<BaseinfoLocationDock> dockList =  baseinfoLocationDockDao.getBaseinfoLocationDockList(mapQuery);
        BaseinfoLocationDock dock =  dockList.get(0);
        return dock;
    }

    /**
     * Location的主表没有码头的出和入的性质
     * 不包含码头的dock_type数目就是主表的数
     * 含有出入码头,就计数为dock表的条目数
     *
     * @param params
     * @return
     */
    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationDockDao.countBaseinfoLocationDock(params);
    }

    /**
     * 返回BaseinfoLocationDock的getList
     *
     * @param params
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (List<BaseinfoLocation>)(List<?>)baseinfoLocationDockDao.getBaseinfoLocationDockList(params);
    }

}


