package com.lsh.wms.service.wave;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.CollectionUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.pick.*;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.stock.StockUtil;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.core.service.wave.WaveAllocService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.model.wave.WaveAllocDetail;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveTemplate;
import com.lsh.wms.service.wave.split.SplitModel;
import com.lsh.wms.service.wave.split.SplitNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

import java.util.*;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Component
public class WaveCore {
    private static Logger logger = LoggerFactory.getLogger(WaveCore.class);

    @Autowired
    WaveService waveService;
    @Autowired
    WaveAllocService allocService;
    @Autowired
    PickTaskService taskService;
    @Autowired
    PickModelService modelService;
    @Autowired
    PickZoneService zoneService;
    @Autowired
    SoOrderService orderService;
    @Autowired
    ItemService itemService;
    @Reference
    private ITaskRpcService taskRpcService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ItemLocationService itemLocationService;
    @Autowired
    private StockQuantService stockQuantService;
    @Autowired
    private WaveTemplateService waveTemplateService;
    @Autowired
    private MessageService messageService;
    
    private WaveHead waveHead;
    List<OutbSoDetail> orderDetails;
    List<OutbSoHeader> orderList;
    Map<Long, OutbSoHeader> mapOrder2Head;
    WaveTemplate waveTemplate;
    List<BaseinfoLocation> unUsedCollectionRoadList;
    Map<String, BaseinfoLocation> mapRoute2CollectRoad;
    Map<Long, Long> mapOrder2CollectBin;
    PickModelTemplate modelTpl;
    List<PickModel> modelList;
    List<PickZone> zoneList;
    Map<Long, PickZone> mapZone;
    Map<Long, List<BaseinfoLocation>> mapZone2StoreLocations;
    private long waveId;
    Map<String, List<Long>> mapItemAndPickZone2PickLocations;
    Map<String, Long> mapItemAndPickZone2PickLocationRound;
    Map<String, BigDecimal> mapPickZoneLeftAllocQty;
    List<WaveAllocDetail> pickAllocDetailList;
    List<TaskEntry> entryList;
    Map<String, Map<Long, BigDecimal>> mapItemArea2LocationInventory;
    

    public int release(long iWaveId) throws BizCheckedException{
        //获取波次信息
        waveId = iWaveId;
        //执行波次准备
        this._prepare();
        //处理线路\运输计划,分配集货道
        this._allocDock();
        //执行配货
        this._alloc();
        logger.info("begin to run outbound model");
        //执行捡货模型,输出最小捡货单元
        this._executePickModel();
        //锁定集货区,记得发货的时候释放哟
        for(BaseinfoLocation locaion : mapRoute2CollectRoad.values()) {
            locationService.lockLocation(locaion.getLocationId());
        }
        //创建捡货任务
        taskRpcService.batchCreate(TaskConstant.TYPE_PICK, entryList);
        //标记成功,这里有风险,就是捡货任务已经创建了,但是这里标记失败了,看咋搞????
        waveService.setStatus(waveId, WaveConstant.STATUS_RELEASE_SUCC);
        //发给调度创建纪录,调度器可能需要做些处理
        try {
            Set<Long> items = new HashSet<Long>();
            for (OutbSoDetail detail : orderDetails) {
                items.add(detail.getItemId());
            }
            TaskMsg msg = new TaskMsg();
            msg.setType(TaskConstant.EVENT_WAVE_RELEASE);
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("itemList", items.toArray());
            msg.setMsgBody(body);
            messageService.sendMessage(msg);
        } catch (Exception e){
            e.printStackTrace();
            logger.warn("report wave info to redis fail");
        }
        return 0;
    }

