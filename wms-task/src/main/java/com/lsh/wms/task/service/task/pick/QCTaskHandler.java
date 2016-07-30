package com.lsh.wms.task.service.task.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.pick.PickTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.pick.PickTaskHead;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/30.
 */
public class QCTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private WaveService waveService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_QC, this);
    }


    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>)(List<?>)waveService.getDetailsByQCTaskId(taskEntry.getTaskInfo().getTaskId()));
    }
}