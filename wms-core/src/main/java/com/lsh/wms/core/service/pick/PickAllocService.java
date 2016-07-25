package com.lsh.wms.core.service.pick;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.pick.PickAllocDetailDao;
import com.lsh.wms.model.pick.PickAllocDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
@Transactional(readOnly = true)
public class PickAllocService {
    private static final Logger logger = LoggerFactory.getLogger(PickAllocService.class);

    @Autowired
    private PickAllocDetailDao allocDetailDao;

    @Transactional(readOnly = false)
    public void addAllocDetail(PickAllocDetail detail) {
        allocDetailDao.insert(detail);
    }

    @Transactional(readOnly = false)
    public void addAllocDetails(List<PickAllocDetail> details){
        for(int i = 0; i < details.size(); ++i) {
            PickAllocDetail detail = details.get(i);
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            allocDetailDao.insert(detail);
        }
    }

    public List<PickAllocDetail> getAllocDetailsByWaveId(long iWaveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        return allocDetailDao.getPickAllocDetailList(mapQuery);
    }

    public List<PickAllocDetail> getAllocDetailsByZoneId(long iWaveId, long iPickZoneId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        mapQuery.put("pickZoneId", iPickZoneId);
        return allocDetailDao.getPickAllocDetailList(mapQuery);
    }
}
