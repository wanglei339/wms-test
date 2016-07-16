package com.lsh.wms.core.service.pick;

import com.lsh.wms.core.dao.pick.PickWaveHeadDao;
import com.lsh.wms.core.dao.so.OutbSoHeaderDao;
import com.lsh.wms.model.pick.PickWaveHead;
import org.apache.tomcat.jni.Time;
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
public class PickWaveService {
    private static final Logger logger = LoggerFactory.getLogger(PickWaveService.class);

    @Autowired
    private PickWaveHeadDao waveHeadDao;
    @Autowired
    private OutbSoHeaderDao soHeaderDao;

    @Transactional(readOnly = false)
    public int createWave(PickWaveHead head, List<Long> vOrders){
        waveHeadDao.insert(head);
        this.addToWave(head.getWaveId(), vOrders);
        return 0;
    }

    @Transactional(readOnly = false)
    public void addToWave(long iWaveId, List<Long> vOrders) {
        for(int i = 0; i < vOrders.size(); i++){
            //更新wave信息
        }
    }

    public List<PickWaveHead> getWaveList(Map<String, Object> mapQuery){
        return waveHeadDao.getPickWaveHeadList(mapQuery);
    }

    public int getWaveCount(Map<String, Object> mapQuery){
        return waveHeadDao.countPickWaveHead(mapQuery);
    }

    public PickWaveHead getWave(long iWaveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        List<PickWaveHead> pickWaveHeadList = waveHeadDao.getPickWaveHeadList(mapQuery);
        return pickWaveHeadList.size() == 0 ? null : pickWaveHeadList.get(0);
    }

    private void update(PickWaveHead head){
        waveHeadDao.update(head);
    }

    public void setStatus(long iWaveId, int iStatus){
        PickWaveHead head = this.getWave(iWaveId);
        if(head == null) return ;
        head.setStatus((long)iStatus);
        head.setUpdatedAt(System.currentTimeMillis()/1000);
        this.update(head);
    }
}
