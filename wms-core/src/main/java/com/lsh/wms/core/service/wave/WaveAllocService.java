package com.lsh.wms.core.service.wave;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.dao.wave.WaveAllocDetailDao;
import com.lsh.wms.core.dao.wave.WaveAllocDetailDao;
import com.lsh.wms.model.wave.WaveAllocDetail;
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
public class WaveAllocService {
    private static final Logger logger = LoggerFactory.getLogger(WaveAllocService.class);

    @Autowired
    private WaveAllocDetailDao allocDetailDao;

    @Transactional(readOnly = false)
    public void addAllocDetail(WaveAllocDetail detail) {
        allocDetailDao.insert(detail);
    }

    @Transactional(readOnly = false)
    public void addAllocDetails(List<WaveAllocDetail> details){
        for(int i = 0; i < details.size(); ++i) {
            WaveAllocDetail detail = details.get(i);
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            detail.setCreatedAt(DateUtils.getCurrentSeconds());
            allocDetailDao.insert(detail);
        }
    }

    public List<WaveAllocDetail> getAllocDetailsByWaveId(long iWaveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        return allocDetailDao.getWaveAllocDetailList(mapQuery);
    }

    public List<WaveAllocDetail> getAllocDetailsByZoneId(long iWaveId, long iPickZoneId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        mapQuery.put("pickZoneId", iPickZoneId);
        return allocDetailDao.getWaveAllocDetailList(mapQuery);
    }
}
