package com.lsh.wms.task.service.task.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.back.InStorageService;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.back.BackTaskDetail;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.seed.SeedingTaskHead;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.task.service.handler.AbsTaskHandler;
import com.lsh.wms.task.service.handler.TaskHandlerFactory;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/10/17.
 */
@Component
public class BackOutTaskHandler extends AbsTaskHandler {
    @Autowired
    private TaskHandlerFactory handlerFactory;
    @Autowired
    private InStorageService inStorageService;
    @Reference
    ITaskRpcService taskRpcService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    LocationService locationService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private CsiSkuService skuService;

    private static Logger logger = LoggerFactory.getLogger(BackOutTaskHandler.class);


    @PostConstruct
    public void postConstruct() {
        handlerFactory.register(TaskConstant.TYPE_BACK_OUT, this);
    }



    public void create(Long taskId) {
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        List<StockQuant> quants = quantService.getQuantsByLocationId(info.getToLocationId());
        if(quants == null || quants.size()==0){
            throw new BizCheckedException("2880003");
        }
        TaskEntry entry = new TaskEntry();
        info = new TaskInfo();
        info.setType(TaskConstant.TYPE_BACK_OUT);
        info.setStatus(TaskConstant.Draft);
        info.setLocationId(quants.get(0).getLocationId());

        List<BackTaskDetail> backTaskDetails = new ArrayList<BackTaskDetail>();
        Map<String,String> skuMap = new HashMap<String, String>();
        Map<String,Integer> skuIndex = new HashMap<String, Integer>();
        int index=0;
        for(StockQuant quant:quants){
            if(skuMap.get(quant.getSkuId().toString())==null) {
                BackTaskDetail backTaskDetail = new BackTaskDetail();
                CsiSku csiSku = skuService.getSku(quant.getSkuId());
                backTaskDetail.setSkuName(csiSku.getSkuName());
                backTaskDetail.setPackUnit(quant.getPackUnit());
                backTaskDetail.setBarcode(csiSku.getCode());
                backTaskDetail.setSkuId(csiSku.getSkuId());
                backTaskDetail.setPackName(quant.getPackName());
                backTaskDetail.setQty(quant.getQty());
                backTaskDetails.add(backTaskDetail);
                skuIndex.put(quant.getSkuId().toString(), index);
                skuMap.put(quant.getSkuId().toString(),quant.getSkuId().toString());
                index++;
            }else {
                String skuId =  skuMap.get(quant.getSkuId().toString());
                int value = skuIndex.get(skuId);
                BackTaskDetail detail = backTaskDetails.get(value);
                detail.setQty(detail.getQty().add(quant.getQty()));
                backTaskDetails.set(value,detail);
            }
        }
        entry.setTaskInfo(info);
        entry.setTaskDetailList((List<Object>) (List<?>) backTaskDetails);
        taskRpcService.create(TaskConstant.TYPE_BACK_OUT,entry);
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
    public void doneConcrete(Long taskId) {
        TaskEntry entry = taskRpcService.getTaskEntryById(taskId);
        TaskInfo info = entry.getTaskInfo();
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(info.getOrderId());
        header.setOrderStatus(4);
        soOrderService.update(header);
        moveService.moveToConsume(info.getLocationId(), info.getTaskId(), info.getOperator());
    }
    public void updteConcrete(TaskEntry taskEntry) {
        List<Object> objectList = taskEntry.getTaskDetailList();
        for(Object object:objectList){
            BackTaskDetail detail = (BackTaskDetail)object;
            detail.setUpdatedAt(DateUtils.getCurrentSeconds());
            inStorageService.updatedetail(detail);
        }
    }
    public void getConcrete(TaskEntry taskEntry) {
        taskEntry.setTaskDetailList((List<Object>) (List<?>) inStorageService.getDetailByTaskId(taskEntry.getTaskInfo().getTaskId()));
    }

}
