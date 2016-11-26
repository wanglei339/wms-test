package com.lsh.wms.api.service.seed;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by wuhao on 16/9/28.
 */
public interface ISeedProveiderRestService {
    String getSeedTask();
    String countSeedTask();
    String getSeedTaskByContainerId(Long realContainerId);
}
