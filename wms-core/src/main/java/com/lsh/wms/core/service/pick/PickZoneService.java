package com.lsh.wms.core.service.pick;

import com.lsh.wms.core.dao.pick.PickZoneDao;
import com.lsh.wms.model.pick.PickZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
@Transactional(readOnly = true)
public class PickZoneService {
    private static final Logger logger = LoggerFactory.getLogger(PickZoneService.class);

    @Autowired
    PickZoneDao zoneDao;

    public int insertPickZone(PickZone zone){
        zoneDao.insert(zone);
        return 0;
    }

    public PickZone getPickZone(long iPickZoneId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickZoneId", iPickZoneId);
        final List<PickZone> pickZoneList = zoneDao.getPickZoneList(mapQuery);
        return pickZoneList.size() == 0 ? null : pickZoneList.get(0);
    }

    public List<PickZone> getPickZoneList(Map<String, Object> mapQuery){
        return zoneDao.getPickZoneList(mapQuery);
    }

    public int getPickZoneCount(Map<String, Object> mapQuery){
        return zoneDao.countPickZone(mapQuery);
    }

}
