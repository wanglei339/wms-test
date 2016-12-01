package com.lsh.wms.core.service.baseinfo;

import com.lsh.wms.core.dao.baseinfo.BaseinfoItemTypeDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemTypeRelationDao;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional(readOnly = false)
    public  void insertItemType(BaseinfoItemType baseinfoItemType){
        baseinfoItemTypeDao.insert(baseinfoItemType);
    }
    @Transactional(readOnly = false)
    public  void updateItemType(BaseinfoItemType baseinfoItemType){
        baseinfoItemTypeDao.update(baseinfoItemType);
    }

    public BaseinfoItemType getBaseinfoItemTypeByItemId(Integer itemTypeId){
        return baseinfoItemTypeDao.getBaseinfoItemTypeByItemId(itemTypeId);
    }

    public List<BaseinfoItemType> getBaseinfoItemTypeList(Map<String, Object> params){
        return baseinfoItemTypeDao.getBaseinfoItemTypeList(params);
    }
    public Integer countItemtypeList(Map<String, Object> params){
        return baseinfoItemTypeDao.countBaseinfoItemType(params);
    }
    @Transactional(readOnly = false)
    public void insertItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation){
        baseinfoItemTypeRelationDao.insert(baseinfoItemTypeRelation);
    }
    @Transactional(readOnly = false)
    public void updateItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation){
        baseinfoItemTypeRelationDao.update(baseinfoItemTypeRelation);
    }
    @Transactional(readOnly = false)
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
    public Map<Long, ArrayList<Long>> getBaseinfoItemTypeAllRelationList(Map<String, Object> params){
        List<BaseinfoItemTypeRelation> list = getBaseinfoItemTypeRelationList(params);
        if(list == null || list.size() <= 0){
            return null;
        }
        Map<Long, ArrayList<Long>> returnMap = new HashMap<Long, ArrayList<Long>>();
        for(BaseinfoItemTypeRelation b : list){
            if(returnMap.get(b.getItemTypeId()) == null){
                ArrayList<Long> mutexList= new ArrayList<Long>();
                mutexList.add(b.getItemMutexId());
                returnMap.put(b.getItemTypeId(),mutexList);
            }else{
                ArrayList<Long> mutexList = returnMap.get(b.getItemTypeId());
                mutexList.add(b.getItemMutexId());
                returnMap.put(b.getItemTypeId(),mutexList);
            }
            if(returnMap.get(b.getItemMutexId()) == null){
                ArrayList<Long> mutexList= new ArrayList<Long>();
                mutexList.add(b.getItemTypeId());
                returnMap.put(b.getItemMutexId(),mutexList);
            }else{
                ArrayList<Long> mutexList = returnMap.get(b.getItemMutexId());
                mutexList.add(b.getItemTypeId());
                returnMap.put(b.getItemMutexId(),mutexList);
            }

        }

        return returnMap;
    }

}
