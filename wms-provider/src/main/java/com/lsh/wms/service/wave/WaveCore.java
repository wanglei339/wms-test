package com.lsh.wms.service.wave;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.pick.*;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.wave.WaveAllocService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveAllocDetail;
import com.lsh.wms.model.wave.WaveDetail;
import com.lsh.wms.model.wave.WaveHead;
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
    
    private WaveHead waveHead;
    List<OutbSoDetail> orderDetails;
    Map<Long, OutbSoHeader> mapOrder2Head;
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
    

    public int release(long iWaveId) throws BizCheckedException{
        //获取波次信息
        waveId = iWaveId;
        //执行波次准备
        this._prepare();
        //执行配货
        this._alloc();
        logger.info("begin to run pick model");
        //执行捡货模型,输出最小捡货单元
        this._executePickModel();
        //处理线路\运输计划,分配集货道
        this._allocDock();
        //创建捡货任务
        taskRpcService.batchCreate(TaskConstant.TYPE_PICK, entryList);
        //标记成功,这里有风险,就是捡货任务已经创建了,但是这里标记失败了,看咋搞????
        waveService.setStatus(waveId, WaveConstant.STATUS_RELEASE_SUCC);
        return 0;
    }

    private void _allocDock() throws BizCheckedException{

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
                info.setSubType(PickConstant.SHELF_TASK_TYPE);
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
                        detail.setPickZoneId(BigDecimal.valueOf(zone.getPickZoneId()));
                        pickTaskDetails.add(detail);
                        head.setDeliveryId(detail.getOrderId());
                        head.setTransPlan(mapOrder2Head.get(detail.getOrderId()).getTransPlan());
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

    private void _alloc() throws BizCheckedException{
        pickAllocDetailList = new ArrayList<WaveAllocDetail>();
        if(waveHead.getIsResAlloc() == 0) {
            logger.info("begin to run alloc waveId[%d]", waveId);
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
                BigDecimal leftAllocQty = new BigDecimal(detail.getOrderQty());
                for (PickModel model : modelList) {
                    if (leftAllocQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                    PickZone zone = mapZone.get(model.getPickZoneId());
                    long pickLocationId = this._getPickLocation(item, zone);
                    if(pickLocationId==0){
                        continue;
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
                        unitName = "H"+pick_ea_num;
                    } else if (pick_unit == 3) {
                        //整托盘,卧槽托盘上的商品数怎么求啊,这里是有风险的,因为实际的码盘数量可能和实际的不一样.
                        pick_ea_num = item.getPackUnit().multiply(BigDecimal.valueOf(item.getPileX() * item.getPileY() * item.getPileZ()));
                        if (pick_ea_num.compareTo(BigDecimal.ZERO) == 0) {
                        }
                        unitName = (item.getPileX() * item.getPileY() * item.getPileZ())+"H"+pick_ea_num;
                    }
                    //获取分拣分区下的可分配库存数量,怎么获取?
                    BigDecimal zone_qty = this._getPickZoneLeftAllocQty(item, zone);
                    if(zone_qty.compareTo(BigDecimal.ZERO)<=0){
                        continue;
                    }
                    int alloc_x = leftAllocQty.divide(pick_ea_num, 0, BigDecimal.ROUND_DOWN).intValue();
                    int zone_alloc_x = zone_qty.divide(pick_ea_num, 0, BigDecimal.ROUND_DOWN).intValue();
                    alloc_x = alloc_x > zone_alloc_x ? zone_alloc_x : alloc_x;
                    if(alloc_x==0){
                        continue;
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
                    pickAllocDetailList.add(allocDetail);

                    leftAllocQty = leftAllocQty.subtract(alloc_qty);
                }
                if (leftAllocQty.compareTo(BigDecimal.ZERO) > 0) {
                    logger.error("alloc "+item.getItemId()+" left "+leftAllocQty.toString());
                }
            }
            //存储配货结果
            waveService.storeAlloc(waveHead, pickAllocDetailList);
            //allocService.addAllocDetails(pickAllocDetailList);
        }else{
            logger.info("skip to run alloc waveId[%d], load from db", waveId);
            pickAllocDetailList = allocService.getAllocDetailsByWaveId(waveId);
        }
    }


    private void _prepare() throws BizCheckedException{
        mapItemAndPickZone2PickLocations = new HashMap<String, List<Long>>();
        mapItemAndPickZone2PickLocationRound = new HashMap<String, Long>();
        mapPickZoneLeftAllocQty = new HashMap<String, BigDecimal>();
        this._prepareWave();
        this._prepareOrder();
        this._preparePickModel();
    }

    private void _prepareWave() throws BizCheckedException{
        waveHead = waveService.getWave(waveId);
        if(waveHead==null){
            throw new BizCheckedException("");
        }
    }
    
    private void _prepareOrder() throws BizCheckedException{
        orderDetails = new LinkedList<OutbSoDetail>();
        mapOrder2Head = new HashMap<Long, OutbSoHeader>();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", waveId);
        List<OutbSoHeader> orders = orderService.getOutbSoHeaderList(mapQuery);
        Collections.sort(orders, new Comparator<OutbSoHeader>() {
            //此处可以设定一个排序规则,对波次中的订单优先级进行排序
            public int compare(OutbSoHeader o1, OutbSoHeader o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        for(int i = 0;i  < orders.size(); ++i){
            mapOrder2Head.put(orders.get(i).getOrderId(), orders.get(i));
            List<OutbSoDetail> details = orderService.getOutbSoDetailListByOrderId(orders.get(i).getOrderId());
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
                logger.error("get pick zone fail %d", modelList.get(i).getPickZoneId());
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
            mapZone2StoreLocations.put(zone.getPickZoneId(), locationList);
        }
    }

    private void _prepareX(){
    }

    private long _getPickLocation(BaseinfoItem item, PickZone zone){
        /*
        这其实应该有一个捡货位分配算法.
        地堆区的捡货是个蛋疼的问题.
         */
        String key = String.format("%d-%d", item.getItemId(), zone.getPickZoneId());
        List<Long> pickLocationIds = mapItemAndPickZone2PickLocations.get(key);
        if(pickLocationIds == null) {
            List<Long> pickLocationIdList = new ArrayList<Long>();
            final List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationList(item.getItemId());
            //判断此区域是否有对应的捡货位
            List<BaseinfoLocation> locationList = mapZone2StoreLocations.get(zone.getPickZoneId());
            for(BaseinfoItemLocation pickLocation : itemLocationList){
                for(BaseinfoLocation location : locationList){
                    if ( pickLocation.getPickLocationid() >= location.getLeftRange()
                            && pickLocation.getPickLocationid() <= location.getRightRange()){
                        pickLocationIdList.add(pickLocation.getPickLocationid());
                    }
                }
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

    private BigDecimal _getPickZoneLeftAllocQty(BaseinfoItem item, PickZone zone){
        //锁库存.怎么锁??
        String key = String.format("%d-%d", item.getItemId(), zone.getPickZoneId());
        BigDecimal leftQty = mapPickZoneLeftAllocQty.get(key);
        if ( leftQty == null ) {
            //可用库存=仓位有效stock_quant求和(怎么求)-wave_detail的有效alloc数量+wave_detail中的捡货数量
            BigDecimal stockQty = new BigDecimal("0.0000");
            List<BaseinfoLocation> locationList = mapZone2StoreLocations.get(zone.getPickZoneId());
            for(BaseinfoLocation location : locationList) {
                stockQty = stockQty.add(stockQuantService.getRealtimeQty(location,  item.getItemId()));
            }
            Map<String, Object> mapSumQuery = new HashMap<String, Object>();
            mapSumQuery.put("skuId", item.getSkuId());
            mapSumQuery.put("ownerId", item.getOwnerId());
            mapSumQuery.put("isLive", 1);
            mapSumQuery.put("isValid", 1);
            mapSumQuery.put("pickZoneId", zone.getPickZoneId());
            BigDecimal unPickedQty = waveService.getUnPickedQty(mapSumQuery);
            leftQty = stockQty.subtract(unPickedQty);
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
