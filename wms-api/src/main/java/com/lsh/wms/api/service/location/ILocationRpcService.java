package com.lsh.wms.api.service.location;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;

import java.util.List;
import java.util.Map;


/**
 * Created by fengkun on 16/7/11.
 */
public interface ILocationRpcService {
    public BaseinfoLocation getLocation(Long locationId);
    public List<BaseinfoLocation> getStoreLocations(Long locationId);
    public List<BaseinfoLocation> getChildrenLocations(Long locationId);
    public BaseinfoLocation getFatherLocation(Long locationId);
    public List<Long> getLocationIds(List<BaseinfoLocation> locations);
    public BaseinfoLocation getFatherByType(Long locationId, String type);
}