    private void _allocDock() throws BizCheckedException{
        Map<Long, Long> mapCollectBinCount = new HashMap<Long, Long>();
        Map<Long, List<BaseinfoLocation>> mapCollectRoad2Bin = new HashMap<Long, List<BaseinfoLocation>>();
        int iUseIdx = 0;
        for(OutbSoHeader order : orderList){
            Long collectAllocId = 0L;
            String rout = order.getTransPlan();
            BaseinfoLocation collecRoad = mapRoute2CollectRoad.get(rout);
            if ( collecRoad == null ){
                if(iUseIdx >= unUsedCollectionRoadList.size()){
                    throw  new BizCheckedException("2040012");
                }
                collecRoad = unUsedCollectionRoadList.get(iUseIdx);
                iUseIdx++;
                mapRoute2CollectRoad.put(rout, collecRoad);
                mapCollectBinCount.put(collecRoad.getLocationId(), 0L);
            }
            long binIdx = mapCollectBinCount.get(collecRoad.getLocationId());
            if(waveTemplate.getUseCollectBin() == 0){
                //不使用精细的集货位
                collectAllocId = collecRoad.getLocationId();
            }else{
                //使用精细的集货位
                List<BaseinfoLocation> collectionBins = mapCollectRoad2Bin.get(collecRoad.getLocationId());
                if(collectionBins == null){
                    if(collecRoad.getIsLeaf()==1){
                        //卧槽,已经是叶子节点了,没有集货位啊,怎么搞啊
                        collectionBins = new ArrayList<BaseinfoLocation>();
                    }
                    collectionBins = locationService.getChildrenLocationsByType(collecRoad.getLocationId(), LocationConstant.COLLECTION_BIN);
                    mapCollectRoad2Bin.put(collecRoad.getLocationId(), collectionBins);
                }
                if(order.getWaveIndex()>= collectionBins.size()){
                    //卧槽,越界了,根本放不下啊,就放在道上将就将就吧
                    collectAllocId = collecRoad.getLocationId();
                }else{
                    //哈哈,终于找到你拉兄弟
                    //不过这里我们不处理集货位的占用处理逻辑,TMS你不要瞎搞啊,否则就把我害惨了
                    collectAllocId = collectionBins.get(order.getWaveIndex()).getLocationId();
                }
            }
            mapOrder2CollectBin.put(order.getOrderId(), collectAllocId);
        }
    }

    private void _executePickModel() throws BizCheckedException{
        //List<PickTaskHead> taskHeads = new LinkedList<PickTaskHead>();
        //List<WaveDetail> taskDetails = new LinkedList<WaveDetail>();
        entryList = new LinkedList<TaskEntry>();
        for(int zidx = 0; zidx < zoneList.size(); ++zidx){
            PickZone zone = zoneList.get(zidx);
            List<SplitNode> splitNodes = new LinkedList<SplitNode>();
            {
                //初始化分裂数据
                SplitNode node = new SplitNode();
                node.details = new ArrayList<WaveDetail>();
                for (WaveAllocDetail ad : pickAllocDetailList) {
                    if(ad.getPickZoneId() != zone.getPickZoneId()){
                        continue;
                    }
                    WaveDetail detail = new WaveDetail();
                    ObjUtils.bean2bean(ad, detail);
                    detail.setAllocCollectLocation(mapOrder2CollectBin.get(detail.getOrderId()));
                    node.details.add(detail);
                }
                if(node.details.size()>0) {
                    splitNodes.add(node);
                }
            }
            if(splitNodes.size()==0){
                continue;
            }
            List<SplitNode> stopNodes = new LinkedList<SplitNode>();
            PickModel model = modelList.get(zidx);
            String splitModelNames[] = {
                    "SplitModelSetGroup",
                    "SplitModelBigItem",
                    "SplitModelSet",
                    "SplitModelSmallItem",
                    "SplitModelOrder",
                    "SplitModelContainer", //这个必须是最后一个,否则就会出大问题.
            };
            for(String modelName : splitModelNames){
                SplitModel splitModel = null;
                try {
                    splitModel = (SplitModel) Class.forName("com.lsh.wms.service.wave.split."+modelName).newInstance();
                } catch (Exception e){
                    logger.error("class init fail "+modelName);
                    throw  new BizCheckedException("");
                }
                splitModel.init(model, splitNodes);
                splitModel.split(stopNodes);
                splitNodes = splitModel.getSplitedNodes();
            }
            if(splitNodes.size()>0){
                //卧槽,这是怎么回事,出bug了?
            }
            //转换成为任务
            //多zone的捡货单元不可混合,所用分开执行
            long iContainerTake = model.getContainerNumPerTask();
            //计算最佳划分组合
            long [] bestCutPlan = this.getBestCutPlan(stopNodes.size(), iContainerTake);
            int iChooseIdx = 0;
            for(int i = 0; i < bestCutPlan.length; ++i){
                TaskEntry entry = new TaskEntry();
                TaskInfo info = new TaskInfo();
                info.setPlanId(waveId);
                info.setWaveId(waveId);
                List<Object> pickTaskDetails = new LinkedList<Object>();
                info.setType(TaskConstant.TYPE_PICK);
                info.setSubType(zone.getPickType());
                PickTaskHead head = new PickTaskHead();
                head.setWaveId(waveId);
                head.setPickType(1);
                //head.setTransPlan("");
                //head.setDeliveryId(1L);
                info.setTaskName(String.format("波次[%d]-捡货任务[%d]", waveId, entryList.size()+1));
                for(int j = 0; j < bestCutPlan[i]; j++){
                    SplitNode node = stopNodes.get(iChooseIdx+j);
                    for(int k = 0; k < node.details.size(); ++k){
                        WaveDetail detail = node.details.get(k);
                        detail.setPickZoneId(zone.getPickZoneId());
                        pickTaskDetails.add(detail);
                        head.setDeliveryId(detail.getOrderId());
                        info.setOrderId(detail.getOrderId());
                        head.setTransPlan(mapOrder2Head.get(detail.getOrderId()).getTransPlan());
                        head.setAllocCollectLocation(detail.getAllocCollectLocation());
                    }
                }
                iChooseIdx += bestCutPlan[i];
                entry.setTaskInfo(info);
                entry.setTaskHead(head);
                entry.setTaskDetailList(pickTaskDetails);
                entryList.add(entry);
            }
        }
    }

