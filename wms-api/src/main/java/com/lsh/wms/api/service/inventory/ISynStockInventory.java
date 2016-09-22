package com.lsh.wms.api.service.inventory;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/9/22
 * Time: 16/9/22.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.inventory.
 * desc:类功能描述
 */
public interface ISynStockInventory {
    public void synStock(Long item_id, Double qty);
}
