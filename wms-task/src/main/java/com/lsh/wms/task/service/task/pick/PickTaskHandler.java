package com.lsh.wms.task.service.task.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/23.
 */

@Component
public class PickTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private PickTaskService pickTaskService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_PICK, this);
    }

    @Transactional(readOnly = false)
    protected void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        PickTaskHead head = (PickTaskHead)taskEntry.getTaskHead();
        Long taskId = taskEntry.getTaskInfo().getTaskId();
        head.setTaskId(taskId);
        List<WaveDetail> details = (List<WaveDetail>)(List<?>)taskEntry.getTaskDetailList();
        if (details.size() < 1) {
            throw new BizCheckedException("2060002");
        }
        // 通过波次详情设置拣货头信息
        WaveDetail tmpDetail = details.get(0);
        head.setDeliveryId(tmpDetail.getOrderId());
        head.setWaveId(tmpDetail.getWaveId());
        head.setPickType(1); // TODO
        head.setContainerId(tmpDetail.getContainerId());
        
        for(WaveDetail detail : details){
            if (!detail.getPickTaskId().equals("") || detail.getPickTaskId() != null) {
                throw new BizCheckedException("2060001");
            }
            detail.setPickTaskId(taskId);
        }
        pickTaskService.createPickTask(head, details);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(pickTaskService.getPickTaskHead(taskEntry.getTaskInfo().getTaskId()));
        taskEntry.setTaskDetailList((List<Object>)(List<?>)pickTaskService.getPickTaskDetails(taskEntry.getTaskInfo().getTaskId()));
    }

    protected void getHeadConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(pickTaskService.getPickTaskHead(taskEntry.getTaskInfo().getTaskId()));
    }
}
