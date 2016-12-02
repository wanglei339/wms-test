package com.lsh.wms.worker.service.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wuhao on 16/8/12.
 */
@Component
public class ProcurementTaskDeal implements IScheduleTaskDealSingle<String> {
    @Reference
    private IProcurementProveiderRpcService iProcurementProveiderRpcService;

    private static Logger logger = LoggerFactory.getLogger(ProcurementTaskDeal.class);

    public boolean execute(String task, String ownSign) throws Exception {
        try {
            logger.info("in 11111"+task);
            iProcurementProveiderRpcService.createProcurement();
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public List<String> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        //taskItemList.get(0).getTaskItemId();
        // logger.info(taskItemList.size()+"");
        // logger.info("[" + new Timestamp(System.currentTimeMillis()) + "] [select] " + Thread.currentThread().getName() + "..." + taskItemList.get(0).getTaskItemId());
        // int num = eachFetchDataNum / taskItemList.size();
        List<String> list = new ArrayList<String>();
        String id = taskItemList.get(0).getTaskItemId();
        list.add("[" + id + "]111");
        return list;
    }

    public Comparator<String> getComparator() {
        // System.out.println(Thread.currentThread().getName());
        return new Comparator<String>() {
            public int compare(String o1, String o2) {
                return 0;
            }
        };
    }

}
