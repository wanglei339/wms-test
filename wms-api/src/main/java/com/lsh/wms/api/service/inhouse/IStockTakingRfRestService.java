package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/7/30.
 */
public interface IStockTakingRfRestService {
    String doOne() throws BizCheckedException;
    String getTaskInfo() throws BizCheckedException;
    String assign() throws BizCheckedException;
    String view() throws BizCheckedException;
    String restore() throws BizCheckedException;
}
