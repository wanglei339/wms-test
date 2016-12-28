package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IProcurementRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.ContainerConstant;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationBinService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationBin;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.transfer.StockTransferPlan;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "dubbo")
public class ProcurementProviderRpcService implements IProcurementProveiderRpcService {
    private static final Logger logger = LoggerFactory.getLogger(ProcurementProviderRpcService.class);

    @Autowired
    private StockTransferCore core;

    @Reference
    private IStockQuantRpcService stockQuantService;

    @Autowired
    private StockQuantService quantService;

    @Reference
    private IProcurementRpcService rpcService;

    @Autowired
    private ContainerService containerService;

    @Reference
    private ITaskRpcService taskRpcService;

    @Autowired
    private LocationService locationService;


    @Reference
    private ILocationRpcService locationRpcService;

    @Reference
    private IItemRpcService itemRpcService;

    @Reference
    private IItemRpcService itemLocationService;

    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BaseinfoLocationBinService locationBinService;

    //生成补货任务
    public boolean addProcurementPlan(StockTransferPlan plan){
        //系统自动创建的任务,无需进行任务检查,不可抛异常
        /*if(!this.checkPlan(plan)){
            logger.error("error plan ：" + plan.toString());
            return false;
        }*/
        //移动至checkplan中完成
        /*if (baseTaskService.checkTaskByToLocation(plan.getToLocationId(), TaskConstant.TYPE_PROCUREMENT)) {
            throw new BizCheckedException("2550015");
        }
        StockQuantCondition condition = new StockQuantCondition();
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo taskInfo = new TaskInfo();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);
        List<StockQuant> quants = stockQuantService.getQuantList(condition);
        if(quants==null || quants.size()==0){
            throw new BizCheckedException("2550008");
        }*/
        //移动至checkplan中完成并将方法改为checkAndFillPlan
       // core.fillTransferPlan(plan);

        //移动至checkAndFillPlan
       /* if ( plan.getQty().multiply(quants.get(0).getPackUnit()).compareTo(total) > 0) { // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550008");
        }
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getSubType().equals(2L)) {
            containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
        }*/


        TaskInfo taskInfo = new TaskInfo();
        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setTaskPackQty(taskInfo.getQty().divide(taskInfo.getPackUnit(), 2, BigDecimal.ROUND_HALF_DOWN));
        taskInfo.setContainerId(plan.getContainerId());
        taskInfo.setStep(1);

        TaskEntry taskEntry = new TaskEntry();
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.create(TaskConstant.TYPE_PROCUREMENT, taskEntry);
        return true;
    }

