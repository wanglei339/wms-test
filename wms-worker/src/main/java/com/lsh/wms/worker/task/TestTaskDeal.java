package com.lsh.wms.worker.task;

import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangdong on 16/6/23.
 */
@Component
public class TestTaskDeal implements IScheduleTaskDealSingle<String> {

    public boolean execute(String task, String ownSign) throws Exception {
        System.out.println();
        System.out.println("[" + new Timestamp(System.currentTimeMillis()) + "] [execute][S] " + Thread.currentThread().getName() + "..." + task);
        Thread.sleep(TimeUnit.SECONDS.toMillis(8));
        System.out.println("[" + new Timestamp(System.currentTimeMillis()) + "] [execute][E] " + Thread.currentThread().getName() + "..." + task);
        return false;
    }

    public List<String> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        System.out.println(taskItemList.size());
        System.out.println("[" + new Timestamp(System.currentTimeMillis()) + "] [select] " + Thread.currentThread().getName() + "..." + taskItemList.get(0).getTaskItemId());
        int num = eachFetchDataNum / taskItemList.size();
        List<String> list = new ArrayList<String>();
        String id = taskItemList.get(0).getTaskItemId();
        list.add("[" + id + "]111");
        list.add("[" + id + "]222");
        list.add("[" + id + "]333");
        list.add("[" + id + "]444");
        list.add("[" + id + "]555");
        list.add("[" + id + "]666");
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
