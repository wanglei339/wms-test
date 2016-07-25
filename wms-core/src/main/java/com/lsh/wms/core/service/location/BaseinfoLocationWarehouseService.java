package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationWarehouseDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationWarehouseDao.insert((BaseinfoLocationWarehouse) baseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationWarehouseDao.update((BaseinfoLocationWarehouse) baseinfoLocaltionModel);

    }

    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long locationId) {
        Map<String,Object> mapQuery = new HashMap<String,Object>();
        mapQuery.put("locationId",locationId);
        List<BaseinfoLocationWarehouse> lists =
                baseinfoLocationWarehouseDao.getBaseinfoLocationWarehouseList(mapQuery);
        BaseinfoLocationWarehouse baseinfoLocationWarehouse = null;
        if(lists.size()>0){
            baseinfoLocationWarehouse = lists.get(0);
        }
        return baseinfoLocationWarehouse;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationWarehouseDao.countBaseinfoLocationWarehouse(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocationWarehouse> list = baseinfoLocationWarehouseDao.getBaseinfoLocationWarehouseList(params);
        List<IBaseinfoLocaltionModel> resList = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocationWarehouse baseinfoLocationWarehouse:list){
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocationWarehouse;
            resList.add(iBaseinfoLocaltionModel);
        }
        return resList;
    }
}
