package com.lsh.wms.task.service.task.shelve;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.shelve.IShelveRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.core.constant.StockConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.shelve.AtticShelveTaskDetailService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.stock.StockDelta;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
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
 * Created by wuhao on 16/8/16.
 */
@Component
public class AtticShelveTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private AtticShelveTaskDetailService detailService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private StockLotService stockLotService;
    @Reference
    private IShelveRpcService iShelveRpcService;
    @Reference
    private IStockMoveRpcService iStockMoveRpcService;
    @Autowired
    private StockSummaryService stockSummaryService;
    @Autowired
    StockLotService lotService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_ATTIC_SHELVE, this);
    }

    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>) (List<?>) detailService.getShelveTaskDetail(taskEntry.getTaskInfo().getTaskId()));
    }
    public void create(TaskEntry taskEntry) {
        Long containerId = taskEntry.getTaskInfo().getContainerId();
        // 检查容器信息
        if (containerId == null || containerId.equals("")) {
            throw new BizCheckedException("2030003");
        }
        // 检查该容器是否已创建过任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        StockLot lot = lotService.getStockLotByLotId(quant.getLotId());

        TaskInfo taskInfo = new TaskInfo();

        ObjUtils.bean2bean(quant, taskInfo);

        taskInfo.setTaskName("阁楼上架任务[ " + taskInfo.getContainerId() + "]");
        taskInfo.setTaskQty(taskInfo.getQty().divide(taskInfo.getPackUnit(), 2, BigDecimal.ROUND_DOWN));
        taskInfo.setType(TaskConstant.TYPE_ATTIC_SHELVE);
        taskInfo.setFromLocationId(quant.getLocationId());
        taskInfo.setExt9(quant.getSupplierId().toString());
        taskInfo.setPriority(1L);
        taskInfo.setSubType(2L);
        taskInfo.setOrderId(lot.getPoId());

        taskEntry.setTaskInfo(taskInfo);

        super.create(taskEntry);
    }
    public void calcPerformance(TaskInfo taskInfo) {
        taskInfo.setTaskQty(taskInfo.getQty().divide(taskInfo.getPackUnit(),2,BigDecimal.ROUND_DOWN));
    }

//    public void  doneConcrete(Long taskId) {
//        Long containerId = this.getTask(taskId).getTaskInfo().getContainerId();
//        // 更新可用库存
//        List<StockQuant> quantList = stockQuantService.getQuantsByContainerId(containerId);
//        for(StockQuant quant : quantList) {
//            StockDelta delta = new StockDelta();
//            delta.setItemId(quant.getItemId());
//            delta.setInhouseQty(quant.getQty());
//            delta.setBusinessId(taskId);
//            delta.setType(StockConstant.TYPE_SHELVE);
//            stockSummaryService.changeStock(delta);
//        }
//    }
}
