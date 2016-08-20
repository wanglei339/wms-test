package com.lsh.wms.service.inbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.shelve.IAtticShelveRestService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationBinService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.shelve.AtticShelveTaskDetailService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.shelve.AtticShelveTaskDetail;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
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
 * Created by wuhao on 16/8/16.
 */
@Service(protocol = "rest")
@Path("inbound/attic_shelve")
public class AtticShelveRestService implements IAtticShelveRestService{
    private static Logger logger = LoggerFactory.getLogger(AtticShelveRestService.class);
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Reference
    private IProcurementRpcService rpcService;
    @Autowired
    private BaseinfoLocationBinService locationBinService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private AtticShelveTaskDetailService shelveTaskService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockLotService lotService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ContainerService containerService;
    @Reference
    private ISysUserRpcService iSysUserRpcService;

    private Long taskType = TaskConstant.TYPE_ATTIC_SHELVE;

    /**
     * 创建上架任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createTask")
    public String createTask() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long containerId = 0L;
        try {
            containerId = Long.valueOf(mapQuery.get("containerId").toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        // 检查容器信息
        if (containerId == null || containerId.equals("")) {
            throw new BizCheckedException("2030003");
        }
        // 检查该容器是否已创建过任务
        if (baseTaskService.checkTaskByContainerId(containerId)) {
            throw new BizCheckedException("2030008");
        }
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(containerId);
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        TaskInfo taskInfo = new TaskInfo();
        TaskEntry entry = new TaskEntry();

        ObjUtils.bean2bean(quant, taskInfo);

        taskInfo.setType(taskType);
        taskInfo.setFromLocationId(quant.getLocationId());

        entry.setTaskInfo(taskInfo);

        iTaskRpcService.create(taskType, entry);


        return JsonUtils.SUCCESS();
    }

    /**
     * 创建上架详情
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("createDetail")
    public String createDetail() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = 0L;
        Long operator = 0L;
        List <AtticShelveTaskDetail> details =null;
        try {
            taskId = Long.valueOf(mapQuery.get("taskId").toString());
            details = (List)mapQuery.get("detailList");
            operator = Long.valueOf(mapQuery.get("operator").toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry ==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }
        TaskInfo info = entry.getTaskInfo();
        info.setStatus(TaskConstant.Assigned);
        info.setExt1(1L); //pc创建任务详情标示
        info.setOperator(operator);
        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(info.getContainerId());
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);
        StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
        for(AtticShelveTaskDetail detail:details) {
            ObjUtils.bean2bean(quant, detail);
            detail.setTaskId(taskId);
            detail.setReceiptId(lot.getReceiptId());
            detail.setOrderId(lot.getPoId());
            detail.setOperator(operator);
            if(detail.getId().compareTo(0L)==0){
                shelveTaskService.create(detail);
            }else {
                shelveTaskService.updateDetail(detail);
            }
        }
        return JsonUtils.SUCCESS();
    }
    /**
     * 确认上架详情
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("confimDetail")
    public String conFirmDetail() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = 0L;
        List <AtticShelveTaskDetail> details = new ArrayList<AtticShelveTaskDetail>();
        try {
            taskId = Long.valueOf(mapQuery.get("taskId").toString());
            details = (List)mapQuery.get("detailList");
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry ==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }


        // 获取quant
        List<StockQuant> quants = stockQuantService.getQuantsByContainerId(entry.getTaskInfo().getContainerId());
        if (quants.size() < 1) {
            throw new BizCheckedException("2030001");
        }
        StockQuant quant = quants.get(0);

        for(AtticShelveTaskDetail detail:details){
            this.doneDetail(detail, quant);
        }
        iTaskRpcService.done(taskId);

        return JsonUtils.SUCCESS();
    }
    /**
     * 获取上架任务列表
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getTaskList")
    public String getTaskList() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        List<Map> resultList = new ArrayList<Map>();
        List<TaskEntry> entries = iTaskRpcService.getTaskList(TaskConstant.TYPE_ATTIC_SHELVE, mapQuery);
        for(TaskEntry entry :entries){
            Long canEdit=0L;
            Map<String,Object> one =  new HashMap<String, Object>();
            TaskInfo info = entry.getTaskInfo();
            if(info.getStatus().compareTo(TaskConstant.Draft)==0 || (info.getStatus().compareTo(TaskConstant.Assigned)==0 && info.getExt1()==1)){
                canEdit = 1L;
            }
            one.put("status",info.getStatus());
            one.put("canEdit",canEdit);
            one.put("operator",info.getOperator());
            one.put("taskId", info.getTaskId());
            one.put("containerId",info.getContainerId());


            List<Object> details = entry.getTaskDetailList();
            if(details ==null || details.size()==0) {
                // 获取quant
                List<StockQuant> quants = stockQuantService.getQuantsByContainerId(entry.getTaskInfo().getContainerId());
                if (quants.size() < 1) {
                    throw new BizCheckedException("2030001");
                }
                StockQuant quant = quants.get(0);
                StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
                one.put("orderId", lot.getPoId());
                one.put("packName",quant.getPackName());
                one.put("qty",stockQuantService.getQuantQtyByLocationIdAndItemId(quant.getLocationId(), quant.getItemId()).divide(quant.getPackUnit(), BigDecimal.ROUND_HALF_EVEN));
                one.put("supplierId",quant.getSupplierId());
                one.put("ownerId",quant.getOwnerId());
                one.put("finishTime",info.getFinishTime());
                resultList.add(one);
            }else {
                AtticShelveTaskDetail detail = (AtticShelveTaskDetail)(details.get(0));
                one.put("orderId", detail.getOrderId());
                one.put("packName",info.getPackName());
                one.put("qty",stockQuantService.getQuantQtyByLocationIdAndItemId(info.getLocationId(), info.getItemId()).divide(info.getPackUnit(), BigDecimal.ROUND_HALF_EVEN));
                one.put("supplierId",detail.getSupplierId());
                one.put("ownerId",detail.getOwnerId());
                one.put("finishTime",info.getFinishTime());
                resultList.add(one);
            }
        }
        return JsonUtils.SUCCESS(resultList);
    }
    /**
     * 获得上架详情
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getDetail")
    public String getDetail() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Long taskId = 0L;
        Map<String,Object>  head = new HashMap<String, Object>();
        Map<String,Object>  result = new HashMap<String, Object>();
        try {
            taskId = Long.valueOf(mapQuery.get("taskId").toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry ==null){
            return JsonUtils.TOKEN_ERROR("任务不存在");
        }
        List<Object> details = entry.getTaskDetailList();
        TaskInfo info = entry.getTaskInfo();
        if(details ==null || details.size()==0){
            // 获取quant
            List<StockQuant> quants = stockQuantService.getQuantsByContainerId(info.getContainerId());
            if (quants.size() < 1) {
                throw new BizCheckedException("2030001");
            }
            StockQuant quant = quants.get(0);
            StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
            head.put("containerId",quant.getContainerId());
            head.put("orderId",lot.getPoId());
            head.put("supplierId",quant.getSupplierId());
            head.put("ownerId",quant.getOwnerId());
            head.put("status",info.getStatus());
            head.put("packName",quant.getPackName());
            head.put("qty",stockQuantService.getQuantQtyByLocationIdAndItemId(quant.getLocationId(), quant.getItemId()).divide(quant.getPackUnit(), BigDecimal.ROUND_HALF_EVEN));
            head.put("operator",info.getOperator());
        }else {
            AtticShelveTaskDetail detail = (AtticShelveTaskDetail)(details.get(0));
            head.put("containerId",detail.getContainerId());
            head.put("orderId",detail.getOrderId());
            head.put("supplierId",detail.getSupplierId());
            head.put("ownerId",detail.getOwnerId());
            head.put("status",info.getStatus());
            head.put("packName",info.getPackName());
            head.put("qty",stockQuantService.getQuantQtyByLocationIdAndItemId(info.getLocationId(), info.getItemId()).divide(info.getPackUnit(), BigDecimal.ROUND_HALF_EVEN));
            head.put("operator",info.getOperator());
        }
        result.put("head", head);
        if(details==null || details.size()==0){
            details = new ArrayList<Object>();
        }
        result.put("detail", details);

        return JsonUtils.SUCCESS(result);
    }

    private void doneDetail(AtticShelveTaskDetail detail,StockQuant quant) {

        detail.setShelveAt(DateUtils.getCurrentSeconds());
        detail.setStatus(2L);
        if(detail.getRealQty().compareTo(BigDecimal.ZERO)==0){
            detail.setRealQty(detail.getQty());
        }
        if(detail.getRealLocationId().compareTo(0L)==0){
            detail.setRealLocationId(detail.getAllocLocationId());
        }

        //移动库存
        List<StockQuant> pickQuant = stockQuantService.getQuantsByLocationId(detail.getRealLocationId());
        Long containerId = 0L;
        if(pickQuant ==null ||pickQuant.size() ==0){
            containerId = containerService.createContainerByType(1L).getContainerId();
        }else {
            containerId = pickQuant.get(0).getContainerId();
        }
        if(detail.getId().compareTo(0L)==0){
            shelveTaskService.create(detail);
        }
        StockMove move = new StockMove();
        ObjUtils.bean2bean(quant, move);
        move.setFromLocationId(quant.getLocationId());
        move.setToLocationId(detail.getRealLocationId());
        move.setQty(detail.getRealQty().multiply(quant.getPackUnit()));
        move.setFromContainerId(quant.getContainerId());
        move.setToContainerId(containerId);
        stockQuantService.move(move);
        locationService.unlockLocation(detail.getAllocLocationId());
        shelveTaskService.updateDetail(detail);
    }
}
