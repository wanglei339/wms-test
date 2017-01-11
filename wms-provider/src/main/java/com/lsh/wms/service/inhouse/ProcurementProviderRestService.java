package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProviderRestService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.utils.NsHeadClient;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.service.sync.AsyncEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "rest")
@Path("inhouse/procurement")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ProcurementProviderRestService implements IProcurementProviderRestService {
    private static Logger logger = LoggerFactory.getLogger(ProcurementProviderRestService.class);

    @Autowired
    private ProcurementProviderRpcService rpcService;
    @Reference
    private ITaskRpcService taskRpcService;

    @GET
    @Path("fetchTask")
    public String fetchTask(@QueryParam("uid")long uid, @QueryParam("zoneId")long zoneId) throws BizCheckedException{
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("uid", uid);
        query.put("cmd", "fetchTask");
        query.put("zone_id", zoneId);
        String ip = PropertyUtils.getString("replenish_svr_ip");
        int port = PropertyUtils.getInt("replenish_svr_port");
        String rst = NsHeadClient.jsonCall(ip, port, JsonUtils.obj2Json(query));
        return JsonUtils.SUCCESS(rst);
    }

    @POST
    @Path("add")
    public String addProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        try{
            if(!rpcService.checkAndFillPlan(plan)){
                return JsonUtils.TOKEN_ERROR("补货计划参数错误");
            }
             boolean isTrue = rpcService.addProcurementPlan(plan);
            if(!isTrue){
                return JsonUtils.TOKEN_ERROR("创建失败");
            }
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("check")
    public String check()  throws BizCheckedException {
        StockTransferPlan plan = new StockTransferPlan();
        plan.setItemId(6938608045749l);
        plan.setToLocationId(13401921259027l);
        plan.setFromLocationId(13298032526093l);
        plan.setSubType(1l);
        plan.setPriority(1l);
        return JsonUtils.SUCCESS( rpcService.checkAndFillPlan(plan));
    }
    @POST
    @Path("update")
    public String updateProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        try{
            if(!rpcService.checkAndFillPlan(plan)){
                return JsonUtils.TOKEN_ERROR("补货计划参数错误");
            }
            boolean isTrue = rpcService.updateProcurementPlan(plan);
            if(!isTrue){
                return JsonUtils.TOKEN_ERROR("更新失败");
            }
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("cancel")
    public String cancelProcurementPlan(@QueryParam("taskId") long taskId)  throws BizCheckedException {
        try{
            taskRpcService.cancel(taskId);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("getOutBoundLocation")
    public String getOutBoundLocation(@QueryParam("itemId") long itemId,@QueryParam("locationId") long locationId)  throws BizCheckedException {
        Set<Long> outBoundLocation = new HashSet<Long>();
        try{
            outBoundLocation = rpcService.getOutBoundLocation(itemId,locationId);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS(outBoundLocation);
    }
    @GET
    @Path("autoCreate")
    public String createProcurement()  throws BizCheckedException {
        try{
//            rpcService.createProcurement(true);
            AsyncEventService.post(true);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("testAutoCreate")
    public String autoCreate()  throws BizCheckedException {
        try{
//            rpcService.createProcurement(true);
            AsyncEventService.post(false);
        } catch (BizCheckedException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS();
    }
}
