package com.lsh.wms.core.service.po;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.dao.po.IbdDetailDao;
import com.lsh.wms.core.dao.po.InbReceiptDetailDao;
import com.lsh.wms.core.dao.po.ReceiveDetailDao;
import com.lsh.wms.core.dao.po.ReceiveHeaderDao;
import com.lsh.wms.core.service.persistence.PersistenceManager;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.po.ReceiveHeader;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
@Component
@Transactional(readOnly = true)
public class ReceiveService {

    @Autowired
    private ReceiveHeaderDao receiveHeaderDao;

    @Autowired
    private ReceiveDetailDao receiveDetailDao;

    @Autowired
    private InbReceiptDetailDao inbReceiptDetailDao;

    @Autowired
    private IbdDetailDao ibdDetailDao;

    @Autowired
    private PersistenceProxy persistenceProxy;

    @Autowired
    private PersistenceManager persistenceManager;


    /**
     * 根据参数获取receiveHeaderList
     */
    public List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params){
        return receiveHeaderDao.getReceiveHeaderList(params);
    }

    /**
     * 根据参数获取receiveHeaderList count
     */
    public Integer countReceiveHeader(Map<String, Object> params){
        return receiveHeaderDao.countReceiveHeader(params);
    }

    /**
     * 根据 orderId 获取正常状态的receiveHeader
     */
    public ReceiveHeader getReceiveHeader(Long orderId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderId",orderId);
        mapQuery.put("orderStatus",1);
        List<ReceiveHeader> list = this.getReceiveHeaderList(mapQuery);
        if(list.size() <= 0){
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据receiveId获取receiveHeader
     */
    public ReceiveHeader getReceiveHeaderByReceiveId(Long receiveId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("receiveId",receiveId);
        List<ReceiveHeader> list = this.getReceiveHeaderList(mapQuery);
        if(list.size() <= 0){
            return null;
        }
        return list.get(0);
    }

    /**
     * 插入receiveHeader及receiveDetail
     * @param receiveHeader
     * @param receiveDetails
     */
    @Transactional(readOnly = false)
    public void insertReceive(ReceiveHeader receiveHeader, List<ReceiveDetail> receiveDetails) {

        receiveHeaderDao.insert(receiveHeader);

        receiveDetailDao.batchInsert(receiveDetails);

    }

    /**
     * 根据OrderId及skuCode获取InbPoDetail
     * @param receiveId
     * @param skuCode
     * @return
     */
    public ReceiveDetail getReceiveDetailByReceiveIdAndSkuCode(Long receiveId, String skuCode) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("receiveId", receiveId);
        params.put("skuCode", skuCode);

        return getReceiveDetail(params);
    }

    /**
     * 根据条件查询receiveDetail
     */
    public ReceiveDetail getReceiveDetail(Map<String,Object> params){
        List<ReceiveDetail> receiveDetails =  receiveDetailDao.getReceiveDetailList(params);
        if(receiveDetails.size() <= 0){
            return null;
        }
        return receiveDetails.get(0);
    }

    /**
     * 获取receiveDetailList
     */
    public List<ReceiveDetail> getReceiveDetailListByReceiveId(Long receiveId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("receiveId",receiveId);
        List<ReceiveDetail> receiveDetails = receiveDetailDao.getReceiveDetailList(map);
        if (receiveDetails.size() <=0 ) {
            return null;
        }
        return receiveDetails;
    }


    /**
     * receiveHeader填充receiveDetail
     * @param receiveHeader
     */
    public void fillDetailToHeader(ReceiveHeader receiveHeader) {
        if(receiveHeader == null) {
            return;
        }

        List<ReceiveDetail> receiveDetails = this.getReceiveDetailListByReceiveId(receiveHeader.getReceiveId());

        receiveHeader.setReceiveDetails(receiveDetails);
    }

    /**
     * 修改receiveHeader状态
     */
    @Transactional(readOnly = false)
    public void updateStatus(ReceiveHeader receiveHeader){
        receiveHeader.setUpdatedAt(DateUtils.getCurrentSeconds());
        receiveHeaderDao.update(receiveHeader);
        persistenceProxy.doOne(SysLogConstant.LOG_TYPE_IBD,receiveHeader.getReceiveId());
    }

    /**
     * 根据receive_id 与detail_other_id来修改receive_detail
     */
    @Transactional(readOnly = false)
    public void updateByReceiveIdAndDetailOtherId(ReceiveDetail receiveDetail){
        receiveDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
        receiveDetailDao.updateByReceiveIdAndDetailOtherId(receiveDetail);
    }


    /**
     * 根据OrderId及detailOtherId获取InbPoDetail
     * @param receiveId
     * @param detailOtherId
     * @return
     */
    public ReceiveDetail getReceiveDetailByReceiveIdAnddetailOtherId(Long receiveId, String detailOtherId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("receiveId", receiveId);
        params.put("detailOtherId", detailOtherId);

        return getReceiveDetail(params);
    }

    @Transactional(readOnly = false)
    public void updateQty(ReceiveDetail receiveDetail, IbdDetail ibdDetail, InbReceiptDetail inbReceiptDetail){
        receiveDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
        receiveDetailDao.update(receiveDetail);
        ibdDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
        ibdDetailDao.update(ibdDetail);
        inbReceiptDetail.setUpdatetime(new Date());
        inbReceiptDetailDao.update(inbReceiptDetail);

    }


    /**
     *根据order_id获取receiveHeaderList
     */
    public List<ReceiveHeader> getReceiveHeaderList(Long orderId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderId",orderId);
        List<ReceiveHeader> list = this.getReceiveHeaderList(mapQuery);
        if(list.size() <= 0){
            return null;
        }
        return list;
    }



}
