package com.lsh.wms.core.service.item;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemQuantRangeDao;
import com.lsh.wms.core.dao.csi.CsiSkuDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemQuantRange;
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
    @Autowired
    private BaseinfoItemQuantRangeDao rangeDao;
    @Autowired
    private CsiSkuService csiSkuService;
    @Autowired
    private CsiSkuDao csiSkuDao;
    @Autowired
    private IdGenerator idGenerator;


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
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
        }
        return new_item;
    }
    public BaseinfoItemQuantRange getItemRange(Long itemId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId",itemId);
        List<BaseinfoItemQuantRange> ranges = rangeDao.getBaseinfoItemQuantRangeList(mapQuery);

        return ranges==null ||ranges.isEmpty() ? null :ranges.get(0);
    }
    @Transactional(readOnly = false)
    public void insertItemRange(BaseinfoItemQuantRange range){
        range.setUpdatedAt(DateUtils.getCurrentSeconds());
        range.setCreatedAt(DateUtils.getCurrentSeconds());
        rangeDao.insert(range);

    }
    @Transactional(readOnly = false)
    public void updateItemRange(BaseinfoItemQuantRange range){
        range.setUpdatedAt(DateUtils.getCurrentSeconds());
        rangeDao.update(range);

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
        //如果sku表中不存在,更新sku表
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("code",item.getCode());
        mapQuery.put("codeType",item.getCodeType());
        List<CsiSku> skus = csiSkuDao.getCsiSkuList(mapQuery);
        if(skus.size() > 0){
            item.setSkuId(skus.get(0).getSkuId());
        }else{
            CsiSku sku = new CsiSku();
            String code = item.getCode();
            sku.setCode(code);
            sku.setCodeType(item.getCodeType().toString());
            sku.setShelfLife(item.getShelfLife());
            sku.setSkuName(item.getSkuName());
            sku.setHeight(item.getHeight());
            sku.setLength(item.getLength());
            sku.setWidth(item.getWidth());
            sku.setWeight(item.getWeight());
            sku.setCreatedAt(DateUtils.getCurrentSeconds());
            //生成csi_sku表
            csiSkuService.insertSku(sku);
            item.setSkuId(sku.getSkuId());
        }
        //gen itemId

        item.setItemId(idGenerator.genId("item_main", false, false));
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
        //更新缓存
        BaseinfoItem newItem = this.getItem(item.getItemId());
        Long key = ((newItem.getOwnerId())<<32) + (newItem.getSkuId());
        m_ItemCache.put(key,newItem);
    }

    public int deleteItem(BaseinfoItem item){
        return -1;
    }

    //按品类,sku_id,owner等查询
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery){
        //增加skuName模糊查询
        String skuName = (String) mapQuery.get("skuName");
        if(skuName != null){
            skuName = "%"+skuName+"%";
            mapQuery.put("skuName",skuName);
        }

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

    @Transactional(readOnly = false)
    public void setStatus(long iItemId,long iStatus){
        BaseinfoItem item = new BaseinfoItem();
        item.setItemId(iItemId);
        item.setStatus(iStatus);
        this.updateItem(item);
    }



}
