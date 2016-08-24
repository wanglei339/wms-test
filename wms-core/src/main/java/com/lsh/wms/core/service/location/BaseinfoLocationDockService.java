package com.lsh.wms.core.service.location;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDockDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        mapQuery.put("isValid", LocationConstant.IS_VALID);
        List<BaseinfoLocationDock> dockList = baseinfoLocationDockDao.getBaseinfoLocationDockList(mapQuery);
//        BaseinfoLocationDock dock = dockList.get(0);
        return dockList.size() > 0 ? dockList.get(0) : null;
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
        params.put("isValid",LocationConstant.IS_VALID);
        return baseinfoLocationDockDao.countBaseinfoLocationDock(params);
    }

    /**
     * 返回BaseinfoLocationDock的getList
     *
     * @param params
     * @return
     */
    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        params.put("isValid",LocationConstant.IS_VALID);
        return (List<BaseinfoLocation>) (List<?>) baseinfoLocationDockDao.getBaseinfoLocationDockList(params);
    }

    /**
     * 删除码头细节表,将isvalid置为0
     * @param locationId
     * @return
     */
    @Transactional(readOnly = false)
    public IBaseinfoLocaltionModel removeLocation(Long locationId) {
        //先查,然后删除
        BaseinfoLocationDock temp = (BaseinfoLocationDock) this.getBaseinfoItemLocationModelById(locationId);
        if (temp == null) {
            throw new BizCheckedException("2180003");
        }
        temp.setIsValid(LocationConstant.NOT_VALID);
        this.update(temp);
        return temp;
    }

}


