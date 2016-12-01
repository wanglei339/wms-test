package com.lsh.wms.core.service.system;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.system.SysLogDao;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/18.
 */
@Component
@Transactional(readOnly = true)
public class SysLogService {

    @Autowired
    private SysLogDao sysLogDao;
    private static Logger logger = LoggerFactory.getLogger(SysLogService.class);



    @Transactional(readOnly = false)
    public Long insertSysLog(SysLog sysLog){
        sysLog.setLogId(RandomUtils.genId());
        sysLog.setCreatedAt(DateUtils.getCurrentSeconds());
        sysLogDao.insert(sysLog);
        return sysLog.getLogId();
    }

    @Transactional(readOnly = false)
    public void updateSysLog(SysLog sysLog) {
        sysLog.setRetryTimes(sysLog.getRetryTimes() + 1);
        sysLog.setUpdatedAt(DateUtils.getCurrentSeconds());
        sysLogDao.update(sysLog);
    }

    public List<SysLog> getSysLogList(Map<String, Object> params){
        return sysLogDao.getSysLogList(params);
    }

    public Integer countSysLog(Map<String, Object> params){
        return sysLogDao.countSysLog(params);
    }

    /**
     * 根据logId 来找对应的Syslog
     * @param logId
     * @return
     */
    public SysLog getSysLogById(Long logId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("logId",logId);
        List<SysLog> list = this.getSysLogList(mapQuery);
        if(list.size() <= 0){
            return null;
        }
        return list.get(0);
    }

    @Transactional(readOnly = false)
    public List<Long> getAndLockSysLogByType(Long type) {
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("logType",type);
        mapQuery.put("retryTimes", 10);;
        List<SysLog> list = sysLogDao.getTodoList(mapQuery);
        List<Long> logIdList = new ArrayList<Long>();
        for(SysLog log : list){
            logIdList.add(log.getLogId());
        }
        if (! logIdList.isEmpty()) {
            sysLogDao.lockSysLogList(logIdList);
        }
        return logIdList;
    }

}
