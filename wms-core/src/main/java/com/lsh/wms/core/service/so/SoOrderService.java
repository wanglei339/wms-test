package com.lsh.wms.core.service.so;

import com.lsh.wms.core.dao.so.OutbSoDetailDao;
import com.lsh.wms.core.dao.so.OutbSoHeaderDao;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.so.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class SoOrderService {

    @Autowired
    private OutbSoHeaderDao outbSoHeaderDao;
    @Autowired
    private OutbSoDetailDao outbSoDetailDao;

    @Transactional(readOnly = false)
    public void insert(OutbSoHeader outbSoHeader, List<OutbSoDetail> outbSoDetailList){
        outbSoHeader.setInserttime(new Date());
        outbSoHeaderDao.insert(outbSoHeader);
        for (OutbSoDetail outbSoDetail :outbSoDetailList) {
            outbSoDetail.setOrderId(outbSoHeader.getId());
        }
        outbSoDetailDao.batchInsert(outbSoDetailList);
    }

    @Transactional(readOnly = false)
    public void update(OutbSoHeader outbSoHeader){
        outbSoHeaderDao.update(outbSoHeader);
    }

    public OutbSoHeader getOutbSoHeaderById(Long id){
        return null;
    }

    public Integer countOutbSoHeader(Map<String, Object> params){
        return null;
    }

    public List<OutbSoHeader> getOutbSoHeaderList(Map<String, Object> params){
        return null;
    }
}
