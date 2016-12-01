package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.core.dao.stock.StockAllocDetailDao;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockAllocDetail;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.wave.WaveDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mali on 16/11/23.
 */
@Component
@Transactional(readOnly = true)
public class StockAllocService {
    private static final Logger logger = LoggerFactory.getLogger(StockAllocService.class);

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private StockAllocDetailDao stockAllocDetailDao;

    @Transactional(readOnly = false)
    public void alloc(ObdHeader obdHeader, List<com.lsh.wms.model.so.ObdDetail> obdDetailList) {
        Long orderId = obdHeader.getOrderId();
        for (ObdDetail obdDetail : obdDetailList) {

            StockAllocDetail stockAllocDetail = new StockAllocDetail();
            stockAllocDetail.setItemId(obdDetail.getItemId());
            stockAllocDetail.setObdId(obdDetail.getOrderId());
            stockAllocDetail.setObdDetailId(obdDetail.getDetailOtherId());
            stockAllocDetail.setQty(obdDetail.getOrderQty());
            stockAllocDetail.setStatus(1);
            stockAllocDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            stockAllocDetail.setUpdatedAt(DateUtils.getCurrentSeconds());
            stockAllocDetailDao.insert(stockAllocDetail);

            StockDelta delta = new StockDelta();
            delta.setItemId(obdDetail.getItemId());
            delta.setAllocQty(obdDetail.getOrderQty());
            delta.setType(StockConstant.TYPE_SO_INSERT);
            delta.setBusinessId(obdDetail.getOrderId());
            stockSummaryService.changeStock(delta);

            Long itemId = obdDetail.getItemId();
            logger.info(StrUtils.formatString("SO[{0}] DetailId[{1}] inserted, reserve itemId[{2}] with qty is {3}", orderId, obdDetail.getDetailOtherId(), itemId, obdDetail.getOrderQty()));
        }
    }

    @Transactional(readOnly = false)
    public void realease(WaveDetail waveDetail) {
        List<StockAllocDetail> stockAllocDetailList = stockAllocDetailDao.getStockAllocDetailByObdId(waveDetail.getOrderId());
        if (stockAllocDetailList == null || stockAllocDetailList.isEmpty()) {
            logger.error(StrUtils.formatString("So [{0}] not Found in alloc detail", waveDetail.getOrderId()));
            return;
        }

        BigDecimal allocQty = BigDecimal.ZERO;
        for (StockAllocDetail stockAllocDetail : stockAllocDetailList) {
            if (stockAllocDetail.getStatus() == 2) {
                logger.info(StrUtils.formatString("SO[{0}] already deliver out, no need to release ", waveDetail.getOrderId()));
                return;
            }
            allocQty = allocQty.subtract(stockAllocDetail.getQty());
            StockAllocDetail x = new StockAllocDetail();
            x.setId(stockAllocDetail.getId());
            x.setStatus(2);
            stockAllocDetailDao.update(x);
        }

        StockDelta delta = new StockDelta();
        delta.setItemId(waveDetail.getItemId());
        delta.setAllocQty(allocQty);
        delta.setType(StockConstant.TYPE_SO_DELIVERY);
        delta.setBusinessId(waveDetail.getDeliveryId());
        stockSummaryService.changeStock(delta);
        logger.info(StrUtils.formatString("SO[{0}] deliver out, release it", waveDetail.getOrderId()));
    }


}
