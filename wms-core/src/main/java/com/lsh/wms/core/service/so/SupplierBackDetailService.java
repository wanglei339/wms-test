package com.lsh.wms.core.service.so;


import com.lsh.wms.core.dao.so.SupplierBackDetailDao;
import com.lsh.wms.model.so.SupplierBackDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghongling on 16/12/23.
 */
@Component
@Transactional(readOnly = true)
public class SupplierBackDetailService {
    @Autowired
    private SupplierBackDetailDao supplierBackDetailDao;

    @Transactional(readOnly = false)
    public void batchInsertOrder(List<SupplierBackDetail> supplierBackDetailList) {

        supplierBackDetailDao.batchInsert(supplierBackDetailList);
    }

    @Transactional(readOnly = false)
    public void update(SupplierBackDetail supplierBackDetail) {
        supplierBackDetailDao.update(supplierBackDetail);
    }

    public List<SupplierBackDetail> getSupplierBackDetailList(Map<String, Object> params) {
        return supplierBackDetailDao.getSupplierBackDetailList(params);
    }
}
