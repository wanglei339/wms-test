package com.lsh.wms.core.service.baseinfo;

import com.lsh.wms.core.dao.baseinfo.BaseinfoItemTypeDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemTypeRelationDao;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    private BaseinfoItemTypeDao baseinfoItemTypeDao;
    @Autowired
    private BaseinfoItemTypeRelationDao baseinfoItemTypeRelationDao;

    public  void insertItemType(BaseinfoItemType baseinfoItemType){
        baseinfoItemTypeDao.insert(baseinfoItemType);
    }

    public  void updateItemType(BaseinfoItemType baseinfoItemType){
        baseinfoItemTypeDao.update(baseinfoItemType);
    }

    public BaseinfoItemType getBaseinfoItemTypeById(Integer id){
        return baseinfoItemTypeDao.getBaseinfoItemTypeById(id);
    }

    public List<BaseinfoItemType> getBaseinfoItemTypeList(Map<String, Object> params){
        return baseinfoItemTypeDao.getBaseinfoItemTypeList(params);
    }
    public void insertItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation){
        baseinfoItemTypeRelationDao.insert(baseinfoItemTypeRelation);
    }

    public void updateItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation){
        baseinfoItemTypeRelationDao.update(baseinfoItemTypeRelation);
    }

    public void deleteItemTypeRelation(Long id){
        baseinfoItemTypeRelationDao.delete(id);
    }


    public BaseinfoItemTypeRelation getBaseinfoItemTypeRelationById(Long id){
        return baseinfoItemTypeRelationDao.getBaseinfoItemTypeRelationById(id);
    }

    public List<BaseinfoItemTypeRelation> getItemTypeRelationListByItemTypeId(String itemTypeId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("itemTypeId",itemTypeId);
        return baseinfoItemTypeRelationDao.getBaseinfoItemTypeRelationList(params);
    }
    public List<BaseinfoItemTypeRelation> getBaseinfoItemTypeRelationList(Map<String, Object> params){
        return baseinfoItemTypeRelationDao.getBaseinfoItemTypeRelationList(params);
    }
    public List<Map<String, Object>> getBaseinfoItemTypeAllRelationList(Map<String, Object> params){
        return baseinfoItemTypeRelationDao.getBaseinfoItemTypeAllRelationList(params);
    }
    public Integer countBaseinfoItemTypeAllRelationList(Map<String, Object> params){
        return baseinfoItemTypeRelationDao.countBaseinfoItemTypeAllRelationList(params);
    }

}
