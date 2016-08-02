package com.lsh.wms.api.service.inhouse;

import com.lsh.base.common.exception.BizCheckedException;

import java.util.Map;

/**
 * Created by mali on 16/8/2.
 */
public interface IProcurementRestService {
    String scanFromLocation() throws BizCheckedException;
    String scanToLocation() throws  BizCheckedException;
    String taskView() throws BizCheckedException;
    String fetchTask() throws BizCheckedException;
}
