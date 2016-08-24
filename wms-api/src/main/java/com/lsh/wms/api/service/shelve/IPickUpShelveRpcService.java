package com.lsh.wms.api.service.shelve;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskEntry;

/**
 * Created by wuhao on 16/8/24.
 */
public interface IPickUpShelveRpcService {
    TaskEntry createTask(Long containerId) throws BizCheckedException;

}
