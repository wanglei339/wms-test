package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * Created by zengwenjun on 16/7/15.
 */
public interface IPickRestService {
    String createTask() throws BizCheckedException;
    String scanPickTask() throws BizCheckedException;
    String scanPickLocation() throws BizCheckedException;
}
