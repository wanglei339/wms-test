package com.lsh.wms.task.service.task.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Component
public class QCTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private WaveService waveService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_QC, this);
    }

    public void create(Long taskId) throws BizCheckedException {
        TaskEntry pickEntry = this.getTask(taskId);
        Long containerId = pickEntry.getTaskInfo().getContainerId();
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        if(details.size()==0){
            return;
        }

        TaskInfo info = new TaskInfo();
        info.setType(TaskConstant.TYPE_QC);
        info.setContainerId(containerId);
        info.setExt1(pickEntry.getTaskInfo().getTaskId());
        info.setOrderId(details.get(0).getOrderId());
        info.setQty(new BigDecimal(details.size()));

        TaskEntry taskEntry = new TaskEntry();
        taskEntry.setTaskDetailList((List<Object>) (List<?>) details);
        taskEntry.setTaskInfo(info);

        this.create(taskEntry);
    }

    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        List<WaveDetail> details = (List<WaveDetail>)(List<?>)taskEntry.getTaskDetailList();
        for(WaveDetail detail : details){
            detail.setQcTaskId(taskEntry.getTaskInfo().getTaskId());
        }
        waveService.updateDetails(details);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>)(List<?>)waveService.getDetailsByQCTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

    public void doneConcrete(Long taskId){
        //这里做一些处理,做些啥呢?
    }
}