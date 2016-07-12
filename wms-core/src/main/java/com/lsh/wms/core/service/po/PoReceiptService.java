package com.lsh.wms.core.service.po;

import com.lsh.wms.core.dao.po.InbReceiptDetailDao;
import com.lsh.wms.core.dao.po.InbReceiptHeaderDao;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
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
 * Package name:com.lsh.wms.core.service.po.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class PoReceiptService {

    @Autowired
    private InbReceiptDetailDao inbReceiptDetailDao;

    @Autowired
    private InbReceiptHeaderDao inbReceiptHeaderDao;


    @Transactional(readOnly = false)
    public void orderInit(InbReceiptHeader inbReceiptHeader, List<InbReceiptDetail> inbReceiptDetailList){
        inbReceiptHeader.setInserttime(new Date());
        inbReceiptHeaderDao.insert(inbReceiptHeader);
        inbReceiptDetailDao.batchInsert(inbReceiptDetailList);
    }

}
