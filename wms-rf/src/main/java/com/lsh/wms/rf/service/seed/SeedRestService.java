package com.lsh.wms.rf.service.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRestService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.seed.ISeedProveiderRpcService;
import com.lsh.wms.api.service.seed.ISeedRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.store.IStoreRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.seed.SeedingTaskHead;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
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
import java.util.*;

/**
 * Created by wuhao on 16/9/28.
 */

@Service(protocol = "rest")
@Path("seed")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SeedRestService implements ISeedRestService {

    private static Logger logger = LoggerFactory.getLogger(SeedRestService.class);

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Reference
    private ISeedProveiderRpcService rpcService;

    @Reference
    private IStockQuantRpcService quantRpcService;

    @Autowired
    private StockLotService lotService;

    @Autowired
    private CsiSkuService csiSkuService;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @Reference
    private IStoreRpcService storeRpcService;

    @Autowired
    private BaseTaskService baseTaskService;

    @Reference
    private ILocationRpcService locationRpcService;

    @Reference
    private ISeedProveiderRpcService seedProveiderRpcService;

    @Reference
    private IReceiptRpcService receiptRpcService;

    @Autowired
    private StockQuantService quantService;
    @Autowired
    private StaffService staffService;

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    ContainerService containerService;

    @Autowired
    ItemService itemService;

    @Autowired
    private RedisStringDao redisStringDao;

    @Autowired
    SoOrderService soOrderService;

    @POST
    @Path("assign")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String assign() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Map<String,Object> result =  new HashMap<String, Object>();
        try {
            Long uid = Long.valueOf(RequestUtils.getHeader("uid"));
            Object containerId = mapQuery.get("containerId");
            Object barcode = mapQuery.get("barcode");
            //实际是orderOtherId
            Object orderId = mapQuery.get("orderId");

            Long assignTaskId = baseTaskService.getAssignTaskIdByOperatorAndType(uid, TaskConstant.TYPE_SEED);
            if(assignTaskId!=null){
                TaskEntry entry = iTaskRpcService.getTaskEntryById(assignTaskId);
                SeedingTaskHead head = (SeedingTaskHead) (entry.getTaskHead());
                TaskInfo info = entry.getTaskInfo();
                result.put("storeName", storeRpcService.getStoreByStoreNo(head.getStoreNo()).getStoreName());
                result.put("qty", head.getRequireQty());
                result.put("taskId", assignTaskId.toString());
                result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
                result.put("packName", "EA");
                result.put("itemId", info.getItemId());
                return JsonUtils.SUCCESS(result);
            }
            if((containerId==null && barcode!=null && orderId!=null) ||(containerId!=null)) {
                Long taskId = 0L;
                if(containerId ==null) {
                    IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(orderId.toString().trim());
                    if(ibdHeader == null) {
                        throw new BizCheckedException("2020001");
                    }
                    mapQuery.put("orderId",ibdHeader.getOrderId());
                    seedProveiderRpcService.createTask(mapQuery);
                    taskId = rpcService.getTask(mapQuery);
                }else {
                    StockQuantCondition condition = new StockQuantCondition();
                    condition.setContainerId(Long.valueOf(containerId.toString().trim()));
                    List<StockQuant> quants = quantRpcService.getQuantList(condition);
                    if(quants==null || quants.size()==0){
                        throw new BizCheckedException("2880003");
                    }
                    StockQuant quant = quants.get(0);
                    StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
                    Map<String,Object> query = new HashMap<String, Object>();
                    query.put("orderId",lot.getPoId());
                    query.put("barcode",csiSkuService.getSku(quant.getSkuId()).getCode());
                    query.put("containerId",containerId);
                    taskId = rpcService.getTask(query);
                }
                if (taskId.compareTo(0L) == 0) {
                    throw new BizCheckedException("2880002");
                } else {
                    TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
                    SeedingTaskHead head = (SeedingTaskHead) (entry.getTaskHead());
                    TaskInfo info = entry.getTaskInfo();
                    result.put("storeName", storeRpcService.getStoreByStoreNo(head.getStoreNo()).getStoreName());
                    result.put("qty", head.getRequireQty());
                    result.put("taskId", taskId.toString());
                    result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
                    result.put("packName", "EA");
                    result.put("itemId", info.getItemId());
                    iTaskRpcService.assign(taskId, uid);
                    return JsonUtils.SUCCESS(result);
                }
            }else {
                return JsonUtils.TOKEN_ERROR("传递参数格式错误");
            }
        }catch (BizCheckedException ex){
            throw ex;
        }
//      catch (Exception e) {
//            logger.error(e.getMessage());
//            return JsonUtils.TOKEN_ERROR("系统繁忙");
//        }
    }
    @POST
    @Path("scanContainer")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanContainer() throws BizCheckedException {
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Map<String,Object> result =  new HashMap<String, Object>();
        Long uid = Long.valueOf(RequestUtils.getHeader("uid"));
        try {
            BigDecimal qty = BigDecimal.ZERO;
            Long taskId = 0L;
            Long containerId = 0L;
            Long type = 0L;
            try{
                qty = new BigDecimal(mapQuery.get("qty").toString().trim());
                taskId = Long.valueOf(mapQuery.get("taskId").toString().trim());
                containerId = Long.valueOf(mapQuery.get("containerId").toString().trim());
                type = Long.valueOf(mapQuery.get("type").toString().trim());
            }catch (Exception e){
                return JsonUtils.TOKEN_ERROR("数据格式有误");
            }
            TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
            if(entry==null){
                return JsonUtils.TOKEN_ERROR("播种任务不存在");
            }

            //判断是否已集货
            Map<String,Object> query = new HashMap<String, Object>();
            query.put("containerId",containerId);
            query.put("type",TaskConstant.TYPE_SET_GOODS);
            List<TaskInfo> infos = baseTaskService.getTaskInfoList(query);
            if(infos!=null && infos.size()!=0){
                for(TaskInfo info: infos){
                    if(info.getStep()==2){
                        return JsonUtils.TOKEN_ERROR("该托盘已集货，不能播种到该托盘上");
                    }
                }
            }

            SeedingTaskHead head = (SeedingTaskHead)(entry.getTaskHead());
            //判断输入门店，判断是不是系统提供的门店
            List<Long> quants = quantService.getLocationIdByContainerId(containerId);
            if(quants != null && quants.size()!=0){
                BaseinfoLocation location = locationRpcService.getLocation(quants.get(0));
                Long storeNo = location.getStoreNo();
                if(storeNo.compareTo(head.getStoreNo())!=0){
                    throw new BizCheckedException("2880006");
                }
            }

            TaskInfo info = entry.getTaskInfo();
            if(head.getRequireQty().compareTo(qty)<0) {
                return JsonUtils.TOKEN_ERROR("播种数量超出门店订单数量");
            }
            // type 2:继续播，1:剩余门店不播了 ,3:跳过当前任务，播下一个任务
            if(containerId != null){
                BaseinfoContainer container = containerService.getContainer(Long.valueOf(containerId.toString().trim()));
                if(container==null){
                    throw new BizCheckedException("2880013");
                }
            }
            head.setRealContainerId(containerId);
            info.setQty(qty);
            entry.setTaskInfo(info);
            entry.setTaskHead(head);
            iTaskRpcService.update(TaskConstant.TYPE_SEED,entry);
            iTaskRpcService.done(taskId);
            if(type.compareTo(2L)==0){
               if(qty.compareTo(head.getRequireQty())!=0) {
                   //创建剩余数量门店任务

                   info.setTaskId(0L);
                   info.setId(0L);
                   info.setStatus(TaskConstant.Draft);
                   info.setPlanId(uid);
                   info.setContainerId(0L);
                   head.setRequireQty(head.getRequireQty().subtract(info.getQty()));
                   head.getRequireQty().subtract(info.getQty());
                   entry.setTaskInfo(info);
                   entry.setTaskHead(head);
                   iTaskRpcService.create(TaskConstant.TYPE_SEED, entry);
                   if(info.getSubType().compareTo(1L)==0) {
                       return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                           {
                               put("response", true);
                           }
                       });
                   }
               }

            }else if(type.compareTo(1L)==0){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("orderId", info.getOrderId());
                map.put("status",TaskConstant.Draft);
                map.put("type",TaskConstant.TYPE_SEED);
                infos = baseTaskService.getTaskInfoList(map);
                if(infos!=null && infos.size()!=0) {
                    List<Long> taskList = new ArrayList<Long>();
                    for (TaskInfo taskInfo : infos) {
                        taskList.add(taskInfo.getTaskId());
                    }
                    iTaskRpcService.batchCancel(TaskConstant.TYPE_SEED, taskList);
                }
                return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                    {
                        put("response", true);
                    }
                });
            }
            mapQuery.put("orderId", info.getOrderId());
            CsiSku sku = csiSkuService.getSku(info.getSkuId());
            mapQuery.put("barcode", sku.getCode());
            mapQuery.put("containerId", info.getContainerId());
            taskId = rpcService.getTask(mapQuery);

            if(taskId.compareTo(0L)!=0){
                entry = iTaskRpcService.getTaskEntryById(taskId);
                head = (SeedingTaskHead) (entry.getTaskHead());
                info = entry.getTaskInfo();
                result.put("storeName", storeRpcService.getStoreByStoreNo(head.getStoreNo()).getStoreName());
                result.put("qty", head.getRequireQty());
                result.put("taskId", taskId.toString());
                result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
                result.put("packName", "EA");
                result.put("itemId", info.getItemId());
                iTaskRpcService.assign(taskId, uid);
                return JsonUtils.SUCCESS(result);
            }
        }catch (BizCheckedException ex) {
            throw ex;
        }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return JsonUtils.TOKEN_ERROR("系统繁忙");
//        }
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }
    /**
     * 回溯任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("restore")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String restore() throws BizCheckedException {
        Long uId=0L;
        Long taskId = 0L;
        Map<String,Object> result = new HashMap<String, Object>();
        try {
            uId =  Long.valueOf(RequestUtils.getHeader("uid"));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        SysUser user =  iSysUserRpcService.getSysUserById(uId);
        if(user==null){
            return JsonUtils.TOKEN_ERROR("用户不存在");
        }
        // 检查是否有已分配的任务
        taskId = baseTaskService.getAssignTaskIdByOperatorAndType(uId, TaskConstant.TYPE_SEED);
        if(taskId==null) {
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", false);
                }
            });
        }
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        SeedingTaskHead head = (SeedingTaskHead) entry.getTaskHead();
        result.put("storeName", storeRpcService.getStoreByStoreNo(head.getStoreNo()).getStoreName());
        result.put("qty", head.getRequireQty());
        result.put("taskId", taskId.toString());
        result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
        result.put("packName", "EA");
        result.put("itemId", info.getItemId());
        return JsonUtils.SUCCESS(result);
    }
}
