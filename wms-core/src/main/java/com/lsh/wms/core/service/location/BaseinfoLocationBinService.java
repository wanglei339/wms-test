package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationBinDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
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
 * @Date 16/7/23 下午6:50
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationBinService implements IStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private BaseinfoLocationBinDao baseinfoLocationBinDao;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationBinDao.insert((BaseinfoLocationBin) baseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationBinDao.insert((BaseinfoLocationBin) baseinfoLocaltionModel);
    }


    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long locationId) {
        Map<String,Object> mapQuery = new HashMap<String,Object>();
        mapQuery.put("locationId",locationId);
        List<BaseinfoLocationBin> lists = baseinfoLocationBinDao.getBaseinfoLocationBinList(mapQuery);
        BaseinfoLocationBin  baseinfoLocationBin = null;
        if(lists.size()>0){
            baseinfoLocationBin = lists.get(0);
        }

        return baseinfoLocationBin;
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {
        return baseinfoLocationBinDao.countBaseinfoLocationBin(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocationBin> list = baseinfoLocationBinDao.getBaseinfoLocationBinList(params);
        List<IBaseinfoLocaltionModel> resList = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocationBin baseinfoLocationBin:list){
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocationBin;
            resList.add(iBaseinfoLocaltionModel);
        }
        return resList;
    }
}
