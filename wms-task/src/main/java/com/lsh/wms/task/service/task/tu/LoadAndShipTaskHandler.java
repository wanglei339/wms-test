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
        handlerFactory.register(TaskConstant.TYPE_DIRECT_SHIP, this);
    }

    /**
     * 看是大店还是小店
     *
     * @param taskId
     */
    public void doneConcrete(Long taskId) {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        Long storeType = info.getSubType();
        Long containerId = info.getContainerId();
        if (TaskConstant.TASK_DIRECT_SMALL_SHIP.equals(storeType)) {//小店 不和板子
            stockMoveService.moveToConsume(containerId);
        } else {  //板子码
            List<WaveDetail> waveDetails = waveService.getWaveDetailsByMergedContainerId(info.getContainerId());
            if (null == waveDetails || waveDetails.size() < 1) {
                throw new BizCheckedException("2990051");
            }
            for (WaveDetail detail : waveDetails) {
                stockMoveService.moveToConsume(detail.getContainerId());
            }
        }
    }


}
