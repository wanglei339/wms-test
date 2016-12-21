package com.lsh.wms.core.service.zone;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.zone.WorkZoneDao;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.zone.WorkZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by zengwenjun on 16/12/05.
 */
@Component
@Transactional(readOnly = true)
public class WorkZoneService {
    private static final Logger logger = LoggerFactory.getLogger(WorkZoneService.class);

    @Autowired
    WorkZoneDao workZoneDao;
    @Autowired
    private IdGenerator idGenerator;

    @Transactional(readOnly = false)
    public int insertWorkZone(WorkZone zone){
        zone.setZoneId(idGenerator.genId("work_zone", false, false));
        zone.setCreatedAt(DateUtils.getCurrentSeconds());
        zone.setUpdatedAt(DateUtils.getCurrentSeconds());
        workZoneDao.insert(zone);
        return 0;
    }

    public WorkZone getWorkZone(long zoneId) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("zoneId", zoneId);
        final List<WorkZone> zoneList = workZoneDao.getWorkZoneList(mapQuery);
        return (zoneList == null || zoneList.size() == 0) ? null : zoneList.get(0);
    }

    public List<WorkZone> getWorkZoneList(Map<String, Object> mapQuery){
        return workZoneDao.getWorkZoneList(mapQuery);
    }

    public int getWorkZoneCount(Map<String, Object> mapQuery){
        return workZoneDao.countWorkZone(mapQuery);
    }

    @Transactional(readOnly = false)
    public void updateWorkZone(WorkZone zone){
        zone.setUpdatedAt(DateUtils.getCurrentSeconds());
        workZoneDao.update(zone);
    }

    @Transactional(readOnly = false)
    public void deleteWorkZone(Long zoneId){
        WorkZone zone = new WorkZone();
        zone.setZoneId(zoneId);
        zone.setIsValid(0L);
        zone.setUpdatedAt(DateUtils.getCurrentSeconds());
        workZoneDao.update(zone);
    }

    public List<WorkZone> getWorkZoneByType(Long type){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("type", type);
        mapQuery.put("isValid", 1);
        List<WorkZone> zoneList = workZoneDao.getWorkZoneList(mapQuery);
        return zoneList == null ? (new LinkedList<WorkZone>()) : zoneList;
    }
}
