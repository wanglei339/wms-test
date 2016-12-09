package com.lsh.wms.task.service.task.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.seed.ISeedRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.seed.SeedTaskHeadService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.staff.StaffService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiCustomer;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
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
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wuhao on 16/10/17.
 */
@Component
public class SeedTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    StockLotService lotService;
    @Autowired
    CsiSupplierService csiSupplierService;
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
    @Autowired
    WaveService waveService;
    @Autowired
    CsiSkuService csiSkuService;
    @Reference
    ISeedRpcService seedRpcService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockMoveService moveService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    CsiCustomerService csiCustomerService;
    @Autowired
    private StockSummaryService stockSummaryService;

    private static Logger logger = LoggerFactory.getLogger(SeedTaskHandler.class);


    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_SEED, this);
    }

    public void calcPerformance(TaskInfo taskInfo) {

        taskInfo.setTaskPackQty(taskInfo.getQty().divide(taskInfo.getPackUnit(),0,BigDecimal.ROUND_DOWN));
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
        Map<String,Long> storeMap = new HashMap<String, Long>();
        List<CsiCustomer> storeList = csiCustomerService.getCustomerList(new HashMap<String, Object>());
        if(storeList!=null && storeList.size()!=0) {
            for (int i = 0; i < storeList.size(); i++) {
                CsiCustomer csiCustomer = storeList.get(i);
                storeMap.put(csiCustomer.getCustomerCode(), csiCustomer.getSeedQueue());
            }
        }

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(orderId);
        if(ibdHeader ==null){
            throw new BizCheckedException("2880004");
        }
        BaseinfoItem item = itemService.getItem(quant.getItemId());
        mapQuery.put("type", TaskConstant.TYPE_SEED);
        mapQuery.put("orderId",orderId);
        mapQuery.put("itemId",item.getItemId());
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(mapQuery);
        if(infos!=null && infos.size()!=0){
            TaskInfo info = infos.get(0);
            mapQuery.put("step",1);
            //查找未播种未够数量，却已完成播种的任务
            infos = baseTaskService.getTaskInfoList(mapQuery);
            for (TaskInfo taskInfo:infos){
                TaskEntry entry = new TaskEntry();
                SeedingTaskHead head = seedTaskHeadService.getHeadByTaskId(taskInfo.getTaskId());
                info.setTaskId(0L);
                info.setId(0L);
                info.setStatus(TaskConstant.Draft);
                info.setPlanId(0L);
                info.setContainerId(0L);
                info.setStep(0);
                head.setRequireQty(head.getRequireQty().subtract(info.getQty()));
                entry.setTaskInfo(info);
                entry.setTaskHead(head);
                iTaskRpcService.create(TaskConstant.TYPE_SEED, entry);
            }
            return;
        }

        String orderOtherId = ibdHeader.getOrderOtherId();
        List<IbdDetail> ibdDetails= poOrderService.getInbPoDetailByOrderAndSkuCode(orderId, item.getSkuCode());
        List<TaskEntry> entries = new ArrayList<TaskEntry>();
        for(IbdDetail ibdDetail:ibdDetails) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("ibdOtherId", orderOtherId);
            queryMap.put("ibdDetailId", ibdDetail.getDetailOtherId());

            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);

            for (IbdObdRelation ibdObdRelation : ibdObdRelations) {
                String obdOtherId = ibdObdRelation.getObdOtherId();
                String obdDetailId = ibdObdRelation.getObdDetailId();

                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherIdAndType(obdOtherId, SoConstant.ORDER_TYPE_DIRECT);
                String storeNo = obdHeader.getDeliveryCode();
                Long obdOrderId = obdHeader.getOrderId();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderId", obdOrderId);
                map.put("detailOtherId", obdDetailId);
                List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailList(map);
                for(ObdDetail obdDetail:obdDetails) {

                    //拼装任务
                    SeedingTaskHead head = new SeedingTaskHead();
                    TaskInfo info = new TaskInfo();
                    TaskEntry entry = new TaskEntry();

                    CsiCustomer csiCustomer = csiCustomerService.getCustomerByCustomerCode(storeNo);
                    if(csiCustomer==null){
                        logger.error("门店:"+storeNo+",未在系统维护");
                        throw new BizCheckedException("2880018",storeNo,"");
                    }
                    head.setPackUnit(obdDetail.getPackUnit());
                    head.setRequireQty(obdDetail.getUnitQty());
                    head.setStoreNo(storeNo);
                    head.setOrderId(orderId);
                    head.setIbdDetailId(ibdDetail.getDetailOtherId());
                    head.setObdDetailId(obdDetail.getDetailOtherId());
                    head.setStoreType(csiCustomer.getCustomerType());
                    head.setSkuId(obdDetail.getSkuId());
                    //无收货播种任务标示
                    info.setSubType(subType);
                    //门店播放规则
                    if (storeMap.containsKey(storeNo)) {
                        info.setTaskOrder(storeMap.get(storeNo));
                    }
                    head.setTaskOrder(info.getTaskOrder());
                    info.setItemId(obdDetail.getItemId());
                    info.setSkuId(quant.getSkuId());
                    info.setOrderId(orderId);
                    info.setIsShow(0);
                    info.setOwnerId(ibdHeader.getOwnerUid());
                    info.setTaskName("播种任务[ " + storeNo + "]");
                    info.setPackUnit(item.getPackUnit());
                    info.setType(TaskConstant.TYPE_SEED);
                    info.setPackName(obdDetail.getPackName());
                    entry.setTaskHead(head);
                    entry.setTaskInfo(info);
                    entries.add(entry);
                }
            }
        }
        taskRpcService.batchCreate(TaskConstant.TYPE_SEED,entries);

    }
    public void getHeadConcrete(TaskEntry entry){
        TaskInfo info = entry.getTaskInfo();
        SeedingTaskHead head = seedTaskHeadService.getHeadByTaskId(info.getTaskId());
        entry.setTaskHead(head);
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
        if(info.getQty().compareTo(BigDecimal.ZERO)==0){
            return;
        }
        SeedingTaskHead head = (SeedingTaskHead)entry.getTaskHead();

        head.setStatus(TaskConstant.Done);
        seedTaskHeadService.update(head);

        StockMove move = new StockMove();

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(info.getOrderId());
        if(ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }

        //收货播种
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



            //根据po +sku + storeNo 找so
            Long soOrderId = 0L;

            CsiSku sku = csiSkuService.getSku(info.getSkuId());

            WaveDetail detail =  waveService.getDetailByContainerIdAndItemId(info.getContainerId(), info.getItemId());

            if(detail== null ){
                throw new BizCheckedException("2880012");
            }
            BaseinfoItem item = itemService.getItem(ibdHeader.getOwnerUid(), sku.getSkuId());

            String orderOtherId = ibdHeader.getOrderOtherId();
            IbdDetail ibdDetail= poOrderService.getInbPoDetailByOrderIdAndDetailId(info.getOrderId(), head.getIbdDetailId());
            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("ibdOtherId", orderOtherId);
            queryMap.put("ibdDetailId", ibdDetail.getDetailOtherId());
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);
            for(IbdObdRelation ibdObdRelation :ibdObdRelations) {
                String obdOtherId = ibdObdRelation.getObdOtherId();
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherIdAndType(obdOtherId, SoConstant.ORDER_TYPE_DIRECT);

                if(obdHeader.getDeliveryCode().equals(head.getStoreNo())) {
                    soOrderId = obdHeader.getOrderId();
                }
            }
            waveService.splitWaveDetail(detail,info.getQty(),head.getRealContainerId(),soOrderId,info.getPackUnit(),head.getStoreNo());


        }else {
            move.setSkuId(info.getSkuId());
            move.setFromContainerId(info.getContainerId());
            List<BaseinfoLocation> locations = locationService.getLocationsByType(LocationConstant.SUPPLIER_AREA);
            move.setFromLocationId(locations.get(0).getLocationId());
        }
        List<StockQuant> quantList = quantService.getQuantsByContainerId(head.getRealContainerId());
        if(quantList ==null || quantList.size()==0){


            //获取location的id
            CsiCustomer customer = csiCustomerService.getCustomerByCustomerCode(head.getStoreNo()); // 门店对应的集货道
            if (null == customer) {
                throw new BizCheckedException("2180023");
            }
            if (null == customer.getSeedRoadId()) {
                throw new BizCheckedException("2180025");
            }
            BaseinfoLocation location = locationService.getLocation(customer.getSeedRoadId());

            move.setToLocationId(location.getLocationId());
        }else {
            move.setToLocationId(quantList.get(0).getLocationId());
        }
        move.setToContainerId(head.getRealContainerId());
        move.setQty(info.getQty());
        move.setTaskId(taskId);
        move.setItemId(info.getItemId());
        if(info.getSubType().compareTo(2L)==0) {
            // 订单播种，这里的货是不收货直接播种
            StockLot lot =new StockLot();
            CsiSupplier supplier = csiSupplierService.getSupplier(ibdHeader.getSupplierCode(),ibdHeader.getOwnerUid());
            lot.setItemId(info.getItemId());
            lot.setPoId(info.getOrderId());
            lot.setPackUnit(info.getPackUnit());
            lot.setSkuId(info.getSkuId());
            lot.setPackName(info.getPackName());
            if(supplier!=null) {
                lot.setSupplierId(supplier.getSupplierId());
            }
            moveService.move(move, lot);
            ReceiptRequest receiptRequest = this.fillReceipt(entry);
            seedRpcService.insertReceipt(receiptRequest);
            // 直流订单在此预占库存
            stockSummaryService.allocPresale(move.getItemId(), move.getQty(), info.getOrderId());
        }else {
            // 托盘播种，这里的货是已经收过的
            moveService.move(move);
        }
        if(head.getRequireQty().compareTo(info.getQty())>0) {
            info.setTaskId(0L);
            info.setId(0L);
            info.setStatus(TaskConstant.Draft);
            info.setPlanId(info.getOperator());
            info.setContainerId(info.getContainerId());
            head.setRequireQty(head.getRequireQty().subtract(info.getQty()));
            head.setStatus(TaskConstant.Draft);
            entry.setTaskInfo(info);
            entry.setTaskHead(head);
            iTaskRpcService.create(TaskConstant.TYPE_SEED, entry);
        }
    }
    public void updteConcrete(TaskEntry entry) {
        SeedingTaskHead head = (SeedingTaskHead)entry.getTaskHead();
        headService.update(head);
    }
    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskHead(headService.getHeadByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

    private ReceiptRequest fillReceipt( TaskEntry entry ){

        ReceiptRequest receiptRequest = new ReceiptRequest();
        ReceiptItem receiptItem = new ReceiptItem();
        Map<String,Long> orderMap = new HashMap<String, Long>();

        TaskInfo info = entry.getTaskInfo();
        SeedingTaskHead head = (SeedingTaskHead) entry.getTaskHead();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid", info.getOperator());
        Long staffId = staffService.getStaffList(map).get(0).getStaffId();
        receiptRequest.setStaffId(staffId);

        receiptRequest.setReceiptTime(new Date());

        CsiSku sku = csiSkuService.getSku(info.getSkuId());

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(info.getOrderId());

        if(ibdHeader == null) {
            throw new BizCheckedException("2020001");
        }

        String orderOtherId = ibdHeader.getOrderOtherId();
        IbdDetail ibdDetail= poOrderService.getInbPoDetailByOrderIdAndDetailId(info.getOrderId(), head.getIbdDetailId());
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ibdOtherId", orderOtherId);
        queryMap.put("ibdDetailId", ibdDetail.getDetailOtherId());
        List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);
        for(IbdObdRelation ibdObdRelation :ibdObdRelations) {
            String obdOtherId = ibdObdRelation.getObdOtherId();
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderOtherIdAndType(obdOtherId, SoConstant.ORDER_TYPE_DIRECT);

//            if(obdHeader.getDeliveryCode().equals(head.getStoreNo().toString())) {
            String key = StrUtils.formatString(RedisKeyConstant.PO_STORE, info.getOrderId(), head.getStoreNo());
            orderMap.put(key,obdHeader.getOrderId());
//            }
        }


        receiptRequest.setOrderOtherId(ibdHeader.getOrderOtherId());
        receiptRequest.setContainerId(head.getRealContainerId());
        receiptRequest.setStoreId(head.getStoreNo().toString());
        receiptRequest.setReceiptUser("");

        receiptItem.setOrderId(info.getOrderId());
        receiptItem.setSkuId(info.getSkuId());
        receiptItem.setSkuName(sku.getSkuName());
        receiptItem.setBarCode(sku.getCode());
        receiptItem.setDetailotherId(head.getIbdDetailId());
        receiptItem.setPackUnit(info.getPackUnit());
        receiptItem.setInboundQty(info.getQty());
        receiptItem.setPackName(PackUtil.PackUnit2Uom(info.getPackUnit(),"EA"));
        List<ReceiptItem> items = new ArrayList<ReceiptItem>();
        items.add(receiptItem);
        receiptRequest.setItems(items);
        receiptRequest.setOrderMap(orderMap);
        return receiptRequest;
    }
}
