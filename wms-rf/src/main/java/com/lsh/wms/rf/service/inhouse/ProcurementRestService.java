package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRestService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WorkZoneConstant;
import com.lsh.wms.core.dao.utils.NsHeadClient;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.zone.WorkZoneService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.zone.WorkZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    private IProcurementProveiderRpcService iProcurementProveiderRpcService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @Reference
    private IStockQuantRpcService quantRpcService;

    @Autowired
    private WorkZoneService workZoneService;
    @Autowired
    private BaseTaskService baseTaskService;

    @POST
    @Path("getZoneList")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getZoneList() throws BizCheckedException {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("cmd", "getZoneList");
        String ip = PropertyUtils.getString("replenish_svr_ip");
        int port = PropertyUtils.getInt("replenish_svr_port");
        String rstString = NsHeadClient.jsonCall(ip, port, JsonUtils.obj2Json(query));
        Map rst = JsonUtils.json2Obj(rstString, Map.class);
        if ( rst == null || Long.valueOf(rst.get("ret").toString())!=0){
            return JsonUtils.TOKEN_ERROR("服务器错误");
        }else{
            return JsonUtils.SUCCESS(rst);
        }

    }

    @POST
    @Path("loginToZone")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String loginToZone() throws BizCheckedException {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("cmd", "loginToZone");
        query.put("zone_id",  RequestUtils.getRequest().get("zoneId"));
        query.put("uid", Long.valueOf(RequestUtils.getHeader("uid")));
        String ip = PropertyUtils.getString("replenish_svr_ip");
        int port = PropertyUtils.getInt("replenish_svr_port");
        String rstString = NsHeadClient.jsonCall(ip, port, JsonUtils.obj2Json(query));
        Map rst = JsonUtils.json2Obj(rstString, Map.class);
        if ( rst == null || Long.valueOf(rst.get("ret").toString())!=0){
            return JsonUtils.TOKEN_ERROR("服务器错误");
        }else{
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });
        }
    }

    @POST
    @Path("logoutFromZone")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String logoutFromZone() throws BizCheckedException {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("cmd", "logoutFromZone");
        query.put("zone_id",  RequestUtils.getRequest().get("zoneId"));
        query.put("uid", Long.valueOf(RequestUtils.getHeader("uid")));
        String ip = PropertyUtils.getString("replenish_svr_ip");
        int port = PropertyUtils.getInt("replenish_svr_port");
        String rstString = NsHeadClient.jsonCall(ip, port, JsonUtils.obj2Json(query));
        Map rst = JsonUtils.json2Obj(rstString, Map.class);
        if ( rst == null || Long.valueOf(rst.get("ret").toString())!=0){
            return JsonUtils.TOKEN_ERROR("服务器错误");
        }else{
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });
        }
    }

    @POST
    @Path("getZoneTaskList")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getZoneTaskList() throws BizCheckedException {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("cmd", "getZoneTaskList");
        query.put("zone_id",  RequestUtils.getRequest().get("zoneId"));
        query.put("uid", Long.valueOf(RequestUtils.getHeader("uid")));
        String ip = PropertyUtils.getString("replenish_svr_ip");
        int port = PropertyUtils.getInt("replenish_svr_port");
        String rstString = NsHeadClient.jsonCall(ip, port, JsonUtils.obj2Json(query));
        Map rst = JsonUtils.json2Obj(rstString, Map.class);
        if ( rst == null || Long.valueOf(rst.get("ret").toString())!=0){
            return JsonUtils.TOKEN_ERROR("服务器错误");
        }else{
            return JsonUtils.SUCCESS(rst);
        }
    }

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
            iProcurementProveiderRpcService.scanFromLocation(mapQuery);
        }catch (BizCheckedException ex){
            throw ex;
        }
// catch (Exception e) {
//            logger.error(e.getMessage());
//            return JsonUtils.TOKEN_ERROR("系统繁忙");
//        }
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
            iProcurementProveiderRpcService.scanToLocation(params);
        }catch (BizCheckedException ex){
            throw ex;
        }
