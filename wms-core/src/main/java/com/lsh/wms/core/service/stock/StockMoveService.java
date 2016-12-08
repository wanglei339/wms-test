package com.lsh.wms.core.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.CollectionUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockQuantMoveRelDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.*;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/7/11.ck
 */

@Component
@Transactional(readOnly = true)
public class StockMoveService {

    private static final Logger logger = LoggerFactory.getLogger(StockMoveService.class);

    @Autowired
    private StockMoveDao moveDao;

    @Autowired
    private StockQuantService quantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockQuantMoveRelDao relDao;

    @Autowired
    private StockSummaryService stockSummaryService;

    @Autowired
    private PersistenceProxy persistenceProxy;

    @Transactional(readOnly = false)
    public void create(StockMove move) {
        move.setCreatedAt(DateUtils.getCurrentSeconds());
        move.setUpdatedAt(DateUtils.getCurrentSeconds());
        moveDao.insert(move);
    }

    @Transactional(readOnly = false)
    private void update(StockMove move) {
        move.setUpdatedAt(DateUtils.getCurrentSeconds());
        moveDao.update(move);
    }

    @Transactional(readOnly = false)
    public void done(Long moveId) {
        StockMove move = moveDao.getStockMoveById(moveId);
        move.setStatus(TaskConstant.Done);
        this.update(move);
    }

    public List<StockMove> traceQuant(Long quantId) {
        return moveDao.traceQuant(quantId);
    }

    public List<StockQuantMoveRel> getHistoryById(Long moveId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("moveId", moveId);
        return relDao.getStockQuantMoveRelList(mapQuery);
    }

    @Transactional(readOnly = false)
    public void moveWholeContainer(Long containerId, Long taskId, Long staffId, Long fromLocationId, Long toLocationId) throws BizCheckedException {
        locationService.lockLocationByContainer(containerId);
        this.moveWholeContainer(containerId, containerId, taskId, staffId, fromLocationId, toLocationId);
    }

    @Transactional(readOnly = false)
    public void moveWholeContainer(Long fromContainerId, Long toContainerId, Long taskId, Long staffId, Long fromLocationId, Long toLocationId) throws BizCheckedException {
        locationService.lockLocationByContainer(fromContainerId);
        //List<StockQuant> quantList = quantService.reserveByContainer(fromContainerId, taskId);
        List<StockQuant> quantList = quantService.getQuantsByContainerId(fromContainerId);
        List<StockMove> moveList = new ArrayList<StockMove>();
        for (StockQuant quant : quantList) {
            StockMove move = new StockMove();
            move.setTaskId(taskId);
            move.setFromLocationId(quant.getLocationId());
            move.setToLocationId(toLocationId);
            move.setFromContainerId(fromContainerId);
            move.setToContainerId(toContainerId);
            move.setItemId(quant.getItemId());
            move.setQty(quant.getQty());
            move.setOperator(staffId);
            moveList.add(move);
//            if (move.getQty().compareTo(BigDecimal.ZERO) <= 0) {
//                throw new BizCheckedException("1550001");
//            }
//            this.create(move);
//            quantService.move(move);
//            BaseinfoLocation location = locationService.getLocation(move.getToLocationId());
//            if (location.getType().compareTo(LocationConstant.CONSUME_AREA) != 0) {
//                quantService.unReserveById(quant.getId());
//            }
        }
        move(moveList);
    }

    @Transactional(readOnly = false)
    public void move(List<StockMove> moveList) throws BizCheckedException {
        // 按照itemId排序，避免死锁
        Collections.sort(moveList, new Comparator<StockMove>() {
            public int compare(StockMove o1, StockMove o2) {
                return o1.getItemId().compareTo(o2.getItemId());
            }
        });
        for (StockMove move : moveList) {
            if (move.getQty().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BizCheckedException("1550001");
            }
            this.move(move);
        }
     }

    public void checkMove(StockMove move) throws BizCheckedException {
        if (move.getQty().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizCheckedException("1550001");
        }
    }

    @Transactional(readOnly = false)
    public void move(StockMove move) throws BizCheckedException {

        checkMove(move);

        locationService.lockLocationById(move.getFromLocationId());

        this.create(move);

        if (move.getLot() == null) {
            quantService.move(move);
        } else {
            this.moveWithLot(move, move.getLot());
        }

        stockSummaryService.changeStock(move);

        //库存转移 转残 转退 都需要回传sap
        BaseinfoLocation fromLocation = locationService.getLocation(move.getFromLocationId());
        BaseinfoLocation toLocation = locationService.getLocation(move.getToLocationId());
        Long fromType = fromLocation.getType();
        Long toType = toLocation.getType();
        boolean fromflag = LocationConstant.BACK_AREA == fromType || LocationConstant.DEFECTIVE_AREA == fromType;
        boolean toflag = LocationConstant.BACK_AREA == toType || LocationConstant.DEFECTIVE_AREA == toType;
        if((fromflag || toflag) && !(fromflag && toflag)){
            persistenceProxy.doOne(SysLogConstant.LOG_TYPE_MOVING,move.getTaskId(),null);
        }
    }

