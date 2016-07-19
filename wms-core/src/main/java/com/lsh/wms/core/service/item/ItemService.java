package com.lsh.wms.core.service.item;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemDao;
import com.lsh.wms.core.dao.csi.CsiSkuDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zengwenjun on 16/7/6.
 */

@Component
@Transactional(readOnly = true)
public class ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private static final ConcurrentMap<Long, BaseinfoItem> m_ItemCache = new ConcurrentHashMap<Long, BaseinfoItem>();
    @Autowired
    private BaseinfoItemDao itemDao;


    public BaseinfoItem getItem(long iOwnerId, long iSkuId){
        Long key = (((long)iOwnerId)<<32) + (iSkuId);
        BaseinfoItem item = m_ItemCache.get(key);
        if(item == null){
            //cache中不存在,穿透查询mysql
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("ownerId", iOwnerId);
            mapQuery.put("skuId", iSkuId);
            List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
            if(items.size() == 1){
                item = items.get(0);
                m_ItemCache.put(key, item);
            } else {
                return null;
            }
        }
        BaseinfoItem new_item = new BaseinfoItem();
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(new_item, item);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new_item;
    }

    public List<BaseinfoItem> getItemsBySkuCode(long iOwnerId, String sSkuCode){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ownerId", iOwnerId);
        mapQuery.put("skuCode", sSkuCode);
        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
        return items;
    }

    @Transactional(readOnly = false)
    public BaseinfoItem insertItem(BaseinfoItem item){
        //gen itemId
        item.setItemId(RandomUtils.genId());
        item.setCreatedAt(DateUtils.getCurrentSeconds());
        //创建商品
        itemDao.insert(item);
        return item;
    }

    @Transactional(readOnly = false)
    public void updateItem(BaseinfoItem item){
        item.setUpdatedAt(DateUtils.getCurrentSeconds());
        //更新商品
        itemDao.update(item);
    }

    public int deleteItem(BaseinfoItem item){
        return -1;
    }

    //按品类,sku_id,owner等查询
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery){
        return itemDao.getBaseinfoItemList(mapQuery);
    }

    //按获取count
    public int countItem(Map<String, Object> mapQuery){
        return itemDao.countBaseinfoItem(mapQuery);
    }

    public BaseinfoItem getItem(long itemId){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", itemId);
        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
        return items.size() == 0 ? null : items.get(0);
    }



}
