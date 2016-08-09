package com.lsh.wms.core.service.location;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationWarehouseDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
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
 * @Date 16/7/23 下午8:29
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationWarehouseService implements IStrategy {

    @Autowired
    private BaseinfoLocationWarehouseDao baseinfoLocationWarehouseDao;
    @Autowired
    private LocationService locationService;
    @Autowired
    private FatherToChildUtil fatherToChildUtil;


    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationWarehouseDao.insert((BaseinfoLocationWarehouse) iBaseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel iBaseinfoLocaltionModel) {
        baseinfoLocationWarehouseDao.update((BaseinfoLocationWarehouse) iBaseinfoLocaltionModel);
    }

    public BaseinfoLocation getBaseinfoItemLocationModelById(Long id) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", id);
        List<BaseinfoLocationWarehouse> warehouseList = baseinfoLocationWarehouseDao.getBaseinfoLocationWarehouseList(mapQuery);
//        BaseinfoLocationWarehouse warehouse =  warehouseList.get(0);
        return warehouseList.size() > 0 ? null : warehouseList.get(0);
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationWarehouseDao.countBaseinfoLocationWarehouse(params);
    }

    public List<BaseinfoLocation> getBaseinfoLocaltionModelList(Map<String, Object> params)  {
        return (List<BaseinfoLocation>) (List<?>) baseinfoLocationWarehouseDao.getBaseinfoLocationWarehouseList(params);
    }
}
