package com.lsh.wms.core.service.po;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.dao.po.InbPoDetailDao;
import com.lsh.wms.core.dao.po.InbReceiptDetailDao;
import com.lsh.wms.core.dao.po.InbReceiptHeaderDao;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private InbPoDetailDao inbPoDetailDao;

    /**
     * 插入InbReceiptHeader及List<InbReceiptDetail>
     * @param inbReceiptHeader
     * @param inbReceiptDetailList
     */
    @Transactional(readOnly = false)
    public void orderInit(InbReceiptHeader inbReceiptHeader, List<InbReceiptDetail> inbReceiptDetailList){
        inbReceiptHeader.setInserttime(new Date());
        inbReceiptHeaderDao.insert(inbReceiptHeader);
        for (InbReceiptDetail inbReceiptDetail:inbReceiptDetailList) {
            inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
        }
        inbReceiptDetailDao.batchInsert(inbReceiptDetailList);
    }

    /**
     * 插入InbReceiptHeader及List<InbReceiptDetail>
     * @param inbReceiptHeader
     * @param inbReceiptDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(InbReceiptHeader inbReceiptHeader, List<InbReceiptDetail> inbReceiptDetailList,List<InbPoDetail> updateInbPoDetailList ) {
        //插入订单
        inbReceiptHeader.setInserttime(new Date());
        inbReceiptHeaderDao.insert(inbReceiptHeader);
        inbReceiptDetailDao.batchInsert(inbReceiptDetailList);
        inbPoDetailDao.batchUpdateInboundQtyByOrderIdAndSkuId(updateInbPoDetailList);

    }

    /**
     * 根据ReceiptId更新InbReceiptHeader
     * @param inbReceiptHeader
     */
    @Transactional(readOnly = false)
    public void updateInbReceiptHeaderByReceiptId(InbReceiptHeader inbReceiptHeader) {
        inbReceiptHeader.setUpdatetime(new Date());

        inbReceiptHeaderDao.updateByReceiptId(inbReceiptHeader);
    }

    /**
     * 通过ID获取InbReceiptHeader
     * @param id
     * @return
     */
    public InbReceiptHeader getInbReceiptHeaderById(Long id) {
        return inbReceiptHeaderDao.getInbReceiptHeaderById(id);
    }

    /**
     * 根据参数获取InbReceiptHeader数量
     * @param params
     * @return
     */
    public Integer countInbReceiptHeader(Map<String, Object> params) {
        return inbReceiptHeaderDao.countInbReceiptHeader(params);
    }

    /**
     * 自定义参数获取List<InbReceiptHeader>
     * @param params
     * @return
     */
    public List<InbReceiptHeader> getInbReceiptHeaderList(Map<String, Object> params) {
        return inbReceiptHeaderDao.getInbReceiptHeaderList(params);
    }

    /**
     * 通过ID获取InbReceiptDetail
     * @param id
     * @return
     */
    public InbReceiptDetail getInbReceiptDetailById(Long id) {
        return inbReceiptDetailDao.getInbReceiptDetailById(id);
    }

    /**
     * 根据参数获取InbReceiptDetail数量
     * @param params
     * @return
     */
    public Integer countInbReceiptDetail(Map<String, Object> params) {
        return inbReceiptDetailDao.countInbReceiptDetail(params);
    }

    /**
     * 自定义参数获取List<InbReceiptDetail>
     * @param params
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailList(Map<String, Object> params) {
        return inbReceiptDetailDao.getInbReceiptDetailList(params);
    }

    /**
     * 自定义参数获取InbReceiptHeader
     * @param params
     * @return
     */
    public InbReceiptHeader getInbReceiptHeaderByParams(Map<String, Object> params) {
        List<InbReceiptHeader> inbReceiptHeaderList = getInbReceiptHeaderList(params);

        if(inbReceiptHeaderList.size() <= 0 || inbReceiptHeaderList.size() > 1) {
            return  null;
        }

        return inbReceiptHeaderList.get(0);
    }

    /**
     * 根据ReceiptId获取InbReceiptHeader
     * @param receiptId
     * @return
     */
    public InbReceiptHeader getInbReceiptHeaderByReceiptId(Long receiptId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("receiptOrderId", receiptId);

        return getInbReceiptHeaderByParams(params);
    }

    /**
     * 根据ReceiptId获取List<InbReceiptDetail>
     * @param receiptId
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailListByReceiptId(Long receiptId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("receiptOrderId", receiptId);

        return getInbReceiptDetailList(params);
    }

    /**
     * 根据OrderId获取List<InbReceiptDetail>
     * @param orderId
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailListByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getInbReceiptDetailList(params);
    }

    /**
     * List<InbReceiptHeader>填充InbReceiptDetail
     * @param inbReceiptHeaderList
     */
    public void fillDetailToHeaderList(List<InbReceiptHeader> inbReceiptHeaderList) {
        for(InbReceiptHeader inbReceiptHeader : inbReceiptHeaderList) {
            fillDetailToHeader(inbReceiptHeader);
        }
    }

    /**
     * InbReceiptHeader填充InbReceiptDetail
     * @param inbReceiptHeader
     */
    public void fillDetailToHeader(InbReceiptHeader inbReceiptHeader) {
        if(inbReceiptHeader == null) {
            return;
        }

        List<InbReceiptDetail> inbReceiptDetailList = getInbReceiptDetailListByReceiptId(inbReceiptHeader.getReceiptOrderId());

        inbReceiptHeader.setReceiptDetails(inbReceiptDetailList);
    }
}
