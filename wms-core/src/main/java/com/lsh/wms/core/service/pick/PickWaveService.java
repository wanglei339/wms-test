package com.lsh.wms.core.service.pick;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.pick.PickWaveHeadDao;
import com.lsh.wms.core.dao.so.OutbSoHeaderDao;
import com.lsh.wms.model.pick.PickWaveHead;
import com.lsh.wms.model.so.OutbSoHeader;
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
    public void createWave(PickWaveHead head, List<Long> vOrders){
        //gen waveId
        long waveId = RandomUtils.genId();
        head.setWaveId(waveId);
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        waveHeadDao.insert(head);
        //this.addToWave(head.getWaveId(), vOrders);
    }

    @Transactional(readOnly = false)
    public void addToWave(long iWaveId, List<Long> vOrders) {
        for(int i = 0; i < vOrders.size(); i++){
            //更新wave信息
            long soId = vOrders.get(i);
            OutbSoHeader outbSoHeader = new OutbSoHeader();

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
    @Transactional(readOnly = false)
    public void update(PickWaveHead head){
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        waveHeadDao.update(head);
    }
    @Transactional(readOnly = false)
    public void setStatus(long iWaveId, int iStatus){
        PickWaveHead head = this.getWave(iWaveId);
        if(head == null) return ;
        head.setStatus((long)iStatus);
        this.update(head);
    }
}
