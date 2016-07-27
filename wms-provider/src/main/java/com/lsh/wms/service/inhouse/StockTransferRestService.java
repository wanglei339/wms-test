package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.stock.StockQuant;
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
public class StockTransferRestService {
    private static final Logger logger = LoggerFactory.getLogger(StockTransferRestService.class);
    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private StockQuantService stockQuantService;

    @Autowired
    private ILocationRpcService locationRpcService;



    @POST
    @Path("add")
    public String addPlan(StockTransferPlan plan)  throws BizCheckedException {
        try{
            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("locationId", plan.getFromLocationId());
            mapQuery.put("itemId", plan.getItemId());
            BigDecimal total = stockQuantService.getQty(mapQuery);

            BigDecimal requiredQty = plan.getUomQty();
            if (requiredQty.equals(BigDecimal.ZERO)) {
                requiredQty =  total;
                plan.setQty(requiredQty);
            }
            if ( requiredQty.compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
                throw new BizCheckedException("2040001");
            }

            TaskEntry taskEntry = new TaskEntry();
            TaskInfo taskInfo = new TaskInfo();
            ObjUtils.bean2bean(plan, taskInfo);
            taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
            taskEntry.setTaskInfo(taskInfo);
            taskRpcService.create(TaskConstant.TYPE_STOCK_TRANSFER, taskEntry);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("confirm")
    public String confirmTransfer(StockTransferPlan plan) throws BizCheckedException {
        try{
            Long taskId = plan.getTaskId();

            taskRpcService.done(taskId);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Create failed");
        }
        return JsonUtils.SUCCESS();
    }
}
