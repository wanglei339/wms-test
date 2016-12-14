package com.lsh.wms.api.service.inventory;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/8/20
 * Time: 16/8/20.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.inventory.
 * desc:类功能描述
 */
public interface ISynInventory {
    public void synInventory(Long item_id,Double qty) throws BizCheckedException;
}
