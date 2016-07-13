package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 16/7/11.
 */

@Service
@Transactional(readOnly = true)
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    @Autowired
    private BaseinfoLocationDao locationDao;

    public BaseinfoLocation getLocation (long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseinfoLocation location;
        params.put("locationId", locationId);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        if (locations.size() == 1) {
            location = locations.get(0);
        } else {
            return null;
        }
        return location;
    }

    public List<BaseinfoLocation> getChildrenLocations (long locationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<Long, BaseinfoLocation> childrenLocations = new HashMap<Long, BaseinfoLocation>();
        // 判断是否已为子节点
        BaseinfoLocation curLocation = this.getLocation(locationId);
        if (curLocation.getIsLeaf() == 1) {
            return null;
        }
        params.put("fatherId", locationId);
        params.put("isValid", 1);
        List<BaseinfoLocation> locations = locationDao.getBaseinfoLocationList(params);
        return locations;
    }

    public BaseinfoLocation getFatherLocation (long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (fatherId == 0) {
            return null;
        }
        return this.getLocation(fatherId);
    }
}
