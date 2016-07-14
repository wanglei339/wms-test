package com.lsh.wms.core.service.stock;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.stock.StockLotDao;
import com.lsh.wms.model.stock.StockLot;
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
        StockLot new_lot = new StockLot();
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(new_lot, lot);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new_lot;
    }

    @Transactional(readOnly = false)
    public int insertLot(StockLot lot){
        // if exist
        if(this.getStockLotByLotId(lot.getLotId())!=null){
            return -1;
        }
        //create
        lot.setCreatedAt(DateUtils.getCurrentSeconds());
        lot.setUpdatedAt(DateUtils.getCurrentSeconds());
        lotDao.insert(lot);
        return 0;
    }

    @Transactional(readOnly = false)
    public int updateLot(StockLot lot){
        StockLot getLot = this.getStockLotByLotId(lot.getLotId());
        //if exist
        if(getLot == null){
            return -1;
        }
        //update
        lot.setCreatedAt(getLot.getCreatedAt());
        lot.setUpdatedAt(DateUtils.getCurrentSeconds());
        lotDao.update(lot);
        return 0;
    }

    public List<StockLot> searchLot(Map<String, Object> mapQuery){
        return lotDao.getStockLotList(mapQuery);
    }

}