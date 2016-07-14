package com.lsh.wms.core.service.stock;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockQuantMoveRelDao;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.model.stock.StockQuantMoveRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/6/29.
 */
@Component
@Transactional(readOnly = true)
public class StockQuantService {
    private static final Logger logger = LoggerFactory.getLogger(StockQuantService.class);

    @Autowired
    private StockQuantDao stockQuantDao;

    @Autowired
    private StockMoveDao moveDao;

    @Autowired
    private StockQuantMoveRelDao relDao;

    public List<StockQuant> getQuants(Map<String, Object> params) {
        return stockQuantDao.getQuants(params);
    }

    public StockQuant getQuantById(Long quantId) {
        return stockQuantDao.getStockQuantById(quantId);
    }

    @Transactional(readOnly =  false)
    public void create(StockQuant quant) {
        quant.setCreatedAt(DateUtils.getCurrentSeconds());
        quant.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockQuantDao.insert(quant);
    }

    @Transactional(readOnly =  false)
    public void update(StockQuant quant) {
        quant.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void move(Long moveId) {
        StockMove move = moveDao.getStockMoveById(moveId);
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("reserveMoveId", moveId);
        List<StockQuant> quantList = stockQuantDao.getQuants(mapQuery);
        BigDecimal qtyDone = move.getQtyDone();
        for (StockQuant quant : quantList) {
            this.unReserve(quant);
            this.split(quant, qtyDone);
            quant.setContainerId(move.getToContainerId());
            quant.setLocationId(move.getToLocationId());
            this.update(quant);
            qtyDone = qtyDone.subtract(quant.getQty());
        }
    }

    @Transactional(readOnly = false)
    public void split(StockQuant quant, BigDecimal requiredQty){
        if ( quant.getQty().compareTo(requiredQty) <= 0) {
            return;
        }

        StockQuant newQuant = (StockQuant) quant.clone();
        newQuant.setQty(quant.getQty().subtract(requiredQty));
        this.create(newQuant);
        quant.setQty(requiredQty);
    }

    @Transactional(readOnly = false)
    public void reserve(Long quantId, Long moveId, BigDecimal requiredQty) {
        StockQuant quant =  this.getQuantById(quantId);
        if (quant.getReserveMoveId() > 0 || quant.getIsFrozen() != 0) {
            return;
        }
        this.split(quant, requiredQty);
        quant.setReserveMoveId(moveId);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void unReserve(StockQuant quant) {
        quant.setReserveMoveId(0L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void freeze(StockQuant quant) {
        quant.setIsFrozen(1L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void unFreeze(StockQuant quant) {
        quant.setIsFrozen(0L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void unReserveByMoveId(Long moveId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("reserveMoveId", moveId);
        List<StockQuant> quantList = this.getQuants(mapQuery);
        for (StockQuant quant : quantList) {
            this.unReserve(quant);
        }
    }

}