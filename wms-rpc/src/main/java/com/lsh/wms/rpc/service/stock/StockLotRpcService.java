package com.lsh.wms.rpc.service.stock;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.stock.IStockLotRpcService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.stock.StockLot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/7/29.
 */

public class StockLotRpcService implements IStockLotRpcService{
    private static Logger logger = LoggerFactory.getLogger(StockLotRpcService.class);

    @Autowired
    private StockLotService stockLotService;


    public StockLot getStockLotByLotId(@QueryParam("lotId") long iLotId) {
        StockLot stockLot = stockLotService.getStockLotByLotId(iLotId);
        return stockLot;
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
    public Boolean insertLot(StockLot lot) {
        lot.setLotId(RandomUtils.genId());
        if(stockLotService.getStockLotByLotId(lot.getLotId()) != null) {
            return false;
        }
        try {
            stockLotService.insertLot(lot);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return false;
        }
        return true;
    }

    public boolean updateLot(StockLot lot) {
        if(stockLotService.getStockLotByLotId(lot.getLotId()) == null) {
            return false;
        }
        try {
            stockLotService.updateLot(lot);
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return false;
        }
        return true;
    }

    public List searchLot(Map<String, Object> mapQuery) {
        return stockLotService.searchLot(mapQuery);
    }


}
