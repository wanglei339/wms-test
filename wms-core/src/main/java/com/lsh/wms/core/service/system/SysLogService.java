package com.lsh.wms.core.service.system;

import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.dao.system.SysLogDao;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional(readOnly = false)
    public Long insertSysLog(SysLog sysLog){
        sysLog.setLogId(RandomUtils.genId());
        sysLog.setCreatedAt(DateUtils.getCurrentSeconds());
        sysLogDao.insert(sysLog);
        return sysLog.getLogId();
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

}