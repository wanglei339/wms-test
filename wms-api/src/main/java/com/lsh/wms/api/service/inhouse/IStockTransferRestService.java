package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;
/**
 * Created by mali on 16/7/26.
 */
public interface IStockTransferRestService {
    String taskView() throws BizCheckedException;
    String scanLocation() throws BizCheckedException;
    String createScrap() throws BizCheckedException;
    String createReturn() throws BizCheckedException;
    String fetchTask() throws BizCheckedException;
    String unFetchTask() throws BizCheckedException;
}
