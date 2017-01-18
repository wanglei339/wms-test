package com.lsh.wms.api.service.back;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/10/21.
 */

public interface IInStorageRfRestService {
    String getPickLocation() throws BizCheckedException;
    String scanLocation() throws BizCheckedException;
}
