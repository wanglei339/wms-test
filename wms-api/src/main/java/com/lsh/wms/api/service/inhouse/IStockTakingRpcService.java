package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.taking.StockTakingDetail;

/**
 * Created by wuhao on 2016/11/24.
 */
public interface IStockTakingRpcService {
    StockTakingDetail fillDetail(StockTakingDetail detail) throws BizCheckedException;
}
