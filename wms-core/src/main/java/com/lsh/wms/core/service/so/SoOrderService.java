package com.lsh.wms.core.service.so;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.so.OutbSoDetailDao;
import com.lsh.wms.core.dao.so.OutbSoHeaderDao;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
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

    /**
     * 插入OutbSoHeader及OutbSoDetail
     * @param outbSoHeader
     * @param outbSoDetailList
     */
    @Transactional(readOnly = false)
    public void insert(OutbSoHeader outbSoHeader, List<OutbSoDetail> outbSoDetailList){
        outbSoHeader.setInserttime(new Date());
        outbSoHeader.setOrderId(RandomUtils.genId());
        outbSoHeader.setOrderStatus(BusiConstant.EFFECTIVE_YES);
        outbSoHeaderDao.insert(outbSoHeader);
        for (OutbSoDetail outbSoDetail :outbSoDetailList) {
            outbSoDetail.setOrderId(outbSoHeader.getOrderId());
        }
        outbSoDetailDao.batchInsert(outbSoDetailList);
    }

    /**
     * 插入OutbSoHeader及OutbSoDetail
     * @param outbSoHeader
     * @param outbSoDetailList
     */
    @Transactional
    public void insertOrder(OutbSoHeader outbSoHeader, List<OutbSoDetail> outbSoDetailList) {
        outbSoHeaderDao.insert(outbSoHeader);

        outbSoDetailDao.batchInsert(outbSoDetailList);
    }

    /**
     * 更新OutbSoHeader
     * @param outbSoHeader
     */
    @Transactional(readOnly = false)
    public void update(OutbSoHeader outbSoHeader){
        outbSoHeaderDao.update(outbSoHeader);
    }

    /**
     * 根据OrderOtherId或OrderId更新OutbSoHeader
     * @param outbSoHeader
     */
    @Transactional(readOnly = false)
    public void updateOutbSoHeaderByOrderOtherIdOrOrderId(OutbSoHeader outbSoHeader) {
        outbSoHeader.setUpdatetime(new Date());

        outbSoHeaderDao.updateByOrderOtherIdOrOrderId(outbSoHeader);
    }

    /**
     * 根据ID获取OutbSoHeader
     * @param id
     * @return
     */
    public OutbSoHeader getOutbSoHeaderById(Long id){
        return outbSoHeaderDao.getOutbSoHeaderById(id);
    }

    /**
     * 自定义参数获取OutbSoHeader数量
     * @param params
     * @return
     */
    public Integer countOutbSoHeader(Map<String, Object> params){
        return outbSoHeaderDao.countOutbSoHeader(params);
    }

    /**
     * 根据参数获取List<OutbSoHeader>
     * @param params
     * @return
     */
    public List<OutbSoHeader> getOutbSoHeaderList(Map<String, Object> params){
        return outbSoHeaderDao.getOutbSoHeaderList(params);
    }

    /**
     * 根据ID获取OutbSoDetail
     * @param id
     * @return
     */
    public OutbSoDetail getOutbSoDetailById(Long id) {
        return outbSoDetailDao.getOutbSoDetailById(id);
    }

    /**
     * 自定义参数获取OutbSoDetail数量
     * @param params
     * @return
     */
    public Integer countOutbSoDetail(Map<String, Object> params) {
        return outbSoDetailDao.countOutbSoDetail(params);
    }

    /**
     * 根据参数获取List<OutbSoDetail>
     * @param params
     * @return
     */
    public List<OutbSoDetail> getOutbSoDetailList(Map<String, Object> params){
        return outbSoDetailDao.getOutbSoDetailList(params);
    }

    /**
     * 根据 order_id 获取so订单商品详情
     * @param orderId
     * @return
     */
    public List<OutbSoDetail> getOutbSoDetailListByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getOutbSoDetailList(params);
    }

    /**
     * List<OutbSoHeader>填充OutbSoDetail
     * @param outbSoHeaderList
     */
    public void fillDetailToHeaderList(List<OutbSoHeader> outbSoHeaderList) {
        for(OutbSoHeader outbSoHeader : outbSoHeaderList) {
            fillDetailToHeader(outbSoHeader);
        }
    }

    /**
     * OutbSoHeader填充OutbSoDetail
     * @param outbSoHeader
     */
    public void fillDetailToHeader(OutbSoHeader outbSoHeader) {
        if(outbSoHeader == null) {
            return;
        }

        List<OutbSoDetail> outbSoDetailList = getOutbSoDetailListByOrderId(outbSoHeader.getOrderId());

        outbSoHeader.setOrderDetails(outbSoDetailList);
    }

    /**
     * 根据波次号查询List<OutbSoHeader>
     * @param waveId
     * @return
     */
    public List<OutbSoHeader> getOutSoHeaderListByWaveId(Long waveId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("waveId", waveId);

        List<OutbSoHeader> outbSoHeaderList = outbSoHeaderDao.getOutbSoHeaderList(params);

        fillDetailToHeaderList(outbSoHeaderList);

        return outbSoHeaderList;
    }

    /**
     * 根据OrderId获取OutbSoHeader
     * @param orderId
     * @return
     */
    public OutbSoHeader getOutbSoHeaderByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        //获取OutbSoHeader
        List<OutbSoHeader> outbSoHeaderList = getOutbSoHeaderList(params);
        if(outbSoHeaderList.size() > 1 || outbSoHeaderList.size() <= 0) {
            return null;
        }

        OutbSoHeader outbSoHeader = outbSoHeaderList.get(0);

        //获取OutbSoDetail
        fillDetailToHeader(outbSoHeader);

        return outbSoHeader;
    }

}
