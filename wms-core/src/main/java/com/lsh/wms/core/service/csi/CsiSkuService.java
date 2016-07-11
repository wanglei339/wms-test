package com.lsh.wms.core.service.csi;

import com.lsh.wms.core.dao.csi.CsiSkuDao;
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
    public CsiSku insertSku(CsiSku sku){
        if(sku.getSkuId()==0){
            CsiSku o_sku = this.getSkuByCode(Integer.valueOf(sku.getCodeType()), sku.getCode());
            if(o_sku == null){
                //gen sku_id
                int iSkuId = 0;
                int count = skuDao.countCsiSku(null);
                if(count==0){
                    iSkuId = 100001;
                }else{
                    iSkuId = 100001 + count;
                }
                sku.setSkuId((long)iSkuId);
            }else{
                sku.setSkuId(o_sku.getSkuId());
                return sku;
            }
        }
        this.skuDao.insert(sku);
        return sku;
    }
}
