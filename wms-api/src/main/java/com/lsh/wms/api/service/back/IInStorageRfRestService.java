package com.lsh.wms.api.service.back;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/10/21.
 */

public interface IInStorageRfRestService {
    String getSupplierInfo() throws BizCheckedException;
    String backConfirm() throws BizCheckedException;
    String scanLocation() throws BizCheckedException;
}
