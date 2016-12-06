package com.lsh.wms.api.service.zone;

import com.lsh.wms.model.zone.WorkZone;

import java.util.Map;

/**
 * Created by zengwenjun on 16/12/5.
 */
public interface IWorkZoneRestService {
    String getWorkZoneList(Map<String, Object> mapQuery);
    String getWorkZoneCount(Map<String, Object> mapQuery);
    String getWorkZone(long zoneId);
    String createWorkZone(WorkZone zone);
    String updateWorkZone(WorkZone zone);
    String deleteWorkZone(long zoneId);
}
