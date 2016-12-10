package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.system.SysUser;
import net.sf.json.JSONObject;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRfRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
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
 * Created by wuhao on 16/7/30.
 */
@Service(protocol = "rest")
@Path("inhouse/stock_taking")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StockTakingRfRestService implements IStockTakingRfRestService {

    private static Logger logger = LoggerFactory.getLogger(StockTakingRfRestService.class);

    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private CsiSkuService skuService;
    @Autowired
    private StockTakingTaskService stockTakingTaskService;
    @Autowired
    private ItemService itemService;
    @Reference
    private ISysUserRpcService iSysUserRpcService;
    @Reference
    private ILocationRpcService locationRpcService;
    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;
    @Reference
    private IDataBackService dataBackService;


    @POST
    @Path("doOne")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String doOne() throws BizCheckedException {
        //Long taskId,int qty,String barcode
        Map request = RequestUtils.getRequest();
        List<StockTakingDetail> insertDetails = new ArrayList<StockTakingDetail>();
        JSONObject object = null;
        Long taskId = 0L;
        List<Map> resultList = null;
        try {
            object = JSONObject.fromObject(request.get("result"));
            //locationCode =
            taskId = Long.parseLong(object.get("taskId").toString().trim());
            resultList = object.getJSONArray("list");
        }catch (Exception e){
            return JsonUtils.TOKEN_ERROR("JSON解析失败");
        }

        if (!(resultList == null || resultList.size() == 0)) {
            TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            StockTakingTask task = (StockTakingTask) (entry.getTaskHead());
            StockTakingDetail detail = (StockTakingDetail) (entry.getTaskDetailList().get(0));
            quantService.getQuantsByLocationId(detail.getLocationId());
            if(detail.getTheoreticalQty().equals(BigDecimal.ZERO)) {

            }
            BaseinfoItem item = itemService.getItem(detail.getItemId());
            for (Map<String, Object> beanMap : resultList) {
                Object barcode = beanMap.get("barcode");
                BigDecimal realQty = new BigDecimal(beanMap.get("qty").toString().trim());
                if (item.getCode().equals(barcode.toString().trim())) {
                    BigDecimal qty = quantService.getQuantQtyByLocationIdAndItemId(detail.getLocationId(), detail.getItemId());
                    detail.setTheoreticalQty(qty);
                    detail.setRealQty(realQty);
                    detail.setUpdatedAt(DateUtils.getCurrentSeconds());
                    stockTakingService.updateDetail(detail);
                } else {
                    try {
                        CsiSku csiSku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode.toString().trim());
                        StockTakingDetail newDetail = new StockTakingDetail();
                        newDetail.setRealQty(realQty);
                        newDetail.setTakingId(task.getTakingId());
                        newDetail.setTaskId(taskId);
                        newDetail.setRealSkuId(csiSku.getSkuId());
                        newDetail.setSkuId(csiSku.getSkuId());
                        newDetail.setLocationId(detail.getLocationId());
                        newDetail.setContainerId(detail.getContainerId());
                        newDetail.setRound(detail.getRound());
                        insertDetails.add(newDetail);
                    }catch (Exception e){
                        return JsonUtils.TOKEN_ERROR("国条:"+barcode.toString()+"在仓库无记录");
                    }
                }
            }
            if(insertDetails.size()!=0) {
                stockTakingService.insertDetailList(insertDetails);
            }

        }

        iTaskRpcService.done(taskId);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }

    @POST
    @Path("assign")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String assign() throws BizCheckedException {
        Map<String,Object> result = new HashMap<String, Object>();
        Long uId=0L;
        List<Map> taskList = new ArrayList<Map>();
        try {
            uId =  Long.valueOf(RequestUtils.getHeader("uid"));
        }catch (Exception e){
            logger.info(e.getMessage());
            return JsonUtils.TOKEN_ERROR("违法的账户");
        }
        SysUser user =  iSysUserRpcService.getSysUserById(uId);
        if(user==null){
            return JsonUtils.TOKEN_ERROR("用户不存在");
        }
        Map<String,Object> statusQueryMap = new HashMap();
        statusQueryMap.put("status",TaskConstant.Assigned);
        statusQueryMap.put("operator", uId);

        List<TaskEntry> list = iTaskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TAKING, statusQueryMap);
        if(list!=null && list.size()!=0){
            for(TaskEntry taskEntry:list){
                Map<String,Object>task = new HashMap<String,Object>();
                task.put("taskId",taskEntry.getTaskInfo().getTaskId().toString());
                String locationCode= " ";
                Long locationId = 0L;
                List<Object> objectList = taskEntry.getTaskDetailList();
                if(objectList!=null && objectList.size()!=0){
                    StockTakingDetail detail =(StockTakingDetail)(objectList.get(0));
                    locationId = detail.getLocationId();
                    BaseinfoLocation location = locationService.getLocation(detail.getLocationId());
                    if(location != null){
                        locationCode = location.getLocationCode();
                    }
                }
                task.put("locationId",locationId);
                task.put("locationCode",locationCode);
                taskList.add(task);
            }
            result.put("taskList", taskList);
            return JsonUtils.SUCCESS(result);
        }
        Map<String,Object> queryMap =new HashMap<String, Object>();
        queryMap.put("status", TaskConstant.Draft);
        List<TaskEntry> entries =iTaskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TAKING, queryMap);
        if(entries==null ||entries.size()==0){
            return JsonUtils.TOKEN_ERROR("无盘点任务可领");
        }

        //同一盘点任务，同一个人不能领多次
        TaskInfo info = null;
        StockTakingTask  takingTask = null;
        for(TaskEntry entry:entries){
            takingTask = (StockTakingTask)(entry.getTaskHead());
            if(takingTask.getRound()==1){
                info = entry.getTaskInfo();
                break;
            }else {
                queryMap.put("planId", takingTask.getTakingId());
                queryMap.put("status",TaskConstant.Done);
                List<TaskEntry> entryList = iTaskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TAKING,queryMap);
                Map<Long,Integer> chageMap = new HashMap<Long, Integer>();
                for(TaskEntry tmp:entryList){
                    chageMap.put(tmp.getTaskInfo().getOperator(),1);
                }
                if(!chageMap.containsKey(uId)){
                    info = entry.getTaskInfo();
                    break;
                }
            }

        }
        if(info==null){
            return JsonUtils.TOKEN_ERROR("无盘点任务可领");
        }
        Long round = takingTask.getRound();
        queryMap.put("round", round);
        queryMap.put("isValid",1L);
        queryMap.put("takingId",takingTask.getTakingId());
        List<StockTakingTask> takingTasks =stockTakingTaskService.getTakingTask(queryMap);
        List<Long> taskIdList = new ArrayList<Long>();
        for(StockTakingTask takingtask:takingTasks){
            Map<String,Object> task =new HashMap<String, Object>();
            task.put("taskId",takingtask.getTaskId().toString());
            taskIdList.add(takingtask.getTaskId());
            List<StockTakingDetail> details =stockTakingService.getDetailByTaskId(takingtask.getTaskId());
            if(details == null || details.size() == 0){
                task.put("locationCode","");
                task.put("locationId",0L);
            }else {
                task.put("locationId",details.get(0).getLocationId());
                BaseinfoLocation location = locationService.getLocation(details.get(0).getLocationId());
                if(location==null){
                    task.put("locationCode","");
                }else {
                    task.put("locationCode",location.getLocationCode());
                }
            }
            taskList.add(task);
        }
        iTaskRpcService.batchAssign(TaskConstant.TYPE_STOCK_TAKING, taskIdList, uId);
        result.put("taskList",taskList);
        return JsonUtils.SUCCESS(result);
    }

    @POST
    @Path("getTask")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getTaskInfo() throws BizCheckedException{
        Map<String, Object> params =RequestUtils.getRequest();
        Long taskId = 0L;
        Long locationId = 0L;

        try {
            taskId = Long.valueOf(params.get("taskId").toString().trim());
            String locationCode = params.get("locationCode").toString().trim();
            locationId =  locationRpcService.getLocationIdByCode(locationCode);
        }catch (Exception e){
            return JsonUtils.TOKEN_ERROR("数据格式类型有误");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry==null){
            return JsonUtils.TOKEN_ERROR("盘点任务不存在");
        }
        StockTakingDetail detail = (StockTakingDetail)(entry.getTaskDetailList().get(0));
        if(!detail.getLocationId().equals(locationId)){
            return JsonUtils.TOKEN_ERROR("扫描库位与系统所需盘点库位不相符");
        }
        StockTakingTask task = (StockTakingTask)(entry.getTaskHead());
        StockTakingHead head = stockTakingService.getHeadById(task.getTakingId());
        BigDecimal qty = quantService.getQuantQtyByLocationIdAndItemId(detail.getLocationId(), detail.getItemId());
        detail.setTheoreticalQty(qty);
        stockTakingService.updateDetail(detail);
        Map<String,Object> queryMap =new HashMap<String, Object>();
        queryMap.put("itemId",detail.getItemId());
        queryMap.put("locationId",detail.getLocationId());
        List<StockQuant> quantList = quantService.getQuants(queryMap);
        String packName = (quantList==null ||quantList.size()==0) ? "" : quantList.get(0).getPackName();
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("viewType",head.getViewType());
        result.put("taskId",taskId.toString());
        BaseinfoLocation location = locationService.getLocation(detail.getLocationId());
        BaseinfoItem item = itemService.getItem(detail.getItemId());
        result.put("locationCode",location==null ? "":location.getLocationCode());
        result.put("itemName", item == null ? "" : item.getSkuName());
        result.put("qty", qty);
        result.put("packName",packName);
        return JsonUtils.SUCCESS(result);

    }
    @POST
    @Path("view")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String view() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        Long taskId = 0L;
        Long locationId = 0L;
        String locationCode = "";
        try {
            if (params.get("taskId") != null) {
                taskId = Long.valueOf(params.get("taskId").toString().trim());
            }
            locationCode = params.get("locationCode").toString().trim();
            locationId = locationRpcService.getLocationIdByCode(locationCode);
        } catch (Exception e) {
            return JsonUtils.TOKEN_ERROR("数据格式类型有误");
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("locationId", locationId);
        List<StockQuant> quantList = quantService.getQuants(queryMap);
        StockQuant quant = null;
        if(quantList!=null && quantList.size()!=0){
            quant = quantList.get(0);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        if (!taskId.equals(0L)){
            result.put("taskId", taskId.toString());
        }
        BaseinfoItem item = null;
        if(quant!=null) {
            item = itemService.getItem(quant.getItemId());
        }
        result.put("locationCode",locationCode);
        result.put("itemName", item == null ? "" : item.getSkuName());
        result.put("packName",quant!=null ? "" : quantList.get(0).getPackName());
        return JsonUtils.SUCCESS(result);

    }

}
