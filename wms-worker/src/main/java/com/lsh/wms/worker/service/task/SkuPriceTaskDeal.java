package com.lsh.wms.worker.service.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.datareport.ISkuMapRpcService;
import com.lsh.wms.core.service.item.ItemService;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/9.
 */
@Component
public class SkuPriceTaskDeal implements IScheduleTaskDealSingle<String> {
    @Reference
    private ISkuMapRpcService skuMapRpcService;
    @Autowired
    private ItemService itemService;


    private static Logger logger = LoggerFactory.getLogger(SkuPriceTaskDeal.class);

    public boolean execute(String s, String ownSign) throws Exception {
        try {
            logger.info("in execute:" + s);
            List<String> skuCodes  = itemService.getSkuCodeList();
            skuMapRpcService.insertSkuMap(skuCodes);
            //iTransportService.dealOne(sysLogId);
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
