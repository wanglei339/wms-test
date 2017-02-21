package com.lsh.wms.task.service.task.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.wave.WaveService;
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
 * 物美直流发货|区别于优供波次发货任务
 *
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/24 上午10:46
 */
@Component
public class LoadAndShipTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Reference
    ITaskRpcService taskRpcService;
    @Autowired
    private StockMoveService stockMoveService;
    @Autowired
    private WaveService waveService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_TU_SHIP, this);
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
    protected void getOldConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>)(List<?>)waveService.getDetailsByShipTaskIdPc(taskEntry.getTaskInfo().getTaskId()));
    }

    public void doneConcrete(Long taskId){
        //这里做一些处理,如集货区释放等,但这个怎么才能具有一定的通用性呢?
    }

}
