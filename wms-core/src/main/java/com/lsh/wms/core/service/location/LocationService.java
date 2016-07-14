package com.lsh.wms.core.service.location;

import com.lsh.wms.core.dao.baseinfo.BaseinfoLocationDao;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by fengkun on 16/7/11.
 */

@Service
@Transactional(readOnly = true)
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    @Autowired
    private BaseinfoLocationDao locationDao;
    // location类型定义
    public static final Map<String, Long> locationType = new HashMap<String, Long>() {
        {
            put("area", new Long(2)); // 区域
        }
    };

    // 获取location
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

    // 获取location_id
    public List<Long> getLocationIds(List<BaseinfoLocation> locations) {
        List<Long> locationIds = new ArrayList<Long>();
        for (BaseinfoLocation location : locations) {
            locationIds.add(location.getLocationId());
        }
        return locationIds;
    }

    // 获取一个location下一层的子节点
    public List<BaseinfoLocation> getChildrenLocations (Long locationId) {
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

    // 获取一个location下一层的子节点id
    public List<Long> getChildrenLocationIds (Long locationId) {
        List<BaseinfoLocation> locations = this.getChildrenLocations(locationId);
        return this.getLocationIds(locations);
    }

    // 获取一个location下所有是存储位的子节点
    public List<BaseinfoLocation> getStoreLocations(Long locationId) {
        List<BaseinfoLocation> locations = new ArrayList();
        BaseinfoLocation curLocation = this.getLocation(locationId);
        if (curLocation.getCanStore() == 1) {
            locations.add(curLocation);
        }
        if (curLocation.getIsLeaf() == 0) {
            List<BaseinfoLocation> childrenLocations = this.getChildrenLocations(locationId);
            // 深度优先,递归遍历
            for (BaseinfoLocation location : childrenLocations) {
                List<BaseinfoLocation> childrenStoreLocations = this.getStoreLocations(location.getLocationId());
                locations.addAll(childrenStoreLocations);
            }
        }
        return locations;
    }

    // 获取一个location下所有是存储位的子节点id
    public List<Long> getStoreLocationIds(Long locationId) {
        List<BaseinfoLocation> locations = this.getStoreLocations(locationId);
        return this.getLocationIds(locations);
    }

    // 获取父级节点
    public BaseinfoLocation getFatherLocation (Long locationId) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (fatherId == 0) {
            return null;
        }
        return this.getLocation(fatherId);
    }

    // 获取父级区域location节点
    public BaseinfoLocation getFatherByType(Long locationId, String type) {
        BaseinfoLocation curLocation = this.getLocation(locationId);
        Long fatherId = curLocation.getFatherId();
        if (curLocation.getType().equals(this.locationType.get(type))) {
            return curLocation;
        }
        if (fatherId == 0) {
            return null;
        }
        return this.getFatherByType(fatherId, type);
    }

    // 获取父级区域location节点id
    public Long getFatherIdByType(Long locationId, String type) {
        BaseinfoLocation fatherLocation = this.getFatherByType(locationId, type);
        return fatherLocation.getLocationId();
    }
    @Transactional(readOnly = false)
    public BaseinfoLocation insertLocation(BaseinfoLocation location){
        if(location.getLocationId() == 0){
            //添加locationId
            int iLocationId = 0 ;
            location.setLocationId((long)iLocationId);
        }
        //添加新增时间
        long createdAt = new Date().getTime()/1000;
        location.setCreatedAt(createdAt);
        locationDao.insert(location);
        return location;
    }

    @Transactional(readOnly = false)
    public BaseinfoLocation updateLocation(BaseinfoLocation location){
        if(this.getLocation(location.getLocationId()) == null){
            return null;
        }
        long updatedAt = new Date().getTime()/1000;
        location.setUpdatedAt(updatedAt);
        locationDao.update(location);
        return location;

    }
}
