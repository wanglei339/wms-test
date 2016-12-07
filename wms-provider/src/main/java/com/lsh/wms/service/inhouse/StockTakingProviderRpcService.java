package com.lsh.wms.service.inhouse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.inhouse.IStockTakingProviderRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WorkZoneConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.zone.WorkZoneService;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.taking.StockTakingRequest;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.zone.WorkZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by mali on 16/7/26.
 */

@Service(protocol = "dubbo")
public class StockTakingProviderRpcService implements IStockTakingProviderRpcService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakingProviderRpcService.class);

    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private RedisStringDao redisStringDao;
    @Autowired
    private LocationService locationService;
    @Autowired
    private StockQuantService quantService;
    @Autowired
    private StockTakingService stockTakingService;
    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private WorkZoneService workZoneService;
    @Autowired
    private WaveService waveService;


    public void create(Long locationId,Long uid) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("type", TaskConstant.TYPE_STOCK_TAKING);
        queryMap.put("valid", 1);
        queryMap.put("locationId",locationId);
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(queryMap);
        if(infos==null || infos.size()==0) {
            StockTakingRequest request = new StockTakingRequest();
            List<Long> longList = new ArrayList<Long>();
            longList.add(locationId);
            request.setLocationList(JSON.toJSONString(longList));
            request.setPlanner(uid);
            request.setTakingId(RandomUtils.genId());

            String key = StrUtils.formatString(RedisKeyConstant.TAKING_KEY, request.getTakingId());
            redisStringDao.set(key, request.getTakingId(), 24, TimeUnit.HOURS);

            StockTakingHead head = new StockTakingHead();
            ObjUtils.bean2bean(request, head);
            List<StockTakingDetail> detailList = prepareDetailList(head);
            stockTakingService.insertHead(head);
            this.createTask(head, detailList, 1L, head.getDueTime());
        }
    }
    public void createTask(StockTakingHead head, List<StockTakingDetail> detailList,Long round,Long dueTime) throws BizCheckedException{
        List<TaskEntry> taskEntryList=new ArrayList<TaskEntry>();
        for(StockTakingDetail detail:detailList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskName("盘点任务[ " + detail.getLocationId() + "]");
            taskInfo.setPlanId(head.getTakingId());
            taskInfo.setDueTime(dueTime);
            taskInfo.setPlanner(head.getPlanner());
            taskInfo.setStatus(TaskConstant.Draft);
            taskInfo.setLocationId(detail.getLocationId());
            taskInfo.setSkuId(detail.getSkuId());
            taskInfo.setItemId(detail.getItemId());
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
        }
        iTaskRpcService.batchCreate(head, taskEntryList);
    }
    public List<StockTakingDetail> prepareDetailList(StockTakingHead head) {

        List<Long> locationList= JSON.parseArray(head.getLocationList(), Long.class);
        List<Long> locations=new ArrayList<Long>();
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("type",TaskConstant.TYPE_STOCK_TAKING);
        queryMap.put("valid",1);
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(queryMap);
        List<Long> taskLocation =new ArrayList<Long>();
        if (locationList != null && locationList.size()!=0) {
            for(Long locationId:locationList) {
                locations.add(locationId);
            }
        }
        if(infos!=null && infos.size()!=0){
            for(TaskInfo info:infos){
                taskLocation.add(info.getLocationId());
            }
        }
        locations.removeAll(taskLocation);
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("locationIdList", locations);
        mapQuery.put("itemId", head.getItemId());
        mapQuery.put("lotId", head.getLotId());
        mapQuery.put("ownerId", head.getOwnerId());
        mapQuery.put("supplierId", head.getSupplierId());
        List<StockQuant> quantList = quantService.getQuants(mapQuery);

        if (head.getTakingType().equals(1L)) {
            return this.prepareDetailListByItem(quantList);
        }
        else {
            return this.prepareDetailListByLocation(locationList, quantList);
        }
    }
    private List<StockTakingDetail> prepareDetailListByLocation(List<Long> locationList, List<StockQuant> quantList){
        Map<Long, StockQuant> mapLoc2Quant = new HashMap<Long, StockQuant>();
        for (StockQuant quant : quantList) {
            mapLoc2Quant.put(quant.getLocationId(), quant);
        }

        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        for (Long locationId : locationList) {
            StockTakingDetail detail = new StockTakingDetail();
            detail.setLocationId(locationId);
            detail.setDetailId(idx);

            StockQuant quant = mapLoc2Quant.get(locationId);
            if (quant != null ) {
                detail.setTheoreticalQty(quant.getQty());
                detail.setSkuId(quant.getSkuId());
                detail.setRealSkuId(detail.getSkuId());
            }
            idx++;
            detailList.add(detail);
        }
        return detailList;
    }
    private List<StockTakingDetail> prepareDetailListByItem(List<StockQuant> quantList) {
        Long idx = 0L;
        List<StockTakingDetail> detailList = new ArrayList<StockTakingDetail>();
        Map<String,StockQuant> mergeQuantMap =new HashMap<String, StockQuant>();
        for(StockQuant quant:quantList){
            String key= "i:"+quant.getItemId()+"l:"+quant.getLocationId();
            if (mergeQuantMap.containsKey(key)){
                StockQuant stockQuant =mergeQuantMap.get(key);
                stockQuant.setQty(quant.getQty().add(stockQuant.getQty()));
            }else {
                mergeQuantMap.put(key,quant);
            }
        }
        for (String key : mergeQuantMap.keySet()) {
            StockQuant quant=mergeQuantMap.get(key);
            StockTakingDetail detail = new StockTakingDetail();
            detail.setDetailId(idx);
            detail.setLocationId(quant.getLocationId());
            detail.setSkuId(quant.getSkuId());
            detail.setContainerId(quant.getContainerId());
            detail.setItemId(quant.getItemId());
            detail.setRealItemId(quant.getItemId());
            detail.setRealSkuId(detail.getSkuId());
            detail.setTheoreticalQty(quant.getQty());
            detail.setPackName(quant.getPackName());
            detail.setPackUnit(quant.getPackUnit());
            detail.setOwnerId(quant.getOwnerId());
            detail.setLotId(quant.getLotId());
            detailList.add(detail);
            idx++;
        }
        logger.info(JsonUtils.SUCCESS(detailList));
        return detailList;
    }


    public void createPlanWarehouse(){
        //get zone list;
        List<WorkZone> zoneList = workZoneService.getWorkZoneByType(WorkZoneConstant.ZONE_STOCK_TAKING);
        if(zoneList.size()==0){
            //抛异常也行
            return;
        }
        //get all location list;
        List<Long> binLocations = locationService.getALLBins();
        //cluster by zone;计算量稍微有点大,会不会超时啊?
        Set<Long> setBinDup = new HashSet<Long>();
        List<List<Long>> zoneBinArrs = new LinkedList<List<Long>>();
        for (WorkZone zone : zoneList) {
            List<Long> zoneBinLocations = new ArrayList<Long>();
            if (zone.getLocations().trim().compareTo("") != 0) {

                String[] locationIdStrs = zone.getLocations().split(",");
                for (String locationIdStr : locationIdStrs) {
                    Long locationId = Long.valueOf(locationIdStr);
                    BaseinfoLocation location = locationService.getLocation(locationId);
                    if (location == null) {
                        //抛异常也行
                        continue;
                    }
                    for (Long bin : binLocations) {
                        if (location.getLocationId() <= bin && bin <= location.getRightRange()) {
                            //hehe in the zone;
                            if (!setBinDup.contains(bin)) {
                                zoneBinLocations.add(bin);
                                setBinDup.add(bin);
                            }
                        }
                    }
                }
            } else {
                //抛异常也行
            }
            zoneBinArrs.add(zoneBinLocations);
        }
        //call create
        //这里数据库插入量非常高,非常容易超时,看怎么处理.
    }

    public void createPlanSales(){
        //get zone list;
        List<WorkZone> zoneList = workZoneService.getWorkZoneByType(WorkZoneConstant.ZONE_STOCK_TAKING);
        if(zoneList.size()==0){
            //抛异常也行
            return;
        }
        //get all location list;
        List<Long> binLocations = locationService.getALLBins();
        //拉取动销库位,从wavedetail里面去拿,通过picklocation
        long begin_at = 0;
        long end_at = 0;
        List<Long> pickLocations = waveService.getPickLocationsByPickTimeRegion(begin_at, end_at);
        Set<Long> setBinDup = new HashSet<Long>();
        List<List<Long>> zoneBinArrs = new LinkedList<List<Long>>();
        for (WorkZone zone : zoneList) {
            List<Long> zoneBinLocations = new ArrayList<Long>();
            if (zone.getLocations().trim().compareTo("") != 0) {
                String[] locationIdStrs = zone.getLocations().split(",");
                for (String locationIdStr : locationIdStrs) {
                    Long locationId = Long.valueOf(locationIdStr);
                    BaseinfoLocation location = locationService.getLocation(locationId);
                    if (location == null) {
                        //抛异常也行
                        continue;
                    }
                    for (Long bin : pickLocations) {
                        if (location.getLocationId() <= bin && bin <= location.getRightRange()) {
                            //hehe in the zone;
                            if (!setBinDup.contains(location.getLocationId())) {
                                zoneBinLocations.add(bin);
                                setBinDup.add(bin);
                            }
                        }
                    }
                }
            } else {
                //抛异常也行
            }
            zoneBinArrs.add(zoneBinLocations);
        }
        //create;
    }
}
