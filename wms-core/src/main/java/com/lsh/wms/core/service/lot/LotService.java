package com.lsh.wms.core.service.lot;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.model.baseinfo.BaseinfoLot;
import com.lsh.wms.core.dao.baseinfo.BaseinfoLotDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Ming on 7/11/16.
 */

@Component
@Transactional(readOnly = true)

public class LotService {
    private static final Logger logger = LoggerFactory.getLogger(LotService.class);
    private static final ConcurrentMap<Long, BaseinfoLot> m_LotCache = new
            ConcurrentHashMap<Long, BaseinfoLot>();
    @Autowired
    private BaseinfoLotDao lotDao;

    public BaseinfoLot getLotByLotId(long iLotId){
        Long key = iLotId;
        BaseinfoLot lot = m_LotCache.get(key);
        if(lot == null){
            //not exist in cache, search in mysql
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("lotId", iLotId);
            List<BaseinfoLot> lots = lotDao.getBaseinfoLotList(mapQuery);

            if(lots.size() == 1){
                lot = lots.get(0);
                m_LotCache.put(key,lot);
            } else {
                return null;
            }
        }
        BaseinfoLot new_lot = new BaseinfoLot();
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
    public int insertLot(BaseinfoLot lot){
        // if exist
        if(this.getLotByLotId(lot.getLotId())!=null){
            return -1;
        }
        //create
        lot.setCreatedAt(DateUtils.getCurrentSeconds());
        lot.setUpdatedAt(DateUtils.getCurrentSeconds());
        lotDao.insert(lot);
        return 0;
    }

    @Transactional(readOnly = false)
    public int updateLot(BaseinfoLot lot){
        BaseinfoLot getLot = this.getLotByLotId(lot.getLotId());
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

    public List<BaseinfoLot> searchLot(Map<String, Object> mapQuery){
        return lotDao.getBaseinfoLotList(mapQuery);
    }

}
