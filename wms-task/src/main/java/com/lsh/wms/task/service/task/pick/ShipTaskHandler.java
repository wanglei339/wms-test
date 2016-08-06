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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/30.
 */
@Component
public class ShipTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private WaveService waveService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SHIP, this);
    }


    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException {
        List<WaveDetail> details = (List<WaveDetail>)(List<?>)taskEntry.getTaskDetailList();
        for(WaveDetail detail : details){
            detail.setShipTaskId(taskEntry.getTaskInfo().getTaskId());
        }
        waveService.updateDetails(details);
    }

    protected void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>)(List<?>)waveService.getDetailsByShipTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

    public void doneConcrete(Long taskId){
        //这里做一些处理,如集货区释放等,但这个怎么才能具有一定的通用性呢?
    }
}