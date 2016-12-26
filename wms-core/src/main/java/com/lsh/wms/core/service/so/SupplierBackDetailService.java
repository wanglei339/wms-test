package com.lsh.wms.core.service.so;


import com.lsh.wms.core.dao.so.SupplierBackDetailDao;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.SupplierBackDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private SoOrderService soOrderService;

    @Transactional(readOnly = false)
    public void batchInsertOrder(List<SupplierBackDetail> addList,List<SupplierBackDetail> updateList,ObdDetail obdDetail) {
        if(addList != null && addList.size() > 0){
            supplierBackDetailDao.batchInsert(addList);
        }
        if(updateList != null && updateList.size() > 0){
            for(SupplierBackDetail supplierBackDetail : updateList){
                supplierBackDetailDao.update(supplierBackDetail);
            }
        }
        //更新obdDetail实收数量
        soOrderService.updateObdDetail(obdDetail);

    }

    @Transactional(readOnly = false)
    public void update(SupplierBackDetail supplierBackDetail,ObdDetail obdDetail) {
        supplierBackDetailDao.update(supplierBackDetail);
        //更新obdDetail实收数量
        soOrderService.updateObdDetail(obdDetail);
    }

    public List<SupplierBackDetail> getSupplierBackDetailList(Map<String, Object> params) {
        return supplierBackDetailDao.getSupplierBackDetailList(params);
    }
}
