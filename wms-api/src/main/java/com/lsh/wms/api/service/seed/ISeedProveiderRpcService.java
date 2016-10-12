package com.lsh.wms.api.service.seed;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.transfer.StockTransferPlan;

import java.util.Map;
import java.util.Set;

/**
 * Created by wuhao on 16/9/28.
 */
public interface ISeedProveiderRpcService {
    Long getTask( Map<String, Object> mapQuery) throws BizCheckedException;
    void createTask( Map<String, Object> mapQuery) throws BizCheckedException;
}
