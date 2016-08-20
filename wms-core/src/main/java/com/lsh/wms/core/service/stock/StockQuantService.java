package com.lsh.wms.core.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockQuantMoveRelDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
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

    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void lockQuantByLocation(Long locationId) {
        if (locationId.equals(0L)) {
            return;
        }
        Map<String, Object> condition = new HashMap<String, Object>();
        List<BaseinfoLocation> list = new ArrayList<BaseinfoLocation>();
        list.add(locationService.getLocation(locationId));
        condition.put("locationList", list);
        stockQuantDao.lock(condition);
    }

    @Transactional(readOnly = false)
    public void lockQuantByContainerId(Long containerId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("containerId", containerId);
        stockQuantDao.lock(condition);
    }

    @Transactional(readOnly = false)
    public void lockQuantById(Long id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("id", id);
        stockQuantDao.lock(condition);
    }

    private void prepareQuery(Map<String, Object> params) {
        List<BaseinfoLocation> locationList = new ArrayList<BaseinfoLocation>();
        if (params.get("locationId") != null) {
            BaseinfoLocation location = locationService.getLocation((Long) params.get("locationId"));
            locationList.add(location);
        }
        if  (params.get("locationList") != null) {
            for (BaseinfoLocation location : (List<BaseinfoLocation>) params.get("locationList")) {
                locationList.add(location);
            }
        }
        if (params.get("locationIdList") != null) {
            for (Long locaitonId : (List<Long>) params.get("locationIdList")) {
                BaseinfoLocation location = locationService.getLocation(locaitonId);
                if(location!=null) {
                    locationList.add(location);
                }
            }
        }
        if ( ! locationList.isEmpty() ) {
            params.put("locationList", locationList);
        }
    }

    @Transactional(readOnly = false)
    public BigDecimal getRealtimeQty(Long locationId, Long itemId) {
        return this.getRealtimeQty(locationService.getLocation(locationId), itemId);
    }

    @Transactional(readOnly = false)
    public BigDecimal getRealtimeQty(BaseinfoLocation location, Long itemId) {
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("itemId", itemId);
        List<BaseinfoLocation> list = new ArrayList<BaseinfoLocation>();
        list.add(location);
        mapCondition.put("locationList", list);
        return this.getQty(mapCondition);
    }


    public BigDecimal getQty(Map<String, Object> mapQuery) {
        this.prepareQuery(mapQuery);
        BigDecimal total = stockQuantDao.getQty(mapQuery);
        return total == null ? BigDecimal.ZERO : total;
    }

    public List<StockQuant> getQuants(Map<String, Object> params) {
        this.prepareQuery(params);
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
        String packName = "";
        if (quant.getPackUnit().equals(BigDecimal.ONE)) {
            packName = "EA";
        } else {
            packName = String.format("H%02d", quant.getPackUnit().toBigInteger());
        }
        quant.setPackName(packName);
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
    public void move(StockMove move) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", move.getItemId());
        mapQuery.put("locationId", move.getFromLocationId());
        mapQuery.put("containerId", move.getFromContainerId());
        List<StockQuant> quantList = stockQuantDao.getQuants(mapQuery);
        BigDecimal qtyDone = move.getQty();
        for (StockQuant quant : quantList) {
            if (quant.getReserveTaskId() != 0 && quant.getReserveTaskId().compareTo(move.getTaskId()) != 0) {
                continue;
            }
            this.split(quant, qtyDone);
            quant.setLocationId(move.getToLocationId());
            quant.setContainerId(move.getToContainerId());
            this.update(quant);
            qtyDone = qtyDone.subtract(quant.getQty());
            // 新建 quant move历史记录
            StockQuantMoveRel moveRel =new StockQuantMoveRel();
            moveRel.setMoveId(move.getId());
            moveRel.setQuantId(quant.getId());
            relDao.insert(moveRel);
        }
        if (qtyDone.compareTo(BigDecimal.ZERO) < 0 ) {

            throw new BizCheckedException("2550008");
        }
    }

    @Transactional(readOnly = false)
    public void move(StockMove move,StockQuant oldQuant) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", move.getItemId());
        mapQuery.put("locationId", move.getFromLocationId());
        mapQuery.put("containerId", move.getFromContainerId());
        List<StockQuant> quantList = stockQuantDao.getQuants(mapQuery);
        BigDecimal qtyDone = move.getQty();
        if((quantList==null || quantList.size()==0) && locationService.getInventoryLostLocationId().compareTo(move.getFromLocationId())==0){
            quantList = new ArrayList<StockQuant>();
            StockQuant newQuant = (StockQuant)oldQuant.clone();
            newQuant.setQty(BigDecimal.ZERO);
            newQuant.setLocationId(move.getFromLocationId());
            newQuant.setContainerId( move.getFromContainerId());
            quantList.add(newQuant);
        }
        for (StockQuant quant : quantList) {
            if (quant.getReserveTaskId() != 0 && quant.getReserveTaskId().compareTo(move.getTaskId()) != 0) {
                continue;
            }
            this.split(quant, qtyDone);
            quant.setLocationId(move.getToLocationId());
            quant.setContainerId(move.getToContainerId());
            this.update(quant);
            qtyDone = qtyDone.subtract(quant.getQty());
            // 新建 quant move历史记录
            StockQuantMoveRel moveRel =new StockQuantMoveRel();
            moveRel.setMoveId(move.getId());
            moveRel.setQuantId(quant.getId());
            relDao.insert(moveRel);
        }
        if (qtyDone.compareTo(BigDecimal.ZERO) < 0 ) {

            throw new BizCheckedException("2550008");
        }
    }

    @Transactional(readOnly = false)
    public void split(StockQuant quant, BigDecimal requiredQty){
        if ( quant.getQty().compareTo(requiredQty) <= 0 && quant.getQty().compareTo(BigDecimal.ZERO) > 0) {
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
    public List<StockQuant> reserve(Map<String, Object> mapQuery, Long taskId, BigDecimal requiredQty) throws BizCheckedException {
        List<StockQuant> quantList = this.getQuants(mapQuery);
        List<StockQuant> resultList = new ArrayList<StockQuant>();
        for (StockQuant quant : quantList) {
            if (! quant.isAvailable()) {
               continue;
            }
            this.split(quant, requiredQty);
            quant.setReserveTaskId(taskId);
            resultList.add(quant);
            stockQuantDao.update(quant);
            requiredQty = requiredQty.subtract(quant.getQty());
            if (requiredQty.compareTo(BigDecimal.ZERO) == 0){
                break;
            }
        }
        if (requiredQty.compareTo(BigDecimal.ZERO) > 0) {
            throw new BizCheckedException("2550001");
        }
        return resultList;
    }

    @Transactional(readOnly = false)
    public List<StockQuant> reserveByContainer(Long containerId, Long taskId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        List<StockQuant> quantList = this.getQuants(mapQuery);
        for (StockQuant quant : quantList) {
            if (quant.getReserveTaskId() != 0 && quant.getReserveTaskId().compareTo(taskId) != 0) {
                throw new BizCheckedException("3550002");
            }
            this.reserve(quant, taskId);
        }
        return quantList;
    }

    @Transactional(readOnly = false)
    public void reserve(StockQuant quant, Long taskId) {
        quant.setReserveTaskId(taskId);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void unReserveById(Long quantId) {
        StockQuant quant = stockQuantDao.getStockQuantById(quantId);
        quant.setReserveTaskId(0L);
        this.update(quant);
    }

    @Transactional(readOnly = false)
    public void unReserve(Long taskId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("reserveTaskId", taskId);
        List<StockQuant> quantList = this.getQuants(mapQuery);
        for (StockQuant quant : quantList) {
            quant.setReserveTaskId(0L);
            stockQuantDao.update(quant);
        }
    }

    @Transactional(readOnly = false)
    public void freeze(StockQuant quant) {
        quant.setIsFrozen(1L);
        stockQuantDao.update(quant);
    }


    @Transactional(readOnly = false)
    public void unFreeze(StockQuant quant) {
        quant.setIsDefect(0L);
        quant.setIsRefund(0L);
        quant.setIsFrozen(0L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void toDefect(StockQuant quant) {
        quant.setIsFrozen(0L);
        quant.setIsDefect(1L);
        quant.setIsRefund(0L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void toRefund(StockQuant quant) {
        quant.setIsFrozen(0L);
        quant.setIsRefund(1L);
        quant.setIsDefect(0L);
        stockQuantDao.update(quant);
    }


    @Transactional(readOnly = false)
    private void dealOne(StockQuant quant, String operation) {
        if (operation.equals("freeze")) {
            this.freeze(quant);
        } else if (operation.equals("unFreeze")) {
            this.unFreeze(quant);
        } else if (operation.equals("toDefect")) {
            this.toDefect(quant);
        } else if (operation.equals("toRefund")) {
            this.toRefund(quant);
        }
    }

    @Transactional(readOnly = false)
    public void process(Map<String, Object> mapCondition, String operation) {
        this.lockQuantByLocation((Long) mapCondition.get("locationId"));

        BigDecimal requiredQty = new BigDecimal(mapCondition.get("requiredQty").toString());
        List<StockQuant> quantList = this.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            if (requiredQty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            // need > have
            if (requiredQty.compareTo(quant.getQty()) > 0) {
                this.dealOne(quant, operation);
                requiredQty = requiredQty.subtract(quant.getQty());
            } else {
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    this.split(quant, requiredQty);
                }
                this.dealOne(quant, operation);
                break;
            }
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
            qty = qty.add(quant.getQty());
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