    @Transactional(readOnly = false)
    public void moveToConsume(Long locationId, Long taskId, Long staffId) throws BizCheckedException {

        List<StockQuant> quants = quantService.getQuantsByLocationId(locationId);
        if(quants==null){
            return;
        }
        BaseinfoLocation location = locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0);

        //存储已经生成move的ContainerId
        Map<Long, Integer> isMovedMap = new HashMap<Long, Integer>();

        for (StockQuant quant : quants) {
            if (!isMovedMap.containsKey(quant.getContainerId())) {
                this.moveWholeContainer(quant.getContainerId(), taskId, staffId, quant.getLocationId(), location.getLocationId());
                isMovedMap.put(quant.getContainerId(), 1);
            }
        }
    }

//    @Transactional(readOnly = false)
//    public void moveToConsume(Set<Long> containerIds) throws BizCheckedException {
//        List<StockQuant> stockQuants = new ArrayList<StockQuant>();
//        for (Long containerId : containerIds) {
//            List<StockQuant> quants = quantService.getQuantsByContainerId(containerId);
//            if (quants == null || quants.isEmpty()) {
//                continue;
//            }
//            stockQuants.addAll(quants);
//        }
//        BaseinfoLocation location = locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0);
//
//        //存储已经生成move的ContainerId
//        Map<Long, Integer> isMovedMap = new HashMap<Long, Integer>();
//
//        for (StockQuant quant : stockQuants) {
//            if (!isMovedMap.containsKey(quant.getContainerId())) {
//                this.moveWholeContainer(quant.getContainerId(), 0L, 0L, quant.getLocationId(), location.getLocationId());
//                isMovedMap.put(quant.getContainerId(), 1);
//            }
//        }
//    }

    @Transactional(readOnly = false)
    public void moveToConsume(Set<Long> containerIds) throws BizCheckedException {
        BaseinfoLocation location = locationService.getLocationsByType(LocationConstant.CONSUME_AREA).get(0);

        for (Long containerId : containerIds ) {
            Long fromLocationId = quantService.getLocationIdByContainerId(containerId).get(0);
            this.moveWholeContainer(containerId, 0L, 0L, fromLocationId, location.getLocationId());
        }
    }

    @Transactional(readOnly = false)
    public void moveToContainer(Long itemId, Long operator, Long fromContainer, Long toContainer, Long locationId, BigDecimal qty) throws BizCheckedException {
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizCheckedException("1550001");
        }

        locationService.lockLocationByContainer(fromContainer);

        Map<String, Object> queryMap = new HashMap<String, Object>();
        BigDecimal total = BigDecimal.ZERO;
        queryMap.put("itemId", itemId);
        queryMap.put("containerId", fromContainer);
        List<StockQuant> stockQuants = quantService.getQuants(queryMap);
        if (stockQuants == null || stockQuants.size() == 0) {
            throw new BizCheckedException("2550009");
        }
        for (StockQuant quant : stockQuants) {
            total = total.add(quant.getQty());
        }
        if (total.subtract(qty).floatValue() < 0) {
            throw new BizCheckedException("2550008");
        }
        StockQuant quant = stockQuants.get(0);
        StockMove move = new StockMove();
        move.setQty(qty);
        move.setSkuId(quant.getSkuId());
        move.setOwnerId(quant.getOwnerId());
        move.setItemId(quant.getItemId());
        move.setFromLocationId(quant.getLocationId());
        move.setToLocationId(locationId);
        move.setFromContainerId(fromContainer);
        move.setToContainerId(toContainer);
        move.setOperator(operator);
        this.move(move);
    }

    @Transactional(readOnly = false)
    private void moveWithLot(StockMove move, StockLot lot) {
        quantService.move(move, lot);
    }

    @Transactional(readOnly = false)
    public void move(StockMove move, StockLot lot) {
        move.setLot(lot);
        this.move(move);
    }

    public StockMove getStockMoveByTaskId(Long taskId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("taskId",taskId);
        List<StockMove> lists = moveDao.getStockMoveList(map);
        if (CollectionUtils.isEmpty(lists)){
            return null;
        }
        return lists.get(0);

    }
}
