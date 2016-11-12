package com.lsh.wms.core.service.csi;

import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiCategoryDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zengwenjun on 16/7/8.
 */

@Component
@Transactional(readOnly = true)
public class CsiCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CsiCategoryService.class);
    @Autowired
    private CsiCategoryDao catDao;

    public CsiCategory getCatInfo(long iCatId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("catId", iCatId);
        List<CsiCategory> cats = catDao.getCsiCategoryList(mapQuery);
        if (cats.size() == 1) {
            return cats.get(0);
        } else {
            return null;
        }
    }

    public List<CsiCategory> getFullCatInfo(long iCatId){
        List<CsiCategory> list = new LinkedList<CsiCategory>();
        CsiCategory cat = this.getCatInfo(iCatId);
        if(cat == null){
            return null;
        }
        if(cat.getLevel()==3){
            CsiCategory cat2 = this.getCatInfo(cat.getFCatId());
            if(cat2 == null){
                return null;
            }
            CsiCategory cat1 = this.getCatInfo(cat2.getFCatId());
            if(cat1 == null){
                return null;
            }
            list.add(cat2);
            list.add(cat1);
        } else if ( cat.getLevel() == 2 ){
            CsiCategory cat1 = this.getCatInfo(cat.getFCatId());
            if(cat1 == null){
                return null;
            }
            list.add(cat1);
        } else if ( cat.getLevel() > 3){
            return null;
        }
        return list;
    }

    public List<CsiCategory> getChilds(long iCatId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("fCatId", iCatId);
        List<CsiCategory> cats = catDao.getCsiCategoryList(mapQuery);
        if (cats.size() > 0) {
            return cats;
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    public void insertCategory(CsiCategory category){
        //gen iCatId
        long iCatId = RandomUtils.genId();
        category.setCatId(iCatId);
        catDao.insert(category);
    }

    @Transactional(readOnly = false)
    public void updateCategory(CsiCategory csiCategory){
        catDao.update(csiCategory);
    }
}
