package com.lsh.wms.worker.service.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lixin-mac on 2017/2/15.
 */
@Component
public class ClosePoTaskDeal implements IScheduleTaskDealSingle<String> {
    private static Logger logger = LoggerFactory.getLogger(ClosePoTaskDeal.class);

    @Reference
    private IPoRpcService poRpcService;

    public boolean execute(String s, String ownSign) throws Exception {
        try {
            logger.info("~~~ in execute poClose ~~~");
            poRpcService.closeIbdOrder();
            logger.info("~~~ end poClose ~~~");
            
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public List<String> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        List<String> list = new ArrayList<String>();
        String id = taskItemList.get(0).getTaskItemId();
        list.add("[" + id + "]111");
        return list;
    }

    public Comparator<String> getComparator() {
        return null;
    }
}
