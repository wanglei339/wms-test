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
import com.lsh.wms.core.constant.*;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.datareport.SkuMapService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.zone.WorkZoneService;
import com.lsh.wms.model.taking.FillTakingPlanParam;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.datareport.SkuMap;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockQuant;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import com.lsh.wms.model.taking.StockTakingRequest;
import com.lsh.wms.model.task.StockTakingTask;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.zone.WorkZone;
import com.lsh.wms.service.sync.AsyncEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
    private ItemService itemService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private CsiSkuService skuService;
    @Autowired
    private StockMoveService moveService;
    @Autowired
    private SkuMapService skuMapService;




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
            Long takingId = RandomUtils.genId();

            String key = StrUtils.formatString(RedisKeyConstant.TAKING_KEY, takingId);
            redisStringDao.set(key, takingId, 24, TimeUnit.HOURS);

            StockTakingHead head = new StockTakingHead();
            ObjUtils.bean2bean(request, head);
            head.setTakingId(takingId);
            List<StockTakingDetail> detailList = prepareDetailList(head);
            stockTakingService.insertHead(head);
            this.createTask(head, detailList, 1L, head.getDueTime());
        }
    }
    public void calcelTask(Long taskId) throws BizCheckedException {
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        if(info!=null && info.getStatus().equals(TaskConstant.Draft)) {
            StockTakingHead head = stockTakingService.getHeadById(info.getPlanId());
            iTaskRpcService.cancel(info.getTaskId(), head);
        }
    }
