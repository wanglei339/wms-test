package com.lsh.wms.core.service.po;

import com.lsh.wms.core.dao.po.InbPoDetailDao;
import com.lsh.wms.core.dao.po.InbPoHeaderDao;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.po.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class PoOrderService {

    @Autowired
    private InbPoHeaderDao inbPoHeaderDao;

    @Autowired
    private InbPoDetailDao inbPoDetailDao;

    @Transactional(readOnly = false)
    public void orderInit(InbPoHeader inbPoHeader, List<InbPoDetail> inbPoDetailList){
        inbPoHeader.setInserttime(new Date());
        inbPoHeaderDao.insert(inbPoHeader);
        for (InbPoDetail inbPoDetai: inbPoDetailList) {
            inbPoDetai.setOrderId(inbPoHeader.getId());
        }
        inbPoDetailDao.batchInsert(inbPoDetailList);
    }

    @Transactional(readOnly = false)
    public void insertOrder(InbPoHeader inbPoHeader, List<InbPoDetail> inbPoDetailList) {
        //插入订单
        inbPoHeader.setInserttime(new Date());
        inbPoHeader.setOrderStatus(1);
        inbPoHeaderDao.insert(inbPoHeader);

        //插入订单子项
        for(InbPoDetail inbPoDetail : inbPoDetailList) {
            inbPoDetail.setOrderId(inbPoHeader.getId());
        }
        inbPoDetailDao.batchInsert(inbPoDetailList);
    }

    @Transactional(readOnly = false)
    public void editOrder(InbPoHeader inbPoHeader){

    }

    public InbPoHeader getInbPoHeaderById(Integer id){
        return null;
    }

    public Integer countInbPoHeader(Map<String, Object> params){
        return null;
    }

    public List<InbPoHeader> getInbPoHeaderList(Map<String, Object> params){
        return null;
    }


}