    public boolean updateProcurementPlan(StockTransferPlan plan)  throws BizCheckedException {
        /*TaskEntry entry =  taskRpcService.getTaskEntryById(plan.getTaskId());
        if(entry == null){
            throw new BizCheckedException("3040001");
        }
        TaskInfo taskInfo = entry.getTaskInfo();
        if (taskInfo.getToLocationId().compareTo(plan.getToLocationId())!=0 && baseTaskService.checkTaskByToLocation(plan.getToLocationId(), TaskConstant.TYPE_PROCUREMENT)){
            throw new BizCheckedException("2550015");
        }*/
   /*     StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(plan.getFromLocationId());
        condition.setItemId(plan.getItemId());
        BigDecimal total = stockQuantService.getQty(condition);
        core.fillTransferPlan(plan);

        if ( plan.getQty().compareTo(total) > 0) { // 移库要求的数量超出实际库存数量

            throw new BizCheckedException("2550008");
        }
        List<StockQuant> quantList = stockQuantService.getQuantList(condition);
        Long containerId = quantList.get(0).getContainerId();
        if (plan.getSubType().equals(2L)) {
            containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
        }
*/
        TaskInfo taskInfo = baseTaskService.getTaskInfoById(plan.getTaskId());
        ObjUtils.bean2bean(plan, taskInfo);
        taskInfo.setTaskName("补货任务[ " + taskInfo.getFromLocationId() + " => " + taskInfo.getToLocationId() + "]");
        taskInfo.setType(TaskConstant.TYPE_PROCUREMENT);
        taskInfo.setContainerId(plan.getContainerId());
        taskInfo.setTaskPackQty(taskInfo.getQty().divide(taskInfo.getPackUnit(), 2, BigDecimal.ROUND_HALF_DOWN));
        taskInfo.setStep(1);
        taskInfo.setStatus(TaskConstant.Draft);
        TaskEntry taskEntry = new TaskEntry();
        taskEntry.setTaskInfo(taskInfo);
        taskRpcService.update(TaskConstant.TYPE_PROCUREMENT, taskEntry);
        return true;
    }
    //创建货架补货任务
    private void createShelfProcurement(boolean canMax) throws BizCheckedException {
        Map<String,Object> mapQuery =new HashMap<String, Object>();
        //获取所有货架拣货位的位置信息
        List<BaseinfoLocation> shelfLocationList = locationService.getBinsByFatherTypeAndUsage(LocationConstant.SHELF, BinUsageConstant.BIN_UASGE_PICK);
        //获取所有货架的存储位
        List<BaseinfoLocation> shelfList = locationService.getBinsByFatherTypeAndUsage(LocationConstant.SHELF, BinUsageConstant.BIN_UASGE_STORE);

        for (BaseinfoLocation shelfCollectionBin : shelfLocationList) {
            //获取该拣货位存放的商品ID,目前逻辑,一个拣货位只能对应一种商品
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationID(shelfCollectionBin.getLocationId());

            for (BaseinfoItemLocation itemLocation : itemLocationList) {
                //判断商品是否需要补货
                if (rpcService.needProcurement(itemLocation.getPickLocationid(), itemLocation.getItemId(),canMax)) {
                    //该位置当前是否有补货任务
                    if (baseTaskService.checkTaskByToLocation(itemLocation.getPickLocationid(), TaskConstant.TYPE_PROCUREMENT)) {
                        continue;
                    }
                    /*
                    *找该商品的存储位
                     */
                    StockQuantCondition condition = new StockQuantCondition();
                    condition.setLocationList(shelfList);
                    condition.setItemId(itemLocation.getItemId());
                    condition.setReserveTaskId(0L);
                    //获取存该商品的存储位信息
                    List<StockQuant> quantList = stockQuantService.getQuantList(condition);
                    if (quantList.isEmpty()) {
                        logger.warn("ItemId:" + itemLocation.getItemId() + "缺货异常");
                        continue;
                    }

                    //去除小数
                    BigDecimal maxQty = itemLocation.getMaxQty();
                    BaseinfoItem item = itemService.getItem(itemLocation.getItemId());
                    BigDecimal [] maxDecimals = maxQty.divideAndRemainder(item.getPackUnit());
                    maxQty = maxQty.subtract(maxDecimals[1]);

                    mapQuery.put("locationId", itemLocation.getPickLocationid());
                    BigDecimal nowQuant = quantService.getQty(mapQuery);

//                    //取库位中库存最小的
//                    BigDecimal total = BigDecimal.ZERO;
//                    StockQuant quant = null;
//                    StockQuant beginQuant = null;
//                    Map<Long,Long> locationMap = new HashMap<Long, Long>();
//                    Map<String,Object> queryMap = new HashMap<String, Object>();
//                    for(StockQuant stockQuant:quantList) {
//                        if(locationMap.get(stockQuant.getLocationId())==null) {
//                            if(beginQuant==null){
//                                beginQuant = stockQuant;
//                                quant = stockQuant;
//                            }
//                            if(beginQuant.getExpireDate().compareTo(stockQuant.getExpireDate())==0) {
//                                //获取存储位该商品库存量
//                                queryMap.put("locationId", stockQuant.getLocationId());
//                                BigDecimal one = quantService.getQty(queryMap);
//                                if (total.compareTo(one) > 0 || total.compareTo(BigDecimal.ZERO) == 0) {
//                                    total = one;
//                                    quant = stockQuant;
//                                }
//                                beginQuant = quant;
//                            }else {
//                                quant = beginQuant;
//                                break;
//                            }
//                        }
//                        locationMap.put(stockQuant.getLocationId(),stockQuant.getLocationId());
//                    }
                    //根据库存数量和过期时间排序
                    List<StockQuant> sortQuants = this.sortQuant(quantList);

                    for(StockQuant quant:sortQuants) {

                        //判断存货位是否有捡货任务
                        Map<String,Object> checkMap = new HashMap<String, Object>();
                        checkMap.put("fromLocationId",quant.getLocationId());
                        checkMap.put("type", TaskConstant.TYPE_PROCUREMENT);
                        checkMap.put("valid",1);
                        List<TaskInfo> checkTaskInfos = baseTaskService.getTaskInfoList(checkMap);
                        if(checkTaskInfos!=null && checkTaskInfos.size()>0){
                            continue;
                        }


                        mapQuery.put("locationId", quant.getLocationId());
                        BigDecimal qty = quantService.getQty(mapQuery);
                        //去除小数
                        BigDecimal [] decimals = qty.divideAndRemainder(quant.getPackUnit());
                        qty = qty.subtract(decimals[1]);
                        //不够一箱，不能补货
                        if(decimals[0].compareTo(BigDecimal.ZERO)==0){
                            continue;
                        }
                        if (nowQuant.compareTo(maxQty) < 0) {
                            // 创建任务
                            nowQuant = nowQuant.add(qty);
                            StockTransferPlan plan = new StockTransferPlan();
                            plan.setContainerId(quant.getContainerId());
                            plan.setPackUnit(quant.getPackUnit());
                            plan.setPackName(quant.getPackName());


                            plan.setPriority(this.getPackPriority(itemLocation.getItemId()));
                            plan.setContainerId(quant.getContainerId());
                            plan.setItemId(itemLocation.getItemId());
                            plan.setFromLocationId(quant.getLocationId());
                            plan.setToLocationId(itemLocation.getPickLocationid());
                            plan.setPackName(quant.getPackName());
                            plan.setPackUnit(quant.getPackUnit());
                            if(nowQuant.compareTo(maxQty)>0){
                                plan.setSubType(2L);//不够一拖，按箱补
                                plan.setQty(maxQty.subtract(nowQuant.subtract(qty)));
                            }else {
                                plan.setSubType(1L);//整托
                                plan.setQty(qty);
                            }
                            //所需库存数不够一箱了，直接退出
                            if(plan.getQty().divide(quant.getPackUnit(),0,BigDecimal.ROUND_HALF_DOWN).compareTo(BigDecimal.ZERO)==0){
                                break;
                            }
                            this.addProcurementPlan(plan);
                        }
                    }
                }
            }
        }
    }
    //获取商品拣货优先级
    private Long getPackPriority(Long itemId){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", itemId);
        mapQuery.put("isValid", 1);
        mapQuery.put("isAlive", 1);
        List<WaveDetail> detailList = waveService.getWaveDetails(mapQuery);
        if(detailList == null){
            return 1L;
        }
        boolean haveAssignedTask = false;//有进行中的拣货任务
        boolean haveDraftTask = false;//有还未开始的拣货任务
        boolean haveSo = false;//已下单,还没生成拣货任务

        for(WaveDetail wd : detailList){
           Long pickTaskId =  wd.getPickTaskId();
            if(pickTaskId == 0){
                haveSo = true;
            }
            Map<String, Object> taskQuery = new HashMap<String, Object>();
            taskQuery.put("taskId", pickTaskId);
            List<TaskInfo> taskInfoList =  baseTaskService.getTaskInfoList(taskQuery);
            for(TaskInfo t : taskInfoList){
                if(TaskConstant.Done.equals(t.getStatus()) && wd.getPickQty().compareTo(wd.getAllocQty()) == -1){
                    //有已完成的拣货任务,且拣货数量小于要求拣货的数量
                    return 5L;//缺交
                }else if(TaskConstant.Assigned.equals(t.getStatus())){
                    //有进行中的拣货任务
                    haveAssignedTask = true;
                }else if(TaskConstant.Draft.equals(t.getStatus())){
                    //有尚未开始的拣货任务
                    haveDraftTask = true;
                }
            }
        }
        if(haveAssignedTask){
            return 4L; //有进行中的拣货任务
        }else if(haveDraftTask){
            return 3L;//有尚未开始的拣货任务
        }else if(haveSo){
            return 2L;
        }else{
            return 1L;
        }

    }


