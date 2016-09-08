package com.lsh.wms.api.service.inventory;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/9/8
 * Time: 16/9/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.inventory.
 * desc:类功能描述
 */
public interface ISynStockService {
    public void synStock(Long sku_id,Double qty);
}
