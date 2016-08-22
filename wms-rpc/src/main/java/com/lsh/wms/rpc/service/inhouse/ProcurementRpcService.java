package com.lsh.wms.rpc.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.rpc.service.stock.StockQuantRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by mali on 16/7/30.
 */
@Service(protocol = "dubbo")
public class ProcurementRpcService implements IProcurementRpcService{
    @Autowired
    private StockQuantRpcService quantService;

    private static Logger logger = LoggerFactory.getLogger(ProcurementRpcService.class);


    @Autowired
    private ItemService itemService;

    public boolean needProcurement(Long locationId, Long itemId) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        BigDecimal qty = quantService.getQty(condition);
        if (qty.equals(BigDecimal.ZERO)) {
            return true;
        }
        StockQuant quant = quantService.getQuantList(condition).get(0);
        qty = qty.divide(quant.getPackUnit(),4);
        BaseinfoItem itemInfo = itemService.getItem(itemId);
        qty = qty.divide(itemInfo.getPackUnit(),4);
        if (itemInfo.getItemLevel() == 1) {
            return qty.compareTo(new BigDecimal(5.0)) >= 0 ? false : true;
        } else if (itemInfo.getItemLevel() == 2) {
            return qty.compareTo(new BigDecimal(3.0)) >= 0 ? false : true;
        } else if (itemInfo.getItemLevel() == 3) {
            return qty.compareTo(new BigDecimal(2.0)) >= 0 ? false : true;
        } else {
            return false;
        }
    }

    public BigDecimal getProcurementQty(BaseinfoItemLocation itemLocation) throws BizCheckedException {
        BigDecimal qty = BigDecimal.ZERO;
        if (itemLocation.getPickLocationid() == 0L) {
            // 阁楼去补货需要计算补货数量
        }
        return qty;
    }

}