    public Long assign(Long staffId) throws BizCheckedException {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("status", TaskConstant.Done);
        List<TaskEntry> list = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT, mapQuery);
        if(list==null ||list.isEmpty()){
            mapQuery.put("status", TaskConstant.Draft);
            list = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT, mapQuery);
            if (list.isEmpty()) {
                return 0L;
            } else {
                for(TaskEntry entry:list){
                    BaseinfoLocation passageLocation = locationService.getPassageByBin(entry.getTaskInfo().getToLocationId());
                    Map<String,Object> queryMap = new HashMap<String, Object>();
                    queryMap.put("status",TaskConstant.Assigned);
                    queryMap.put("locationObj",passageLocation);
                    List<TaskEntry> entries = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT, queryMap);
                    if(entries==null ||entries.isEmpty()){
                        taskRpcService.assign(entry.getTaskInfo().getTaskId(), staffId);
                        return entry.getTaskInfo().getTaskId();
                    }

                }
                return 0L;
            }
        }
        

        Long taskId = this.getNextTask(list.get(list.size()-1).getTaskInfo().getToLocationId());
        if(taskId.compareTo(0L)==0){
            return 0L;
        }
        taskRpcService.assign(taskId, staffId);
        return taskId;
    }


    //创建阁楼补货任务
    private void createLoftProcurement(boolean canMax) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        Map<String,Object> mapQuery =new HashMap<String, Object>();

        //获取所有阁楼拣货位的位置信息
        List<BaseinfoLocation> loftPickLocationList = locationService.getBinsByFatherTypeAndUsage(LocationConstant.LOFT, BinUsageConstant.BIN_UASGE_PICK);
        //获取所有阁楼存货位的信息
        List<BaseinfoLocation> loftList = locationService.getBinsByFatherTypeAndUsage(LocationConstant.LOFT, BinUsageConstant.BIN_UASGE_STORE);

        for (BaseinfoLocation loftPick : loftPickLocationList) {
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationID(loftPick.getLocationId());
            for (BaseinfoItemLocation itemLocation : itemLocationList) {
                //判断是否需要补货
                if (rpcService.needProcurement(itemLocation.getPickLocationid(),itemLocation.getItemId(),canMax)) {
                    //是否有未完成的补货任务
                    if (baseTaskService.checkTaskByToLocation(itemLocation.getPickLocationid(), TaskConstant.TYPE_PROCUREMENT)) {
                        continue;
                    }
                    // 找合适的quant
                    StockQuantCondition condition = new StockQuantCondition();
                    condition.setLocationList(loftList);
                    condition.setItemId(itemLocation.getItemId());
                    condition.setReserveTaskId(0L);
                    List<StockQuant> quantList = stockQuantService.getQuantList(condition);
                    if (quantList.isEmpty()) {
                        logger.warn("ItemId:" + itemLocation.getItemId() + "缺货异常");
                        continue;
                    }

                    //去除小数
                    BigDecimal maxQty = itemLocation.getMaxQty();
                    BaseinfoItem item = itemService.getItem(itemLocation.getItemId());
                    BigDecimal [] maxDecimals = maxQty.divideAndRemainder(item.getPackUnit());
                    maxQty = maxQty.subtract(maxDecimals[1]);


                    mapQuery.put("locationId", itemLocation.getPickLocationid());
                    BigDecimal nowQuant = quantService.getQty(mapQuery);
                    //根据库存数量和过期时间排序
                    List<StockQuant> sortQuants = this.sortQuant(quantList);
                    for(StockQuant quant:sortQuants) {
                        //判断存货位是否有补货任务
                        Map<String,Object> checkMap = new HashMap<String, Object>();
                        checkMap.put("fromLocationId",quant.getLocationId());
                        checkMap.put("type", TaskConstant.TYPE_PROCUREMENT);
                        checkMap.put("valid", 1);
                        List<TaskInfo> checkTaskInfos = baseTaskService.getTaskInfoList(checkMap);
                        if(checkTaskInfos!=null && checkTaskInfos.size()>0){
                            continue;
                        }

                        mapQuery.put("locationId",quant.getLocationId());
                        BigDecimal qty = quantService.getQty(mapQuery);
                        //去除小数
                        BigDecimal [] decimals = qty.divideAndRemainder(quant.getPackUnit());
                        qty = qty.subtract(decimals[1]);
                        //不够一箱，不能补货
                        if(decimals[0].compareTo(BigDecimal.ZERO)==0){
                            continue;
                        }

                        if (nowQuant.compareTo(maxQty)<0) {
                            nowQuant = nowQuant.add(qty);
                            // 创建任务

//                            BigDecimal bulk = BigDecimal.ONE;
//                            //计算包装单位的体积
//                            bulk = bulk.multiply(item.getPackLength());
//                            bulk = bulk.multiply(item.getPackHeight());
//                            bulk = bulk.multiply(item.getPackWidth());
//
//                            BaseinfoLocationBin bin = (BaseinfoLocationBin) locationBinService.getBaseinfoItemLocationModelById(itemLocation.getPickLocationid());
//
//                            //80%为有效体积
//                            BigDecimal volume = bin.getVolume().multiply(BigDecimal.valueOf(0.8));
//
//                            BigDecimal num = volume.divide(bulk, 0, BigDecimal.ROUND_DOWN);
//                            if (total.subtract(num).compareTo(BigDecimal.ZERO) >= 0) {
//                                requiredQty = num;
//                            } else {
//                                requiredQty = total;
//                            }
//
//                            //减掉已有库存
//                            BigDecimal unitQty = quantService.getQuantQtyByLocationIdAndItemId(itemLocation.getPickLocationid(), quant.getItemId());
//                            BigDecimal umoQty = unitQty.divide(quant.getPackUnit(), 0, BigDecimal.ROUND_UP);

                            StockTransferPlan plan = new StockTransferPlan();

                            Long containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
                            plan.setContainerId(containerId);
                            plan.setPackUnit(quant.getPackUnit());
                            plan.setPackName(quant.getPackName());

                            plan.setPriority(this.getPackPriority(itemLocation.getItemId()));
                            plan.setItemId(itemLocation.getItemId());
                            plan.setFromLocationId(quant.getLocationId());
                            plan.setToLocationId(itemLocation.getPickLocationid());
                            plan.setPackName(quant.getPackName());
                            if(nowQuant.compareTo(maxQty)>0){
                                plan.setQty(maxQty.subtract(nowQuant.subtract(qty)));
                            }else {
                                plan.setQty(qty);
                            }
                            plan.setPriority(this.getPackPriority(itemLocation.getItemId()));
                            plan.setSubType(2L);

                            //所需库存数不够一箱了，直接退出
                            BigDecimal [] planDecimals = plan.getQty().divideAndRemainder(quant.getPackUnit());

                            if(planDecimals[0].compareTo(BigDecimal.ZERO)==0){
                                break;
                            }

                            this.addProcurementPlan(plan);
                        }
                    }
                }
            }
        }
    }

    public void createProcurement(boolean canMax) throws BizCheckedException {
        this.createShelfProcurement(canMax);
        this.createLoftProcurement(canMax);
    }

    public void scanFromLocation(Map<String, Object> params) throws BizCheckedException {
        core.outbound(params);
    }

    public void scanToLocation(Map<String, Object> params) throws  BizCheckedException {
        core.inbound(params);
    }
    public boolean checkPlan(StockTransferPlan plan) throws BizCheckedException {
        StockQuantCondition condition = new StockQuantCondition();
        Long fromLocationId = plan.getFromLocationId();
        Long toLocationId = plan.getToLocationId();
        BaseinfoLocation fromLocation = locationRpcService.getLocation(fromLocationId);
        BaseinfoLocation toLocation = locationRpcService.getLocation(toLocationId);
        //货架捡货位只能在货架存货位取货，阁楼捡货位只能在阁楼捡货位取货

        if(fromLocation!=null && toLocation!=null && fromLocation.getRegionType().equals(toLocation.getRegionType())){
            condition.setLocationId(fromLocationId);
            List<StockQuant> quants = stockQuantService.getQuantList(condition);
            List<BaseinfoItemLocation> itemLocations = itemRpcService.getItemLocationByLocationID(toLocationId);
            for(StockQuant quant: quants) {
                for(BaseinfoItemLocation itemLocation:itemLocations){
                    if(itemLocation.getItemId().compareTo(quant.getItemId())==0){
                        return true;
                    }
                }
            }
        }

        return false;
    }
    //检查并重新封装补货任务
    public boolean checkAndFillPlan(StockTransferPlan plan) throws BizCheckedException {
        //补出存储位ID
        Long fromLocationId = plan.getFromLocationId();
        //补入拣货位ID
        Long toLocationId = plan.getToLocationId();
        //补货商品
        Long itemId = plan.getItemId();
        //任务ID
        Long taskId = plan.getTaskId();

        /*
         *参数验证
         */
        //
        if(fromLocationId == null || toLocationId == null || itemId == null){
            return false;
        }

        if(taskId.compareTo(0L)!=0 ){
            //更新任务
            TaskEntry entry =  taskRpcService.getTaskEntryById(plan.getTaskId());
            if(entry == null){
                //任务不存在
                throw new BizCheckedException("3040001");
            }
            TaskInfo taskInfo = entry.getTaskInfo();
            if(taskInfo == null || !TaskConstant.Draft.equals(taskInfo.getStatus())){
                //该任务非新建状态,不可修改
                throw new BizCheckedException("2550033");
            }
            //拣货位是否修改,新拣货位是否有未完成的拣货任务
            if (taskInfo.getToLocationId().compareTo(plan.getToLocationId())!=0 && baseTaskService.checkTaskByToLocation(plan.getToLocationId(), TaskConstant.TYPE_PROCUREMENT)){
                throw new BizCheckedException("2550015");
            }
        }else{
            //新建任务
            //拣货位是否有未完成的拣货任务
            if (baseTaskService.checkTaskByToLocation(toLocationId, TaskConstant.TYPE_PROCUREMENT)) {
                throw new BizCheckedException("2550015");
            }
        }

        BaseinfoLocation fromLocation = locationRpcService.getLocation(fromLocationId);
        BaseinfoLocation toLocation = locationRpcService.getLocation(toLocationId);
        if(fromLocation == null || toLocation == null ){
            return false;
        }


        if(!fromLocation.getRegionType().equals(toLocation.getRegionType()) && fromLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_STORE) && toLocation.getBinUsage().equals(BinUsageConstant.BIN_UASGE_PICK)){
            return false;
        }

        //获取拣货位对应的货品
        List<BaseinfoItemLocation> itemLocations = itemRpcService.getItemLocationByLocationID(toLocationId);
        if(itemLocations == null || itemLocations.size() != 1){
            throw new BizCheckedException("2060012");
        }
        //拣货位商品是否对应
        if(itemLocations.get(0).getItemId().compareTo(itemId)!=0){
            throw new BizCheckedException("2060012");
        }


        StockQuantCondition condition = new StockQuantCondition();
        condition.setLocationId(fromLocationId);
        condition.setItemId(itemId);

        /*
         *验证库存是否满足拣货要求
         */
        List<StockQuant> quants = stockQuantService.getQuantList(condition);
        if(quants==null || quants.size()==0){
            throw new BizCheckedException("2550008");
        }
        //获取存储位该商品库存量
        BigDecimal total = stockQuantService.getQty(condition);
        StockQuant quant = quants.get(0);
        if ( plan.getUomQty().multiply(quant.getPackUnit()).compareTo(total) > 0) {
            // 移库要求的数量超出实际库存数量
            throw new BizCheckedException("2550008");
        }

        //验证完成后补充封装任务数据
        Long containerId = quant.getContainerId();
        if (plan.getSubType().equals(2L)) {
            containerId = containerService.createContainerByType(ContainerConstant.CAGE).getContainerId();
        }
        plan.setContainerId(containerId);
        plan.setPackUnit(quant.getPackUnit());
        plan.setPackName(quant.getPackName());
        plan.setQty(plan.getUomQty().multiply(plan.getPackUnit()));

        return true;
    }
    public Set<Long> getOutBoundLocation(Long itemId,Long locationId) {
        StockQuantCondition condition = new StockQuantCondition();
        Set<Long> outBondLocations = new HashSet<Long>();
        condition.setItemId(itemId);

        BaseinfoLocation pickLocation = locationService.getLocation(locationId);

        if(pickLocation.getRegionType().compareTo(LocationConstant.LOFTS)==0){
            List<StockQuant> quants = stockQuantService.getQuantList(condition);
            for(StockQuant quant:quants){
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                if(location.getRegionType().compareTo(LocationConstant.LOFTS)==0 && location.getBinUsage().equals(BinUsageConstant.BIN_UASGE_STORE) ){
                    outBondLocations.add(location.getLocationId());
                }
            }
        }else if(pickLocation.getRegionType().compareTo(LocationConstant.SHELFS) ==0){
            List<StockQuant> quants = stockQuantService.getQuantList(condition);
            for(StockQuant quant:quants){
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                if(location.getRegionType().compareTo(LocationConstant.SHELFS)==0 && location.getBinUsage().equals(BinUsageConstant.BIN_UASGE_STORE)){
                    outBondLocations.add(location.getLocationId());
                }
            }
        }
        return outBondLocations;
    }
    public Long getNextTask(Long locationId) {
        BaseinfoLocation passageLocation = locationService.getPassageByBin(locationId);
        int priority=5;
        Map<String,Object> queryMap = new HashMap<String, Object>();
        while (priority!=0){
            queryMap.put("priority", priority);
            List<BaseinfoLocation> locations = locationRpcService.getNearestPassage(passageLocation);
            for(BaseinfoLocation location:locations){
                queryMap.put("status",TaskConstant.Draft);
                queryMap.put("locationObj",location);
                List<TaskEntry> entries = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT,queryMap);
                if(entries==null || entries.size()==0) {
                    continue;
                }

                queryMap.put("status", TaskConstant.Assigned);
                List<TaskEntry> taskEntryList = taskRpcService.getTaskList(TaskConstant.TYPE_PROCUREMENT,queryMap);
                if(taskEntryList==null || taskEntryList.size()==0){
                    List<BaseinfoLocation> locationList = new ArrayList<BaseinfoLocation>();

                    if(priority==5){
                        return entries.get(0).getTaskInfo().getTaskId();
                    }
                    Map<Long,Long> TaskMap = new HashMap<Long, Long>();
                    for(TaskEntry entry:entries){
                        TaskMap.put(entry.getTaskInfo().getToLocationId(),entry.getTaskInfo().getTaskId());
                        locationList.add(locationService.getLocation(entry.getTaskInfo().getToLocationId()));
                    }
                    locationList = locationRpcService.sortLocationInOnePassage(locationList);
                    return TaskMap.get(locationList.get(0).getLocationId());
                }
            }
            priority--;
        }
        return 0L;
    }
    List<StockQuant> sortQuant(List<StockQuant> quants){
        List<StockQuant> quantList = new ArrayList<StockQuant>();
        Map<Long,Long> locationMap = new HashMap<Long, Long>();
        Map<String,Object> queryMap = new HashMap<String, Object>();
        Map<Long,BigDecimal> qtyMap = new HashMap<Long, BigDecimal>();
       for(StockQuant quant:quants){
           if(locationMap.containsKey(quant.getLocationId())){
               continue;
           }
           locationMap.put(quant.getLocationId(),quant.getLocationId());

           for(int i=0;i<quantList.size();i++){
               StockQuant sortQuant = quantList.get(i);
               if(sortQuant.getExpireDate().compareTo(quant.getExpireDate())==0) {
                   //获取存储位该商品库存量
                   queryMap.put("locationId", quant.getLocationId());
                   BigDecimal one = quantService.getQty(queryMap);

                   qtyMap.put(quant.getLocationId(),one);
                   if (qtyMap.get(sortQuant.getLocationId()).compareTo(one) > 0) {
                       quantList.add(i,quant);
                       break;
                   }
               }else {
                   queryMap.put("locationId", quant.getLocationId());
                   BigDecimal one = quantService.getQty(queryMap);
                   qtyMap.put(quant.getLocationId(),one);
                   quantList.add(quant);
                   break;
               }
           }
           if(quantList.size()==0){
               //初始化quantList和qtyMap
               //获取存储位该商品库存量
               queryMap.put("locationId", quant.getLocationId());
               BigDecimal one = quantService.getQty(queryMap);
               qtyMap.put(quant.getLocationId(),one);
               quantList.add(quant);

           }
       }
        return quantList;
    }
}
