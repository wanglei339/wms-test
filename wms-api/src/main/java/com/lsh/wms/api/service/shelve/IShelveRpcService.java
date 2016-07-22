package com.lsh.wms.api.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;

/**
 * Created by fengkun on 16/7/15.
 */
public interface IShelveRpcService {
    public BaseinfoLocation assginShelveLocation(BaseinfoContainer container) throws BizCheckedException;
    public BaseinfoLocation assignPickingLocation(BaseinfoContainer container) throws BizCheckedException;
    public BaseinfoLocation assignShelfLocation(BaseinfoContainer container, BaseinfoLocation pickingLocation) throws BizCheckedException;
}
