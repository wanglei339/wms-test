package com.lsh.wms.worker.service.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.model.so.ObdHeader;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by mali on 16/12/13.
 */
@Component
public class SoCloseTaskDeal implements IScheduleTaskDealSingle<Long> {
    @Reference
    private ISoRpcService iSoRpcService;

    private static Logger logger = LoggerFactory.getLogger(SoCloseTaskDeal.class);

    public boolean execute(Long orderId, String ownSign) throws Exception {
        try {
            logger.info("begin to deal with order "+ orderId);
            iSoRpcService.eliminateDiff(orderId);
        }catch (Exception e){
            logger.error("Error occurred during deal with order "+ orderId);
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public List<Long> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        List<Long> list = new ArrayList<Long>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isClosed", 0L);
        List<ObdHeader> headerList = iSoRpcService.getOutbSoHeaderList(params);
        for (ObdHeader header : headerList) {
            Integer orderType = header.getOrderType();
            if (orderType.equals(SoConstant.ORDER_TYPE_STO) || orderType.equals(SoConstant.ORDER_TYPE_SO)) {
                list.add(header.getOrderId());
            }
        }
        return list;
    }

    public Comparator<Long> getComparator() {
        return null;
    }
}
