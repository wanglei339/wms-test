package com.lsh.wms.core.service.po;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.po.*;
import com.lsh.wms.model.po.*;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private IbdHeaderDao ibdHeaderDao;

    @Autowired
    private IbdDetailDao ibdDetailDao;

    @Autowired
    private IbdObdRelationDao ibdObdRelationDao;

    @Autowired
    private ReceiveHeaderDao receiveHeaderDao;

    @Autowired
    private ReceiveDetailDao receiveDetailDao;



    /**
     * 插入InbPoHeader及InbPoDetail
     * @param ibdHeader
     * @param ibdDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(IbdHeader ibdHeader, List<IbdDetail> ibdDetailList) {
        ibdHeaderDao.insert(ibdHeader);

        ibdDetailDao.batchInsert(ibdDetailList);
    }

    /**
     * 根据ID编辑InbPoHeader
     * @param ibdHeader
     */
    @Transactional(readOnly = false)
    public void updateInbPoHeader(IbdHeader ibdHeader){
        ibdHeader.setUpdatedAt(DateUtils.getCurrentSeconds());

        ibdHeaderDao.update(ibdHeader);
    }

    /**
     * 根据OrderOtherId或OrderId更新InbPoHeader
     * @param ibdHeader
     */
    @Transactional(readOnly = false)
    public void updateInbPoHeaderByOrderOtherIdOrOrderId(IbdHeader ibdHeader){
        ibdHeader.setUpdatedAt(DateUtils.getCurrentSeconds());

        ibdHeaderDao.updateByOrderOtherIdOrOrderId(ibdHeader);
    }

    /**
     * 根据ID获取InbPoHeader
     * @param id
     * @return
     */
    public IbdHeader getInbPoHeaderById(Long id){
        return ibdHeaderDao.getIbdHeaderById(id);
    }

    /**
     * 根据参数获取InbPoHeader数量
     * @param params
     * @return
     */
    public Integer countInbPoHeader(Map<String, Object> params){
        return ibdHeaderDao.countIbdHeader(params);
    }

    /**
     * 自定义参数获取List<InbPoHeader>
     * @param params
     * @return
     */
    public List<IbdHeader> getInbPoHeaderList(Map<String, Object> params){
        return ibdHeaderDao.getIbdHeaderList(params);
    }

    /**
     * 根据ID编辑InbPoDetail
     * @param ibdDetail
     */
    @Transactional(readOnly = false)
    public void updateInbPoDetail(IbdDetail ibdDetail) {
        ibdDetailDao.update(ibdDetail);
    }

    /**
     * 编辑InboundQty
     * @param inboundQty
     * @param orderId
     * @param detailOtherId
     */
    @Transactional(readOnly = false)
    public void updateInbPoDetailInboundQtyByOrderIdAndSkuId(Long inboundQty, Long orderId, String detailOtherId) {
        ibdDetailDao.updateInboundQtyByOrderIdAndDetailOtherId(inboundQty, orderId, detailOtherId);
    }

    /**
     * 根据ID获取InbPoDetail
     * @param id
     * @return
     */
    public IbdDetail getInbPoDetailById(Long id) {
        return ibdDetailDao.getIbdDetailById(id);
    }

    /**
     * 根据参数获取InbPoDetail数量
     * @param params
     * @return
     */
    public Integer countInbPoDetail(Map<String, Object> params) {
        return ibdDetailDao.countIbdDetail(params);
    }

    /**
     * 自定义参数获取List<InbPoDetail>
     * @param params
     * @return
     */
    public List<IbdDetail> getInbPoDetailList(Map<String, Object> params) {
        return ibdDetailDao.getIbdDetailList(params);
    }

    /**
     * 自定义参数获取InbPoHeader
     * @param params
     * @return
     */
    public IbdHeader getInbPoHeaderByParams(Map<String, Object> params) {
        List<IbdHeader> ibdHeaderList = getInbPoHeaderList(params);

        if(ibdHeaderList.size() <= 0 || ibdHeaderList.size() > 1) {
            return  null;
        }

        return ibdHeaderList.get(0);
    }

    /**
     * 根据OrderId获取InbPoHeader
     * @param orderId
     * @return
     */
    public IbdHeader getInbPoHeaderByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getInbPoHeaderByParams(params);
    }

    /**
     * 根据OrderOtherId获取InbPoHeader
     * @param orderOtherId
     * @return
     */
    public IbdHeader getInbPoHeaderByOrderOtherId(String orderOtherId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderOtherId", orderOtherId);

        return getInbPoHeaderByParams(params);
    }

    /**
     * 自定义参数获取InbPoDetail
     * @param params
     * @return
     */
    public IbdDetail getInbPoDetailByParams(Map<String, Object> params) {
        List<IbdDetail> ibdDetailList = getInbPoDetailList(params);

        if(ibdDetailList.size() <= 0 || ibdDetailList.size() > 1) {
            return  null;
        }

        return ibdDetailList.get(0);
    }

    /**
     * 根据OrderId及SkuId获取InbPoDetail
     * @param orderId
     * @param detailOtherId
     * @return
     */
    public IbdDetail getInbPoDetailByOrderIdAndDetailOtherId(Long orderId,String detailOtherId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderId", orderId);
        params.put("detailOtherId", detailOtherId);

        return getInbPoDetailByParams(params);
    }
