package com.lsh.wms.worker.service.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wuhao on 16/8/12.
 * 修改投单状态任务,由待收货改为待投单
 * 解决单的锁定及解锁
 */
@Component
public class PoThrowStatueTaskDeal implements IScheduleTaskDealSingle<Long> {
    @Reference
    private IPoRpcService iPoRpcService;

    private static Logger logger = LoggerFactory.getLogger(IPoRpcService.class);

    public boolean execute(Long task, String ownSign) throws Exception {
        try {
            logger.info("execute, 已投单未收货状态修改,投单间隔intervalTime is "+task + " H");
            iPoRpcService.updateStatusTOthrow(task);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public List<Long> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        //taskItemList.get(0).getTaskItemId();
        // logger.info(taskItemList.size()+"");
        // logger.info("[" + new Timestamp(System.currentTimeMillis()) + "] [select] " + Thread.currentThread().getName() + "..." + taskItemList.get(0).getTaskItemId());
        // int num = eachFetchDataNum / taskItemList.size();
        List<Long> list = new ArrayList<Long>();
        Long id = Long.parseLong(taskItemList.get(0).getTaskItemId());
        list.add(id);
        return list;
    }

    public Comparator<Long> getComparator() {
       return null;
    }

}
