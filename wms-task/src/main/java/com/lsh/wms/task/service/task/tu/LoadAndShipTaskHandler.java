package com.lsh.wms.task.service.task.tu;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 物美直流发货|区别于优供波次发货任务
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

    public void createConcrete(TaskEntry taskEntry) throws BizCheckedException {

    }

    protected void getConcrete(TaskEntry taskEntry) {
//        taskEntry.setTaskDetailList((List<Object>)(List<?>)waveService.getDetailsByShipTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

    public void doneConcrete(Long taskId){
        //这里做一些处理,如集货区释放等,但这个怎么才能具有一定的通用性呢?
    }

}
