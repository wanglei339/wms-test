package com.lsh.wms.api.service.seed;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.po.ReceiptRequest;

import java.util.Map;

/**
 * Created by wuhao on 16/9/28.
 */
public interface ISeedRpcService {
    void insertReceipt(ReceiptRequest receiptRequest) throws BizCheckedException;
}
