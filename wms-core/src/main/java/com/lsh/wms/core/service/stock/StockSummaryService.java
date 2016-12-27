package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.dao.stock.StockSummaryDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
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

    public StockSummary getStockSummaryByItemId(Long itemId) {
        return stockSummaryDao.getStockSummaryByItemId(itemId);
    }

    public List<StockSummary> getAvailQty(Long ownerId, List<String> skuCodeList) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ownerId", ownerId);
        mapQuery.put("skuCodeList", skuCodeList);
        logger.debug(mapQuery.toString());
        return stockSummaryDao.getStockSummaryList(mapQuery);
    }

}
