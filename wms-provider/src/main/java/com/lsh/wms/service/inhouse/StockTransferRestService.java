package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.container.IContainerRestService;
import com.lsh.wms.api.service.container.IContainerRpcService;
import com.lsh.wms.api.service.inhouse.IStockTransferRestService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRestService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockMoveRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.model.transfer.StockTransferTaskDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/7/26.
 */

@Service(protocol = "rest")
@Path("inhouse/stock_transfer")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTransferRestService implements IStockTransferRestService {
    private static final Logger logger = LoggerFactory.getLogger(StockTransferRestService.class);
    @Reference
    private ITaskRpcService taskRpcService;

    @Reference
    private IStockQuantRpcService stockQuantService;

    @Reference
    private IItemRpcService itemRpcService;

    @Autowired
    private LocationService locationService;

    @Reference
    private IStockMoveRpcService moveRpcService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private StockTransferCore core;

    @Reference
    private ILocationRpcService locationRpcService;


    @POST
    @Path("add")
    public String addPlan(StockTransferPlan plan)  throws BizCheckedException {
        try{
            StockQuantCondition condition = new StockQuantCondition();
            condition.setLocationId(plan.getFromLocationId());
            condition.setItemId(plan.getItemId());
            BigDecimal total = stockQuantService.getQty(condition);
            core.fillTransferPlan(plan);

            if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
                throw new BizCheckedException("2040001");
            }

            List<StockQuant> quantList = stockQuantService.getQuantList(condition);
            Long containerId = quantList.get(0).getContainerId();
            if (containerService.getContainer(containerId).getType() != 1L) {
                containerId = containerService.createContainerByType(2L).getId();
            }

            TaskEntry taskEntry = new TaskEntry();
            TaskInfo taskInfo = new TaskInfo();
            ObjUtils.bean2bean(plan, taskInfo);
            taskInfo.setTaskName("移库任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
            taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
            taskInfo.setContainerId(containerId);
            taskEntry.setTaskInfo(taskInfo);
            taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
        } catch (BizCheckedException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("scanFromLocation")
    public String scanFromLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        Long fromLocationId = Long.valueOf(mapQuery.get("fromLocationId").toString());

        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }
        if (fromLocationId != taskEntry.getTaskInfo().getFromLocationId()) {
            throw new BizCheckedException("2040005");
        }

        StockMove move = new StockMove();
        ObjUtils.bean2bean(taskEntry.getTaskInfo(), move);
        move.setToLocationId(locationService.getAreaFatherId(fromLocationId));
        moveRpcService.create(move);
        moveRpcService.done(move.getId());

        return JsonUtils.SUCCESS(true);
    }

    @POST
    @Path("scanFromLocation")
    public String scanToLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        Long toLocationId = Long.valueOf(mapQuery.get("LocationId").toString());

        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("3040001");
        }

        StockMove move = new StockMove();
        ObjUtils.bean2bean(taskEntry.getTaskInfo(), move);
        move.setFromLocationId(locationService.getAreaFatherId(taskEntry.getTaskInfo().getFromLocationId()));
        move.setToLocationId(toLocationId);
        moveRpcService.create(move);
        moveRpcService.done(move.getId());

        taskRpcService.done(taskId);
        return JsonUtils.SUCCESS(true);
    }

}
