package com.lsh.wms.core.service.wave;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.dao.so.OutbSoHeaderDao;
import com.lsh.wms.core.dao.wave.WaveDetailDao;
import com.lsh.wms.core.dao.wave.WaveHeadDao;
import com.lsh.wms.core.dao.wave.WaveQcExceptionDao;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.wave.WaveAllocDetail;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveQcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zengwenjun on 16/7/15.
 */

@Component
@Transactional(readOnly = true)
public class WaveService {
    private static final Logger logger = LoggerFactory.getLogger(WaveService.class);

    @Autowired
    private WaveHeadDao waveHeadDao;
    @Autowired
    private OutbSoHeaderDao soHeaderDao;
    @Autowired
    private WaveAllocService allocService;
    @Autowired
    private WaveDetailDao detailDao;
    @Autowired
    private WaveQcExceptionDao qcExceptionDao;

    @Transactional(readOnly = false)
    public void createWave(WaveHead head, List<Long> vOrders){
        //gen waveId
        long waveId = RandomUtils.genId();
        head.setWaveId(waveId);
        head.setCreatedAt(DateUtils.getCurrentSeconds());
        waveHeadDao.insert(head);
        this.addToWave(head.getWaveId(), vOrders);
    }

    @Transactional(readOnly = false)
    public void addToWave(long iWaveId, List<Long> vOrders) {
        for(int i = 0; i < vOrders.size(); i++){
            //更新wave信息
            long soId = vOrders.get(i);
            OutbSoHeader outbSoHeader = new OutbSoHeader();
            outbSoHeader.setOrderId(soId);
            outbSoHeader.setWaveId(iWaveId);
            soHeaderDao.updateByOrderOtherIdOrOrderId(outbSoHeader);
        }
    }

    public List<WaveHead> getWaveList(Map<String, Object> mapQuery){
        return waveHeadDao.getWaveHeadList(mapQuery);
    }

    public int getWaveCount(Map<String, Object> mapQuery){
        return waveHeadDao.countWaveHead(mapQuery);
    }

    public WaveHead getWave(long iWaveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
        List<WaveHead> WaveHeadList = waveHeadDao.getWaveHeadList(mapQuery);
        return WaveHeadList.size() == 0 ? null : WaveHeadList.get(0);
    }

    @Transactional(readOnly = false)
    public void update(WaveHead head){
        head.setUpdatedAt(DateUtils.getCurrentSeconds());
        waveHeadDao.update(head);
    }

    @Transactional(readOnly = false)
    public void setStatus(long iWaveId, int iStatus){
        WaveHead head = this.getWave(iWaveId);
        if(head == null) return ;
        head.setStatus((long)iStatus);
        this.update(head);
    }

    @Transactional(readOnly = false)
    public void storeAlloc(WaveHead head, List<WaveAllocDetail> details){
        head.setIsResAlloc(1L);
        this.update(head);
        allocService.addAllocDetails(details);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByPickTaskId(long pickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", pickTaskId);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getOrderedDetailsByPickTaskId(long pickTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", pickTaskId);
        return detailDao.getOrderedWaveDetailList(mapQuery);
    }


    public List<WaveDetail> getDetaileByWaveId(long waveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", waveId);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public WaveDetail getDetailByPickTaskIdAndPickOrder(Long pickTaskId, Long pickOrder) {
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("pickTaskId", pickTaskId);
        mapQuery.put("pickOrder", pickOrder);
        return detailDao.getOrderedWaveDetailList(mapQuery).get(0);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByQCTaskId(long qcTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("qcTaskId", qcTaskId);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByContainerId(long containerId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId", containerId);
        mapQuery.put("is_alive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByShipTaskId(long shipTaskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("shipTaskId", shipTaskId);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveDetail> getDetailsByLocationId(long locationId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationId", locationId);
        mapQuery.put("is_alive", 1);
        return detailDao.getWaveDetailList(mapQuery);
    }

    @Transactional(readOnly = false)
    public void updateDetail(WaveDetail detail){
        detail.setUpdatedAt(DateUtils.getCurrentSeconds());
        detailDao.update(detail);
    }

    @Transactional(readOnly = false)
    public void updateDetails(List<WaveDetail> details){
        for(WaveDetail detail : details) {
            this.updateDetail(detail);
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal getUnPickedQty(Map<String, Object> mapQuery)
    {
        BigDecimal unPickedQty = detailDao.getUnPickedQty(mapQuery);
        if ( unPickedQty == null ){
            unPickedQty = new BigDecimal("0.0000");
        }
        return unPickedQty;
    }

    @Transactional(readOnly = false)
    public void insertQCException(WaveQcException exception){
        exception.setCreatedAt(DateUtils.getCurrentSeconds());
        exception.setUpdatedAt(DateUtils.getCurrentSeconds());
        qcExceptionDao.insert(exception);
    }

    @Transactional(readOnly = true)
    public List<WaveQcException> getExceptionsByWaveId(long waveId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", waveId);
        return qcExceptionDao.getWaveQcExceptionList(mapQuery);
    }

    @Transactional(readOnly = true)
    public List<WaveQcException> getExceptionsByQcTaskId(long taskId){
        HashMap<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("qcTaskId", taskId);
        return qcExceptionDao.getWaveQcExceptionList(mapQuery);
    }

}
