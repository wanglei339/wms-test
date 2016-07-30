package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * Created by mali on 16/7/28.
 */
@Service(protocol = "dubbo")
public class StockMoveRpcService implements IStockMoveRpcService {
    @Autowired
    private StockQuantService quantService;

    @Autowired
    private StockMoveService moveService;


    public void create(StockMove move) {
        moveService.create(move);
    }
    public void create(List<StockMove> moveList) {
        moveService.create(moveList);
    }

    @Transactional(readOnly = false)
    public void done(Long moveId) throws BizCheckedException{
        StockMove move = moveService.getMoveById(moveId);
        if (move == null) {
            throw new BizCheckedException("1550001");
        }
        if ( ! move.isValid() ) {
            throw new BizCheckedException("1550002");
        }
        quantService.move(move);
        moveService.done(moveId);
    }

    @Transactional(readOnly = false)
    public void moveWholeContainer(Long containerId, Long taskId, Long staffId, Long fromLocationId, Long toLocationId) throws BizCheckedException {
        List<StockQuant> quantList = quantService.reserveByContainer(containerId, taskId);

        for (StockQuant quant : quantList) {
            StockMove move = new StockMove();
            move.setTaskId(taskId);
            move.setFromLocationId(fromLocationId);
            move.setToLocationId(toLocationId);
            move.setFromContainerId(containerId);
            move.setToContainerId(containerId);
            move.setItemId(quant.getItemId());
            move.setQty(quant.getQty());
            move.setOperator(staffId);
            moveService.create(move);
            this.done(move.getId());
            quantService.unReserveById(quant.getId());
        }
    }
}
