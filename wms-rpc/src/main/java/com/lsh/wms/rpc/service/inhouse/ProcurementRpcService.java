package com.lsh.wms.rpc.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.rpc.service.stock.StockQuantRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by mali on 16/7/30.
 */
@Service(protocol = "dubbo")
public class ProcurementRpcService implements IProcurementRpcService{
    @Autowired
    private StockQuantRpcService quantService;


    @Autowired
    private ItemService itemService;

    public boolean needProcurement(Long locationId, Long itemId) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        condition.setItemId(itemId);
        condition.setLocationId(locationId);
        BigDecimal qty = quantService.getQty(condition);
        qty = qty.divide(itemService.getItem(itemId).getPackUnit());

        BaseinfoItem itemInfo = itemService.getItem(itemId);
        qty = qty.divide(itemInfo.getPackUnit());
        if (itemInfo.getItemLevel() == 1) {
            return qty.compareTo(new BigDecimal(5.0)) < 0 ? false : true;
        } else if (itemInfo.getItemLevel() == 2) {
            return qty.compareTo(new BigDecimal(3.0)) < 0 ? false : true;
        } else if (itemInfo.getItemLevel() == 3) {
            return qty.compareTo(new BigDecimal(2.0)) < 0 ? false : true;
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