//        catch (Exception e) {
//            logger.error(e.getMessage());
//            return JsonUtils.TOKEN_ERROR("系统繁忙");
//        }
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
        params.put("uid",RequestUtils.getHeader("uid"));
        try {
            Long taskId = Long.valueOf(params.get("taskId").toString().trim());
            final TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            Long type = Long.parseLong(params.get("type").toString().trim());
            if(type.compareTo(2L)==0) {
                if (entry == null) {
                    return JsonUtils.TOKEN_ERROR("任务不存在");
                } else {
                    String locationCode = params.get("locationCode").toString();
                    Long toLocationId =  locationRpcService.getLocationIdByCode(locationCode);
                    if (entry.getTaskInfo().getToLocationId().compareTo(toLocationId) != 0) {
                        return JsonUtils.TOKEN_ERROR("扫描库位和系统库位不一致");
                    }
                }
                iProcurementProveiderRpcService.scanToLocation(params);
                return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                    {
                        put("response", true);
                    }
                });
            }else if(type.compareTo(1L)==0) {
                if(entry==null ){
                    return JsonUtils.TOKEN_ERROR("任务不存在");
                }
                TaskInfo info = entry.getTaskInfo();
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
                //判断能否整除
                final BaseinfoItem item = itemRpcService.getItem(info.getItemId());
                iProcurementProveiderRpcService.scanFromLocation(params);
                final TaskInfo taskInfo = baseTaskService.getTaskInfoById(taskId);
                final BigDecimal [] decimals = taskInfo.getQty().divideAndRemainder(info.getPackUnit());
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", taskInfo.getTaskId().toString());
                        put("type",2);
                        put("barcode",item.getCode());
                        put("skuCode",item.getSkuCode());
                        put("locationId", taskInfo.getToLocationId());
                        put("locationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
                        put("toLocationId", taskInfo.getToLocationId());
                        put("toLocationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
                        put("fromLocationId", taskInfo.getFromLocationId());
                        put("fromLocationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
                        put("subType",taskInfo.getSubType());
                        put("itemId", taskInfo.getItemId());
                        put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
                        if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                            put("qty", decimals[0]);
                            put("packName", taskInfo.getPackName());
                        }else {
                            put("qty", taskInfo.getQty());
                            put("packName", "EA");
                        }
                    }
                });
            }else {
                return JsonUtils.TOKEN_ERROR("任务状态异常");
            }
        }catch (BizCheckedException ex){
            throw ex;
        }
