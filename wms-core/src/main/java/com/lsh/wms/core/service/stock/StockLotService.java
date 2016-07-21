package com.lsh.wms.core.service.stock;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.stock.StockLotDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Ming on 7/14/16.
 */

@Component
@Transactional(readOnly = true)

public class StockLotService {
    private static final Logger logger = LoggerFactory.getLogger(StockLotService.class);
    private static final ConcurrentMap<Long, StockLot> m_LotCache = new
            ConcurrentHashMap<Long, StockLot>();
    @Autowired
    private StockLotDao lotDao;

    @Autowired
    private StockQuantDao quantDao;

    public StockLot getStockLotByLotId(long iLotId){
        Long key = iLotId;
        StockLot lot = m_LotCache.get(key);
        if(lot == null){
            //not exist in cache, search in mysql
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("lotId", iLotId);
            List<StockLot> lots = lotDao.getStockLotList(mapQuery);

            if(lots.size() == 1){
                lot = lots.get(0);
                m_LotCache.put(key,lot);
            } else {
                return null;
            }
        }
        return lot;
    }

    @Transactional(readOnly = false)
    public void insertLot(StockLot lot){
        if (lot.getLotId()==null || lot.getLotId()==0){
            lot.setLotId(RandomUtils.genId());
        }
        lot.setCreatedAt(DateUtils.getCurrentSeconds());
        lot.setUpdatedAt(DateUtils.getCurrentSeconds());
        lotDao.insert(lot);
    }

    @Transactional(readOnly = false)
    public void updateLot(StockLot lot){
        lot.setUpdatedAt(DateUtils.getCurrentSeconds());
        lotDao.update(lot);
    }

    public List<StockLot> searchLot(Map<String, Object> mapQuery){
        return lotDao.getStockLotList(mapQuery);
    }


}