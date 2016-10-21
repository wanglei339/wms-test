package com.lsh.wms.core.service.po;

import com.lsh.wms.core.dao.po.ReceiveDetailDao;
import com.lsh.wms.core.dao.po.ReceiveHeaderDao;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.po.ReceiveHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 根据参数获取receiveHeaderList
     */
    List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params){
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

}
