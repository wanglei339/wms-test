package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
public class BaseinfoLocationService implements IStrategy{

    @Autowired
    private BaseinfoLocationDao baseinfoLocationDao;

    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationDao.insert((BaseinfoLocation) baseinfoLocaltionModel);
    }

    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationDao.update((BaseinfoLocation) baseinfoLocaltionModel);
    }

    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long id) {
        return baseinfoLocationDao.getBaseinfoLocationById(id);
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationDao.countBaseinfoLocation(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocation> list  = baseinfoLocationDao.getBaseinfoLocationList(params);
        List<IBaseinfoLocaltionModel> resList = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocation baseinfoLocation :list ) {
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocation;
            resList.add(iBaseinfoLocaltionModel);
        }
        return resList;
    }
}
