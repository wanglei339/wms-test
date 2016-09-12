package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.stock.IStockLotRpcService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.stock.StockLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/7/29.
 */
@Service(protocol = "dubbo")
public class StockLotRpcService implements IStockLotRpcService {
    private static Logger logger = LoggerFactory.getLogger(StockLotRpcService.class);

    @Autowired
    private StockLotService stockLotService;

    public StockLot getLotByLotId(long iLotId) {
        return stockLotService.getStockLotByLotId(iLotId);
    }

    /***
     * skuId         商品id
     * serialNo      生产批次号
     * inDate        入库时间
     * productDate   生产时间
     * expireDate    保质期失效时间
     * itemId
     * poId          采购订单
     * receiptId     收货单
     * packUnit      包装单位
     * packName      包装名称
     * supplierId    供应商Id
     */
    public void insert(StockLot lot) {
        stockLotService.insertLot(lot);
    }

    public void update(StockLot lot) {
        stockLotService.updateLot(lot);
    }

    public List<StockLot> search(Map<String, Object> mapQuery) {
        return stockLotService.searchLot(mapQuery);
    }

}
