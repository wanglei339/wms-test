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
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
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
        } catch (Exception e) {
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
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
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    @POST
    @Path("fetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fetchTask() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Long staffId = 0L;
        try {
            Long uid = Long.valueOf(params.get("uId").toString());
            staffId = iSysUserRpcService.getSysUserById(uid).getStaffId();
        }catch (Exception e){
            return JsonUtils.TOKEN_ERROR("违法的账户");
        }
        final Long taskId = rpcService.assign(staffId);
        if(taskId == 0) {
            throw new BizCheckedException("2040001");
        }
        TaskEntry taskEntry = taskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2040001");
        }
        final TaskInfo taskInfo = taskEntry.getTaskInfo();
        final Long fromLocationId = taskInfo.getFromLocationId();
        final String fromLocationCode =  locationRpcService.getLocation(fromLocationId).getLocationCode();
        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
            {
                put("taskId", taskId);
                put("fromLocationId", fromLocationId);
                put("fromLocationCode",fromLocationCode);
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
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR(e.getCause().getMessage());
        }
    }
}
