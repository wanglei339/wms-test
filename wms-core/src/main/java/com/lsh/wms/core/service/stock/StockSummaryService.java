package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.dao.stock.StockDeltaDao;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mali on 16/11/22.
 */
@Component
@Transactional(readOnly = true)
public class StockSummaryService {
    private static Logger logger = LoggerFactory.getLogger(StockSummaryService.class);

    @Autowired
    private StockSummaryDao stockSummaryDao;

    @Autowired
    private StockDeltaDao stockDeltaDao;

    @Autowired
    private ItemService itemService;

    @Autowired
    private SynStockService synStockService;

    @Transactional(readOnly = false)
    public void changeStock(StockDelta delta) throws BizCheckedException {
        logger.info(StrUtils.formatString("change stock summary for item[{0}] : inhouse delta is [{1}], alloc delta is [{2}]", delta.getItemId(), delta.getInhouseQty(), delta.getAllocQty()));
        stockDeltaDao.insert(delta);
        StockSummary summary = new StockSummary();
        ObjUtils.bean2bean(delta, summary);
        BaseinfoItem item = itemService.getItem(summary.getItemId());
        if (item == null) {
            throw new BizCheckedException("2120001");
        }
        summary.setSkuCode(item.getSkuCode());
        summary.setOwnerId(item.getOwnerId());
        stockSummaryDao.changeStock(summary);

        // TODO 上线后删掉
        StockSummary stockSummary = stockSummaryDao.getStockSummaryByItemId(delta.getItemId());
        synStockService.synStock(stockSummary.getItemId(), stockSummary.getAvailQty().doubleValue());
    }

    public StockSummary getStockSummaryByItemId(Long itemId) {
        return stockSummaryDao.getStockSummaryByItemId(itemId);
    }

}
