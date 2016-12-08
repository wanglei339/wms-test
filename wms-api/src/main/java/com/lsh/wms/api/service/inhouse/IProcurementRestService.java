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
    String scanLocation() throws BizCheckedException;
    public String getZoneList() throws BizCheckedException;
    public String loginToZone() throws BizCheckedException;
    public String logoutFromZone() throws BizCheckedException;
}
