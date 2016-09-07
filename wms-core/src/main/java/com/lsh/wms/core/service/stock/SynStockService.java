package com.lsh.wms.core.service.stock;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.inventory.ISynInventory;
import com.lsh.wms.api.service.inventory.ISynStockService;
import org.springframework.stereotype.Component;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/9/7
 * Time: 16/9/7.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.stock.
 * desc:类功能描述
 */
@Component
@Service(protocol = "dubbo")
public class SynStockService implements ISynStockService {

    @Reference(async = true)
    private ISynInventory iSynInventory;


    public void synStock(Long sku_id, Double qty) {
        // iSynInventory.synInventory(sku_id,qty);
    }
}
