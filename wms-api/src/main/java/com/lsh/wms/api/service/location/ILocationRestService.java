package com.lsh.wms.api.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;

/**
 * Created by fengkun on 16/7/11.
 */
public interface ILocationRestService {
    public String getLocation(Long locationId);
    public String getStoreLocationIds(Long locationId);
    public String getFatherByType(Long locationId, String type);
    public String getFatherArea(Long locationId);
    public String getWarehouseLocationId();
    public String getInventoryLostLocationId();
    public String insertLocation(BaseinfoLocation location);
    public String updateLocation(BaseinfoLocation location);
}