    private BigDecimal _allocNormal(OutbSoDetail detail, PickZone zone, BaseinfoItem item, BaseinfoLocation location, BigDecimal leftAllocQty) throws BizCheckedException{
        long pickLocationId = this._getPickLocation(item, location);
        if (pickLocationId == 0) {
            return leftAllocQty;
        }
        long pick_unit = zone.getPickUnit();
        BigDecimal pick_ea_num = null;
        String unitName = "";
        if (pick_unit == 1) {
            //ea
            pick_ea_num = BigDecimal.valueOf(1L);
            unitName = "EA";
        } else if (pick_unit == 2) {
            //整箱
            pick_ea_num = item.getPackUnit();
            //TODO ZHE  LI YOU WENTI
            unitName = StockUtil.PackUnit2Uom(item.getPackUnit(), "EA");
        } else if (pick_unit == 3) {
            //整托盘,卧槽托盘上的商品数怎么求啊,这里是有风险的,因为实际的码盘数量可能和实际的不一样.
            pick_ea_num = item.getPackUnit().multiply(BigDecimal.valueOf(item.getPileX() * item.getPileY() * item.getPileZ()));
            if (pick_ea_num.compareTo(BigDecimal.ZERO) == 0) {
            }
            unitName = (item.getPileX() * item.getPileY() * item.getPileZ()) + StockUtil.PackUnit2Uom(item.getPackUnit(), "EA");
        }
        //获取分拣分区下的可分配库存数量,怎么获取?
        BigDecimal zone_qty = this._getPickZoneLeftAllocQty(item, location);
        if (zone_qty.compareTo(BigDecimal.ZERO) <= 0) {
            return leftAllocQty;
        }
        int alloc_x = leftAllocQty.divide(pick_ea_num, 0, BigDecimal.ROUND_DOWN).intValue();
        int zone_alloc_x = zone_qty.divide(pick_ea_num, 0, BigDecimal.ROUND_DOWN).intValue();
        alloc_x = alloc_x > zone_alloc_x ? zone_alloc_x : alloc_x;
        if (alloc_x == 0) {
            return leftAllocQty;
        }
        BigDecimal alloc_qty = pick_ea_num.multiply(BigDecimal.valueOf(alloc_x));
        WaveAllocDetail allocDetail = new WaveAllocDetail();
        allocDetail.setId(RandomUtils.genId());
        allocDetail.setSkuId(detail.getSkuId());
        allocDetail.setAllocQty(alloc_qty);
        //allocDetail.setLocId(detail.getLotNum()); ??
        allocDetail.setOrderId(detail.getOrderId());
        allocDetail.setOwnerId(mapOrder2Head.get(detail.getOrderId()).getOwnerUid());
        allocDetail.setPickZoneId(zone.getPickZoneId());
        allocDetail.setReqQty(new BigDecimal(0));
        allocDetail.setAllocPickLocation(pickLocationId);
        allocDetail.setItemId(item.getItemId());
        //allocDetail.setSupplierId(mapOrder2Head.get(detail.getOrderId()).get); ??
        allocDetail.setWaveId(waveId);
        allocDetail.setAllocUnitQty(BigDecimal.valueOf(alloc_x));
        allocDetail.setAllocUnitName(unitName);
        allocDetail.setPickAreaLocation(location.getLocationId());
        pickAllocDetailList.add(allocDetail);
        leftAllocQty = leftAllocQty.subtract(alloc_qty);
        return leftAllocQty;
    }

