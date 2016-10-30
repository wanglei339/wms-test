package com.lsh.wms.task.service.task.tu;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

/**
 * 物美直流发货|区别于优供波次发货任务
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/24 上午10:46
 */
@Component
public class LoadAndShipTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_DIRECT_SHIP, this);
    }

    public void calcPerformance(TaskInfo taskInfo) {
        //记录绩效
        taskInfo.setTaskPackQty(taskInfo.getTaskQty().divide(taskInfo.getPackUnit(), 0, BigDecimal.ROUND_DOWN));        //取整?
        taskInfo.setTaskEaQty(taskInfo.getQty());
    }

    protected void getConcrete(TaskEntry taskEntry) {
//        taskEntry.setTaskDetailList((List<Object>)(List<?>)waveService.getDetailsByShipTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

    public void doneConcrete(Long taskId) {
        //todo 清库存,释放集货道,写绩效
    }

}
