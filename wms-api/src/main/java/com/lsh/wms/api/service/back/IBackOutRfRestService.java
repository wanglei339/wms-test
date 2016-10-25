package com.lsh.wms.api.service.back;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/10/21.
 */

public interface IBackOutRfRestService {

    String outConfirm() throws BizCheckedException;

    String getInfo() throws BizCheckedException;
}
