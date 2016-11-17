package com.lsh.wms.worker.service.transporter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.back.ITransportService;
import com.lsh.wms.core.service.system.SysLogService;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mali on 16/11/17.
 */
@Component
public class TransportTaskGenerator implements IScheduleTaskDealSingle<Long> {
    @Reference
    private ITransportService iTransportService;

    @Autowired
    private SysLogService sysLogService;

    private static Logger logger = LoggerFactory.getLogger(TransportTaskGenerator.class);

    public boolean execute(Long sysLogId, String ownSign) throws Exception {
        try {
            iTransportService.dealOne(sysLogId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public List<Long> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        List<Long> list  = new ArrayList<Long>();
        for (TaskItemDefine item : taskItemList) {
            logger.info(StrUtils.formatString("begin get SYSLog of type {0}",  item.getTaskItemId()));
            List<Long> sysLogList = sysLogService.getAndLockSysLogByType(Long.valueOf(item.getTaskItemId()));
            for (Long id : sysLogList) {
                logger.info(StrUtils.formatString("insert into {0}", id));
            }
            list.addAll(sysLogList);
        }

        return list;
    }

    public Comparator<Long> getComparator() {
        return null;
    }

}
