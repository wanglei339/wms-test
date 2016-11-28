package com.lsh.wms.rf.service.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.seed.ISeedProveiderRpcService;
import com.lsh.wms.api.service.seed.ISeedRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.system.IExceptionCodeRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.seed.SeedTaskHeadService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.seed.SeedingTaskHead;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CsiCustomerService csiCustomerService;

    @Reference
    private ISysUserRpcService iSysUserRpcService;

    @Reference
    private ICsiRpcService csiRpcService;

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

    @Autowired
    SeedTaskHeadService seedTaskHeadService;
    @Reference
    IExceptionCodeRpcService iExceptionCodeRpcService;


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
            logger.info("params:"+mapQuery);

            Long assignTaskId = baseTaskService.getAssignTaskIdByOperatorAndType(uid, TaskConstant.TYPE_SEED);
            if(assignTaskId!=null){
                TaskEntry entry = iTaskRpcService.getTaskEntryById(assignTaskId);
                SeedingTaskHead head = (SeedingTaskHead) (entry.getTaskHead());
                TaskInfo info = entry.getTaskInfo();
                result.put("taskId", assignTaskId.toString());
                if(mapQuery.get("type")!=null &&mapQuery.get("type").toString().equals("1")) {
                    info.setIsShow(0);
                    baseTaskService.update(info);
                    return JsonUtils.SUCCESS(result);
                }else {
                    info.setIsShow(1);
                    baseTaskService.update(info);
                }
                result.put("storeName", csiRpcService.getCustomerByCode(info.getOwnerId(), head.getStoreNo()).getCustomerName());
                result.put("qty", head.getRequireQty());
                result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
                result.put("packName", info.getPackName());
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
                    BaseinfoLocation location = locationRpcService.getLocation(quant.getLocationId());
                    if(location.getType().compareTo(LocationConstant.TEMPORARY)!=0){
                        return JsonUtils.TOKEN_ERROR("该托盘不在暂存区，不能播种");
                    }
                    if(location.getRegionType().compareTo(LocationConstant.SOW_AREA)==0||location.getType().compareTo(LocationConstant.SOW_AREA)==0){
                        return JsonUtils.TOKEN_ERROR("该托盘在播种区，不能播种");
                    }
                    StockLot lot = lotService.getStockLotByLotId(quant.getLotId());
                    Map<String,Object> query = new HashMap<String, Object>();
                    query.put("orderId",lot.getPoId());
                    query.put("barcode",csiSkuService.getSku(quant.getSkuId()).getCode());
                    query.put("containerId",containerId);
                    query.put("type",mapQuery.get("type"));
                    taskId = rpcService.getTask(query);
                }
                if (taskId.compareTo(0L) == 0) {
                    throw new BizCheckedException("2880002");
                } else {
                    TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
                    SeedingTaskHead head = (SeedingTaskHead) (entry.getTaskHead());
                    TaskInfo info = entry.getTaskInfo();
                    iTaskRpcService.assign(taskId, uid);


                    result.put("taskId", taskId.toString());
                    if(info.getIsShow()==0){
                        return JsonUtils.SUCCESS(result);
                    }
                    result.put("storeName", csiRpcService.getCustomerByCode(info.getOwnerId(), head.getStoreNo()).getCustomerName());
                    result.put("qty", head.getRequireQty());
                    result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
                    result.put("packName", info.getPackName());
                    result.put("itemId", info.getItemId());
                    result.put("storeNo", head.getStoreNo());
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
        logger.info("params:"+ JSON.toJSONString(mapQuery));
        try {
            BigDecimal qty = BigDecimal.ZERO;
            Long taskId = 0L;
            Long containerId = 0L;
            Long type = 0L;
            Boolean is_use_code = false;
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


            TaskInfo info = entry.getTaskInfo();
            //判断商品类型，如例外代码校验通过，则不校验
              //校验例外代码
            if(mapQuery.get("exceptionCode")!=null) {
                String exceptionCode = iExceptionCodeRpcService.getExceptionCodeByName("seed");
                if(!exceptionCode.equals(mapQuery.get("exceptionCode").toString())){
                    return JsonUtils.TOKEN_ERROR("所输例外代码非法");
                }
                is_use_code = true;
            }else {
                //校验商品类型
                List<StockQuant> stockQuants = quantService.getQuantsByContainerId(containerId);
                if(stockQuants!=null && stockQuants.size()!=0){
                    StockQuant quant = stockQuants.get(0);
                    BaseinfoItem oldItem = itemService.getItem(quant.getItemId());
                    BaseinfoItem newItem = itemService.getItem(info.getItemId());
                    if(oldItem.getItemType().compareTo(newItem.getItemType())!=0){
                        return JsonUtils.TOKEN_ERROR("商品类型不一样，不能播种到同一托盘");
                    }
                }
            }


            //判断是否已集货
            Map<String,Object> query = new HashMap<String, Object>();
            query.put("containerId",containerId);
            query.put("type",TaskConstant.TYPE_SET_GOODS);
            List<TaskInfo> infos = baseTaskService.getTaskInfoList(query);
            if(infos!=null && infos.size()!=0){
                for(TaskInfo taskInfo: infos){
                    if(taskInfo.getStep()==2){
                        return JsonUtils.TOKEN_ERROR("该托盘已集货，不能播种到该托盘上");
                    }
                }
            }


            //手动播种
            if(info.getIsShow()==0 ){
                String storeNo =  mapQuery.get("storeNo").toString();
                List<SeedingTaskHead> heads = seedTaskHeadService.getHeadByOrderIdAndStoreNo(info.getOrderId(),storeNo);
                if(heads==null){
                    return JsonUtils.TOKEN_ERROR("该门店不存在播种任务");
                }
                SeedingTaskHead head = null;
                for(SeedingTaskHead seedingTaskHead:heads){
                    info = iTaskRpcService.getTaskInfo(seedingTaskHead.getTaskId());
                    if(info.getStatus().compareTo(TaskConstant.Draft)!=0 && info.getStatus().compareTo(TaskConstant.Assigned)!=0){
                        continue;
                    }
                    head = seedingTaskHead;
                }
                if(head==null){
                    return JsonUtils.TOKEN_ERROR("该门店已播种完成");
                }

                if(head.getRequireQty().compareTo(qty)<0) {
                    return JsonUtils.TOKEN_ERROR("播种数量超出门店订单数量");
                }

                //判断输入门店，判断是不是系统提供的门店
                List<Long> quants = quantService.getLocationIdByContainerId(containerId);
                if(quants != null && quants.size()!=0){
                    BaseinfoLocation location = locationRpcService.getLocation(quants.get(0));
                    CsiCustomer csiCustomer = csiCustomerService.getCustomerByseedRoadId(location.getLocationId());
                    if(!csiCustomer.getCustomerCode().equals(head.getStoreNo())){
                        throw new BizCheckedException("2880006");
                    }
                }

                info = iTaskRpcService.getTaskInfo(head.getTaskId());

                //(不收货播种)判断是否已经结束收货
                if(info.getSubType().compareTo(2L)==0){
                    IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(info.getOrderId());
                    if(ibdHeader.getOrderStatus().compareTo(PoConstant.ORDER_RECTIPT_ALL)==0){

                        return JsonUtils.TOKEN_ERROR("该po单已结束收货");
                    }
                }

                info.setQty(qty);
                head.setRealContainerId(containerId);
                head.setIsUseExceptionCode(is_use_code==true?1:0);
                entry.setTaskInfo(info);
                entry.setTaskHead(head);
                iTaskRpcService.update(TaskConstant.TYPE_SEED, entry);

                if(head.getRequireQty().compareTo(qty)>0){
                    //创建剩余数量门店任务
                    info.setTaskId(0L);
                    info.setId(0L);
                    info.setStatus(TaskConstant.Draft);
                    info.setPlanId(uid);
                    info.setContainerId(info.getContainerId());
                    head.setRequireQty(head.getRequireQty().subtract(info.getQty()));
                    entry.setTaskInfo(info);
                    entry.setTaskHead(head);
                    iTaskRpcService.create(TaskConstant.TYPE_SEED, entry);
                }
                iTaskRpcService.done(taskId);
                if(head.getTaskId().compareTo(taskId)==0) {
                    mapQuery.put("type",1);
                    mapQuery.put("orderId", info.getOrderId());
                    CsiSku sku = csiSkuService.getSku(info.getSkuId());
                    mapQuery.put("barcode", sku.getCode());
                    mapQuery.put("containerId", info.getContainerId());
                    taskId = rpcService.getTask(mapQuery);
                    if (taskId == null || taskId == 0L) {
                        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                            {
                                put("response", true);
                            }
                        });
                    }
                }
                final String finalTaskId = taskId.toString();
                return JsonUtils.SUCCESS(new HashMap<String, String>() {
                    {
                        put("taskId", finalTaskId);
                    }
                });

            }

            SeedingTaskHead head = (SeedingTaskHead)(entry.getTaskHead());
            //判断输入门店，判断是不是系统提供的门店
            List<Long> quants = quantService.getLocationIdByContainerId(containerId);
            if(quants != null && quants.size()!=0){
                BaseinfoLocation location = locationRpcService.getLocation(quants.get(0));
                CsiCustomer csiCustomer = csiCustomerService.getCustomerByseedRoadId(location.getLocationId());
                if(!csiCustomer.getCustomerCode().equals(head.getStoreNo())){
                    throw new BizCheckedException("2880006");
                }
            }


            //(不收货播种)判断是否已经结束收货
            if(info.getSubType().compareTo(2L)==0){
                IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(info.getOrderId());
                if(ibdHeader.getOrderStatus().compareTo(PoConstant.ORDER_RECTIPT_ALL)==0){

                    return JsonUtils.TOKEN_ERROR("该po单已结束收货");
                }
            }


            if(head.getRequireQty().compareTo(qty)<0) {
                return JsonUtils.TOKEN_ERROR("播种数量超出门店订单数量");
            }
            // type 2:继续播，1:剩余门店不播了 ,3:跳过当前任务，播下一个任务
            if(containerId != null){
                BaseinfoContainer container = containerService.getContainer(containerId);
                if(container==null){
                    throw new BizCheckedException("2880013");
                }
            }
            if(TaskConstant.Done.compareTo(info.getStatus())==0){
                return JsonUtils.TOKEN_ERROR("该门店已播种完成");
            }
            head.setRealContainerId(containerId);
            head.setIsUseExceptionCode(is_use_code==true?1:0);
            info.setQty(qty);
            entry.setTaskInfo(info);
            entry.setTaskHead(head);
            iTaskRpcService.update(TaskConstant.TYPE_SEED, entry);
            iTaskRpcService.done(taskId);
            if(type.compareTo(2L)==0){
               if(qty.compareTo(head.getRequireQty())!=0) {
                   //创建剩余数量门店任务

                   info.setTaskId(0L);
                   info.setId(0L);
                   info.setStatus(TaskConstant.Draft);
                   info.setPlanId(uid);
                   info.setContainerId(info.getContainerId());
                   head.setRequireQty(head.getRequireQty().subtract(info.getQty()));
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
                return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                    {
                        put("response", true);
                    }
                });
            }
            if(type.compareTo(3L)==0){
                info.setStep(1);
                baseTaskService.update(info);
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
                result.put("storeName", csiRpcService.getCustomerByCode(info.getOwnerId(), head.getStoreNo()).getCustomerName());
                result.put("qty", head.getRequireQty());
                result.put("taskId", taskId.toString());
                result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
                result.put("packName", info.getPackName());
                result.put("storeNo", head.getStoreNo());
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
        if(taskId==null ) {
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", false);
                }
            });
        }
        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        if(info.getIsShow()==0){
            return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
                {
                    put("response", false);
                }
            });
        }
        SeedingTaskHead head = (SeedingTaskHead) entry.getTaskHead();
        result.put("storeName", csiRpcService.getCustomerByCode(info.getOwnerId(), head.getStoreNo()).getCustomerName());
        result.put("storeNo", head.getStoreNo());
        result.put("qty", head.getRequireQty());
        result.put("taskId", taskId.toString());
        result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
        result.put("packName", info.getPackName());
        result.put("itemId", info.getItemId());
        return JsonUtils.SUCCESS(result);
    }
    /**
     * 回溯任务
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("view")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String view() throws BizCheckedException {
        Long taskId = 0L;
        String storeNo = "";
        Map<String, Object> mapQuery = RequestUtils.getRequest();
        Map<String,Object> result = new HashMap<String, Object>();
        try {
            storeNo =  mapQuery.get("storeNo").toString();
            taskId = Long.valueOf(mapQuery.get("taskId").toString());
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }

        TaskEntry entry = iTaskRpcService.getTaskEntryById(taskId);
        if(entry==null){
            return JsonUtils.TOKEN_ERROR("播种任务不存在");
        }
        List<SeedingTaskHead> heads = seedTaskHeadService.getHeadByOrderIdAndStoreNo(entry.getTaskInfo().getOrderId(), storeNo);
        if(heads==null || heads.size()==0){
            return JsonUtils.TOKEN_ERROR("该门店不存在播种任务");
        }
        SeedingTaskHead head = heads.get(0);
        TaskInfo info = iTaskRpcService.getTaskInfo(head.getTaskId());
        result.put("storeName", csiRpcService.getCustomerByCode(info.getOwnerId(),head.getStoreNo()).getCustomerName());
        result.put("storeNo", head.getStoreNo());
        result.put("qty", head.getRequireQty());
        result.put("taskId", taskId.toString());
        result.put("skuName", csiSkuService.getSku(info.getSkuId()).getSkuName());
        result.put("packName", info.getPackName());
        result.put("itemId", info.getItemId());
        return JsonUtils.SUCCESS(result);
    }
}
