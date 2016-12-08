package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/11/22.
 */
@Component
@Transactional(readOnly = true)
public class StockSummaryService {

    private static Logger logger = LoggerFactory.getLogger(StockSummaryService.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockSummaryDao stockSummaryDao;

    @Autowired
    private ItemService itemService;

    @Autowired
    private StockMoveDao stockMoveDao;

    @Autowired
    private SoDeliveryService soDeliveryService;

    @Transactional(readOnly = false)
    public void changeStock(StockMove move) throws BizCheckedException {
        BaseinfoLocation fromRegion = locationService.getLocation(move.getFromLocationId());
        Long fromRegionType = fromRegion.getRegionType();
        BaseinfoLocation toRegion   = locationService.getLocation(move.getToLocationId());
        Long toRegionType = toRegion.getRegionType();

        if (fromRegionType.equals(toRegionType)) { // 同一区块内的移动不必更新可用库存
            return;
        }

        if ( ! (StockConstant.REGION_TO_FIELDS.containsKey(fromRegionType) && StockConstant.REGION_TO_FIELDS.containsKey(toRegionType)) ) {
            throw new BizCheckedException("2000004");
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("itemId", move.getItemId());
        params.put(StockConstant.REGION_TO_FIELDS.get(fromRegionType), BigDecimal.ZERO.subtract(move.getQty()));
        params.put(StockConstant.REGION_TO_FIELDS.get(toRegionType), move.getQty());
        StockSummary summary = BeanMapTransUtils.map2Bean(params, StockSummary.class);

        BaseinfoItem item = itemService.getItem(summary.getItemId());
        if (item == null) {
            throw new BizCheckedException("2120001");
        }
        summary.setSkuCode(item.getSkuCode());
        summary.setOwnerId(item.getOwnerId());
        summary.setCreatedAt(DateUtils.getCurrentSeconds());
        summary.setUpdatedAt(DateUtils.getCurrentSeconds());
        logger.info("change stock: " + summary);
        stockSummaryDao.changeStock(summary);

    }

    @Transactional(readOnly = false)
    public void allocPresale(Long itemId, BigDecimal orderQty, Long orderId) {
        StockMove presale = new StockMove();
        presale.setItemId(itemId);
        presale.setFromLocationId(locationService.getConsumerArea().getLocationId());
        presale.setToLocationId(locationService.getSoAreaDirect().getLocationId());
        presale.setQty(orderQty);
        presale.setTaskId(orderId);
        stockMoveDao.insert(presale);
        changeStock(presale);
    }

    @Transactional(readOnly = false)
    public void alloc(ObdHeader obdHeader, List<ObdDetail> obdDetailList) throws BizCheckedException {
        if (obdHeader.getOrderType().equals(SoConstant.ORDER_TYPE_DIRECT)) { // 直流订单不做库存占用
            return;
        }
        // 订单明细按照itemId做排序，避免死锁
        Collections.sort(obdDetailList, new Comparator<ObdDetail>() {
            public int compare(ObdDetail o1, ObdDetail o2) {
                return o1.getItemId().compareTo(o2.getItemId());
            }
        });
        // 根据排序后的detailIst进行库存占用
        for (ObdDetail detail : obdDetailList) {
            StockMove move = new StockMove();
            move.setItemId(detail.getItemId());
            move.setQty(detail.getOrderQty());
            move.setFromLocationId(locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0).getLocationId());
            move.setToLocationId(locationService.getSoAreaInbound().getLocationId());
            move.setTaskId(detail.getOrderId());
            stockMoveDao.insert(move);
            changeStock(move);
        }
    }

    @Transactional(readOnly = false)
    public void release(StockMove move, int orderType) {
        StockMove newMove = new StockMove();
        newMove.setItemId(move.getItemId());
        newMove.setQty(move.getQty());
        newMove.setTaskId(move.getTaskId());
        if (orderType == SoConstant.ORDER_TYPE_DIRECT) {
            newMove.setFromLocationId(locationService.getSoAreaDirect().getLocationId());
        } else {
            newMove.setFromLocationId(locationService.getSoAreaInbound().getLocationId());
        }
        newMove.setToLocationId(locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0).getLocationId());
        stockMoveDao.insert(newMove);
        changeStock(newMove);
    }

    /*
     * 为了消除订单关闭时，发货数量小于订单数量，造成库存虚占的问题。
     * TODO：SO订单关闭
     */
    @Transactional(readOnly = false)
    public void eliminateDiff(ObdHeader obdHeader, List<ObdDetail> obdDetailList) {
        if (obdHeader.getOrderType().equals(SoConstant.ORDER_TYPE_DIRECT)) { // 直流订单不用消除diff
            return;
        }
        // 订单明细按照itemId做排序，避免死锁
        Collections.sort(obdDetailList, new Comparator<ObdDetail>() {
            public int compare(ObdDetail o1, ObdDetail o2) {
                return o1.getItemId().compareTo(o2.getItemId());
            }
        });
        // 根据排序后的detaiList进行库存占用
        for (ObdDetail detail : obdDetailList) {
            StockMove move = new StockMove();
            move.setItemId(detail.getItemId());
            BigDecimal deliveryQty = soDeliveryService.getDeliveryQtyBySoOrderIdAndItemId(detail.getOrderId(), detail.getItemId());
            BigDecimal orderQty = detail.getOrderQty();
            if (orderQty.compareTo(deliveryQty) < 0) {
                throw new BizCheckedException("2990045");
            }
            move.setQty(orderQty.subtract(deliveryQty));
            move.setFromLocationId(locationService.getSoAreaInbound().getLocationId());
            move.setToLocationId(locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0).getLocationId());
            move.setTaskId(detail.getOrderId());
            stockMoveDao.insert(move);
            changeStock(move);
        }
    }


    /*
     * 解决直流订单的库存续重问题，唯一调用处QC产生差异时
     */
    @Transactional(readOnly = false)
    public void eliminateDiff(StockMove move) {
        if ( ! LocationConstant.DIFF_AREA.equals(locationService.getLocation(move.getToLocationId()).getLocationId())
                || ! LocationConstant.COLLECTION_AREA.equals(locationService.getLocation(move.getFromLocationId()).getLocationId())
                )
        {// 避免误伤，只有 集货区 => 差异区 的move才可以进行下一步操作， 同时要求调用者保证是直流订单的qc差异才调用此方法
            return;
        }
        StockMove diff = new StockMove();
        diff.setItemId(move.getItemId());
        diff.setQty(move.getQty());
        diff.setFromLocationId(locationService.getSoAreaInbound().getLocationId());
        diff.setToLocationId(locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0).getLocationId());
        diff.setTaskId(move.getTaskId());
        stockMoveDao.insert(diff);
        changeStock(diff);
    }

    public StockSummary getStockSummaryByItemId(Long itemId) {
        return stockSummaryDao.getStockSummaryByItemId(itemId);
    }

}
