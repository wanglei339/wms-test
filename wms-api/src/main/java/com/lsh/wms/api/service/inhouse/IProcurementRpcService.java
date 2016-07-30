package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;

/**
 * Created by mali on 16/7/30.
 */
public interface IProcurementRpcService {
    boolean needProcurement(BaseinfoItemLocation itemLocation) throws BizCheckedException;
}
