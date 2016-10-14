package com.lsh.wms.service.seed;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.api.service.seed.ISeedProveiderRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.seed.SeedingTaskHead;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.service.po.PoRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/8/2.
 */
@Service(protocol = "dubbo")
public class SeedProviderRpcService implements ISeedProveiderRpcService {
    private static final Logger logger = LoggerFactory.getLogger(SeedProviderRpcService.class);

    @Autowired
    CsiSkuService skuService;

    @Reference
    ITaskRpcService taskRpcService;

    @Autowired
    PoOrderService poOrderService;

    @Reference
    IPoRpcService poRpcService;

    @Autowired
    SoOrderService soOrderService;

    @Autowired
    ItemService itemService;

    @Autowired
    private RedisStringDao redisStringDao;

    @Reference
    ILocationRpcService locationRpcService;

    @Autowired
    BaseTaskService baseTaskService;
    @Autowired
    ContainerService containerService;

    public Long getTask( Map<String, Object> mapQuery) throws BizCheckedException{
        String orderId = mapQuery.get("orderId").toString().trim();
        String barcode = mapQuery.get("barcode").toString().trim();
        Object containerId = mapQuery.get("containerId");
        Long skuId = 0L;
        Map<String, Object> query = new HashMap<String, Object>();
        CsiSku sku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode);
        if(sku==null){
            throw new BizCheckedException("2880001");
        }
        skuId= sku.getSkuId();
        query.put("orderId",orderId);
        query.put("skuId",skuId);
        query.put("status",TaskConstant.Draft);
        query.put("orderBy","taskOrder");
        query.put("orderType", "asc");
        List<TaskEntry> entries = taskRpcService.getTaskList(TaskConstant.TYPE_SEED, query);
        if(entries==null || entries.size()==0){
            return 0L;
        }
        if(containerId != null){
            BaseinfoContainer container = containerService.getContainer(Long.valueOf(containerId.toString().trim()));
            if(container==null){
                throw new BizCheckedException("2880013");
            }
        }else {
            containerId = 0L;
        }
        TaskEntry entry = entries.get(0);
        TaskInfo info = entry.getTaskInfo();
        info.setContainerId(Long.valueOf(containerId.toString().trim()));
        entry.setTaskInfo(info);
        taskRpcService.update(TaskConstant.TYPE_SEED,entry);
        return entries.get(0).getTaskInfo().getTaskId();

    }
    public void createTask( Map<String, Object> mapQuery) throws BizCheckedException {
        String barcode = "";
        Long orderId = 0L;
        String key = "store_queue";

        try {
            orderId = Long.valueOf(mapQuery.get("orderId").toString().trim());
            barcode = mapQuery.get("barcode").toString();

        }catch (Exception e){
            throw new BizCheckedException("2880010");
        }


        CsiSku sku = skuService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode);
        if(sku==null){
            throw new BizCheckedException("2880001");
        }

        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(orderId);
        if(ibdHeader ==null){
            throw new BizCheckedException("2880004");
        }
        if(ibdHeader.getOrderStatus().compareTo(2)!=0){
            throw new BizCheckedException("2880004");
        }
        mapQuery.put("type", TaskConstant.TYPE_SEED);
        mapQuery.put("skuId",sku.getSkuId());
        List<TaskInfo> infos = baseTaskService.getTaskInfoList(mapQuery);
        if(infos!=null && infos.size()!=0){
            TaskInfo info = infos.get(0);
            if(info.getSubType().compareTo(1L)==0){
                throw new BizCheckedException("2880014");
            }
            return;
        }

        BaseinfoItem item = itemService.getItem(ibdHeader.getOwnerUid(), sku.getSkuId());

        String queueObject = redisStringDao.get(key);
        Map<String,String> storeMap = new HashMap<String, String>();
        if(queueObject == null){
            List<BaseinfoLocation> storeList = locationRpcService.sortSowLocationByStoreNo();
            if(storeList!=null && storeList.size()!=0) {
                for (int i = 0; i < storeList.size(); i++) {
                    storeMap.put(storeList.get(i).getStoreNo().toString(), i+"");
                }
                JSONObject object = JSONObject.fromObject(storeMap);
                redisStringDao.set(key, object.toString());
            }
        }else {
            JSONObject object = JSONObject.fromObject(queueObject);
            storeMap = (HashMap<String,String>)JSONObject.toBean(object, HashMap.class);
        }

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
            info.setSubType(2L);
            //门店播放规则
            if(storeMap.containsKey(storeNo)) {
                info.setTaskOrder(Long.valueOf(storeMap.get(storeNo)));
            }
            info.setTaskName("播种任务[ " + storeNo + "]");
            info.setItemId(item.getItemId());
            info.setSkuId(sku.getSkuId());
            info.setOrderId(orderId);
            info.setPackUnit(item.getPackUnit());
            info.setType(TaskConstant.TYPE_SEED);
            info.setPackName(item.getPackName());
            entry.setTaskHead(head);
            entry.setTaskInfo(info);
            entries.add(entry);
        }
        taskRpcService.batchCreate(TaskConstant.TYPE_SEED,entries);

    }
}
