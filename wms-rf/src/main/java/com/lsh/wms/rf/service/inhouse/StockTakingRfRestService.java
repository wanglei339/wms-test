package com.lsh.wms.rf.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.system.SysUser;
import net.sf.json.JSONObject;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingRfRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
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
    private StockLotService lotService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private CsiSkuService skuService;
    @Autowired
    private StockTakingTaskService stockTakingTaskService;
    @Autowired
    private ItemService itemService;
    @Reference
    private ISysUserRpcService iSysUserRpcService;


    @POST
    @Path("doOne")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String doOne() throws BizCheckedException {
        //Long taskId,int qty,String barcode
        Map request = RequestUtils.getRequest();
        JSONObject object = null;
        Long taskId = 0L;
        List<Map> resultList = null;
        try {
            object = JSONObject.fromObject(request.get("result"));
            taskId = Long.parseLong(object.get("taskId").toString());
            resultList = object.getJSONArray("list");
        }catch (Exception e){
            return JsonUtils.TOKEN_ERROR("JSON解析失败");
        }

        if (!(resultList == null || resultList.size() == 0)) {
            TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            StockTakingTask task = (StockTakingTask) (entry.getTaskHead());
            StockTakingDetail detail = (StockTakingDetail) (entry.getTaskDetailList().get(0));
            BaseinfoItem item = itemService.getItem(detail.getItemId());
            for (Map<String, Object> beanMap : resultList) {
                Object barcode = beanMap.get("barcode");
                BigDecimal realQty = new BigDecimal(beanMap.get("qty").toString());
                if (item.getCode().equals(barcode.toString())) {
                    BigDecimal qty = quantService.getQuantQtyByLocationIdAndItemId(detail.getLocationId(), detail.getItemId());
                    detail.setTheoreticalQty(qty);
                    detail.setRealQty(realQty);
                    detail.setUpdatedAt(DateUtils.getCurrentSeconds());
                    stockTakingService.updateDetail(detail);
                } else {
                    CsiSku csiSku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode.toString());
                    StockTakingDetail newDetail = new StockTakingDetail();
                    newDetail.setRealQty(realQty);
                    newDetail.setTakingId(task.getTakingId());
                    newDetail.setTaskId(taskId);
                    newDetail.setRealSkuId(csiSku.getSkuId());
                    newDetail.setSkuId(csiSku.getSkuId());
                    newDetail.setLocationId(detail.getLocationId());
                    newDetail.setContainerId(detail.getContainerId());
                    newDetail.setRound(detail.getRound());
                    stockTakingService.insertDetail(newDetail);
                }
            }
        }

        iTaskRpcService.done(taskId);
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        StockTakingTask task = (StockTakingTask) (entry.getTaskHead());
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("planId", task.getTakingId());
        queryMap.put("status", 2L);
        List<TaskEntry> entries = iTaskRpcService.getTaskHeadList(TaskConstant.TYPE_STOCK_TAKING, queryMap);
        if (entries == null || entries.size() == 0) {
            this.confirm(task.getTakingId());
        }
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
            Map<String, Object> params = RequestUtils.getRequest();
            uId = Long.valueOf(params.get("uId").toString());
        }catch (Exception e){
            logger.info(e.getMessage());
            return JsonUtils.TOKEN_ERROR("违法的账户");
        }
        SysUser user =  iSysUserRpcService.getSysUserById(uId);
        if(user==null){
            return JsonUtils.TOKEN_ERROR("用户不存在");
        }
        Long staffId = user.getStaffId();
        Map<String,Object> statusQueryMap = new HashMap();
        statusQueryMap.put("status",2);
        statusQueryMap.put("operator", staffId);

        List<TaskEntry> list = iTaskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TAKING, statusQueryMap);
        if(list!=null && list.size()!=0){
            for(TaskEntry taskEntry:list){
                Map<String,Object>task = new HashMap<String,Object>();
                task.put("taskId",taskEntry.getTaskInfo().getTaskId());
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
        queryMap.put("status", 1L);
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
                queryMap.put("status",3L);
                List<TaskEntry> entryList = iTaskRpcService.getTaskList(TaskConstant.TYPE_STOCK_TAKING,queryMap);
                Map<Long,Integer> chageMap = new HashMap<Long, Integer>();
                for(TaskEntry tmp:entryList){
                    chageMap.put(tmp.getTaskInfo().getOperator(),1);
                }
                if(!chageMap.containsKey(staffId)){
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
        List<StockTakingTask> takingTasks =stockTakingTaskService.getTakingTask(queryMap);
        List<Long> taskIdList = new ArrayList<Long>();
        for(StockTakingTask takingtask:takingTasks){
            Map<String,Object> task =new HashMap<String, Object>();
            task.put("taskId",takingtask.getTaskId());
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
        iTaskRpcService.batchAssign(TaskConstant.TYPE_STOCK_TAKING, taskIdList, staffId);
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
            taskId = Long.valueOf(params.get("taskId").toString());
            locationId = Long.valueOf(params.get("locationId").toString());
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
        result.put("taskId",taskId);
        BaseinfoLocation location = locationService.getLocation(detail.getLocationId());
        BaseinfoItem item = itemService.getItem(detail.getItemId());
        result.put("locationCode",location==null ? "":location.getLocationCode());
        result.put("itemName", item == null ? "" : item.getSkuName());
        result.put("qty",qty);
        result.put("packName",packName);
        return JsonUtils.SUCCESS(result);

    }

    public void confirm(Long stockTakingId) throws BizCheckedException {
        // 获取stockingHead
        // 如果是临时盘点, 直接调用confirmDifference
        // 计划盘点,
        //      如果ruund == 1, 发起新一轮盘点
        //      如果round == 2, 获取第一轮,第二轮的差异明细列表, 如果非空, 发起第三轮盘点
        //      如果round == 3, 直接调用confirmDiffence

        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        if (head.getPlanType() == 1) {
            this.confirmDifference(stockTakingId, 1L);
        } else {
            Long times = stockTakingService.chargeTime(stockTakingId);
            if (times == 1) {
                this.createNextDetail(stockTakingId, times);
            } else {
                if (times == 2) {
                    boolean isSame = this.chargeDifference(stockTakingId, times);
                    if (isSame) {
                        this.confirmDifference(stockTakingId, times);
                    } else {
                        this.createNextDetail(stockTakingId, times);
                    }
                } else {
                    this.confirmDifference(stockTakingId, times);
                }
            }
        }
    }


    public void confirmDifference(Long stockTakingId, long roundTime) {
        List<StockTakingDetail> detailList = stockTakingService.getDetailListByRound(stockTakingId, roundTime);
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        head.setStatus(3L);
        stockTakingService.updateHead(head);
        List<StockMove> moveList = new ArrayList<StockMove>();
        for (StockTakingDetail detail : detailList) {
            if(detail.getItemId()==0L){
                continue;
            }
            if (detail.getSkuId().equals(detail.getRealSkuId())) {
                StockMove move = new StockMove();
                move.setTaskId(detail.getTakingId());
                move.setSkuId(detail.getSkuId());
                move.setItemId(detail.getItemId());
                move.setStatus(TaskConstant.Done);
                if (detail.getTheoreticalQty().compareTo(detail.getRealQty()) > 0) {
                    move.setQty(detail.getTheoreticalQty().subtract(detail.getRealQty()));
                    move.setFromLocationId(detail.getLocationId());
                    move.setToLocationId(locationService.getInventoryLostLocationId());
                } else {
                    StockLot lot = lotService.getStockLotByLotId(detail.getLotId());
                    move.setLot(lot);
                    move.setQty(detail.getRealQty().subtract(detail.getTheoreticalQty()));
                    move.setFromLocationId(locationService.getInventoryLostLocationId());
                    move.setToLocationId(detail.getLocationId());
                    move.setToContainerId(detail.getContainerId());
                }
                moveList.add(move);
            } else {
                StockMove moveWin = new StockMove();
                moveWin.setTaskId(detail.getTakingId());
                moveWin.setSkuId(detail.getSkuId());
                moveWin.setToLocationId(locationService.getInventoryLostLocationId());
                moveWin.setFromLocationId(detail.getLocationId());
                moveWin.setQty(detail.getRealQty());
                moveList.add(moveWin);

                StockMove moveLoss = new StockMove();
                moveLoss.setTaskId(detail.getTakingId());
                moveLoss.setSkuId(detail.getSkuId());
                moveLoss.setFromLocationId(locationService.getInventoryLostLocationId());
                moveLoss.setToLocationId(detail.getLocationId());
                moveLoss.setQty(detail.getRealQty());
                moveList.add(moveLoss);
            }
        }
        moveService.move(moveList);
    }

    private boolean chargeDifference(Long stockTakingId, Long round) {
        List<StockTakingDetail> oldDetails =stockTakingService.getDetailListByRound(stockTakingId, round - 1);
        List<StockTakingDetail> details = stockTakingService.getDetailListByRound(stockTakingId, round);
        Map<String,BigDecimal> compareMap = new HashMap<String, BigDecimal>();
        for(StockTakingDetail detail:oldDetails){
            String key = "l:"+detail.getLocationId()+"i:"+detail.getItemId();
            compareMap.put(key,detail.getRealQty().subtract(detail.getTheoreticalQty()));
        }
        for (StockTakingDetail detail : details) {
            String key = "l:"+detail.getLocationId()+"i:"+detail.getItemId();
            BigDecimal differQty = detail.getRealQty().subtract(detail.getTheoreticalQty());
            if((!compareMap.containsKey(key)) || compareMap.get(key).compareTo(differQty)!=0) {
                return false;
            }
        }
        return true;
    }
    public void createNextDetail(Long stockTakingId,Long roundTime) throws BizCheckedException{
        Map<String,Object> queryMap = new HashMap();
        StockTakingHead head = stockTakingService.getHeadById(stockTakingId);
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        queryMap.put("stockTakingId",stockTakingId);
        queryMap.put("round",roundTime);
        queryMap.put("isValid",1);
        List<StockTakingDetail> details=stockTakingService.getDetailListByRound(stockTakingId,roundTime);
        for (StockTakingDetail stockTakingDetail:details){
            //判断是否是盘点时多出来的商品。。。如果是，则不生成下次盘点的detail
            if(stockTakingDetail.getItemId()==0){
                continue;
            }

            stockTakingDetail.setId(0L);
            BigDecimal qty=quantService.getQuantQtyByLocationIdAndItemId(stockTakingDetail.getLocationId(), stockTakingDetail.getItemId());
            stockTakingDetail.setTheoreticalQty(qty);
            stockTakingDetail.setRound(roundTime + 1);
            detailList.add(stockTakingDetail);
        }
        stockTakingService.insertDetailList(detailList);
        this.createTask(head, detailList, roundTime + 1, head.getDueTime());
    }
    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) throws BizCheckedException{
        List<TaskEntry> taskEntryList=new ArrayList<TaskEntry>();
        for(StockTakingDetail detail:detailList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setPlanId(head.getTakingId());
            taskInfo.setDueTime(dueTime);
            taskInfo.setPlanner(head.getPlanner());
            taskInfo.setStatus(1L);
            taskInfo.setLocationId(detail.getLocationId());
            taskInfo.setSkuId(detail.getSkuId());
            taskInfo.setContainerId(detail.getContainerId());
            taskInfo.setType(TaskConstant.TYPE_STOCK_TAKING);
            taskInfo.setDraftTime(DateUtils.getCurrentSeconds());

            StockTakingTask task = new StockTakingTask();
            task.setRound(round);
            task.setTakingId(head.getTakingId());

            TaskEntry taskEntry = new TaskEntry();
            taskEntry.setTaskInfo(taskInfo);
            List details = new ArrayList();
            details.add(detail);
            taskEntry.setTaskDetailList(details);

            StockTakingTask taskHead = new StockTakingTask();
            taskHead.setRound(round);
            taskHead.setTakingId(taskInfo.getPlanId());
            taskEntry.setTaskHead(taskHead);
            taskEntryList.add(taskEntry);
            iTaskRpcService.create(TaskConstant.TYPE_STOCK_TAKING, taskEntry);
        }
    }

}
