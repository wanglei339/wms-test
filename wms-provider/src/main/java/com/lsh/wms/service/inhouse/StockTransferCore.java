package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.transfer.StockTransferPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by mali on 16/7/30.
 */
@Component
public class StockTransferCore {

    private static Logger logger = LoggerFactory.getLogger(StockTransferCore.class);

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private IStockQuantRpcService stockQuantRpcService;

    public void fillTransferPlan(StockTransferPlan plan) throws BizCheckedException {
        BigDecimal packUnit = itemRpcService.getPackUnit(plan.getPackName());
        BigDecimal requiredQty = plan.getUomQty().multiply(packUnit);
        if (requiredQty.equals(BigDecimal.ZERO)) {
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(plan.getFromLocationId());
            condition.setItemId(plan.getItemId());
            BigDecimal total = stockQuantRpcService.getQty(condition);
            plan.setQty(total);
        }
    }

    public void outbound(StockTransferPlan plan) throws BizCheckedException {

    }

    public void inbound(StockTransferPlan plan) throws BizCheckedException {

    }

}
