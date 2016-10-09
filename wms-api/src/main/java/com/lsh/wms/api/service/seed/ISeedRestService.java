package com.lsh.wms.api.service.seed;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/9/28.
 */
public interface ISeedRestService {
    String assign() throws BizCheckedException;
    String scanContainer() throws BizCheckedException;
}
