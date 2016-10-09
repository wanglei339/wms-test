package com.lsh.wms.api.service.seed;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/10/8.
 */
public interface ISetGoodsRestService {
    String doSet() throws BizCheckedException;
    String view() throws BizCheckedException;
}