    private BigDecimal _allocStockPickSame(OutbSoDetail detail, PickZone zone, BaseinfoItem item, BaseinfoLocation location, BigDecimal leftAllocQty) throws BizCheckedException{
        //没补货机制的区域,存捡合一,得精细计算到每个货位
        String key = String.format("%d-%d", item.getItemId(), location.getLocationId());
        Map<Long, BigDecimal> locationInventory = mapItemArea2LocationInventory.get(key);
        if(locationInventory == null) {
            //pick area location
            BaseinfoLocation pickArea = locationService.getLocation(location.getLocationId());
            //getLocationUnAllocQty
            locationInventory = waveService.getLocationUnAllocQty(pickArea, item.getItemId());
            mapItemArea2LocationInventory.put(key, locationInventory);
        }
        List<WaveDetail> splitDetails = new ArrayList<WaveDetail>();
        List<Map> allocInfos = waveService.allocByLocation(locationInventory, leftAllocQty, 0L);
        for (Map info : allocInfos) {
            WaveAllocDetail allocDetail = new WaveAllocDetail();
            allocDetail.setId(RandomUtils.genId());
            allocDetail.setSkuId(detail.getSkuId());
            allocDetail.setAllocQty((BigDecimal) info.get("allocQty"));
            //allocDetail.setLocId(detail.getLotNum()); ??
            allocDetail.setOrderId(detail.getOrderId());
            allocDetail.setOwnerId(mapOrder2Head.get(detail.getOrderId()).getOwnerUid());
            allocDetail.setPickZoneId(zone.getPickZoneId());
            allocDetail.setReqQty(new BigDecimal(0));
            allocDetail.setAllocPickLocation((Long) info.get("locationId"));
            allocDetail.setItemId(item.getItemId());
            //allocDetail.setSupplierId(mapOrder2Head.get(detail.getOrderId()).get); ??
            allocDetail.setWaveId(waveId);
            allocDetail.setAllocUnitQty((BigDecimal) info.get("allocQty"));
            allocDetail.setAllocUnitName("EA");
            allocDetail.setPickAreaLocation(location.getLocationId());
            pickAllocDetailList.add(allocDetail);
            leftAllocQty = leftAllocQty.subtract((BigDecimal) info.get("allocQty"));
        }
        return leftAllocQty;
    }
    private void _alloc() throws BizCheckedException{
        //进行库存精确锁定
        //按捡货分区设置设定库存锁定粒度
        //特殊货位属性精确锁定库存
        //库存锁定全局只允许单向运行,否则将全部错误
        //怎么加锁呢?草勒.
        //地堆区有货,优先配在地堆区
        pickAllocDetailList = new ArrayList<WaveAllocDetail>();
        if(waveHead.getIsResAlloc() == 0) {
            logger.info("begin to run alloc waveId{1}", waveId);
            for (int i = 0; i < orderDetails.size(); ++i) {
                OutbSoDetail detail = orderDetails.get(i);
                int zone_idx = 0;
                //获取商品的基本信息
                BaseinfoItem item = itemService.getItem(mapOrder2Head.get(detail.getOrderId()).getOwnerUid(), detail.getSkuId());
                if (item == null) {
                    logger.error("item get fail %d", detail.getSkuId());
                    throw new BizCheckedException("");
                }
                //获取商品的捡货位
                BigDecimal leftAllocQty = detail.getOrderQty();
                for (PickModel model : modelList) {
                    if (leftAllocQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                    PickZone zone = mapZone.get(model.getPickZoneId());
                    List<BaseinfoLocation> locationList = mapZone2StoreLocations.get(zone.getPickZoneId());
                    for(BaseinfoLocation location : locationList) {
                        if (leftAllocQty.compareTo(BigDecimal.ZERO) <= 0) {
                            break;
                        }
                        if(location.getType() == LocationConstant.SPLIT_AREA
                                || location.getType() == LocationConstant.SPLIT_SHELF
                                || location.getType() == LocationConstant.SPLIT_SHELF_BIN
                                || location.getType() == LocationConstant.SPLIT_SHELF_LEVEL)
                        {
                            leftAllocQty = this._allocStockPickSame(detail, zone, item, location, leftAllocQty);
                        } else {
                            //有补货机制的区域,不考虑捡货位货量,只考虑区域货量
                            leftAllocQty = this._allocNormal(detail, zone, item, location, leftAllocQty);
                        }
                    }
                }
                if (leftAllocQty.compareTo(BigDecimal.ZERO) > 0) {
                    logger.error("alloc "+item.getItemId()+" left "+leftAllocQty.toString());
                }
            }
            //存储配货结果
            waveService.storeAlloc(waveHead, pickAllocDetailList);
        }else{
            logger.info("skip to run alloc waveId[%d], load from db", waveId);
            pickAllocDetailList = allocService.getAllocDetailsByWaveId(waveId);
        }
    }


    private void _prepare() throws BizCheckedException{
        mapItemAndPickZone2PickLocations = new HashMap<String, List<Long>>();
        mapItemAndPickZone2PickLocationRound = new HashMap<String, Long>();
        mapPickZoneLeftAllocQty = new HashMap<String, BigDecimal>();
        mapRoute2CollectRoad = new HashMap<String, BaseinfoLocation>();
        mapOrder2CollectBin = new HashMap<Long, Long>();
        mapItemArea2LocationInventory = new HashMap<String, Map<Long, BigDecimal>>();
        this._prepareWave();
        this._prepareOrder();
        this._preparePickModel();
    }

    private void _prepareWave() throws BizCheckedException{
        waveHead = waveService.getWave(waveId);
        if(waveHead==null){
            throw new BizCheckedException("2040001");
        }
        //集货模版
        waveTemplate = waveTemplateService.getWaveTemplate(waveHead.getWaveTemplateId());
        if(waveTemplate==null){
            throw new BizCheckedException("2040008");
        }
        //获取集货组
        BaseinfoLocation locGrp = locationService.getLocation(waveTemplate.getCollectLocations());
        if(locGrp==null){
            throw new BizCheckedException("2040009");
        }
        //获取集货道列表
        List<BaseinfoLocation> collectLocations = locationService.getChildrenLocationsByType(locGrp.getLocationId(), LocationConstant.COLLECTION_ROAD);
        if(collectLocations.size()==0){
            throw new BizCheckedException("2040010");
        }
        unUsedCollectionRoadList = new ArrayList<BaseinfoLocation>();
        for(BaseinfoLocation location : collectLocations){
            if(location.getCanUse()==1 && location.getIsLocked()==0){
                unUsedCollectionRoadList.add(location);
            }
        }
        //无可用的集货道
        if(unUsedCollectionRoadList.size()==0){
            throw new BizCheckedException("2040011");
        }
        //集货到分配排序
        //TODO collection sort
    }
    
    private void _prepareOrder() throws BizCheckedException{
        orderDetails = new LinkedList<OutbSoDetail>();
        mapOrder2Head = new HashMap<Long, OutbSoHeader>();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", waveId);
        orderList = orderService.getOutbSoHeaderList(mapQuery);
        Collections.sort(orderList, new Comparator<OutbSoHeader>() {
            //此处可以设定一个排序规则,对波次中的订单优先级进行排序
            public int compare(OutbSoHeader o1, OutbSoHeader o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        for(int i = 0;i  < orderList.size(); ++i){
            mapOrder2Head.put(orderList.get(i).getOrderId(), orderList.get(i));
            List<OutbSoDetail> details = orderService.getOutbSoDetailListByOrderId(orderList.get(i).getOrderId());
            orderDetails.addAll(details);
        }
    }

    private void _preparePickModel() throws BizCheckedException{
        //获取捡货模版
        modelTpl = modelService.getPickModelTemplate(waveHead.getPickModelTemplateId());
        modelList = modelService.getPickModelsByTplId(waveHead.getPickModelTemplateId());
        if(modelList.size()==0){
            throw new BizCheckedException("2040006");
        }
        Collections.sort(modelList,new Comparator<PickModel> (){
            //按捡货区权重排序
            public int compare(PickModel arg0, PickModel arg1) {
                return arg1.getPickWeight().compareTo(arg0.getPickWeight());
            }
        });
        //获取分区信息
        zoneList = new LinkedList<PickZone>();
        mapZone = new HashMap<Long, PickZone>();
        for(int i = 0; i < modelList.size(); ++i){
            PickZone zone = zoneService.getPickZone(modelList.get(i).getPickZoneId());
            if(zone == null){
                logger.error("get outbound zone fail %d", modelList.get(i).getPickZoneId());
                throw new BizCheckedException("");
            }
            zoneList.add(zone);
            mapZone.put(zone.getPickZoneId(), zone);
        }
        //将捡货分区location详细信息
        mapZone2StoreLocations = new HashMap<Long, List<BaseinfoLocation>>();
        for(PickModel model : modelList) {
            PickZone zone = mapZone.get(model.getPickZoneId());
            String[] pickLocations = zone.getLocations().split(",");

            List<BaseinfoLocation> locationList = new LinkedList<BaseinfoLocation>();
            for(String loc : pickLocations) {
                if(loc.trim().compareTo("")==0){
                    logger.error("hee");
                    throw new BizCheckedException("");
                }
                locationList.add(locationService.getLocation(Long.valueOf(loc)));
            }
            Collections.sort(locationList, new Comparator<BaseinfoLocation>() {
                //地堆区在前面
                public int compare(BaseinfoLocation o1, BaseinfoLocation o2) {
                    if(o1.getType() == LocationConstant.FLOOR_BIN || o1.getType() == LocationConstant.FLOOR){
                        //01在前
                        return -1;
                    }else if (o2.getType() == LocationConstant.FLOOR_BIN || o2.getType() == LocationConstant.FLOOR){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });
            mapZone2StoreLocations.put(zone.getPickZoneId(), locationList);
        }
    }

    private void _prepareX(){
    }

    private long _getPickLocation(BaseinfoItem item, BaseinfoLocation location){
        /*
        这其实应该有一个捡货位分配算法.
        地堆区的捡货是个蛋疼的问题.
         */
        String key = String.format("%d-%d", item.getItemId(), location.getLocationId());
        List<Long> pickLocationIds = mapItemAndPickZone2PickLocations.get(key);
        if(pickLocationIds == null) {
            List<Long> pickLocationIdList = new ArrayList<Long>();
            final List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationList(item.getItemId());
            //判断此区域是否有对应的捡货位
            for(BaseinfoItemLocation pickLocation : itemLocationList) {
                if (pickLocation.getPickLocationid() >= location.getLeftRange()
                        && pickLocation.getPickLocationid() <= location.getRightRange()) {
                    pickLocationIdList.add(pickLocation.getPickLocationid());
                }
            }
            if(pickLocationIdList.size()==0 &&
                    (location.getType() == LocationConstant.FLOOR || location.getType() == LocationConstant.FLOOR_BIN) ) {
                //地堆区不设置也可以直接捡货
                pickLocationIdList.add(location.getLocationId());
            }
            mapItemAndPickZone2PickLocations.put(key, pickLocationIdList);
            pickLocationIds = pickLocationIdList;
        }
        if(pickLocationIds.size()==0){
            return 0;
        }
        if(mapItemAndPickZone2PickLocationRound.get(key) == null){
            mapItemAndPickZone2PickLocationRound.put(key, 0L);
        }
        Long round = mapItemAndPickZone2PickLocationRound.get(key);
        mapItemAndPickZone2PickLocationRound.put(key, round+1);
        return pickLocationIds.get((int)(round%pickLocationIds.size()));
    }

    private BigDecimal _getPickZoneLeftAllocQty(BaseinfoItem item, BaseinfoLocation location){
        //锁库存.怎么锁??
        String key = String.format("%d-%d", item.getItemId(), location.getLocationId());
        BigDecimal leftQty = mapPickZoneLeftAllocQty.get(key);
        if ( leftQty == null ) {
            /*存捡合一类型,无补货机制,需要判断具体捡货位的数量来做分配*/
            leftQty = waveService.getUnAllocQty(item.getItemId(), location);
            mapPickZoneLeftAllocQty.put(key, leftQty);
        }
        return leftQty;
    }

    private long[] getBestCutPlan(long num, long maxPer){
        int needNum = (int)Math.ceil(num/(float)maxPer);
        long []bestCutPlan = new long[needNum];
        for(int i = 0;i < needNum-1; ++i){
            bestCutPlan[i] = maxPer;
        }
        bestCutPlan[needNum-1] = num - maxPer*(needNum-1);
        if(needNum>1) {
            while(bestCutPlan[needNum-1] < bestCutPlan[needNum-2]-1) {
                int idx = needNum - 1;
                while (idx > 0 && bestCutPlan[needNum - 1] < bestCutPlan[needNum - 2]) {
                    bestCutPlan[needNum-1]++;
                    bestCutPlan[idx]--;
                }
            }
        }
        return bestCutPlan;
    }

}
