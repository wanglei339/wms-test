package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingProviderRpcService;
import com.lsh.wms.api.service.inhouse.IStockTakingRfRestService;
import com.lsh.wms.api.service.inhouse.IStockTakingRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.StockTakingConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.StockTakingTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    private IStockTakingRpcService stockTakingRpcService;
    @Reference
    private IStockTakingProviderRpcService stockTakingProviderRpcService;

    @POST
    @Path("doOne")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String doOne() throws BizCheckedException {
        //Long taskId,int qty,String barcode,String locationCode
        Map request = RequestUtils.getRequest();
        JSONObject object = null;
        Long taskId = 0L;
        String locationCode = "";
        Long uid = 0l;
        String barcode = "";
        BigDecimal realEaQty = BigDecimal.ZERO;
        BigDecimal realUmoQty = BigDecimal.ZERO;
        List<Map> resultList = null;
        logger.info("params:"+request);
        try {
            object = JSONObject.fromObject(request.get("result"));
            //库位编码
            locationCode = object.get("locationCode").toString();
            
            uid =  Long.valueOf(RequestUtils.getHeader("uid"));

            if(object.get("taskId")!=null && !"".equals(object.get("taskId").toString())) {
                taskId = Long.parseLong(object.get("taskId").toString().trim());
            }
            resultList = object.getJSONArray("list");
        }catch (Exception e){
            return JsonUtils.TOKEN_ERROR("JSON解析失败");
        }

        BaseinfoLocation location = locationService.getLocationByCode(locationCode);
        CsiSku sku = null;
        if(location == null){
            return JsonUtils.TOKEN_ERROR("位置不存在");
        }
        Long locationId = location.getLocationId();
        if(resultList!=null && resultList.size()!=0) {

            Map<String, Object> beanMap = resultList.get(0);
            //商品码
            barcode = beanMap.get("barcode").toString().trim();
            //盘点数量
            realEaQty = new BigDecimal(beanMap.get("qty").toString().trim());
            realUmoQty = new BigDecimal(beanMap.get("qty").toString().trim());
            if(barcode != null && !"".equals(barcode)) {
                sku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode);
            }
            if(sku==null){
                throw new BizCheckedException("2550068",barcode,"");
            }
        }


        if(taskId.equals(0L)){
            //临时盘点
            stockTakingProviderRpcService.createAndDoTmpTask(locationId,realEaQty,realUmoQty,barcode,uid);

            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", true);
                }
            });
        }


        //获取当前位置的任务信息
        StockTakingDetail detail = stockTakingService.getDetailByTaskIdAndLocation(taskId,locationId);
        if(detail ==null){
            return JsonUtils.TOKEN_ERROR("盘点详情异常");
        }
        if(!detail.getStatus().equals(StockTakingConstant.Draft) && !detail.getStatus().equals(StockTakingConstant.Assigned)){
            return JsonUtils.TOKEN_ERROR("该库位盘点已完成或取消");
        }
        detail = stockTakingRpcService.fillDetail(detail);
        CsiSku csiSku = null;
        BaseinfoItem item = null;
        if(!detail.getSkuId().equals(0L)) {
            csiSku = skuService.getSku(detail.getSkuId());
        }
        if(!detail.getItemId().equals(0L)){
            item =  itemService.getItem(detail.getItemId());
        }
        if(detail.getTheoreticalQty().compareTo(BigDecimal.ZERO)==0){
            //该盘点位置没有商品
            if(sku != null){
                //将输入填充
                detail.setSkuId(sku.getSkuId());
                detail.setBarcode(sku.getCode());
                detail.setRealSkuId(sku.getSkuId());
                detail.setUmoQty(realUmoQty);
                detail.setRealQty(realEaQty);
            }
        }
        if(!("").equals(barcode) && ((csiSku!=null && csiSku.getCode().equals(barcode)) || (item!=null && item.getPackCode().equals(barcode)))) {
            //库位有商品
            detail.setRealQty(realEaQty);
            detail.setUmoQty(realUmoQty);
            stockTakingService.updateDetail(detail);
        }

        stockTakingService.doneDetail(detail);

        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }
    /*
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
     */
    /**
     * 扫码领取盘点任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("assign")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String assign() throws BizCheckedException {
        Map<String, Object> params =RequestUtils.getRequest();
        Long uId=0L;
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

        Object code = params.get("code");
        if(code==null || !StringUtils.isNotBlank(code.toString())){
            return JsonUtils.TOKEN_ERROR("任务码不能为空");
        }

        //获取用户正在进行的拣货任务
        Map<String,Object> processingTask = getProcessingTask(uId);
        if(processingTask!=null){
            return JsonUtils.SUCCESS(processingTask);
        }
        //盘点签,即任务ID
        Long  taskId = Long.valueOf(code.toString().trim());
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);

        if(entry==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }
        TaskInfo info = entry.getTaskInfo();
        if(!info.getType().equals(TaskConstant.TYPE_STOCK_TAKING)){
            return JsonUtils.TOKEN_ERROR("任务类型不匹配");
        }

        List<Map> taskList = new ArrayList<Map>();
        List<StockTakingDetail> details =(List<StockTakingDetail>) (List<?>) entry.getTaskDetailList();
        if(details==null || details.size()==0){
            return JsonUtils.TOKEN_ERROR("该任务无可盘点的库位");
        }
        List<BaseinfoLocation> locationList = new ArrayList<BaseinfoLocation>();
        Map<Long,Long> statusMap = new HashMap<Long, Long>();
        for(StockTakingDetail detail:details) {
            locationList.add(locationService.getLocation(detail.getLocationId()));
            statusMap.put(detail.getLocationId(),detail.getStatus());
        }
        //将任务通道排序
        Boolean isDone = true;
        locationList  = locationService.calcZwayOrder(locationList,true);
        for(BaseinfoLocation location:locationList) {
            Map<String,Object> taskMap =new HashMap<String, Object>();
            Long status =  statusMap.get(location.getLocationId());
            if(status.compareTo(StockTakingConstant.Done)>0){
                continue;
            }
            if(!status.equals(StockTakingConstant.PendingAudit)){
                isDone = false;
            }
            taskMap.put("taskId",taskId);
            taskMap.put("locationId", location.getLocationId());
            taskMap.put("locationCode", location.getLocationCode());
            taskMap.put("status",status);
            taskList.add(taskMap);
        }
        Map<String,Object> result = new HashMap<String, Object>();
        if(info.getStatus().compareTo(TaskConstant.Draft)>0){
            if(info.getOperator().compareTo(uId)!=0) {
                return JsonUtils.TOKEN_ERROR("该任务已被人领取或失效");
            }
            if(isDone){
                result.put("taskList",new ArrayList<Map>());
            }else {
                result.put("taskList", taskList);
            }
            return JsonUtils.SUCCESS(result);
        }
        if(isDone){
            result.put("taskList",new ArrayList<Map>());
        }else {
            result.put("taskList", taskList);
        }
        iTaskRpcService.assign(taskId,uId);
        return JsonUtils.SUCCESS(result);
    }
    /*
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
     */

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
        queryMap.put("itemId",detail.getItemId().toString());
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
        BaseinfoLocation location = null;
        String locationCode = "";
        logger.info("params:"+params);
        try {
            if (params.get("taskId") != null && !"".equals(params.get("taskId").toString().trim())) {
                taskId = Long.valueOf(params.get("taskId").toString().trim());
            }
            locationCode = params.get("locationCode").toString().trim();
            location = locationService.getLocationByCode(locationCode);
        } catch (Exception e) {
            return JsonUtils.TOKEN_ERROR("数据格式类型有误");
        }
        if(location == null){
            return JsonUtils.TOKEN_ERROR("该库位不存在");
        }
        if(location.getBinUsage().compareTo(0)==0){
            return JsonUtils.TOKEN_ERROR("该库位不是存储库位，不能进行盘点");
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("locationId", location.getLocationId());
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
        CsiSku sku = null;
        if(quant!=null) {
            item = itemService.getItem(quant.getItemId());
            sku = skuService.getSku(quant.getSkuId());
        }
        result.put("locationCode",locationCode);
        result.put("itemName", item == null ? "" : item.getSkuName());
        result.put("barcode", sku == null ? "" : sku.getCode());
        result.put("skuCode", item == null ? "" : item.getSkuCode());
        result.put("packCode", item == null ? "" : item.getPackCode());
        result.put("packName",quant==null ? "" : quantList.get(0).getPackName());
        return JsonUtils.SUCCESS(result);

    }

    /**
     * 回溯操作人正在进行的盘点任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("restore")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String restore() throws BizCheckedException{

        Long  uId =  Long.valueOf(RequestUtils.getHeader("uid"));
        Map<String,Object> result = getProcessingTask(uId);
        if(result==null || ((List)result.get("taskList")).size()==0){
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", false);
                }
            });
        }
        return JsonUtils.SUCCESS(result);

    }

    /**
     * 获取当前用户正在进行的任务
     * @param uid
     * @return
     */
    public Map<String,Object> getProcessingTask(Long uid){
        Map<String,Object> result = new HashMap<String, Object>();

        Map<String,Object> statusQueryMap = new HashMap();
        statusQueryMap.put("status",TaskConstant.Assigned);
        statusQueryMap.put("operator", uid);
        List<TaskEntry> list = iTaskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TAKING, statusQueryMap);
        if(list==null || list.size()==0){
            return null;
        }


        List<Map> taskList = new ArrayList<Map>();

        TaskEntry taskEntry = list.get(0);
        Boolean isDoing = false;

        List<StockTakingDetail> details =(List<StockTakingDetail>) (List<?>) taskEntry.getTaskDetailList();
        if(details!=null && details.size()!=0) {

            List<BaseinfoLocation> locationList = new ArrayList<BaseinfoLocation>();
            Map<Long, Long> statusMap = new HashMap<Long, Long>();
            for (StockTakingDetail detail : details) {
                locationList.add(locationService.getLocation(detail.getLocationId()));
                statusMap.put(detail.getLocationId(), detail.getStatus());
            }
            //将任务通道排序
            locationList = locationService.calcZwayOrder(locationList, true);
            for (BaseinfoLocation location : locationList) {
                Map<String, Object> taskMap = new HashMap<String, Object>();
                Long status = statusMap.get(location.getLocationId());
                if(status.compareTo(StockTakingConstant.Done)>0){
                    continue;
                }
                if (!status.equals(StockTakingConstant.PendingAudit)) {
                    isDoing = true;
                }
                taskMap.put("taskId", taskEntry.getTaskInfo().getTaskId());
                taskMap.put("locationId", location.getLocationId());
                taskMap.put("locationCode", location.getLocationCode());
                taskMap.put("status", status);
                taskList.add(taskMap);
            }
        }
        if(isDoing) {
            result.put("taskList", taskList);
        }else {
            result.put("taskList", new ArrayList<Map>());
        }
        return result;
    }


}
