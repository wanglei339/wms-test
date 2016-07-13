package com.lsh.wms.api.service.location;

/**
 * Created by fengkun on 16/7/11.
 */
public interface ILocationRestService {
    public String getLocation(Long locationId);
    public String getStoreLocationIds(Long locationId);
    public String getFatherByType(Long locationId, String type);
    public String getFatherArea(Long locationId);
}
