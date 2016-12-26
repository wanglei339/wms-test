package com.lsh.wms.core.service.so;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.dao.so.ObdDetailDao;
import com.lsh.wms.core.dao.so.ObdHeaderDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    private ObdHeaderDao obdHeaderDao;

    @Autowired
    private ObdDetailDao obdDetailDao;

    @Autowired
    private StockMoveService stockMoveService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private WaveService waveService;

    @Autowired
    private TuService tuService;

    @Autowired
    private StockMoveService moveService;

    /**
     * 插入OutbSoHeader及OutbSoDetail
     *
     * @param obdHeader
     * @param obdDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(ObdHeader obdHeader, List<ObdDetail> obdDetailList) {
        obdHeader.setCreatedAt(DateUtils.getCurrentSeconds());
        obdHeaderDao.insert(obdHeader);

        obdDetailDao.batchInsert(obdDetailList);

        if(!obdHeader.getOrderType().equals(SoConstant.ORDER_TYPE_DIRECT)) {
            // 在库SO预占库存。
            List<StockMove> moveList = new ArrayList<StockMove>();
            // 根据排序后的detailIst进行库存占用
            for (ObdDetail detail : obdDetailList) {
                StockMove move = new StockMove();
                move.setItemId(detail.getItemId());
                move.setQty(detail.getOrderQty());
                move.setFromLocationId(locationService.getNullArea().getLocationId());
                move.setToLocationId(locationService.getSoAreaInbound().getLocationId());
                move.setTaskId(detail.getOrderId());
                moveList.add(move);
            }
            stockMoveService.move(moveList);
        }
    }

    /**
     * 更新OutbSoHeader
     *
     * @param obdHeader
     */
    @Transactional(readOnly = false)
    public void update(ObdHeader obdHeader) {
        obdHeader.setCreatedAt(null);
        obdHeaderDao.update(obdHeader);
    }

    /**
     * 根据OrderOtherId或OrderId更新OutbSoHeader
     *
     * @param obdHeader
     */
    @Transactional(readOnly = false)
    public void updateOutbSoHeaderByOrderOtherIdOrOrderId(ObdHeader obdHeader) {
        obdHeader.setUpdatedAt(DateUtils.getCurrentSeconds());
        obdHeader.setCreatedAt(null);
        obdHeaderDao.updateByOrderOtherIdOrOrderId(obdHeader);
    }

    /**
     * 根据ID获取OutbSoHeader
     *
     * @param id
     * @return
     */
    public ObdHeader getOutbSoHeaderById(Long id) {
        return obdHeaderDao.getObdHeaderById(id);
    }

    /**
     * 自定义参数获取OutbSoHeader数量
     *
     * @param params
     * @return
     */
    public Integer countOutbSoHeader(Map<String, Object> params) {
        return obdHeaderDao.countObdHeader(params);
    }

    /**
     * 根据参数获取List<OutbSoHeader>
     *
     * @param params
     * @return
     */
    public List<ObdHeader> getOutbSoHeaderList(Map<String, Object> params) {
        return obdHeaderDao.getObdHeaderList(params);
    }

    /**
     * 根据ID获取OutbSoDetail
     *
     * @param id
     * @return
     */
    public ObdDetail getOutbSoDetailById(Long id) {
        return obdDetailDao.getObdDetailById(id);
    }

    /**
     * 自定义参数获取OutbSoDetail数量
     *
     * @param params
     * @return
     */
    public Integer countOutbSoDetail(Map<String, Object> params) {
        return obdDetailDao.countObdDetail(params);
    }

    /**
     * 根据参数获取List<OutbSoDetail>
     *
     * @param params
     * @return
     */
    public List<ObdDetail> getOutbSoDetailList(Map<String, Object> params) {
        List<ObdDetail> details = obdDetailDao.getObdDetailList(params);
        if(details==null){
            return new ArrayList<ObdDetail>();
        }
        return details;
    }

    /**
     * 根据 order_id 获取so订单商品详情
     *
     * @param orderId
     * @return
     */
    public List<ObdDetail> getOutbSoDetailListByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getOutbSoDetailList(params);
    }

    /**
     * List<OutbSoHeader>填充OutbSoDetail
     *
     * @param obdHeaderList
     */
    public void fillDetailToHeaderList(List<ObdHeader> obdHeaderList) {
        for (ObdHeader obdHeader : obdHeaderList) {
            fillDetailToHeader(obdHeader);
        }
    }

    /**
     * OutbSoHeader填充OutbSoDetail
     *
     * @param obdHeader
     */
    public void fillDetailToHeader(ObdHeader obdHeader) {
        if (obdHeader == null) {
            return;
        }

        List<ObdDetail> obdDetailList = getOutbSoDetailListByOrderId(obdHeader.getOrderId());

        obdHeader.setOrderDetails(obdDetailList);
    }

    /**
     * 根据波次号查询List<OutbSoHeader>
     *
     * @param waveId
     * @return
     */
    public List<ObdHeader> getOutSoHeaderListByWaveId(Long waveId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("waveId", waveId);

        List<ObdHeader> obdHeaderList = obdHeaderDao.getObdHeaderList(params);

        fillDetailToHeaderList(obdHeaderList);

        return obdHeaderList;
    }

    /**
     * 根据OrderId获取OutbSoHeader
     *
     * @param orderId
     * @return
     */
    public ObdHeader getOutbSoHeaderByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        //获取OutbSoHeader
        List<ObdHeader> obdHeaderList = getOutbSoHeaderList(params);
        if (obdHeaderList.size() > 1 || obdHeaderList.size() <= 0) {
            return null;
        }

        ObdHeader obdHeader = obdHeaderList.get(0);

        //获取OutbSoDetail
        fillDetailToHeader(obdHeader);

        return obdHeader;
    }


    /**
     * 根据orderOtherId获取OutbSoHeader
     *
     * @param orderOtherId
     * @return
     */
    public ObdHeader getOutbSoHeaderByOrderOtherId(String orderOtherId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderOtherId", orderOtherId);

        //获取OutbSoHeader
        List<ObdHeader> obdHeaderList = getOutbSoHeaderList(params);
        if (obdHeaderList.size() > 1 || obdHeaderList.size() <= 0) {
            return null;
        }

        ObdHeader obdHeader = obdHeaderList.get(0);

        //获取OutbSoDetail
        fillDetailToHeader(obdHeader);

        return obdHeader;
    }

    /**
     * 根据OrderId及SkuId获取InbPoDetail
     *
     * @param orderId
     * @param detailOtherId
     * @return
     */
    public ObdDetail getObdDetailByOrderIdAndDetailOtherId(Long orderId, String detailOtherId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderId", orderId);
        params.put("detailOtherId", detailOtherId);
        List<ObdDetail> detailList = getOutbSoDetailList(params);
        if (detailList.size() <= 0) {
            return null;
        }

        return detailList.get(0);
    }

    /**
     * 根据OrderId及SkuId获取InbPoDetail
     *
     * @param orderId
     * @param itemId
     * @return
     */
    public ObdDetail getObdDetailByOrderIdAndItemId(Long orderId, Long itemId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("orderId", orderId);
        params.put("itemId", itemId);
        List<ObdDetail> detailList = getOutbSoDetailList(params);
        if (null == detailList || detailList.size() <= 0) {
            return null;
        }
        return detailList.get(0);
    }
    /**
     * 根据OrderId及SkuId获取InbPoDetail
     *
     * @param orderId
     * @param itemId
     * @return
     */
    public List<ObdDetail> getObdDetailListByOrderIdAndItemId(Long orderId, Long itemId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        params.put("itemId", itemId);
        return getOutbSoDetailList(params);
    }
    /**
     * 根据orderOtherId获取OutbSoHeader
     *
     * @param orderOtherId
     * @return
     */
    public ObdHeader getOutbSoHeaderByOrderOtherIdAndType(String orderOtherId,int orderType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderOtherId", orderOtherId);
        params.put("orderType",orderType);

        //获取OutbSoHeader
        List<ObdHeader> obdHeaderList = getOutbSoHeaderList(params);
        if (obdHeaderList.size() > 1 || obdHeaderList.size() <= 0) {
            return null;
        }

        ObdHeader obdHeader = obdHeaderList.get(0);

        //获取OutbSoDetail
        fillDetailToHeader(obdHeader);

        return obdHeader;
    }

    /**
     * 更新obddetail
     *
     * @param obdDetail
     */
    @Transactional(readOnly = false)
    public void updateObdDetail(ObdDetail obdDetail) {
        obdDetailDao.update(obdDetail);
    }


    @Transactional(readOnly = false)
    public void increaseReleaseQty(BigDecimal releaseQty, Long orderId, String detailOtherId){
        ObdDetail detail = new ObdDetail();
        detail.setReleaseQty(releaseQty);
        detail.setOrderId(orderId);
        detail.setDetailOtherId(detailOtherId);
        obdDetailDao.increaseReleaseQty(detail);
    }


    @Transactional(readOnly = false)
    public void confirmBack(List<WaveDetail> waveDetails ,List<TuDetail> tuDetails,TuHead tuHead,List<StockMove> moveList){
        //先将托盘都move到同一个托盘上
        moveService.move(moveList);


        //gen waveDetail
        for (WaveDetail waveDetail : waveDetails) {
            waveService.insertDetail(waveDetail);
        }
        //

        //生成obd moveStockQuant
        tuService.createObdAndMoveStockQuant(tuHead,tuDetails);

    }


}
