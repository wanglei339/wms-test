package com.lsh.wms.core.service.csi;

import com.lsh.wms.core.dao.csi.CsiOwnerDao;
import com.lsh.wms.core.dao.csi.CsiSupplierDao;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class CsiSupplierService {
    private static final Logger logger = LoggerFactory.getLogger(CsiSupplierService.class);
    private static final ConcurrentMap<Integer, CsiSupplier> m_SupplierCache = new ConcurrentHashMap<Integer, CsiSupplier>();
    @Autowired
    private CsiSupplierDao supplierDao;

    public CsiSupplier getSupplier(int iSupplierId){
        CsiSupplier cat = m_SupplierCache.get(iSupplierId);
        if(cat == null){
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("supplierId", iSupplierId);
            List<CsiSupplier> items = supplierDao.getCsiSupplierList(mapQuery);
            if(items.size() == 1){
                cat = items.get(0);
                m_SupplierCache.put(iSupplierId, cat);
            } else {
                return null;
            }
        }
        return cat;
    }

}
