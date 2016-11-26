package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.transfer.StockTransferPlan;
/**
 * Created by mali on 16/7/26.
 */

//作废 by 曾文军 @2016.11.24
public interface IStockTransferRestService {
    String taskView() throws BizCheckedException;
    String viewLocation() throws BizCheckedException;
    String createPlan() throws BizCheckedException;
    String scanLocation() throws BizCheckedException;
    String createScrap() throws BizCheckedException;
    String createReturn() throws BizCheckedException;
    String fetchTask() throws BizCheckedException;
}