//    public IbdDetail getInbPoDetailByOrderIdAndSkuId(Long orderId, Long skuId) {
//        Map<String, Object> params = new HashMap<String, Object>();
//
//        params.put("orderId", orderId);
//        params.put("skuId", skuId);
//
//        return getInbPoDetailByParams(params);
//    }

    /**
     * 根据OrderId及skuCode获取InbPoDetail
     * @param orderId
     * @param skuCode
     * @return
     */
    public IbdDetail getInbPoDetailByOrderIdAndSkuCode(Long orderId, String skuCode) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderId", orderId);
        params.put("skuCode", skuCode);

        return getInbPoDetailByParams(params);
    }
//    public IbdDetail getInbPoDetailByOrderIdAndBarCode(Long orderId, String barCode) {
//        Map<String, Object> params = new HashMap<String, Object>();
//
//        params.put("orderId", orderId);
//        params.put("barCode", barCode);
//
//        return getInbPoDetailByParams(params);
//    }

    /**
     * 根据OrderId获取List<InbPoDetail>
     * @param orderId
     * @return
     */
    public List<IbdDetail> getInbPoDetailListByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getInbPoDetailList(params);
    }

    /**
     * List<InbPoHeader>填充InbPoDetail
     * @param ibdHeaderList
     */
    public void fillDetailToHeaderList(List<IbdHeader> ibdHeaderList) {
        for(IbdHeader ibdHeader : ibdHeaderList) {
            fillDetailToHeader(ibdHeader);
        }
    }

    /**
     * InbPoHeader填充InbPoDetail
     * @param ibdHeader
     */
    public void fillDetailToHeader(IbdHeader ibdHeader) {
        if (ibdHeader == null) {
            return;
        }

        List<IbdDetail> ibdDetailList = getInbPoDetailListByOrderId(ibdHeader.getOrderId());

        ibdHeader.setOrderDetails(ibdDetailList);
    }

    /**
     * 根据ibd_other_id 找到obd_other_id列表
     */
    public List<IbdObdRelation> getIbdObdRelationByIbdOtherId(String ibdOtherId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("ibdOtherId",ibdOtherId);
        List<IbdObdRelation> list = ibdObdRelationDao.getIbdObdRelationList(map);
        return list;
    }
    /**
     * 根据传入参数获取IbdObdRelation列表
     */
    public List<IbdObdRelation> getIbdObdRelationList(Map<String,Object> params){
        return ibdObdRelationDao.getIbdObdRelationList(params);
    }

    /**
     *
     */

    public List<IbdObdRelation> getIbdObdRelationListByObd(String obdOtherId,String obdDetailOtherId){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("obdOtherId",obdOtherId);
        params.put("obdDetailId",obdDetailOtherId);
        return ibdObdRelationDao.getIbdObdRelationList(params);
    }


    /**
     * 传入orderIds
     */
    public List<IbdHeader> getIbdListOrderByDate(String orderIds){
        return ibdHeaderDao.getIbdListOrderByDate(orderIds);
    }

    /**
     * 新增ibdObdRelation
     * @param ibdObdRelation
     */
    @Transactional(readOnly = false)
    public void insertIbdObdRelation(IbdObdRelation ibdObdRelation){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("obdOtherId",ibdObdRelation.getObdOtherId());
        param.put("obdDetailId",ibdObdRelation.getObdDetailId());
        List<IbdObdRelation> list = ibdObdRelationDao.getIbdObdRelationList(param);
        //先判断记录是否存在,不存在再插入
        if(list != null && list.size() > 0){

        }else{
            ibdObdRelationDao.insert(ibdObdRelation);
        }
    }




}
