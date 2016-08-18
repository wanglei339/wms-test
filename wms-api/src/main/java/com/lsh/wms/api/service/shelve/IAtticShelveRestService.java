package com.lsh.wms.api.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by wuhao on 16/8/16.
 */
public interface IAtticShelveRestService {
    String scanTargetLocation() throws BizCheckedException;
    String scanContainer() throws BizCheckedException;
    String createTask() throws BizCheckedException;
}
