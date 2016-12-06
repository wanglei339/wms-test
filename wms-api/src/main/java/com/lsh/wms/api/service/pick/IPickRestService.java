package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by zengwenjun on 16/7/15.
 */
public interface IPickRestService {
    String scanPickTask() throws BizCheckedException;
    String scanPickLocation() throws BizCheckedException;
    String restore() throws BizCheckedException;
    String splitNewPickTask() throws BizCheckedException;
    String getPickQuantQty() throws BizCheckedException;
}
