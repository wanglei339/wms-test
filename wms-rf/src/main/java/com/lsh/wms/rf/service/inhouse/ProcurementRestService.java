package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRestService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/2.
 */

@Service(protocol = "rest")
@Path("inhouse/procurement")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ProcurementRestService implements IProcurementRestService {

    private static Logger logger = LoggerFactory.getLogger(ProcurementRestService.class);

    @Reference
    private IProcurementProveiderRpcService rpcService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @Reference
    private IStockQuantRpcService quantRpcService;
    @POST
    @Path("scanFromLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanFromLocation() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        try {
            Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
            TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            if(entry==null ){
                return JsonUtils.TOKEN_ERROR("任务不存在");
            }else {
                Long fromLocation = Long.valueOf(mapQuery.get("locationId").toString());
                if(entry.getTaskInfo().getFromLocationId().compareTo(fromLocation) !=0 ){
                    return JsonUtils.TOKEN_ERROR("扫描库位和系统库位不一致");
                }
            }
            rpcService.scanFromLocation(mapQuery);
        }catch (BizCheckedException ex){
            throw ex;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("系统繁忙");
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    @POST
    @Path("scanToLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanToLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        try {
            Long taskId = Long.valueOf(params.get("taskId").toString());
            TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            if(entry==null ){
                return JsonUtils.TOKEN_ERROR("任务不存在");
            }else {
                Long toLocation = Long.valueOf(params.get("locationId").toString());
                if(entry.getTaskInfo().getToLocationId().compareTo(toLocation) !=0 ){
                    return JsonUtils.TOKEN_ERROR("扫描库位和系统库位不一致");
                }
            }
            rpcService.scanToLocation(params);
        }catch (BizCheckedException ex){
            throw ex;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("系统繁忙");
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    @POST
    @Path("scanLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        try {
            Long taskId = Long.valueOf(params.get("taskId").toString());
            final TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            Long type = Long.parseLong(params.get("type").toString());
            if(type.compareTo(2L)==0) {
                if (entry == null) {
                    return JsonUtils.TOKEN_ERROR("任务不存在");
                } else {
                    Long toLocation = Long.valueOf(params.get("locationId").toString());
                    if (entry.getTaskInfo().getToLocationId().compareTo(toLocation) != 0) {
                        return JsonUtils.TOKEN_ERROR("扫描库位和系统库位不一致");
                    }
                }
                rpcService.scanToLocation(params);
                return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                    {
                        put("response", true);
                    }
                });
            }else if(type.compareTo(1L)==0) {
                if(entry==null ){
                    return JsonUtils.TOKEN_ERROR("任务不存在");
                }
                rpcService.scanFromLocation(params);
                final TaskInfo info = entry.getTaskInfo();
                StockQuantCondition condition = new StockQuantCondition();
                condition.setItemId(info.getItemId());
                condition.setLocationId(info.getFromLocationId());
                BigDecimal qty = quantRpcService.getQty(condition);
                if(qty.compareTo(BigDecimal.ZERO)==0){
                    iTaskRpcService.cancel(taskId);
                    return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                        {
                            put("response", true);
                        }
                    });
                }
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",2);
                        put("locationId", info.getToLocationId());
                        put("subType",info.getSubType());
                        put("locationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("packName", info.getPackName());
                        if(info.getSubType().compareTo(1L)==0){
                            put("uomQty","整托");
                        }else {
                            put("uomQty", info.getQtyDone());
                        }

                    }
                });
            }else {
                return JsonUtils.TOKEN_ERROR("任务状态异常");
            }
        }catch (BizCheckedException ex){
            throw ex;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("系统繁忙");
        }
    }

    @POST
    @Path("fetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fetchTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Long uid = 0L;
        try {
            uid =  Long.valueOf(RequestUtils.getHeader("uid"));
        }catch (Exception e){
            return JsonUtils.TOKEN_ERROR("违法的账户");
        }
        SysUser user = iSysUserRpcService.getSysUserById(uid);
        if(user==null){
            return JsonUtils.TOKEN_ERROR("用户不存在");
        }
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("operator",uid);
        queryMap.put("status",TaskConstant.Assigned);
        List<TaskEntry> entries = iTaskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT, queryMap);
        if(entries!=null && entries.size()!=0){
            TaskEntry entry = entries.get(0);
            final TaskInfo info = entry.getTaskInfo();
            if(info.getExt4().compareTo(1L)==0){
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",2L);
                        put("locationId", info.getToLocationId());
                        put("locationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("packName", info.getPackName());
                        put("subType",info.getSubType());
                        if(info.getSubType().compareTo(1L)==0){
                            put("uomQty","整托");
                        }else {
                            put("uomQty", info.getQtyDone());
                        }

                    }
                });
            }else {
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",1L);
                        put("locationId", info.getFromLocationId());
                        put("locationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());
                        put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("packName", info.getPackName());
                        put("subType",info.getSubType());
                        if(info.getSubType().compareTo(1L)==0){
                            put("uomQty","整托");
                        }else {
                            put("uomQty", info.getQty());
                        }
                    }
                });
            }
        }
        final Long taskId = rpcService.assign(uid);
        if(taskId.compareTo(0L)==0) {
            return JsonUtils.TOKEN_ERROR("无补货任务可领");
        }
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2040001");
        }
        final TaskInfo taskInfo = taskEntry.getTaskInfo();
        final Long fromLocationId = taskInfo.getFromLocationId();
        final String fromLocationCode = locationRpcService.getLocation(fromLocationId).getLocationCode();
        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
            {
                put("taskId", taskInfo.getTaskId().toString());
                put("type", 1L);
                put("locationId", fromLocationId);
                put("locationCode", fromLocationCode);
                put("itemId", taskInfo.getItemId());
                put("subType",taskInfo.getSubType());
                put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
                put("packName", taskInfo.getPackName());
                if(taskInfo.getSubType().compareTo(1L)==0){
                    put("uomQty","整托");
                }else {
                    put("uomQty", taskInfo.getQty());
                }
            }
        });
    }

    @POST
    @Path("view")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String taskView() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        try {
            TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("itemId", taskInfo.getItemId());
            resultMap.put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
            resultMap.put("fromLocationId", taskInfo.getFromLocationId());
            resultMap.put("fromLocationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            resultMap.put("toLocationId", taskInfo.getToLocationId());
            resultMap.put("toLocationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            resultMap.put("packName", taskInfo.getPackName());
            resultMap.put("uomQty", taskInfo.getQty().divide(taskInfo.getPackUnit()));
            return JsonUtils.SUCCESS(resultMap);
        }catch (BizCheckedException ex){
            throw ex;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("系统繁忙");
        }
    }
}
