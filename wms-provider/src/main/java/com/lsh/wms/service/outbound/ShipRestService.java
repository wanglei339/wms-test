package com.lsh.wms.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.model.so.ObdOfcBackRequest;
import com.lsh.wms.api.model.so.ObdOfcItem;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.api.service.wave.IShipRestService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.TaskConstant;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.stock.StockQuantCondition;
import com.lsh.wms.model.task.TaskEntry;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zengwenjun on 16/8/20.
 */
@Service(protocol = "rest")
@Path("outbound/ship")
public class ShipRestService implements IShipRestService {
    private static Logger logger = LoggerFactory.getLogger(ShipRestService.class);
    @Reference
    private ITuRpcService iTuRpcService;
    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private WaveService waveService;
    @Autowired
    private TuService tuService;
    @Reference
    IStockQuantRpcService stockQuantRpcService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private SoOrderService soOrderService;

    @Reference
    private IDataBackService dataBackService;

    @Reference
    private IWuMart wuMart;

    /**
     * 波次的发货操作
     * 1.托盘 2.销库存 3.生成发货单 4.todo 回传物美obd
     * 5.释放集货道
     *
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("shipTu")
    public String ShipTu() throws BizCheckedException {
        Map<String, Object> mapRequest = RequestUtils.getRequest();
        String tuId = mapRequest.get("tuId").toString();
        TuHead tuHead = iTuRpcService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        //拿托盘
        List<TuDetail> details = iTuRpcService.getTuDeailListByTuId(tuId);
        //事务操作,创建任务,发车状态改变 生成任务群
        if (null == details || details.size() < 1) {
            throw new BizCheckedException("2990041");
        }
        Set<Long> totalContainers = new HashSet<Long>();
        Map<Long, Object> containerInfo = new HashMap<Long, Object>();
        List<WaveDetail> totalWaveDetails = new ArrayList<WaveDetail>();
        for (TuDetail detail : details) {
            Long containerId = detail.getMergedContainerId();
            List<WaveDetail> waveDetails = waveService.getAliveDetailsByContainerId(containerId);
            if (null == waveDetails || waveDetails.size() < 1) {
                throw new BizCheckedException("2120018");
            }
            totalWaveDetails.addAll(waveDetails);
            totalContainers.add(containerId);
            //在库不组盘
            Map<String, Object> containerMap = new HashMap<String, Object>();
            containerMap.put("boxNum", detail.getBoxNum());
            containerMap.put("turnoverBoxNum", detail.getTurnoverBoxNum());
            containerInfo.put(containerId, containerMap);
        }

        //结果集里按照orderId聚类托盘,给出箱子数
        Map<Long, Set<Long>> orderContainerSet = new HashMap<Long, Set<Long>>();
        for (WaveDetail waveDetail : totalWaveDetails) {
            if (orderContainerSet.containsKey(waveDetail.getOrderId())) {
                orderContainerSet.get(waveDetail.getOrderId()).add(waveDetail.getContainerId());
            } else {
                Set<Long> contaienrIds = new HashSet<Long>();
                contaienrIds.add(waveDetail.getContainerId());
                orderContainerSet.put(waveDetail.getOrderId(), contaienrIds);
            }
        }
        //封装so单子和箱子数
        Map<Long, Map<String, Object>> orderBoxInfo = new HashMap<Long, Map<String, Object>>();
        //按照
        for (Long key : orderContainerSet.keySet()) {

            Set<Long> containersInOneOrder = orderContainerSet.get(key);
            BigDecimal boxNum = new BigDecimal("0");
            Long turnoverBoxNum = 0L;
            for (Long one : containersInOneOrder) {
                Map<String, Object> oneContainer = (Map<String, Object>) containerInfo.get(one);
                boxNum = boxNum.add(new BigDecimal(oneContainer.get("boxNum").toString()));
                turnoverBoxNum += Long.valueOf(oneContainer.get("turnoverBoxNum").toString());
            }
            Map<String,Object> orderBoxMap = new HashMap<String, Object>();
            orderBoxMap.put("boxNum", boxNum);
            orderBoxMap.put("turnoverBoxNum", turnoverBoxNum);
            orderBoxInfo.put(key, orderBoxMap);
        }


        //创建发货任务
        TaskEntry taskEntry = new TaskEntry();
        TaskInfo shipTaskInfo = new TaskInfo();
        shipTaskInfo.setType(TaskConstant.TYPE_DIRECT_SHIP);
        shipTaskInfo.setTaskName("优供的发货任务[" + totalWaveDetails.get(0).getContainerId() + "]");
        shipTaskInfo.setContainerId(totalWaveDetails.get(0).getContainerId()); //小店没和板子,就是原来了物理托盘码
        shipTaskInfo.setOperator(tuHead.getLoadUid()); //一个人装车
        shipTaskInfo.setBusinessMode(TaskConstant.MODE_INBOUND);
        shipTaskInfo.setLocationId(totalWaveDetails.get(0).getRealCollectLocation());
        taskEntry.setTaskInfo(shipTaskInfo);
        taskEntry.setTaskDetailList((List<Object>) (List<?>) totalWaveDetails);
        Long taskId = iTaskRpcService.create(TaskConstant.TYPE_SHIP, taskEntry);

        //销库存 写在同个事务中,生成发货单 osd的托盘生命结束
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("containerIds", totalContainers);
        map.put("tuHead", tuHead);
        if (tuService.createObdAndMoveStockQuant(map)) {    //销库存生成发货单

            //释放已经没有库存的集货道
            Set<Long> locationIds = new HashSet<Long>();
            for (WaveDetail detail : totalWaveDetails) {
                locationIds.add(detail.getRealCollectLocation());
            }
            //查库存,释放集货道
            for (Long locationId : locationIds) {
                StockQuantCondition condition = new StockQuantCondition();
                condition.setLocationId(locationId);
                java.math.BigDecimal qty = stockQuantRpcService.getQty(condition);
                if (0 == qty.compareTo(BigDecimal.ZERO)) {
                    //释放集货导
                    locationService.unlockLocation(locationId);
                    locationService.setLocationUnOccupied(locationId);
                }
            }
        }

        //获取发货单的header
        List<OutbDeliveryHeader> outbDeliveryHeaders = soDeliveryService.getOutbDeliveryHeaderByTmsId(tuHead.getTuId());
        //发货单的detail
        // TODO: 2016/11/14 回传obd
        List<Long> deliveryIds = new ArrayList<Long>();
        for (OutbDeliveryHeader header : outbDeliveryHeaders) {
            deliveryIds.add(header.getDeliveryId());
        }

        List<OutbDeliveryDetail> deliveryDetails = soDeliveryService.getOutbDeliveryDetailList(deliveryIds);
        Set<Long> orderIds = new HashSet<Long>();
        for (OutbDeliveryDetail deliveryDetail : deliveryDetails) {
            orderIds.add(deliveryDetail.getOrderId());
        }

        for (Long orderId : orderIds) {
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(orderId);
            //查询明细。
            List<ObdDetail> obdDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
            // TODO: 2016/9/23  组装OBD反馈信息 根据货主区分回传lsh或物美

            //组装ofc OBD反馈信息
            ObdOfcBackRequest request = new ObdOfcBackRequest();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            request.setWms(2);//该字段写死 2
            request.setDeliveryTime(now);
            request.setObdCode(obdHeader.getOrderId().toString());
            request.setSoCode(obdHeader.getOrderOtherId());

            //组装物美反馈信息
            CreateObdHeader createObdHeader = new CreateObdHeader();
            createObdHeader.setOrderOtherId(obdHeader.getOrderOtherId());
            //查询明细。
            List<ObdDetail> soDetails = soOrderService.getOutbSoDetailListByOrderId(orderId);
            List<ObdOfcItem> items = new ArrayList<ObdOfcItem>();

            List<CreateObdDetail> createObdDetails = new ArrayList<CreateObdDetail>();

            for (ObdDetail detail : soDetails) {

                ObdOfcItem item = new ObdOfcItem();

                CreateObdDetail createObdDetail = new CreateObdDetail();

                item.setPackNum(detail.getPackUnit());
                //
                OutbDeliveryDetail deliveryDetail = soDeliveryService.getOutbDeliveryDetail(orderId, detail.getItemId());
                if (deliveryDetail == null) {
                    continue;
                }
                BigDecimal outQty = deliveryDetail.getDeliveryNum();
                item.setSkuQty(outQty);

                //ea转换为包装数量。
                createObdDetail.setDlvQty(outQty.divide(detail.getPackUnit()));
                createObdDetail.setRefItem(detail.getDetailOtherId());
                createObdDetail.setMaterial(detail.getSkuCode());
                createObdDetails.add(createObdDetail);

                item.setSupplySkuCode(detail.getSkuCode());
                items.add(item);
            }
            request.setDetails(items);
            if (obdHeader.getOwnerUid() == 1) {
                wuMart.sendSo2Sap(createObdHeader);
            } else if (obdHeader.getOwnerUid() == 2) {
                dataBackService.ofcDataBackByPost(JSON.toJSONString(request), IntegrationConstan.URL_LSHOFC_OBD);
            }


        }


        //同步库存 todo 力哥
        Set<Long> waveIds = new HashSet<Long>();
        for (WaveDetail detail : totalWaveDetails) {
            waveIds.add(detail.getWaveId());
        }
        //设置发货人和发货时间
        tuHead.setDeliveryAt(DateUtils.getCurrentSeconds());
        tuHead.setStatus(TuConstant.SHIP_OVER);
        iTuRpcService.update(tuHead);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("response", true);
        return JsonUtils.SUCCESS(result);
    }
}
