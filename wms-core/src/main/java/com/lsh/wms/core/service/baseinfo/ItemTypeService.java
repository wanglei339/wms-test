package com.lsh.wms.core.service.baseinfo;

import com.lsh.wms.core.dao.baseinfo.BassinfoItemTypeDao;
import com.lsh.wms.core.dao.baseinfo.BassinfoItemTypeRelationDao;
import com.lsh.wms.model.baseinfo.BassinfoItemType;
import com.lsh.wms.model.baseinfo.BassinfoItemTypeRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 课组
 * Created by zhanghongling on 16/11/10.
 */
@Component
@Transactional(readOnly = true)
public class ItemTypeService {
    @Autowired
    private BassinfoItemTypeDao bassinfoItemTypeDao;
    @Autowired
    private BassinfoItemTypeRelationDao bassinfoItemTypeRelationDao;

    public  void insertItemType(BassinfoItemType bassinfoItemType){
        bassinfoItemTypeDao.insert(bassinfoItemType);
    }

    public  void updateItemType(BassinfoItemType bassinfoItemType){
        bassinfoItemTypeDao.update(bassinfoItemType);
    }

    public BassinfoItemType getBassinfoItemTypeById(Integer id){
        return bassinfoItemTypeDao.getBassinfoItemTypeById(id);
    }

    public List<BassinfoItemType> getBassinfoItemTypeList(Map<String, Object> params){
        return bassinfoItemTypeDao.getBassinfoItemTypeList(params);
    }
    public void insertItemTypeRelation(BassinfoItemTypeRelation bassinfoItemTypeRelation){
        bassinfoItemTypeRelationDao.insert(bassinfoItemTypeRelation);
    }

    public void updateItemTypeRelation(BassinfoItemTypeRelation bassinfoItemTypeRelation){
        bassinfoItemTypeRelationDao.update(bassinfoItemTypeRelation);
    }

    public BassinfoItemTypeRelation getBassinfoItemTypeRelationById(Long id){
        return bassinfoItemTypeRelationDao.getBassinfoItemTypeRelationById(id);
    }

    public List<BassinfoItemTypeRelation> getBassinfoItemTypeRelationList(Map<String, Object> params){
        return bassinfoItemTypeRelationDao.getBassinfoItemTypeRelationList(params);
    }
}
