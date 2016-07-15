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
    private static final ConcurrentMap<Long, CsiCategory> m_CatCache = new ConcurrentHashMap<Long, CsiCategory>();
    private static final ConcurrentMap<Long, List<CsiCategory>> m_CatChildCache = new ConcurrentHashMap<Long, List<CsiCategory>>();

    @Autowired
    private CsiCategoryDao catDao;

    public CsiCategory getCatInfo(long iCatId){
        CsiCategory cat = m_CatCache.get(iCatId);
        if(cat == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("catId", iCatId);
            List<CsiCategory> items = catDao.getCsiCategoryList(mapQuery);
            if(items.size() == 1){
                cat = items.get(0);
                m_CatCache.put(iCatId, cat);
            } else {
                return null;
            }
        }
        return cat;
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

    public List<CsiCategory> getChilds(long iCatId){
        List<CsiCategory> cats = m_CatChildCache.get(iCatId);
        if(cats == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("fCatId", iCatId);
            List<CsiCategory> items = catDao.getCsiCategoryList(mapQuery);
            if(items.size() > 0){
                cats = items;
                m_CatChildCache.put(iCatId, cats);
            } else {
                return null;
            }
        }
        return cats;

    }

    @Transactional(readOnly = false)
    public void insertCategory(CsiCategory category){
        //gen iCatId
        long iCatId = RandomUtils.genId();
        category.setCatId(iCatId);
        catDao.insert(category);
        //更新缓存
        m_CatCache.put(category.getCatId(),category);

    }

    @Transactional(readOnly = false)
    public void updateCategory(CsiCategory csiCategory){
        catDao.update(csiCategory);
        //更新缓存
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("catId", csiCategory.getCatId());
        CsiCategory newCategory = catDao.getCsiCategoryList(mapQuery).get(0);
        m_CatCache.put(csiCategory.getCatId(),newCategory);
    }
}
