package com.lsh.wms.core.service.so;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.dao.so.OutbDeliveryDetailDao;
import com.lsh.wms.core.dao.so.OutbDeliveryHeaderDao;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
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

    /**
     * 插入OutbDeliveryHeader及List<OutbDeliveryDetail>
     * @param outbDeliveryHeader
     * @param outbDeliveryDetailList
     */
    @Transactional(readOnly = false)
    public void insert(OutbDeliveryHeader outbDeliveryHeader, List<OutbDeliveryDetail> outbDeliveryDetailList){
        outbDeliveryHeader.setInserttime(new Date());
        outbDeliveryHeaderDao.insert(outbDeliveryHeader);
        for (OutbDeliveryDetail outbDeliveryDetail :outbDeliveryDetailList) {
            outbDeliveryDetail.setDeliveryId(outbDeliveryHeader.getId());
        }
        outbDeliveryDetailDao.batchInsert(outbDeliveryDetailList);
    }

    /**
     * 插入OutbDeliveryHeader及List<OutbDeliveryDetail>
     * @param outbDeliveryHeader
     * @param outbDeliveryDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(OutbDeliveryHeader outbDeliveryHeader, List<OutbDeliveryDetail> outbDeliveryDetailList) {
        outbDeliveryHeaderDao.insert(outbDeliveryHeader);

        outbDeliveryDetailDao.batchInsert(outbDeliveryDetailList);
    }

    /**
     * 更新OutbDeliveryHeader
     * @param outbDeliveryHeader
     */
    @Transactional(readOnly = false)
    public void update(OutbDeliveryHeader outbDeliveryHeader) {
        outbDeliveryHeaderDao.update(outbDeliveryHeader);
    }

    /**
     * 根据DeliveryId更新OutbDeliveryHeader
     * @param outbDeliveryHeader
     */
    @Transactional(readOnly = false)
    public void updateOutbDeliveryHeaderByDeliveryId(OutbDeliveryHeader outbDeliveryHeader) {
        outbDeliveryHeader.setUpdatetime(new Date());

        outbDeliveryHeaderDao.updateByDeliveryId(outbDeliveryHeader);
    }

    /**
     * 通过ID获取OutbDeliveryHeader
     * @param id
     * @return
     */
    public OutbDeliveryHeader getOutbDeliveryHeaderById(Long id) {
        return outbDeliveryHeaderDao.getOutbDeliveryHeaderById(id);
    }

    /**
     * 根据参数获取OutbDeliveryHeader数量
     * @param params
     * @return
     */
    public Integer countOutbDeliveryHeader(Map<String, Object> params) {
        return outbDeliveryHeaderDao.countOutbDeliveryHeader(params);
    }

    /**
     * 自定义参数获取List<OutbDeliveryHeader>
     * @param params
     * @return
     */
    public List<OutbDeliveryHeader> getOutbDeliveryHeaderList(Map<String, Object> params){
        return outbDeliveryHeaderDao.getOutbDeliveryHeaderList(params);
    }

    /**
     * 通过ID获取OutbDeliveryDetail
     * @param id
     * @return
     */
    public OutbDeliveryDetail getOutbDeliveryDetailById(Long id) {
        return outbDeliveryDetailDao.getOutbDeliveryDetailById(id);
    }

    /**
     * 根据参数获取OutbDeliveryDetail数量
     * @param params
     * @return
     */
    public Integer countOutbDeliveryDetail(Map<String, Object> params) {
        return outbDeliveryDetailDao.countOutbDeliveryDetail(params);
    }

    /**
     * 自定义参数获取List<OutbDeliveryDetail>
     * @param params
     * @return
     */
    public List<OutbDeliveryDetail> getOutbDeliveryDetailList(Map<String, Object> params) {
        return outbDeliveryDetailDao.getOutbDeliveryDetailList(params);
    }

    /**
     * 自定义参数获取OutbDeliveryHeader
     * @param params
     * @return
     */
    public OutbDeliveryHeader getOutbDeliveryHeaderByParams(Map<String, Object> params) {
        List<OutbDeliveryHeader> outbDeliveryHeaderList = getOutbDeliveryHeaderList(params);

        if(outbDeliveryHeaderList.size() <=0 || outbDeliveryHeaderList.size() > 1) {
            return null;
        }

        return outbDeliveryHeaderList.get(0);
    }

    /**
     * 根据DeliveryId获取OutbDeliveryHeader
     * @param deliveryId
     * @return
     */
    public OutbDeliveryHeader getOutbDeliveryHeaderByDeliveryId(Long deliveryId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deliveryId", deliveryId);

        return getOutbDeliveryHeaderByParams(params);
    }

    /**
     * 根据DeliveryId获取List<OutbDeliveryDetail>
     * @param deliveryId
     * @return
     */
    public List<OutbDeliveryDetail> getOutbDeliveryDetailListByDeliveryId(Long deliveryId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deliveryId", deliveryId);

        return getOutbDeliveryDetailList(params);
    }

    /**
     * List<OutbDeliveryHeader>填充OutbDeliveryDetail
     * @param outbDeliveryHeaderList
     */
    public void fillDetailToHeaderList(List<OutbDeliveryHeader> outbDeliveryHeaderList) {
       for(OutbDeliveryHeader outbDeliveryHeader : outbDeliveryHeaderList) {
           fillDetailToHeader(outbDeliveryHeader);
       }
    }

    /**
     * OutbDeliveryHeader填充OutbDeliveryDetail
     * @param outbDeliveryHeader
     */
    public void fillDetailToHeader(OutbDeliveryHeader outbDeliveryHeader) {
        if(outbDeliveryHeader == null) {
            return;
        }

        List<OutbDeliveryDetail> outbDeliveryDetailList = getOutbDeliveryDetailListByDeliveryId(outbDeliveryHeader.getDeliveryId());

        outbDeliveryHeader.setDeliveryDetails(outbDeliveryDetailList);
    }

    /**
     * 根据集货道编码获取List<OutbDeliveryHeader>
     * @param shippingAreaCode
     * @return
     */
    public List<OutbDeliveryHeader> getOutbDeliveryHeaderListByShippingAreaCode(String shippingAreaCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shippingAreaCode", shippingAreaCode);

        //获取OutbDeliveryHeader
        List<OutbDeliveryHeader> outbDeliveryHeaderList = getOutbDeliveryHeaderList(params);

        //获取OutbDeliveryDetail
        fillDetailToHeaderList(outbDeliveryHeaderList);

        return outbDeliveryHeaderList;
    }

}
