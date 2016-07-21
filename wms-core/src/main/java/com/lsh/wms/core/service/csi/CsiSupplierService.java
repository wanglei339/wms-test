package com.lsh.wms.core.service.csi;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.csi.CsiOwnerDao;
import com.lsh.wms.core.dao.csi.CsiSupplierDao;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
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
    //将int改为long modify by lixin
    private static final ConcurrentMap<Long, CsiSupplier> m_SupplierCache = new ConcurrentHashMap<Long, CsiSupplier>();
    @Autowired
    private CsiSupplierDao supplierDao;
    //将int改为long modify by lixin
    public CsiSupplier getSupplier(long iSupplierId){
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

    @Transactional(readOnly = false)
    public void insertSupplier(CsiSupplier supplier){
        //gen supplier_id
        long iSupplierId = RandomUtils.genId();
        supplier.setSupplierId(iSupplierId);
        //新增时间
        supplier.setCreatedAt(DateUtils.getCurrentSeconds());
        supplierDao.insert(supplier);
        //更新缓存
        m_SupplierCache.put(supplier.getSupplierId(),supplier);
    }

    @Transactional(readOnly = false)
    public void updateSupplier(CsiSupplier supplier){
        //更新时间
        supplier.setUpdatedAt(DateUtils.getCurrentSeconds());
        //更新供应商信息
        supplierDao.update(supplier);
        //更新缓存中的数据
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("supplierId", supplier.getSupplierId());
        CsiSupplier newSupplier = supplierDao.getCsiSupplierList(mapQuery).get(0);
        m_SupplierCache.put(supplier.getSupplierId(),newSupplier);
    }

    public List<CsiSupplier> getSupplerList(Map<String,Object> mapQuery){
        return supplierDao.getCsiSupplierList(mapQuery);

    }
    public int getSupplerCount(Map<String,Object> mapQuery){
        return supplierDao.countCsiSupplier(mapQuery);

    }

}