//        catch (Exception e) {
//            logger.error(e.getMessage());
//            return JsonUtils.TOKEN_ERROR("系统繁忙");
//        }
    }

    @POST
    @Path("fetchTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String fetchTask() throws BizCheckedException {
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
            final BaseinfoItem item = itemRpcService.getItem(info.getItemId());
            final BigDecimal [] decimals = info.getQty().divideAndRemainder(info.getPackUnit());
            if(info.getStep()==2){
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",2L);
                        put("barcode",item.getCode());
                        put("skuCode",item.getSkuCode());
                        put("locationId", info.getToLocationId());
                        put("locationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("toLocationId", info.getToLocationId());
                        put("toLocationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("fromLocationId", info.getFromLocationId());
                        put("fromLocationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());   put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("subType",info.getSubType());
                        if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                            put("qty", decimals[0]);
                            put("packName", info.getPackName());
                        }else {
                            put("qty", info.getQty());
                            put("packName", "EA");
                        }
                    }
                });
            }else {
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",1L);
                        put("barcode",item.getCode());
                        put("skuCode",item.getSkuCode());
                        put("locationId", info.getFromLocationId());
                        put("locationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());
                        put("toLocationId", info.getToLocationId());
                        put("toLocationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("fromLocationId", info.getFromLocationId());
                        put("fromLocationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());
                        put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("subType",info.getSubType());
                        if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                            put("qty", decimals[0]);
                            put("packName", info.getPackName());
                        }else {
                            put("qty", info.getQty());
                            put("packName", "EA");
                        }
                    }
                });
            }
        }
        //final Long taskId = iProcurementProveiderRpcService.assign(uid);
        Long taskId = null;
        {
            //改成从补货策略服务上获取任务
            //获取到的任务需要立即给指定的人
            //如果因为系统异常导致的错误未能assign成功,补货策略服务讲在一个指定的时间(如10秒)后扫描恢复到任务队列中
            //另外,如果一个任务长时间未执行(一个指定的时间),补货策略服务也会从新调度到队列中可赋给其他人
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("uid", uid);
            query.put("cmd", "fetchTask");
            query.put("zone_id", RequestUtils.getRequest().get("zoneId"));
            String ip = PropertyUtils.getString("replenish_svr_ip");
            int port = PropertyUtils.getInt("replenish_svr_port");
            String rstString = NsHeadClient.jsonCall(ip, port, JsonUtils.obj2Json(query));
            Map rst = JsonUtils.json2Obj(rstString, Map.class);
            if ( rst == null || Long.valueOf(rst.get("ret").toString())!=0){
                return JsonUtils.TOKEN_ERROR("服务器错误");
            }else{
                taskId = Long.valueOf(rst.get("task_id").toString());
                if(taskId == -1){
                    return JsonUtils.TOKEN_ERROR("无补货任务可领");
                }
            }
            iTaskRpcService.assign(taskId, uid);
        }
        if(taskId.compareTo(0L)==0) {
            return JsonUtils.TOKEN_ERROR("无补货任务可领");
        }
        TaskEntry taskEntry = iTaskRpcService.getTaskEntryById(taskId);
        if (taskEntry == null) {
            throw new BizCheckedException("2040001");
        }
        final TaskInfo taskInfo = taskEntry.getTaskInfo();
        final BaseinfoItem item = itemRpcService.getItem(taskInfo.getItemId());
        final BigDecimal [] decimals = taskInfo.getQty().divideAndRemainder(taskInfo.getPackUnit());
        final Long fromLocationId = taskInfo.getFromLocationId();
        final String fromLocationCode = locationRpcService.getLocation(fromLocationId).getLocationCode();
        final String toLocationCode = locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode();
        return JsonUtils.SUCCESS(new HashMap<String, Object>() {
            {
                put("taskId", taskInfo.getTaskId().toString());
                put("type", 1L);
                put("barcode",item.getCode());
                put("skuCode",item.getSkuCode());
                put("fromLocationId", fromLocationId);
                put("fromLocationCode", fromLocationCode);
                put("locationId", fromLocationId);
                put("locationCode", fromLocationCode);
                put("toLocationId", taskInfo.getToLocationId());
                put("toLocationCode", toLocationCode);
                put("itemId", taskInfo.getItemId());
                put("subType",taskInfo.getSubType());
                put("subType",taskInfo.getSubType());
                put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
                if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                    put("qty", decimals[0]);
                    put("packName", taskInfo.getPackName());
                }else {
                    put("qty", taskInfo.getQty());
                    put("packName", "EA");
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
            TaskEntry taskEntry = iTaskRpcService.getTaskEntryById(taskId);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            final BaseinfoItem item = itemRpcService.getItem(taskInfo.getItemId());
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("type",taskInfo.getStep());
            if(taskInfo.getStep()==1){
                resultMap.put("locationId", taskInfo.getFromLocationId());
                resultMap.put("locationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            }else {
                resultMap.put("locationId", taskInfo.getToLocationId());
                resultMap.put("locationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            }
            resultMap.put("itemId", taskInfo.getItemId());
            resultMap.put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
            resultMap.put("fromLocationId", taskInfo.getFromLocationId());
            resultMap.put("fromLocationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            resultMap.put("toLocationId", taskInfo.getToLocationId());
            resultMap.put("toLocationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            resultMap.put("packName", taskInfo.getPackName());
            resultMap.put("uomQty", taskInfo.getQty().divide(taskInfo.getPackUnit(), 0, BigDecimal.ROUND_HALF_DOWN));
            resultMap.put("barcode", item.getCode());
            resultMap.put("skuCode", item.getSkuCode());
            return JsonUtils.SUCCESS(resultMap);
        }catch (BizCheckedException ex){
            throw ex;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("系统繁忙");
        }
    }
    @POST
    @Path("assign")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String assign() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
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
            final BaseinfoItem item = itemRpcService.getItem(info.getItemId());
            final BigDecimal [] decimals = info.getQty().divideAndRemainder(info.getPackUnit());
            if(info.getStep()==2){
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",2L);
                        put("barcode",item.getCode());
                        put("skuCode",item.getSkuCode());
                        put("locationId", info.getToLocationId());
                        put("locationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("toLocationId", info.getToLocationId());
                        put("toLocationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("fromLocationId", info.getFromLocationId());
                        put("fromLocationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());   put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("subType",info.getSubType());
                        if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                            put("qty", decimals[0]);
                            put("packName", info.getPackName());
                        }else {
                            put("qty", info.getQty());
                            put("packName", "EA");
                        }
                    }
                });
            }else {
                return JsonUtils.SUCCESS(new HashMap<String, Object>() {
                    {
                        put("taskId", info.getTaskId().toString());
                        put("type",1L);
                        put("barcode",item.getCode());
                        put("skuCode",item.getSkuCode());
                        put("locationId", info.getFromLocationId());
                        put("locationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());
                        put("toLocationId", info.getToLocationId());
                        put("toLocationCode", locationRpcService.getLocation(info.getToLocationId()).getLocationCode());
                        put("fromLocationId", info.getFromLocationId());
                        put("fromLocationCode", locationRpcService.getLocation(info.getFromLocationId()).getLocationCode());
                        put("itemId", info.getItemId());
                        put("itemName", itemRpcService.getItem(info.getItemId()).getSkuName());
                        put("subType",info.getSubType());
                        if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                            put("qty", decimals[0]);
                            put("packName", info.getPackName());
                        }else {
                            put("qty", info.getQty());
                            put("packName", "EA");
                        }
                    }
                });
            }
        }




        Long taskId = Long.valueOf(mapQuery.get("taskId").toString());
        try {
            TaskEntry taskEntry = iTaskRpcService.getTaskEntryById(taskId);
            iTaskRpcService.assign(taskId,uid);
            if (taskEntry == null) {
                throw new BizCheckedException("2040001");
            }
            TaskInfo taskInfo = taskEntry.getTaskInfo();
            final BigDecimal [] decimals = taskInfo.getQty().divideAndRemainder(taskInfo.getPackUnit());
            final BaseinfoItem item = itemRpcService.getItem(taskInfo.getItemId());
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("type",taskInfo.getStep());
            resultMap.put("taskId",taskInfo.getTaskId().toString());
            if(taskInfo.getStep()==1){
                resultMap.put("locationId", taskInfo.getFromLocationId());
                resultMap.put("locationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            }else {
                resultMap.put("locationId", taskInfo.getToLocationId());
                resultMap.put("locationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            }
            resultMap.put("itemId", taskInfo.getItemId());
            resultMap.put("itemName", itemRpcService.getItem(taskInfo.getItemId()).getSkuName());
            resultMap.put("fromLocationId", taskInfo.getFromLocationId());
            resultMap.put("fromLocationCode", locationRpcService.getLocation(taskInfo.getFromLocationId()).getLocationCode());
            resultMap.put("toLocationId", taskInfo.getToLocationId());
            resultMap.put("toLocationCode", locationRpcService.getLocation(taskInfo.getToLocationId()).getLocationCode());
            if(decimals[1].compareTo(BigDecimal.ZERO)==0) {
                resultMap.put("qty", decimals[0]);
                resultMap.put("packName", taskInfo.getPackName());
            }else {
                resultMap.put("qty", taskInfo.getQty());
                resultMap.put("packName", "EA");
            }
            resultMap.put("barcode", item.getCode());
            resultMap.put("skuCode", item.getSkuCode());
            resultMap.put("subType", taskInfo.getSubType());
            return JsonUtils.SUCCESS(resultMap);
        }catch (BizCheckedException ex){
            throw ex;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("系统繁忙");
        }
    }
}
