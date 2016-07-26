package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.model.transfer.StockTransferTaskDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mali on 16/7/26.
 */

@Service(protocol = "rest")
@Path("inhouse/stock_taking")
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
            TaskEntry taskEntry = new TaskEntry();

            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setType(TaskConstant.TYPE_STOCK_TRANSFER);
            taskEntry.setTaskInfo(taskInfo);

            taskEntry.setTaskHead(plan);

            List<StockTransferTaskDetail> detailList = new ArrayList<StockTransferTaskDetail>();
            StockTransferTaskDetail detail = new StockTransferTaskDetail();
            ObjUtils.bean2bean(plan, detail);


        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Create failed");
        }
        return JsonUtils.SUCCESS();
    }

}