//    @Transactional(readOnly = false)
    public void replay(List<Long> detailList,Long planner) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("status", StockTakingConstant.PendingAudit);
        queryMap.put("isValid", 1);
        queryMap.put("detailList",detailList);
        List<StockTakingDetail> details = stockTakingService.getDetails(queryMap);
        if(details!=null && details.size()!=0){
            StockTakingHead head = new StockTakingHead();
            head.setPlanType(StockTakingConstant.TYPE_REPLAY);
            head.setTakingId(RandomUtils.genId());
            head.setPlanner(planner);
            head.setStatus(StockTakingConstant.Draft);
            List<TaskEntry> taskEntries = new ArrayList<TaskEntry>();
            for(StockTakingDetail detail:details){
                StockTakingDetail newDetail = new StockTakingDetail();
                Long zoneId = detail.getZoneId();
                WorkZone zone = workZoneService.getWorkZone(zoneId);
                TaskEntry taskEntry = new TaskEntry();
                TaskInfo info = new TaskInfo();
                List<Object> newDetails = new ArrayList<Object>();
                newDetail.setTakingId(head.getTakingId());
                detail.setStatus(TaskConstant.Draft);
                newDetail.setRound(detail.getRound() + 1);
                newDetail.setRefTaskType(detail.getRefTaskType());
                newDetail.setDetailId(detail.getDetailId());
                newDetail.setLocationCode(detail.getLocationCode());
                newDetail.setLocationId(detail.getLocationId());
                newDetails.add(newDetail);
                detail.setZoneId(zoneId);
                if(zone==null){
                    info.setTaskName("");
                }else {
                    info.setTaskName(zone.getZoneName());
                }
                info.setType(TaskConstant.TYPE_STOCK_TAKING);
                info.setSubType(StockTakingConstant.TYPE_REPLAY);
                info.setPlanner(planner);
                info.setTaskOrder(1L);
                info.setStatus(TaskConstant.Draft);
                info.setPlanId(head.getTakingId());
                taskEntry.setTaskInfo(info);
                taskEntry.setTaskDetailList(newDetails);
                taskEntries.add(taskEntry);
            }
            iTaskRpcService.batchCreate(head, taskEntries);
        }
    }
    public void confirmDetail(List<Long> detailList) throws BizCheckedException {
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("status", StockTakingConstant.PendingAudit);
        queryMap.put("isValid", 1);
        queryMap.put("detailList",detailList);
        List<StockTakingDetail> details = stockTakingService.getDetails(queryMap);
        if(details!=null && details.size()!=0){
            stockTakingService.confirm(details);
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
    public List<Long> getTakingLocation(StockTakingRequest request,boolean needSort){
        List<Long> locationList = new ArrayList<Long>();
        List<Long> locations = new ArrayList<Long>();
        int locationNum= Integer.MAX_VALUE;
        Long fatherLocationId = 0L;
        if(!request.getLocationNum().equals(0)){
            locationNum = request.getLocationNum();
        }
        //根据，区，通道，货架，层 筛选出location
        if (!request.getShelfLayerId().equals(0L)) {
            fatherLocationId = request.getShelfLayerId();
        } else if (!request.getStorageId().equals(0L)) {
            fatherLocationId = request.getStorageId();
        } else if (!request.getPassageId().equals(0L)) {
            fatherLocationId = request.getPassageId();
        } else if (!request.getAreaId().equals(0L)) {
            fatherLocationId = request.getAreaId();
        }
        List<BaseinfoLocation> baseinfoLocations = locationService.getChildrenLocationsByCanStoreType(fatherLocationId, LocationConstant.BIN, LocationConstant.CAN_STORE);
        for(BaseinfoLocation baseinfoLocation:baseinfoLocations) {
            locationList.add(baseinfoLocation.getLocationId());
        }

        List<Long> taskLocation = new ArrayList<Long>();
        Set<Long> longs = new HashSet<Long>();

        if(request.getSupplierId().compareTo(0L)!=0 || request.getItemId().compareTo(0L)!=0 ) {
            //商品,供应商得到库位
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (request.getSupplierId().compareTo(0L) != 0) {
                queryMap.put("supplierId", request.getSupplierId());
            }
            if (request.getItemId().compareTo(0L) != 0) {
                queryMap.put("itemId", request.getItemId());
            }
            List<StockQuant> quantList = quantService.getQuants(queryMap);

            for (StockQuant quant : quantList) {
                BaseinfoLocation location = locationService.getLocation(quant.getLocationId());
                if(location.getRegionType().equals(LocationConstant.SHELFS) || location.getRegionType().equals(LocationConstant.LOFTS) || location.getRegionType().equals(LocationConstant.SPLIT_AREA)){
                    if(location.getCanStore().equals(LocationConstant.CAN_STORE)) {
                        longs.add(quant.getLocationId());
                    }
                }
            }
            locationList.retainAll(longs);
        }

        //取到盘点库位
        List<StockTakingDetail> details = stockTakingService.getValidDetailList();
        if(details!=null && details.size()!=0){
            for(StockTakingDetail detail:details){
                taskLocation.add(detail.getLocationId());
            }
        }

        locationList.removeAll(taskLocation);
        int i=0 ;
        while(i<locationNum){
            if(locationList.size()==0){
                break;
            }

            // 取出一个随机数
            int r = (int) (Math.random() * locationList.size());
            Long locationId = locationList.get(r);

            // 排除已经取过的值
            locationList.remove(r);

            //过滤掉区的上一层
            if(locationId.compareTo(0L)==0 ||locationId.compareTo(1L)==0 || locationId.compareTo(2L)==0){
                continue;
            }
            BaseinfoLocation location = locationService.getFatherByClassification(locationId);
            if(location==null){
                continue;
            }
            //是阁楼区，货架区，存捡一体区，地堆区
            if(location.getType().compareTo(LocationConstant.SHELFS)==0 ||location.getType().compareTo(LocationConstant.LOFTS)==0  ||location.getType().compareTo(LocationConstant.SPLIT_AREA)==0 ||location.getType().compareTo(LocationConstant.FLOOR)==0 || (location.getType().equals(LocationConstant.BIN) && location.getBinUsage().equals(BinUsageConstant.BIN_FLOOR_STORE))) {
                locations.add(locationId);
                i++;
            }
        }
        //sort location
        if(needSort) {
            List<BaseinfoLocation> sortedLocationList = new ArrayList<BaseinfoLocation>();
            for (Long locationId : locations) {
                sortedLocationList.add(locationService.getLocation(locationId));
            }
            sortedLocationList = locationService.calcZwayOrder(sortedLocationList, true);
            locations.removeAll(locations);
            for (BaseinfoLocation location : sortedLocationList) {
                locations.add(location.getLocationId());
            }
        }
        return locations;
    }
    public void  updateItem(Long itemId,Long detailId,Long proDate,Long round) throws BizCheckedException{
        BaseinfoItem item = itemService.getItem(itemId);
        if(item ==null){
            throw new BizCheckedException("2120001");
        }
        StockTakingDetail detail = stockTakingService.getDetailByRoundAndDetailId(detailId,round);

        if(detail ==null){
            throw new BizCheckedException("2550066");
        }
        if("".equals(detail.getBarcode())){
            throw new BizCheckedException("2550091");
        }
        CsiSku sku = null;
        if(!item.getPackCode().equals(detail.getBarcode())) {
            sku= skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, detail.getBarcode());
            if (!itemService.checkSkuItem(item.getItemId(), sku.getSkuId())) {
                throw new BizCheckedException("2550092");
            }
        }
        if(!detail.getStatus().equals(StockTakingConstant.PendingAudit)){
            throw new BizCheckedException("2550067");
        }
        StockLot lot = new StockLot();
        Long containerId = containerService.createContainerByType(ContainerConstant.PALLET).getContainerId();
        if(item.getIsShelfLifeValid() == 1) {
            if (proDate == null) {
                throw new BizCheckedException("2550065");
            }
        }else {
            if (proDate == null){
                proDate = DateUtils.getCurrentSeconds();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(proDate * 1000));
        calendar.add(calendar.DAY_OF_YEAR, item.getShelfLife().intValue());
        Long expireDate = calendar.getTime().getTime() / 1000;

        lot.setProductDate(proDate);
        lot.setExpireDate(expireDate);
        lot.setLotId(RandomUtils.genId());
        lot.setInDate(DateUtils.getCurrentSeconds());
        lot.setItemId(itemId);
        if(sku!=null) {
            lot.setSkuId(sku.getSkuId());
        }else {
            lot.setSkuId(item.getSkuId());
        }
        lot.setPackName(item.getPackName());
        lot.setPackUnit(item.getPackUnit());
        lot.setCode(item.getCode());
        lot.setUnitName(item.getUnitName());

        detail.setLotId(lot.getLotId());
        detail.setPackName(lot.getPackName());
        detail.setItemId(itemId);
        detail.setOwnerId(item.getOwnerId());
        detail.setSkuName(item.getSkuName());
        detail.setContainerId(containerId);
        detail.setPackName(item.getPackName());
        detail.setPackUnit(item.getPackUnit());
        detail.setSkuCode(item.getSkuCode());
        detail.setSkuId(lot.getSkuId());
        detail.setRealSkuId(lot.getSkuId());

        if(sku==null) {
            detail.setBarcode(item.getCode());
        }else {
            detail.setBarcode(sku.getCode());
        }
        detail.setPackCode(detail.getPackCode());

        if(detail.getOwnerId().compareTo(1L)==0 || detail.getOwnerId().compareTo(2L)==0) {
            SkuMap skuMap = skuMapService.getSkuMapBySkuCodeAndOwner(detail.getSkuCode(), item.getOwnerId());
            if (skuMap == null) {
                throw new BizCheckedException("2880022", detail.getSkuCode(), "");
            }
            detail.setPrice(skuMap.getMovingAveragePrice());
            detail.setDifferencePrice(detail.getRealQty().subtract(detail.getTheoreticalQty()).multiply(detail.getPrice()));
        }
        stockTakingService.fillEmptyDetail(detail,lot);

    }
    public void createTemporary(StockTakingRequest request){
        //get zone list;
        List<WorkZone> zoneList = workZoneService.getWorkZoneByType(WorkZoneConstant.ZONE_STOCK_TAKING);
        if(zoneList.size()==0){
            //抛异常也行
            return;
        }
        List<Long> locationList = new ArrayList<Long>();
        if( request.getLocationList() ==null || request.getLocationList().equals("") || request.getLocationList().equals("null")) {
            locationList = this.getTakingLocation(request,false);
        }else {
            locationList = JSON.parseArray(request.getLocationList(), Long.class);
        }

        Set<Long> setBinDup = new HashSet<Long>();
        Map<Long, List<Long>> mapZoneBinArrs = new HashMap<Long, List<Long>>();
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
                    for (Long bin : locationList) {
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
            mapZoneBinArrs.put(zone.getZoneId(), zoneBinLocations);
        }
        this.batchCreateStockTaking(mapZoneBinArrs,request.getPlanType(),request.getPlanner());

    }

    public void createPlanWarehouse(List<Long> zoneIds, Long planer) throws BizCheckedException {
        List<WorkZone> zoneList = this.getSelectedZones(zoneIds);
        //get all location list;
        List<Long> binLocations = locationService.getALLBins();
        //cluster by zone;计算量稍微有点大,会不会超时啊?
        Set<Long> setBinDup = new HashSet<Long>();
        Map<Long, List<Long>> mapZoneBinArrs = new HashMap<Long, List<Long>>();
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
            if(zoneBinLocations.size()>0) {
                mapZoneBinArrs.put(zone.getZoneId(), zoneBinLocations);
            }
        }
        //call create
        //这里数据库插入量非常高,非常容易超时,看怎么处理.
        this.batchCreateStockTaking(mapZoneBinArrs, StockTakingConstant.TYPE_PLAN, planer);
    }

    public List<WorkZone> getSelectedZones(List<Long> zoneIds){
        List<WorkZone> selectedZoneList = new LinkedList<WorkZone>();
        List<WorkZone> zoneList = workZoneService.getWorkZoneByType(WorkZoneConstant.ZONE_STOCK_TAKING);
        Set<Long> setIds = new HashSet<Long>();
        for(Long id : zoneIds){
            setIds.add(id);
        }
        for(WorkZone zone : zoneList){
            if(setIds.contains(zone.getZoneId())){
                selectedZoneList.add(zone);
            }
        }
        return selectedZoneList;
    }

    public void createPlanSales(List<Long> zoneIds, Long planer) throws BizCheckedException{
        //get zone list;
        List<WorkZone> zoneList = this.getSelectedZones(zoneIds);

        //拉取动销库位,从wavedetail里面去拿,通过picklocation
        long beginAt = DateUtils.getTodayBeginSeconds();
        long endAt = DateUtils.getCurrentSeconds();
        List<Long> saleLocations = moveService.getMovedLocationByDate(beginAt, endAt);
        Set<Long> setBinDup = new HashSet<Long>();
        Map<Long, List<Long>> mapZoneBinArrs = new HashMap<Long, List<Long>>();
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
                    for (Long bin : saleLocations) {
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
            if(zoneBinLocations.size()>0) {
                mapZoneBinArrs.put(zone.getZoneId(), zoneBinLocations);
            }
        }
        this.batchCreateStockTaking(mapZoneBinArrs, StockTakingConstant.TYPE_MOVE_OFF, planer);
    }
    public void createStockTaking(List<Long> locations,Long zoneId,Long takingType,Long planner) throws BizCheckedException {
        if(locations==null || locations.size()==0){
            return;
        }

        List<Long> taskLocation = new ArrayList<Long>();
        //取到盘点库位
        List<StockTakingDetail> taskDetails = stockTakingService.getValidDetailList();
        if(taskDetails!=null && taskDetails.size()!=0){
            for(StockTakingDetail detail:taskDetails){
                taskLocation.add(detail.getLocationId());
            }
        }
        locations.removeAll(taskLocation);

        List<Object> details = new ArrayList<Object>();
        StockTakingHead head = new StockTakingHead();
        head.setPlanType(takingType);
        head.setTakingId(RandomUtils.genId());
        head.setPlanner(planner);
        head.setStatus(StockTakingConstant.Draft);
        WorkZone zone = workZoneService.getWorkZone(zoneId);
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        List<BaseinfoLocation> baseinfoLocationList = new ArrayList<BaseinfoLocation>();
        for(Long locationId:locations){
            baseinfoLocationList.add(locationService.getLocation(locationId));
        }

        //排序
        baseinfoLocationList  = locationService.calcZwayOrder(baseinfoLocationList,true);

        for(BaseinfoLocation location:baseinfoLocationList){
            StockTakingDetail detail = new StockTakingDetail();
            detail.setLocationId(location.getLocationId());
            detail.setLocationCode(location.getLocationCode());
            detail.setDetailId(RandomUtils.genId());
            detail.setTakingId(head.getTakingId());
            detail.setRefTaskType(takingType);
            details.add(detail);
            detail.setZoneId(zoneId);
        }
        if(zone==null){
            info.setTaskName("");
        }else {
            info.setTaskName(zone.getZoneName());
        }
        if(details==null || details.size()==0){
            return;
        }
        info.setTaskOrder(Long.valueOf(details.size()+""));
        info.setType(TaskConstant.TYPE_STOCK_TAKING);
        info.setSubType(takingType);
        info.setPlanner(planner);
        info.setPlanId(head.getTakingId());
        entry.setTaskInfo(info);
        entry.setTaskDetailList(details);
        iTaskRpcService.createTask(head, entry);
    }
    public void batchCreateStockTaking(Map<Long,List<Long>> takingMap,Long takingType,Long planner) throws BizCheckedException {
        List<Long> taskLocation = new ArrayList<Long>();
        //取到盘点库位
        List<StockTakingDetail> taskDetails = stockTakingService.getValidDetailList();
        if(taskDetails!=null && taskDetails.size()!=0){
            for(StockTakingDetail detail:taskDetails){
                taskLocation.add(detail.getLocationId());
            }
        }


        StockTakingHead head = new StockTakingHead();
        head.setPlanType(takingType);
        head.setTakingId(RandomUtils.genId());
        head.setPlanner(planner);
        head.setStatus(StockTakingConstant.Draft);
        Iterator<Map.Entry<Long, List<Long>>> entries = takingMap.entrySet().iterator();
        List<TaskEntry> taskEntries = new ArrayList<TaskEntry>();
        while (entries.hasNext()) {
            Map.Entry<Long, List<Long>> entry = entries.next();
            Long zoneId = entry.getKey();
            WorkZone zone = workZoneService.getWorkZone(zoneId);
            List<Long> locations = entry.getValue();
            if(locations == null || locations.size()==0){
                continue;
            }
            //移除已盘点的库位
            locations.removeAll(taskLocation);
            List<Object> details = new ArrayList<Object>();
            TaskEntry taskEntry = new TaskEntry();
            TaskInfo info = new TaskInfo();

            List<BaseinfoLocation> baseinfoLocationList = new ArrayList<BaseinfoLocation>();
            for(Long locationId:locations){
                baseinfoLocationList.add(locationService.getLocation(locationId));
            }
            //排序
            baseinfoLocationList  = locationService.calcZwayOrder(baseinfoLocationList,true);

            for (BaseinfoLocation location : baseinfoLocationList) {
                StockTakingDetail detail = new StockTakingDetail();
                detail.setLocationId(location.getLocationId());
                detail.setRefTaskType(takingType);
                detail.setDetailId(RandomUtils.genId());
                detail.setTakingId(head.getTakingId());
                detail.setLocationCode(location.getLocationCode());
                details.add(detail);
                detail.setZoneId(zoneId);
            }
            if(zone==null){
                info.setTaskName("");
            }else {
                info.setTaskName(zone.getZoneName());
            }
            if(details==null || details.size()==0){
                continue;
            }
            info.setTaskOrder(Long.valueOf(details.size()+""));
            info.setType(TaskConstant.TYPE_STOCK_TAKING);
            info.setSubType(takingType);
            info.setPlanner(planner);
            info.setPlanId(head.getTakingId());
            taskEntry.setTaskInfo(info);
            taskEntry.setTaskDetailList(details);
            taskEntries.add(taskEntry);
        }
        if(taskEntries!=null && taskEntries.size()!=0) {
            iTaskRpcService.batchCreate(head, taskEntries);
        }
    }
    public void createAndDoTmpTask(Long locationId,BigDecimal realEaQty,BigDecimal realUmoQty,String barcode,Long planner) throws BizCheckedException{

        List<Object> details = new ArrayList<Object>();
        BaseinfoLocation location = locationService.getLocation(locationId);
        StockTakingHead head = new StockTakingHead();
        head.setPlanType(StockTakingConstant.TYPE_TEMPOARY);
        head.setTakingId(RandomUtils.genId());

        head.setPlanner(planner);
        head.setStatus(StockTakingConstant.Done);
        TaskEntry entry = new TaskEntry();
        TaskInfo info = new TaskInfo();
        StockTakingDetail detail = new StockTakingDetail();
        detail.setLocationCode(location.getLocationCode());
        BaseinfoItem item = null;
        CsiSku sku = null;

        //临时盘点，直接填充库位信息
        List<StockQuant> quants = quantService.getQuantsByLocationId(locationId);
        if(quants!=null && quants.size()!=0){
            StockQuant quant = quants.get(0);
            item = itemService.getItem(quant.getItemId());
            sku = skuService.getSku(quant.getSkuId());
            detail.setSkuId(quant.getSkuId());
            detail.setTheoreticalQty(quantService.getQuantQtyByContainerId(quant.getContainerId()));
            detail.setContainerId(quant.getContainerId());
            detail.setItemId(quant.getItemId());
            detail.setRealItemId(quant.getItemId());
            detail.setPackName(quant.getPackName());
            detail.setRealSkuId(sku.getSkuId());
            detail.setPackUnit(quant.getPackUnit());
            detail.setOwnerId(quant.getOwnerId());
            detail.setLotId(quant.getLotId());
            detail.setOperator(planner);
            detail.setBarcode(sku.getCode());
            detail.setSkuCode(item.getSkuCode());
            detail.setSkuName(item.getSkuName());
            detail.setPackCode(item.getPackCode());
        }

        if(item!=null){
            //判断国条是不是系统的国条,如国条为空，则是该库位无商品
            if(sku.getCode().equals(barcode) || item.getPackCode().equals(barcode)){
                detail.setRealQty(realEaQty);
                detail.setUmoQty(realUmoQty);
                SkuMap skuMap = skuMapService.getSkuMapBySkuCodeAndOwner(detail.getSkuCode(),item.getOwnerId());
                if (skuMap == null) {
                    throw new BizCheckedException("2880022", detail.getSkuCode(), "");
                }
                detail.setPrice(skuMap.getMovingAveragePrice());
                detail.setDifferencePrice(detail.getRealQty().subtract(detail.getTheoreticalQty()).multiply(detail.getPrice()));

            }
        }else {
          //系统库位无库存
            if(barcode!=null){
                CsiSku csiSku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode);
                if(sku!=null){
                    detail.setRealQty(realEaQty);
                    detail.setUmoQty(realUmoQty);
                    detail.setSkuId(csiSku.getSkuId());
                    detail.setRealSkuId(csiSku.getSkuId());
                    detail.setBarcode(barcode);
                    detail.setSkuName(sku.getSkuName());
                }else {
                    List<BaseinfoItem> items = itemService.getItemByPackCode(barcode);
                    if (items != null && items.size() != 0) {
                        detail.setPackCode(barcode);
                        detail.setRealQty(realEaQty);
                        detail.setUmoQty(realUmoQty);
                    }else {
                        throw new BizCheckedException("2550068",barcode,"");
                    }
                }

            }
        }
        detail.setStatus(StockTakingConstant.PendingAudit);
        detail.setLocationId(locationId);
        detail.setDetailId(RandomUtils.genId());
        detail.setTakingId(head.getTakingId());
        detail.setRefTaskType(StockTakingConstant.TYPE_TEMPOARY);
        details.add(detail);

        info.setStatus(TaskConstant.Done);
        info.setTaskOrder(1L);
        info.setTaskName("临时盘点库位[" + location.getLocationCode() + "]");
        info.setType(TaskConstant.TYPE_STOCK_TAKING);
        info.setSubType(StockTakingConstant.TYPE_TEMPOARY);
        info.setFinishTime(DateUtils.getCurrentSeconds());
        info.setPlanner(planner);
        info.setOperator(planner);
        info.setPlanId(head.getTakingId());
        entry.setTaskInfo(info);
        entry.setTaskDetailList(details);
        iTaskRpcService.createTask(head, entry);
    }
    public List<StockTakingDetail> fillTask(FillTakingPlanParam planParam) throws BizCheckedException {
//        TaskInfo info = baseTaskService.getTaskInfoById(planParam.getTaskId());
//        if(info.getStatus().compareTo(1L)==0){
//            iTaskRpcService.assign(planParam.getTaskId(),planParam.getOperator());
//        }
        TaskEntry entry = iTaskRpcService.getTaskEntryById(planParam.getTaskId());
        List<StockTakingDetail>  details = (List<StockTakingDetail>) (List<?>)entry.getTaskDetailList();

        stockTakingService.fillDetails(details);
        return details;
    }
    public List<StockTakingDetail> checkFillTask(Long taskId,Long operator) throws BizCheckedException {
        TaskInfo info = baseTaskService.getTaskInfoById(taskId);
        if(info==null){
            return null;
        }
        if(info.getStatus().compareTo(TaskConstant.Assigned)==0 && operator.compareTo(info.getOperator())!=0){
            return null;
        }
        if(info.getStatus().compareTo(TaskConstant.Draft)!=0 && info.getStatus().compareTo(TaskConstant.Assigned)!=0){
            return null;
        }
        FillTakingPlanParam planParam = new FillTakingPlanParam(taskId,operator);
        return this.fillTask(planParam);
        //AsyncEventService.post(planParam);
    }
    public void doneTaskDetail(List detailList) throws BizCheckedException {
        stockTakingService.doneDetails(detailList);
    }
}
