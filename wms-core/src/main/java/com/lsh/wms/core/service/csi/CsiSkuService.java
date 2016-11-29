package com.lsh.wms.core.service.csi;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiSkuDao;
import com.lsh.wms.core.service.utils.IdGenerator;
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
    @Autowired
    private CsiSkuDao skuDao;
    @Autowired
    private IdGenerator idGenerator;

    public CsiSku getSku(long iSkuId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("skuId", iSkuId);
        List<CsiSku> items = skuDao.getCsiSkuList(mapQuery);
        if (items.size() == 1) {
            return items.get(0);
        } else {
            return null;
        }
    }

    public CsiSku getSkuByCode(int iCodeType, String sCode) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("codeType", iCodeType);
        mapQuery.put("code", sCode);
        List<CsiSku> items = skuDao.getCsiSkuList(mapQuery);
        if (items.size() == 1) {
            return items.get(0);
        } else {
            return null;
        }
    }
    @Transactional(readOnly = false)
    public void insertSku(CsiSku sku){
        long iSkuId = idGenerator.genId("csi_sku", false, false);
        sku.setSkuId(iSkuId);
        //增加新增时间
        sku.setCreatedAt(DateUtils.getCurrentSeconds());
        this.skuDao.insert(sku);
    }

    @Transactional(readOnly = false)
    public void updateSku(CsiSku sku){
        //增加更新时间
        sku.setUpdatedAt(DateUtils.getCurrentSeconds());
        //更新商品
        skuDao.update(sku);
    }
}
