package com.lsh.wms.core.service.so;

import com.lsh.wms.core.dao.so.OutbDeliveryDetailDao;
import com.lsh.wms.core.dao.so.OutbDeliveryHeaderDao;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.so.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class SoDeliveryService {

    @Autowired
    private OutbDeliveryHeaderDao outbDeliveryHeaderDao;

    @Autowired
    private OutbDeliveryDetailDao outbDeliveryDetailDao;

    @Transactional(readOnly = false)
    public void insert(OutbDeliveryHeader outbDeliveryHeader, List<OutbDeliveryDetail> outbDeliveryDetailList){
        outbDeliveryHeader.setInserttime(new Date());
        outbDeliveryHeaderDao.insert(outbDeliveryHeader);
        for (OutbDeliveryDetail outbDeliveryDetail :outbDeliveryDetailList) {
            outbDeliveryDetail.setDeliveryId(outbDeliveryHeader.getId());
        }
        outbDeliveryDetailDao.batchInsert(outbDeliveryDetailList);
    }
}
