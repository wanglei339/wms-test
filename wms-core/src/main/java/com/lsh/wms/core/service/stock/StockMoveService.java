package com.lsh.wms.core.service.stock;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.core.dao.stock.StockQuantMoveRelDao;
import com.lsh.wms.core.service.location.LocationService;
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

    public StockMove getMoveById(Long moveId) {
        return moveDao.getStockMoveById(moveId);
    }

    @Transactional(readOnly = false)
    public void create(StockMove move){
        move.setCreatedAt(DateUtils.getCurrentSeconds());
        move.setUpdatedAt(DateUtils.getCurrentSeconds());
        moveDao.insert(move);
    }

    @Transactional(readOnly = false)
    public void create(List<StockMove> moveList) {
        for (StockMove move : moveList) {
            this.create(move);
        }
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

    public List<StockMove> getMoveList(Map<String, Object> mapQuery) {
        return moveDao.getStockMoveList(mapQuery);
    }

    public List<StockQuantMoveRel> getHistoryById(Long moveId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("moveId",moveId);
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
        List<StockQuant> quantList = quantService.reserveByContainer(fromContainerId, taskId);
        for (StockQuant quant : quantList) {
            StockMove move = new StockMove();
            move.setTaskId(taskId);
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(toLocationId);
            move.setFromContainerId(fromContainerId);
            move.setToContainerId(toContainerId);
            move.setItemId(quant.getItemId());
            move.setQty(quant.getQty());
            move.setOperator(staffId);
            this.create(move);
            quantService.move(move);
            quantService.unReserveById(quant.getId());
        }
    }

    @Transactional(readOnly = false)
    public void move(List<StockMove> moveList) throws BizCheckedException{
        boolean islocked = false;
        SortedSet<Long> locationSet = new TreeSet<Long>();
        for (StockMove move : moveList) {
            locationSet.add(move.getFromLocationId());
        }
        for (Long locationId : locationSet) {
            locationService.lockLocation(locationId);
        }
        for (StockMove move : moveList) {
            this.create(move);
            quantService.move(move);
        }
    }

    @Transactional(readOnly = false)
    public void moveToContainer(Long itemId, Long operator,Long fromContainer,Long toContainer,Long locationId,BigDecimal qty) throws BizCheckedException {
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizCheckedException("1550001" );
        }

        locationService.lockLocationByContainer(fromContainer);

        Map<String, Object> queryMap = new HashMap<String, Object>();
        BigDecimal total = BigDecimal.ZERO;
        queryMap.put("itemId",itemId);
        queryMap.put("containerId",fromContainer);
        List<StockQuant> stockQuants = quantService.getQuants(queryMap);
        if(stockQuants==null || stockQuants.size()==0){
            throw new BizCheckedException("2550009");
        }
        for(StockQuant quant:stockQuants){
            total = total.add(quant.getQty());
        }
        if(total.subtract(qty).floatValue() < 0){
            throw new BizCheckedException("2550008");
        }
        StockQuant quant = stockQuants.get(0);
        StockMove move =new StockMove();
        move.setQty(qty);
        move.setSkuId(quant.getSkuId());
        move.setOwnerId(quant.getOwnerId());
        move.setItemId(quant.getItemId());
        move.setFromLocationId(quant.getLocationId());
        move.setToLocationId(locationId);
        move.setFromContainerId(fromContainer);
        move.setToContainerId(toContainer);
        move.setOperator(operator);
        this.create(move);
        quantService.move(move);
    }

}
