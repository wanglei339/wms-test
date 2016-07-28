package com.lsh.wms.core.service.po;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.dao.po.InbPoDetailDao;
import com.lsh.wms.core.dao.po.InbPoHeaderDao;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
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

    /**
     * 插入InbPoHeader及InbPoDetail
     * @param inbPoHeader
     * @param inbPoDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(InbPoHeader inbPoHeader, List<InbPoDetail> inbPoDetailList) {
        inbPoHeaderDao.insert(inbPoHeader);

        inbPoDetailDao.batchInsert(inbPoDetailList);
    }

    /**
     * 根据ID编辑InbPoHeader
     * @param inbPoHeader
     */
    @Transactional(readOnly = false)
    public void updateInbPoHeader(InbPoHeader inbPoHeader){
        inbPoHeader.setUpdatetime(new Date());

        inbPoHeaderDao.update(inbPoHeader);
    }

    /**
     * 根据OrderOtherId或OrderId更新InbPoHeader
     * @param inbPoHeader
     */
    @Transactional(readOnly = false)
    public void updateInbPoHeaderByOrderOtherIdOrOrderId(InbPoHeader inbPoHeader){
        inbPoHeader.setUpdatetime(new Date());

        inbPoHeaderDao.updateByOrderOtherIdOrOrderId(inbPoHeader);
    }

    /**
     * 根据ID获取InbPoHeader
     * @param id
     * @return
     */
    public InbPoHeader getInbPoHeaderById(Long id){
        return inbPoHeaderDao.getInbPoHeaderById(id);
    }

    /**
     * 根据参数获取InbPoHeader数量
     * @param params
     * @return
     */
    public Integer countInbPoHeader(Map<String, Object> params){
        return inbPoHeaderDao.countInbPoHeader(params);
    }

    /**
     * 自定义参数获取List<InbPoHeader>
     * @param params
     * @return
     */
    public List<InbPoHeader> getInbPoHeaderList(Map<String, Object> params){
        return inbPoHeaderDao.getInbPoHeaderList(params);
    }

    /**
     * 根据ID编辑InbPoDetail
     * @param inbPoDetail
     */
    @Transactional(readOnly = false)
    public void updateInbPoDetail(InbPoDetail inbPoDetail) {
        inbPoDetailDao.update(inbPoDetail);
    }

    /**
     * 编辑InboundQty
     * @param inboundQty
     * @param orderId
     * @param skuId
     */
    @Transactional(readOnly = false)
    public void updateInbPoDetailInboundQtyByOrderIdAndSkuId(Long inboundQty, Long orderId, Long skuId) {
        inbPoDetailDao.updateInboundQtyByOrderIdAndSkuId(inboundQty, orderId, skuId);
    }

    /**
     * 根据ID获取InbPoDetail
     * @param id
     * @return
     */
    public InbPoDetail getInbPoDetailById(Long id) {
        return inbPoDetailDao.getInbPoDetailById(id);
    }

    /**
     * 根据参数获取InbPoDetail数量
     * @param params
     * @return
     */
    public Integer countInbPoDetail(Map<String, Object> params) {
        return inbPoDetailDao.countInbPoDetail(params);
    }

    /**
     * 自定义参数获取List<InbPoDetail>
     * @param params
     * @return
     */
    public List<InbPoDetail> getInbPoDetailList(Map<String, Object> params) {
        return inbPoDetailDao.getInbPoDetailList(params);
    }

    /**
     * 自定义参数获取InbPoHeader
     * @param params
     * @return
     */
    public InbPoHeader getInbPoHeaderByParams(Map<String, Object> params) {
        List<InbPoHeader> inbPoHeaderList = getInbPoHeaderList(params);

        if(inbPoHeaderList.size() <= 0 || inbPoHeaderList.size() > 1) {
            return  null;
        }

        return inbPoHeaderList.get(0);
    }

    /**
     * 根据OrderId获取InbPoHeader
     * @param orderId
     * @return
     */
    public InbPoHeader getInbPoHeaderByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getInbPoHeaderByParams(params);
    }

    /**
     * 根据OrderOtherId获取InbPoHeader
     * @param orderOtherId
     * @return
     */
    public InbPoHeader getInbPoHeaderByOrderOtherId(String orderOtherId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderOtherId", orderOtherId);

        return getInbPoHeaderByParams(params);
    }

    /**
     * 自定义参数获取InbPoDetail
     * @param params
     * @return
     */
    public InbPoDetail getInbPoDetailByParams(Map<String, Object> params) {
        List<InbPoDetail> inbPoDetailList = getInbPoDetailList(params);

        if(inbPoDetailList.size() <= 0 || inbPoDetailList.size() > 1) {
            return  null;
        }

        return inbPoDetailList.get(0);
    }

    /**
     * 根据OrderId及SkuId获取InbPoDetail
     * @param orderId
     * @param skuId
     * @return
     */
    public InbPoDetail getInbPoDetailByOrderIdAndSkuId(Long orderId, Long skuId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderId", orderId);
        params.put("skuId", skuId);

        return getInbPoDetailByParams(params);
    }

    /**
     * 根据OrderId获取List<InbPoDetail>
     * @param orderId
     * @return
     */
    public List<InbPoDetail> getInbPoDetailListByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getInbPoDetailList(params);
    }

    /**
     * List<InbPoHeader>填充InbPoDetail
     * @param inbPoHeaderList
     */
    public void fillDetailToHeaderList(List<InbPoHeader> inbPoHeaderList) {
        for(InbPoHeader inbPoHeader : inbPoHeaderList) {
            fillDetailToHeader(inbPoHeader);
        }
    }

    /**
     * InbPoHeader填充InbPoDetail
     * @param inbPoHeader
     */
    public void fillDetailToHeader(InbPoHeader inbPoHeader) {
        if (inbPoHeader == null) {
            return;
        }

        List<InbPoDetail> inbPoDetailList = getInbPoDetailListByOrderId(inbPoHeader.getOrderId());

        inbPoHeader.setOrderDetails(inbPoDetailList);
    }

}
