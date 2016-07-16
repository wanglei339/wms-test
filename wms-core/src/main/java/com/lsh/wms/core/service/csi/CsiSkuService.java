package com.lsh.wms.core.service.csi;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiSkuDao;
import com.lsh.wms.model.csi.CsiSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zengwenjun on 16/7/8.
 */

@Component
@Transactional(readOnly = true)
public class CsiSkuService {
    private static final Logger logger = LoggerFactory.getLogger(CsiSkuService.class);
    private static final ConcurrentMap<Long, CsiSku> m_SkuCache = new ConcurrentHashMap<Long, CsiSku>();
    private static final ConcurrentMap<String, CsiSku> m_SkuCacheByCode = new ConcurrentHashMap<String, CsiSku>();
    @Autowired
    private CsiSkuDao skuDao;

    public CsiSku getSku(long iSkuId){
        CsiSku sku = m_SkuCache.get(iSkuId);
        if(sku == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("skuId", iSkuId);
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

    public CsiSku getSkuByCode(int iCodeType, String sCode){
        String key = String.format("T_%d_LSHWMS_%s", iCodeType, sCode);
        CsiSku sku = m_SkuCacheByCode.get(key);
        if(sku == null) {
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("codeType", iCodeType);
            mapQuery.put("code", sCode);
            List<CsiSku> items = skuDao.getCsiSkuList(mapQuery);
            if(items.size() == 1){
                sku = items.get(0);
                m_SkuCacheByCode.put(key, sku);
            }else{
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
    @Transactional(readOnly = false)
    public void insertSku(CsiSku sku){
        long iSkuId = RandomUtils.genId();
        sku.setSkuId(iSkuId);
        //增加新增时间
        sku.setCreatedAt(DateUtils.getCurrentSeconds());
        this.skuDao.insert(sku);
        //更新缓存
        m_SkuCache.put(sku.getSkuId(),sku);
    }

    @Transactional(readOnly = false)
    public void updateSku(CsiSku sku){
        //增加更新时间
        sku.setUpdatedAt(DateUtils.getCurrentSeconds());
        //更新商品
        skuDao.update(sku);

        //更新缓存
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("skuId", sku.getSkuId());
        CsiSku newSku = skuDao.getCsiSkuList(mapQuery).get(0);
        m_SkuCache.put(sku.getSkuId(),newSku);
    }
}
