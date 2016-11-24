package com.lsh.wms.rpc.service.inhouse;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.inhouse.IStockTakingRpcService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mali on 16/7/22.
 */
@Service(protocol = "dubbo")
public class StockTakingRpcService implements IStockTakingRpcService {

    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private StockQuantService quantService;

}
