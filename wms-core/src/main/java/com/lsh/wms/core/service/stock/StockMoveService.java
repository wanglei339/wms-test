package com.lsh.wms.core.service.stock;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.stock.StockMoveDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.core.dao.stock.StockQuantMoveRelDao;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
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
    public void updateStatus(Long moveId, Long newStatus){
        StockMove move = moveDao.getStockMoveById(moveId);
        move.setStatus(newStatus);
        this.update(move);
    }

    @Transactional(readOnly = false)
    public void assign(Long moveId) {
        this.updateStatus(moveId, TaskConstant.Assigned);
    }

    @Transactional(readOnly = false)
    public void allocate(Long moveId, Long operator) {
        StockMove move = moveDao.getStockMoveById(moveId);
        move.setOperator(operator);
        move.setStatus(TaskConstant.Allocated);
        this.update(move);
    }

    @Transactional(readOnly = false)
    public void done(Long moveId, BigDecimal qtyDone) {
        StockMove move = moveDao.getStockMoveById(moveId);
        move.setQtyDone(qtyDone);
        move.setStatus(TaskConstant.Done);
        quantService.move(moveId);
        this.update(move);
    }

    @Transactional(readOnly = false)
    public void cancel(Long moveId) {
        quantService.unReserveByMoveId(moveId);
        this.updateStatus(moveId, TaskConstant.Cancel);
    }

    public List<StockMove> getMoveList(Map<String, Object> mapQuery) {
        return moveDao.getStockMoveList(mapQuery);
    }

    public List<StockQuantMoveRel> getHistoryById(Long moveId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("moveId",moveId);
        return relDao.getStockQuantMoveRelList(mapQuery);
    }
}
