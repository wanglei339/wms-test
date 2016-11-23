package com.lsh.wms.core.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.core.dao.stock.StockQuantMoveRelDao;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
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

    @Autowired
    private ItemService itemService;

    @Autowired
    private StockRedisService stockRedisService;

    @Autowired
    private BaseTaskService baseTaskService;

    @Autowired StockLotService lotService;


    @Transactional(readOnly = false)
    public void moveToComplete(StockQuant quant) {
        stockQuantDao.moveToComplete(quant.getId());
        stockQuantDao.remove(quant.getId());
    }

    private void prepareQuery(Map<String, Object> params) {
        List<BaseinfoLocation> locationList = new ArrayList<BaseinfoLocation>();
        if (params.get("location") != null) {
            locationList.add((BaseinfoLocation) params.get("location"));
        }
        if (params.get("locationId") != null) {
            BaseinfoLocation location = locationService.getLocation(Long.valueOf(params.get("locationId").toString()));
            if (location != null) {
                locationList.add(location);
            }
        }
        if (params.get("locationList") != null) {
            for (BaseinfoLocation location : (List<BaseinfoLocation>) params.get("locationList")) {
                locationList.add(location);
            }
        }
        if (params.get("locationIdList") != null) {
            for (Long locaitonId : (List<Long>) params.get("locationIdList")) {
                BaseinfoLocation location = locationService.getLocation(locaitonId);
                if (location != null) {
                    locationList.add(location);
                }
            }
        }
        if (!locationList.isEmpty()) {
            params.put("locationList", locationList);
        }
    }

    public List<Long> getLocationIdByContainerId(Long containerId) {
        return stockQuantDao.getLocationIdByContainerId(containerId);
    }
    public Long getLocationIdByLotId(Long lotId) {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("lotId",lotId);
        return  stockQuantDao.getQuants(queryMap).get(0).getLocationId();
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

    public List<Long> getLotIdByLocationId(Long locationId) {
        return stockQuantDao.getLotByLocationId(locationId);
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
    public List<StockQuant> getQuantsByOrderId(Long orderId) {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderId", orderId);
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

    @Transactional(readOnly = false)
    public void create(StockQuant quant) {
        String packName = "";
        //TODO 这个代码有问题,箱规这么取不严格
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

    @Transactional(readOnly = false)
    public void update(StockQuant quant) {
        quant.setUpdatedAt(DateUtils.getCurrentSeconds());
        stockQuantDao.update(quant);
    }

    public int getContainerQty(Long locationId) {
        return stockQuantDao.getContainerIdByLocationId(locationId).size();
    }


    @Transactional(readOnly = false)
    public void updateLocationStatus(Long locationId) throws BizCheckedException {
        BaseinfoLocation fromLocation = locationService.getLocation(locationId);
        if (null == fromLocation) {
            throw new BizCheckedException("2180001");
        } else {
            Long currentVol = new Long(this.getContainerQty(locationId));
            logger.warn("currentVol is " + currentVol);
            locationService.refreshContainerVol(locationId, currentVol);
        }
    }

    @Transactional(readOnly = false)
    public void move(StockMove move) throws BizCheckedException {

        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", move.getItemId());
        mapQuery.put("locationId", move.getFromLocationId());
        mapQuery.put("containerId", move.getFromContainerId());
        List<StockQuant> quantList = stockQuantDao.getQuants(mapQuery);
        if (0 == quantList.size()) {
            throw new BizCheckedException("2550009");
        }
        BigDecimal qtyDone = move.getQty();
        for (StockQuant quant : quantList) {
            if (quant.getReserveTaskId() != 0 && quant.getReserveTaskId().compareTo(move.getTaskId()) != 0) {
                continue;
            }
            this.split(quant, qtyDone);
            quant.setLocationId(move.getToLocationId());
            quant.setContainerId(move.getToContainerId());

            this.update(quant);
            // 新建 quant move历史记录
            if(move.getId() ==null){
                // 创建move
                moveDao.insert(move);
            }
            StockQuantMoveRel moveRel = new StockQuantMoveRel();
            moveRel.setMoveId(move.getId());
            moveRel.setQuantId(quant.getId());
            relDao.insert(moveRel);

            // 对于移出库的库存，移出stock_quant表,
            BaseinfoLocation toLocation = locationService.getLocation(move.getToLocationId());
            if (toLocation.getType().equals(LocationConstant.CONSUME_AREA)) {
                quant.setIsInhouse(0L);
                this.moveToComplete(quant);
            }

            // 是否已经完成move？
            qtyDone = qtyDone.subtract(quant.getQty());
            if (qtyDone.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }
        if (qtyDone.compareTo(BigDecimal.ZERO) > 0) {
            throw new BizCheckedException("2550008");
        }
        this.updateLocationStatus(move.getFromLocationId());
        this.updateLocationStatus(move.getToLocationId());
    }

    @Transactional(readOnly = false)
    public void split(StockQuant quant, BigDecimal requiredQty) {
        if (quant.getQty().compareTo(requiredQty) <= 0 && quant.getQty().compareTo(BigDecimal.ZERO) > 0) {
            return;
        }

        StockQuant newQuant = (StockQuant) quant.clone();
        newQuant.setQty(quant.getQty().subtract(requiredQty));
        newQuant.setReserveTaskId(0L);
        this.create(newQuant);
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("quantId", quant.getId());
        List<StockQuantMoveRel> relList = relDao.getStockQuantMoveRelList(queryMap);
        for (StockQuantMoveRel rel : relList) {
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
            if (!quant.isAvailable()) {
                continue;
            }
            this.split(quant, requiredQty);
            quant.setReserveTaskId(taskId);
            resultList.add(quant);
            stockQuantDao.update(quant);
            requiredQty = requiredQty.subtract(quant.getQty());
            if (requiredQty.compareTo(BigDecimal.ZERO) == 0) {
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
                throw new BizCheckedException("3550003");
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
        if (quant.getIsNormal() == 1) {
            quant.setIsNormal(0L);
            quant.setIsInhouse(0L);
            stockRedisService.outBound(quant.getItemId(), quant.getQty());
        }
        stockQuantDao.update(quant);
    }


    @Transactional(readOnly = false)
    public void unFreeze(StockQuant quant) {
        quant.setIsDefect(0L);
        quant.setIsRefund(0L);
        quant.setIsFrozen(0L);
        quant.setIsNormal(1L);
        quant.setIsInhouse(1L);
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void toDefect(StockQuant quant) {
        quant.setIsFrozen(0L);
        quant.setIsDefect(1L);
        quant.setIsRefund(0L);
        if (quant.getIsNormal() == 1) {
            quant.setIsNormal(0L);
            quant.setIsInhouse(0L);
            stockRedisService.outBound(quant.getItemId(), quant.getQty());
        }
        stockQuantDao.update(quant);
    }

    @Transactional(readOnly = false)
    public void toRefund(StockQuant quant) {
        quant.setIsFrozen(0L);
        quant.setIsRefund(1L);
        quant.setIsDefect(0L);
        if (quant.getIsNormal() == 1) {
            quant.setIsNormal(0L);
            quant.setIsInhouse(0L);
            stockRedisService.outBound(quant.getItemId(), quant.getQty());
        }
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
        //业务需求改动:不需要加锁
        //locationService.lockLocationById((Long) mapCondition.get("locationId"));

        BigDecimal requiredQty = new BigDecimal(mapCondition.get("requiredQty").toString());
        List<StockQuant> quantList = this.getQuants(mapCondition);
        for (StockQuant quant : quantList) {
            if (requiredQty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            // need > have  //// TODO: 16/11/2 修改原因:逻辑错误, need>have应抛出异常,上层已拦截,不会出现该情况
            /*if (requiredQty.compareTo(quant.getQty()) > 0) {
                this.dealOne(quant, operation);
                requiredQty = requiredQty.subtract(quant.getQty());
            } else {*/
                if (requiredQty.compareTo(quant.getQty()) == -1) {
                    this.split(quant, requiredQty);
                }
                this.dealOne(quant, operation);
                break;
            //}
        }
    }

    public List<Long> getContainerIdByLocationId(Long locationId) {
        return stockQuantDao.getContainerIdByLocationId(locationId);
    }

    public BigDecimal getQuantQtyByLocationIdAndItemId(Long locationId, Long itemId) {
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("locationId", locationId);
        queryMap.put("itemId", itemId);
        this.prepareQuery(queryMap);
        List<StockQuant> stockQuants = stockQuantDao.getQuants(queryMap);
        BigDecimal qty = new BigDecimal(0L);
        for (StockQuant quant : stockQuants) {
            qty = qty.add(quant.getQty());
        }
        return qty;
    }

    public BigDecimal getQuantQtyByContainerId(Long containerId) {
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("containerId", containerId);
        this.prepareQuery(queryMap);
        List<StockQuant> stockQuants = stockQuantDao.getQuants(queryMap);
        BigDecimal qty = new BigDecimal(0L);
        for (StockQuant quant : stockQuants) {
            qty = qty.add(quant.getQty());
        }
        return qty;
    }

    public Long getSupplierByLocationAndItemId(Long locationId, Long itemId) {
        Set<Long> suppliers = new HashSet<Long>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("locationId", locationId);
        queryMap.put("itemId", itemId);
        this.prepareQuery(queryMap);
        List<StockQuant> quants = stockQuantDao.getQuants(queryMap);
        if (quants != null && quants.size() != 0) {
            return quants.get(0).getSupplierId();
        } else {
            return null;
        }
    }
    public Long getLocationBylot(Long lotId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("lotId", lotId);
        List<StockQuant> quants = stockQuantDao.getQuants(queryMap);
        if (quants != null && quants.size() != 0) {
            return quants.get(0).getLocationId();
        } else {
            return null;
        }
    }

    public int countStockQuant(Map<String, Object> mapQuery) {
        return stockQuantDao.countStockQuant(mapQuery);
    }

    @Transactional(readOnly = false)
    public void move(StockMove move, StockLot lot) {
        // 创建move
        moveDao.insert(move);

        // 创建quant
        StockLot stockLot = null;
        if(lot.getLotId()!=null&& lot.getLotId()!=0 ) {
            stockLot = lotService.getStockLotByLotId(lot.getLotId());
        }
        if(stockLot==null) {
            lotService.insertLot(lot);
        }

        StockQuant quant = new StockQuant();
        quant.setLotId(lot.getLotId());
        quant.setPackUnit(lot.getPackUnit());
        quant.setPackName(lot.getPackName());
        quant.setSkuId(lot.getSkuId());
        quant.setItemId(lot.getItemId());
        quant.setLocationId(move.getToLocationId());
        quant.setContainerId(move.getToContainerId());
        quant.setSupplierId(lot.getSupplierId());
        quant.setOwnerId(itemService.getItem(quant.getItemId()).getOwnerId());
        quant.setInDate(lot.getInDate());
        quant.setExpireDate(lot.getExpireDate());
        quant.setQty(move.getQty());
        quant.setLotCode(lot.getCode());
        quant.setLotId(lot.getLotId());
        this.create(quant);

        // 新建 quant move历史记录
        StockQuantMoveRel moveRel = new StockQuantMoveRel();
        moveRel.setMoveId(move.getId());
        moveRel.setQuantId(quant.getId());
        relDao.insert(moveRel);

        this.updateLocationStatus(move.getToLocationId());
    }

}