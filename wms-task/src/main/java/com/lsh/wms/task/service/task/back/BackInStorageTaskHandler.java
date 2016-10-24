package com.lsh.wms.task.service.task.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.seed.ISeedRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.back.InStorageService;
import com.lsh.wms.core.service.container.ContainerService;
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
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
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
import net.sf.json.JSONObject;
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
public class BackInStorageTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private InStorageService inStorageService;
    @Reference
    ITaskRpcService taskRpcService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    PoOrderService poOrderService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    LocationService locationService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private CsiSupplierService supplierService;

    private static Logger logger = LoggerFactory.getLogger(BackInStorageTaskHandler.class);


    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_BACK_IN_STORAGE, this);
    }


    public void createConcrete(TaskEntry taskEntry) {
        List<Object> objectList = taskEntry.getTaskDetailList();
        TaskInfo info = taskEntry.getTaskInfo();
        for(Object object:objectList){
            BackTaskDetail detail = (BackTaskDetail)object;
            detail.setTaskId(info.getTaskId());
            inStorageService.insertDetail(detail);
        }
    }
    public void updteConcrete(TaskEntry taskEntry) {
        List<Object> objectList = taskEntry.getTaskDetailList();
        for(Object object:objectList){
            BackTaskDetail detail = (BackTaskDetail)object;
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            inStorageService.updatedetail(detail);
        }
    }
    public void doneConcrete(Long taskId) {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        List<StockMove> moves = new ArrayList<StockMove>();
        TaskInfo info = entry.getTaskInfo();
        List<Object> details = entry.getTaskDetailList();
        Map<Long,BigDecimal> qtyMap = new HashMap<Long, BigDecimal>();
        for(Object one :details) {
            BackTaskDetail detail = (BackTaskDetail)one;
            qtyMap.put(detail.getSkuId(),detail.getRealQty());
        }
        Map<String,Object> query = new HashMap<String, Object>();
        query.put("orderId", info.getOrderId());
        List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailList(query);
        Long containerId ;
        List<Long> containerIdList = quantService.getContainerIdByLocationId(info.getLocationId());
        if(containerIdList ==null || containerIdList.size()==0) {
           containerId =containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
        }else {
            containerId = containerIdList.get(0);
        }
        for(ObdDetail obdDetail:obdDetails){
            StockMove move = new StockMove();
            move.setSkuId(obdDetail.getSkuId());
            List<BaseinfoLocation> locations = locationService.getLocationsByType(LocationConstant.SUPPLIER_AREA);
            move.setFromLocationId(locations.get(0).getLocationId());
            move.setToLocationId(info.getLocationId());
            move.setToContainerId(containerId);
            move.setQty(qtyMap.get(obdDetail.getSkuId()).multiply(obdDetail.getPackUnit()));
            move.setTaskId(taskId);

            StockLot lot =new StockLot();
            lot.setItemId(obdDetail.getItemId());


            // 将so专成po
            ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(info.getOrderId());

            Map<String,Object> queryMap = new HashMap<String, Object>();
            queryMap.put("obdOtherId",header.getOrderOtherId());
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);
            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdObdRelations.get(0).getIbdOtherId());
            CsiSupplier supplier = supplierService.getSuppler(header.getSupplierNo(), header.getOwnerUid());
            lot.setPoId(ibdHeader.getOrderId());
            lot.setPackUnit(obdDetail.getPackUnit());
            lot.setSkuId(obdDetail.getSkuId());
            lot.setPackName(obdDetail.getPackName());
            lot.setSerialNo(obdDetail.getLotCode());
            lot.setSupplierId(supplier.getSupplierId());
            move.setLot(lot);
            moves.add(move);
        }
        moveService.move(moves);
    }
    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>) (List<?>) inStorageService.getDetailByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

}
