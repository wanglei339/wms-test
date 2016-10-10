package com.lsh.wms.task.service.task.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.seed.SeedTaskHeadService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.seed.SeedingTaskHead;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/2.
 */
@Component
public class SeedTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    StockLotService lotService;
    @Autowired
    SeedTaskHeadService seedTaskHeadService;
    @Reference
    ITaskRpcService taskRpcService;
    @Autowired
    SeedTaskHeadService headService;
    @Reference
    private IStockQuantRpcService quantRpcService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private ContainerService containerService;
    @Reference
    private ILocationRpcService locationRpcService;
    @Autowired
    private RedisStringDao redisStringDao;
    @Autowired
    PoOrderService poOrderService;
    @Autowired
    BaseTaskService baseTaskService;
    @Autowired
    ItemService itemService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    LocationService locationService;

    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SEED, this);
    }

    public void calcPerformance(TaskInfo taskInfo) {

        taskInfo.setTaskPackQty(taskInfo.getTaskQty().divide(taskInfo.getPackUnit(),0,BigDecimal.ROUND_DOWN));
        taskInfo.setTaskEaQty(taskInfo.getQty());


    }
    public void create(Long taskId) {
        Long containerId = baseTaskService.getTaskInfoById(taskId).getContainerId();
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        List<StockQuant> quants = quantService.getQuantsByContainerId(containerId);
        if(quants == null || quants.size()==0){
            throw new BizCheckedException("2880003");
        }
        //创建收货播种任务，subType为1
        Long subType = 1L;
        StockQuant quant = quants.get(0);
        StockLot lot = lotService.getStockLotByLotId(quant.getLotId());

        Long orderId = lot.getPoId();
        String key = "store_queue";
        String queueObject = redisStringDao.get(key);
        Map<Long,Long> storeMap = null;
        if(queueObject == null){
            List<BaseinfoLocation> storeList = locationRpcService.sortSowLocationByStoreNo();
            if(storeList!=null && storeList.size()!=0) {
                for (int i = 0; i < storeList.size(); i++) {
                    storeMap.put(storeList.get(i).getStoreNo(), Long.valueOf(i));
                }
                JSONObject object = JSONObject.fromObject(storeMap);
                redisStringDao.set(key, object.toString());
            }
        }else {
            JSONObject object = JSONObject.fromObject(queueObject);
            storeMap = (HashMap<Long,Long>)JSONObject.toBean(object, HashMap.class);
        }

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(orderId);
        if(ibdHeader ==null){
            throw new BizCheckedException("2880004");
        }
        mapQuery.put("type", TaskConstant.TYPE_SEED);
        mapQuery.put("containerId",containerId);
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(mapQuery);
        if(infos!=null && infos.size()!=0){
            throw new BizCheckedException("2880005");
        }

        BaseinfoItem item = itemService.getItem(quant.getItemId());
        String orderOtherId = ibdHeader.getOrderOtherId();
        IbdDetail ibdDetail= poOrderService.getInbPoDetailByOrderIdAndSkuCode(orderId, item.getSkuCode());
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ibdOtherId",orderOtherId);
        queryMap.put("ibdDetailId",ibdDetail.getDetailOtherId());

        List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);
        List<TaskEntry> entries = new ArrayList<TaskEntry>();

        for(IbdObdRelation ibdObdRelation :ibdObdRelations){
            String obdOtherId = ibdObdRelation.getObdOtherId();
            String obdDetailId = ibdObdRelation.getObdDetailId();


            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherId(obdOtherId);
            String storeNo = obdHeader.getDeliveryCode();
            Long obdOrderId = obdHeader.getOrderId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("orderId",obdOrderId);
            map.put("detailOtherId",obdDetailId);
            ObdDetail obdDetail = soOrderService.getOutbSoDetailList(map).get(0);

            //拼装任务
            SeedingTaskHead head = new SeedingTaskHead();
            TaskInfo info = new TaskInfo();
            TaskEntry entry = new TaskEntry();
            head.setPackUnit(item.getPackUnit());
            head.setRequireQty(obdDetail.getUnitQty());
            head.setStoreNo(Long.valueOf(storeNo));
            //无收货播种任务标示
            info.setSubType(subType);
            //门店播放规则
            if(storeMap.containsKey(storeNo)) {
                info.setExt1(storeMap.get(storeNo));
            }
            info.setItemId(item.getItemId());
            info.setSkuId(quant.getSkuId());
            info.setOrderId(orderId);
            info.setTaskName("播种任务[ " + storeNo + "]");
            info.setPackUnit(item.getPackUnit());
            info.setType(TaskConstant.TYPE_SEED);
            info.setPackName(item.getPackName());
            entry.setTaskHead(head);
            entry.setTaskInfo(info);
            entries.add(entry);
        }
        taskRpcService.batchCreate(TaskConstant.TYPE_SEED,entries);

    }
    public void createConcrete(TaskEntry taskEntry) {
        SeedingTaskHead head = (SeedingTaskHead) taskEntry.getTaskHead();
        Long taskId=taskEntry.getTaskInfo().getTaskId();
        head.setTaskId(taskId);
        seedTaskHeadService.create(head);
    }
    public void doneConcrete(Long taskId) {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        SeedingTaskHead head = (SeedingTaskHead)entry.getTaskHead();
        //收货播种
        StockMove move = new StockMove();
        if(info.getSubType().compareTo(1L)==0){
            StockQuantCondition condition = new StockQuantCondition();
            condition.setContainerId(info.getContainerId());
            condition.setItemId(info.getItemId());
            List<StockQuant> quants = quantRpcService.getQuantList(condition);
            if(quants==null || quants.size()==0){
                throw new BizCheckedException("2880003");
            }
            StockQuant quant = quants.get(0);
            move.setItemId(quant.getItemId());
            move.setFromContainerId(info.getContainerId());
            move.setFromLocationId(quant.getLocationId());
        }else {
            move.setSkuId(info.getSkuId());
            move.setFromContainerId(info.getContainerId());
            List<BaseinfoLocation> locations = locationService.getLocationsByType(LocationConstant.SUPPLIER_AREA);
            move.setFromLocationId(locations.get(0).getLocationId());
        }
        List<StockQuant> quantList = quantService.getQuantsByContainerId(head.getRealContainerId());
        if(quantList ==null || quantList.size()==0){
            List<BaseinfoLocation> locations = locationService.getCollectionByStoreNo(head.getStoreNo());
            move.setToLocationId(locations.get(0).getLocationId());
        }else {
            move.setToLocationId(quantList.get(0).getLocationId());
        }
        move.setToContainerId(head.getRealContainerId());
        move.setQty(info.getQty());
        quantService.move(move);
    }
    public void updteConcrete(TaskEntry entry) {
        SeedingTaskHead head = (SeedingTaskHead)entry.getTaskHead();
        headService.update(head);
    }
    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(headService.getHeadByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }
}
