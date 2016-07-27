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
import java.util.*;

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

    public List<StockQuant> getQuantsByContainerId(Long containerId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        return this.getQuants(mapQuery);
    }

    public List<StockQuant> getQuantsByLocationId(Long locationId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        return this.getQuants(mapQuery);
    }

    public StockQuant getQuantById(Long quantId) {
        return stockQuantDao.getStockQuantById(quantId);
    }

    public List<StockQuantMoveRel> getHistoryById(Long quantId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("quantId", quantId);
        return relDao.getStockQuantMoveRelList(mapQuery);
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
        StockQuantMoveRel moveRel =new StockQuantMoveRel();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("reserveMoveId", moveId);
        List<StockQuant> quantList = stockQuantDao.getQuants(mapQuery);
        BigDecimal qtyDone = move.getQtyDone();
        moveRel.setMoveId(moveId);
        for (StockQuant quant : quantList) {
            this.unReserve(quant);
            this.split(quant, qtyDone);
            quant.setContainerId(move.getToContainerId());
            quant.setLocationId(move.getRealToLocationId());
            this.update(quant);
            qtyDone = qtyDone.subtract(quant.getQty());
            moveRel.setQuantId(quant.getId());
            relDao.insert(moveRel);
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
        Map<String,Object> queryMap=new HashMap<String, Object>();
        queryMap.put("quantId", quant.getId());
        List<StockQuantMoveRel> relList= relDao.getStockQuantMoveRelList(queryMap);
        for(StockQuantMoveRel rel:relList){
            rel.setQuantId(newQuant.getId());
            relDao.insert(rel);
        }
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
    public void toDefect(StockQuant quant) {
        quant.setIsFrozen(0L);
        quant.setIsDefect(1L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void toRefund(StockQuant quant) {
        quant.setIsFrozen(0L);
        quant.setIsRefund(1L);
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
    public List<Long> getContainerIdByLocationId(Long locationId) {
        return stockQuantDao.getContainerIdByLocationId(locationId);
    }
    public BigDecimal getQuantQtyByLocationIdAndItemId(Long locationId,Long itemId) {
        Map<String,Object> queryMap=new HashMap();
        queryMap.put("locationId",locationId);
        queryMap.put("itemId", itemId);
        List<StockQuant> stockQuants=stockQuantDao.getQuants(queryMap);
        BigDecimal qty=new BigDecimal(0L);
        for (StockQuant quant:stockQuants){
            qty.add(quant.getQty());
        }
        return qty;
    }
    public Long getSupplierByLocationAndItemId(Long locationId,Long itemId) {
        Set<Long> suppliers=new HashSet<Long>();
        Map<String,Object> queryMap =new HashMap<String, Object>();
        queryMap.put("locationId",locationId);
        queryMap.put("itemId",itemId);
        List<StockQuant> quants=stockQuantDao.getQuants(queryMap);
        if(quants!=null && quants.size()!=0){
            return quants.get(0).getSupplierId();
        }else {
            return null;
        }
    }

    public BigDecimal getItemCount(Long itemId, List<Long> locationList, boolean isNormal) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("itemId",itemId);
        queryMap.put("locationList",locationList);
        queryMap.put("isNormal",isNormal);
        List<StockQuant> stockQuants = stockQuantDao.getQuants(queryMap);

        BigDecimal count = BigDecimal.ZERO;
        for(StockQuant quant : stockQuants) {
            count = count.add(quant.getQty());
        }
        return count;
    }

    public int countStockQuant(Map<String, Object> mapQuery){
        return stockQuantDao.countStockQuant(mapQuery);
    }
}