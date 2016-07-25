package com.lsh.wms.service.pick.wave;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.PickConstant;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.pick.*;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.service.pick.wave.split.SplitModel;
import com.lsh.wms.service.pick.wave.split.SplitNode;
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
    PickWaveService waveService;
    @Autowired
    PickAllocService allocService;
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

    public int release(long iWaveId) throws BizCheckedException{
        //获取波次信息
        PickWaveHead waveHead = waveService.getWave(iWaveId);
        if(waveHead==null){
            return -1;
        }
        List<OutbSoDetail> order_details = new LinkedList<OutbSoDetail>();
        Map<Long, OutbSoHeader> mapOrder2Head = new HashMap<Long, OutbSoHeader>();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", iWaveId);
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
            order_details.addAll(details);
        }
        //设置释放中状态
        //获取捡货模版
        PickModelTemplate modelTpl = modelService.getPickModelTemplate(waveHead.getPickModelTemplateId());
        List<PickModel> modelList = modelService.getPickModelsByTplId(waveHead.getPickModelTemplateId());
        Collections.sort(modelList,new Comparator<PickModel> (){
            //按捡货区权重排序
            public int compare(PickModel arg0, PickModel arg1) {
                return arg1.getPickWeight().compareTo(arg0.getPickWeight());
            }
        });
        //获取分区信息
        List<PickZone> zoneList = new LinkedList<PickZone>();
        Map<Long, PickZone> mapZone = new HashMap<Long, PickZone>();
        for(int i = 0; i < modelList.size(); ++i){
            PickZone zone = zoneService.getPickZone(modelList.get(i).getPickZoneId());
            if(zone == null){
                logger.error("get pick zone fail %d", modelList.get(i).getPickZoneId());
                return -1;
            }
            zoneList.add(zone);
            mapZone.put(zone.getPickZoneId(), zone);
        }
        //执行配货
        List<PickAllocDetail> pickAllocDetailList = new ArrayList<PickAllocDetail>();
        if(waveHead.getIsResAlloc() == 0) {
            logger.info("begin to run alloc waveId[%d]", iWaveId);
            for (int i = 0; i < order_details.size(); ++i) {
                OutbSoDetail detail = order_details.get(i);
                int zone_idx = 0;
                //获取商品的基本信息
                BaseinfoItem item = itemService.getItem(mapOrder2Head.get(detail.getOrderId()).getOwnerUid(), detail.getSkuId());
                if (item == null) {
                    logger.error("item get fail %s", item.getItemId());
                    return -1;
                }
                BigDecimal leftAllocQty = new BigDecimal(detail.getOrderQty());
                for (PickZone zone : zoneList) {
                    if (leftAllocQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                    //判断此区域是否有对应的捡货位
                    long pick_unit = zone.getPickUnit();
                    BigDecimal pick_ea_num = null;
                    if (pick_unit == 1) {
                        //ea
                        pick_ea_num = BigDecimal.valueOf(1L);
                    } else if (pick_unit == 2) {
                        //整箱
                        pick_ea_num = item.getPackUnit();
                    } else if (pick_unit == 3) {
                        //整托盘,卧槽托盘上的商品数怎么求啊,这里是有风险的,因为实际的码盘数量可能和实际的不一样.
                        pick_ea_num = item.getPackUnit().multiply(BigDecimal.valueOf(item.getPileX() * item.getPileY() * item.getPileZ()));
                        if (pick_ea_num.compareTo(BigDecimal.ZERO) == 0) {
                        }
                    }
                    //获取分拣分区下的可分配库存数量,怎么获取?
                    long zone_qty = 10000;
                    int alloc_x = leftAllocQty.divide(pick_ea_num).intValue();
                    int zone_alloc_x = BigDecimal.valueOf(zone_qty).divide(pick_ea_num).intValue();
                    alloc_x = alloc_x > zone_alloc_x ? zone_alloc_x : alloc_x;
                    BigDecimal alloc_qty = pick_ea_num.multiply(BigDecimal.valueOf(alloc_x));
                    //锁库存.怎么锁??
                    PickAllocDetail allocDetail = new PickAllocDetail();
                    allocDetail.setId(RandomUtils.genId());
                    allocDetail.setSkuId(detail.getSkuId());
                    allocDetail.setAllocQty(alloc_qty);
                    //allocDetail.setLocId(detail.getLotNum()); ??
                    allocDetail.setOrderId(detail.getOrderId());
                    allocDetail.setOwnerId(mapOrder2Head.get(detail.getOrderId()).getOwnerUid());
                    allocDetail.setPickZoneId(zone.getPickZoneId());
                    allocDetail.setReqQty(new BigDecimal(0));
                    //allocDetail.setSupplierId(mapOrder2Head.get(detail.getOrderId()).get); ??
                    allocDetail.setWaveId(iWaveId);
                    pickAllocDetailList.add(allocDetail);

                    leftAllocQty = leftAllocQty.subtract(alloc_qty);
                }
            }
            //存储配货结果
            waveService.storeAlloc(waveHead, pickAllocDetailList);
            //allocService.addAllocDetails(pickAllocDetailList);
        }else{
            logger.info("skip to run alloc waveId[%d], load from db", iWaveId);
            pickAllocDetailList = allocService.getAllocDetailsByWaveId(iWaveId);
        }

        logger.info("begin to run pick model");
        //执行捡货模型,输出最小捡货单元
        List<PickTaskHead> taskHeads = new LinkedList<PickTaskHead>();
        List<PickTaskDetail> taskDetails = new LinkedList<PickTaskDetail>();
        List<TaskEntry> entryList = new LinkedList<TaskEntry>();
        for(int zidx = 0; zidx < zoneList.size(); ++zidx){
            PickZone zone = zoneList.get(zidx);
            List<SplitNode> splitNodes = new LinkedList<SplitNode>();
            {
                //初始化分裂数据
                SplitNode node = new SplitNode();
                node.details = new ArrayList<PickTaskDetail>();
                for (PickAllocDetail ad : pickAllocDetailList) {
                    PickTaskDetail detail = new PickTaskDetail();
                    ObjUtils.bean2bean(ad, detail);
                    node.details.add(detail);
                }
                splitNodes.add(node);
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
                    splitModel = (SplitModel) Class.forName("com.lsh.wms.service.pick.wave.split."+modelName).newInstance();
                } catch (Exception e){
                    logger.error("class init fail "+modelName);
                    return -1;
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
                info.setPlanId(iWaveId);
                List<Object> pickTaskDetails = new LinkedList<Object>();
                info.setType(TaskConstant.TYPE_PICK);
                info.setSubType(PickConstant.SHELF_TASK_TYPE);
                PickTaskHead head = new PickTaskHead();
                head.setWaveId(iWaveId);
                //head.setPickTaskId(RandomUtils.genId());
                head.setPickTaskName(String.format("波次[%d]-捡货任务[%d]", iWaveId, taskHeads.size()+1));
                for(int j = 0; j < bestCutPlan[i]; j++){
                    SplitNode node = stopNodes.get(iChooseIdx+j);
                    for(int k = 0; k < node.details.size(); ++k){
                        PickTaskDetail detail = node.details.get(k);
                        //detail.setPickTaskId(head.getPickTaskId());
                        detail.setPickZoneId(BigDecimal.valueOf(zone.getPickZoneId()));
                        taskDetails.add(detail);
                        pickTaskDetails.add(detail);
                    }
                }
                iChooseIdx += bestCutPlan[i];
                taskHeads.add(head);
                entry.setTaskInfo(info);
                entry.setTaskHead(head);
                entry.setTaskDetailList(pickTaskDetails);
                entryList.add(entry);
            }
        }

        taskRpcService.batchCreate(TaskConstant.TYPE_PICK, entryList);
        //TaskEntry entry = new TaskEntry();
        //entry.set
        //iTaskRpcService.create(TaskConstant.TYPE_PICK, entry);
        //waveService.storePickTask(iWaveId, taskHeads, taskDetails);
        waveService.setStatus(iWaveId, WaveConstant.STATUS_RELEASE_SUCC);
        return 0;
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
