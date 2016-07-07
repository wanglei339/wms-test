package com.lsh.wms.core.service.item;

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
    private static final ConcurrentMap<Long, CsiSku> m_SkuCache = new ConcurrentHashMap<Long, CsiSku>();
    @Autowired
    private BaseinfoItemDao itemDao;
    @Autowired
    private CsiSkuDao skuDao;

    public CsiSku getSkuBaseInfo(long iSkuId){
        CsiSku sku = m_SkuCache.get(iSkuId);
        if(sku == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("sku_id", iSkuId);
            List<CsiSku> items = skuDao.getCsiSkuList(mapQuery);
            if(items.size() == 1){
                sku = items.get(0);
                m_SkuCache.put(iSkuId, sku);
            } else {
                return null;
            }
        }
        CsiSku new_sku = new CsiSku();
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(new_sku, sku);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new_sku;
    }

    protected CsiSku getSkuByCode(int iCodeType, String sCode){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("code_type", iCodeType);
        mapQuery.put("code", sCode);
        List<CsiSku> items = skuDao.getCsiSkuList(mapQuery);
        return items.size() == 1 ? items.get(0) : null;
    }

    public BaseinfoItem getItem(long iOwnerId, long iSkuId){
        Long key = ((long)iOwnerId)<<32 + (iSkuId);
        BaseinfoItem item = m_ItemCache.get(key);
        if(item == null){
            //cache中不存在,穿透查询mysql
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("owner_id", iOwnerId);
            mapQuery.put("sku_id", iSkuId);
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

    public List<BaseinfoItem> getItemBySkuCode(long iOwnerId, String sSkuCode){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("owner_id", iOwnerId);
        mapQuery.put("sku_code", sSkuCode);
        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
        return items;
    }

    @Transactional(readOnly = false)
    public int insertItem(BaseinfoItem item){
        //查询是否有相应的sku_id分配出来(code_type, code)
        CsiSku sku = this.getSkuByCode(Integer.valueOf(item.getCodeType()), item.getCode());
        if(sku == null){
            //need to create
            CsiSku new_sku = new CsiSku();
            //gen sku_id
            int iSkuId = 0;
            int count = skuDao.countCsiSku(null);
            if(count==0){
                iSkuId = 100001;
            }else{
                iSkuId = 100001 + count;
            }
            new_sku.setSkuId((long)iSkuId);
            skuDao.insert(new_sku);
            sku = new_sku;
        }
        //判断是否存在
        if(this.getItem(item.getOwnerId(), item.getSkuId())!=null){
            return -1;
        }
        //创建商品
        item.setSkuId(sku.getSkuId());
        itemDao.insert(item);
        return 0;
    }

    @Transactional(readOnly = false)
    public int updateItem(BaseinfoItem item){
        //判断是否存在
        if(this.getItem(item.getOwnerId(), item.getSkuId())==null){
            return -1;
        }
        //更新商品
        itemDao.update(item);
        return 0;
    }

    public int deleteItem(BaseinfoItem item){
        return -1;
    }

    //按品类,sku_id,owner等查询
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery){
        return itemDao.getBaseinfoItemList(mapQuery);
    }

}
