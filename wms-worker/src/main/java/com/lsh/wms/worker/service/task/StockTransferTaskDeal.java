package com.lsh.wms.worker.service.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.inhouse.IStockTransferRpcService;
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
 * Created by Ming on 8/12/16.
 */
@Component
public class StockTransferTaskDeal implements IScheduleTaskDealSingle<String> {
    @Reference
    private IStockTransferRpcService iStockTransferRpcService;

    private static Logger logger =  LoggerFactory.getLogger(StockTransferTaskDeal.class);

    public boolean execute(String task, String ownSign) throws Exception {
        try {
            iStockTransferRpcService.createStockTransfer();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public List<String> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        //taskItemList.get(0).getItemId();
        System.out.println(taskItemList.size());
        System.out.println("[" + new Timestamp(System.currentTimeMillis()) + "] [select] " + Thread.currentThread().getName() + "..." + taskItemList.get(0).getTaskItemId());
        int num = eachFetchDataNum / taskItemList.size();
        List<String> list = new ArrayList<String>();
        String id = taskItemList.get(0).getTaskItemId();
        list.add("[" + id + "]000");
        return list;
    }

    public Comparator<String> getComparator() {
        System.out.println(Thread.currentThread().getName());
        return new Comparator<String>() {
            public int compare(String o1, String o2) {
                return 0;
            }
        };
    }
}
