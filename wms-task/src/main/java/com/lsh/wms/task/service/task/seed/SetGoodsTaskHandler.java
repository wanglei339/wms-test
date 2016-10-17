package com.lsh.wms.task.service.task.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class SetGoodsTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    BaseTaskService baseTaskService;
    @Reference
    ITaskRpcService taskRpcService;
    @Reference
    IStockQuantRpcService stockQuantRpcService;
    @Reference
    ILocationRpcService locationRpcService;
    @Autowired
    LocationService locationService;
    @Reference
    private IStockMoveRpcService moveRpcService;


    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SET_GOODS, this);
    }

    public void doneConcrete(Long taskId) {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        Long containerId = info.getContainerId();
        StockQuantCondition condition = new StockQuantCondition();
        condition.setContainerId(containerId);
        List<StockQuant> quants = stockQuantRpcService.getQuantList(condition);
        if(quants== null || quants.size()==0 ){
            throw new BizCheckedException("2880003");
        }

        BaseinfoLocation location = locationService.getLocation(quants.get(0).getLocationId());
        Long storeNo = location.getStoreNo();

        //获得集货区信息
        List<BaseinfoLocation> locations = locationRpcService.getCollectionByStoreNo(storeNo);
        if(locations == null || locations.size()==0 ){
            throw new BizCheckedException("2880010");
        }

        //移动库存
        moveRpcService.moveWholeContainer(containerId, taskId, info.getOperator(), location.getLocationId(), locations.get(0).getLocationId());

    }
}
