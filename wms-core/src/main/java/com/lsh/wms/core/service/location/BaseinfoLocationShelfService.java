package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationShelfDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocationShelf;
import com.lsh.wms.model.baseinfo.IBaseinfoLocaltionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/7/23 下午7:22
 */
@Component
@Transactional(readOnly = true)
public class BaseinfoLocationShelfService implements IStrategy{

    @Autowired
    private BaseinfoLocationShelfDao baseinfoLocationShelfDao;

    @Transactional(readOnly = false)
    public void insert(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationShelfDao.insert((BaseinfoLocationShelf) baseinfoLocaltionModel);
    }

    @Transactional(readOnly = false)
    public void update(IBaseinfoLocaltionModel baseinfoLocaltionModel) {
        baseinfoLocationShelfDao.update((BaseinfoLocationShelf) baseinfoLocaltionModel);
    }

    public IBaseinfoLocaltionModel getBaseinfoItemLocationModelById(Long id) {
        return baseinfoLocationShelfDao.getBaseinfoLocationShelfById(id);
    }

    public Integer countBaseinfoLocaltionModel(Map<String, Object> params) {

        return baseinfoLocationShelfDao.countBaseinfoLocationShelf(params);
    }

    public List<IBaseinfoLocaltionModel> getBaseinfoLocaltionModelList(Map<String, Object> params) {
        List<BaseinfoLocationShelf> list = baseinfoLocationShelfDao.getBaseinfoLocationShelfList(params);
        List<IBaseinfoLocaltionModel> resList = new ArrayList<IBaseinfoLocaltionModel>();
        for (BaseinfoLocationShelf baseinfoLocationShelf:list){
            IBaseinfoLocaltionModel iBaseinfoLocaltionModel = baseinfoLocationShelf;
            resList.add(iBaseinfoLocaltionModel);
        }
        return resList;
    }
}
