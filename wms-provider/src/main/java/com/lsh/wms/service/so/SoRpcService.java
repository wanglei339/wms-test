package com.lsh.wms.service.so;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.CollectionUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.so.SoItem;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.service.csi.CsiOwnerService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockSummaryService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.so.
 * desc:类功能描述
 */
@Service(protocol = "dubbo")
public class SoRpcService implements ISoRpcService {

    private static Logger logger = LoggerFactory.getLogger(SoRpcService.class);

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CsiOwnerService csiOwnerService;

    @Reference
    private ITaskRpcService iTaskRpcService;
    @Autowired
    protected IdGenerator idGenerator;
    @Autowired
    private StockSummaryService stockSummaryService;
    @Autowired
    private StockMoveService stockMoveService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private SoDeliveryService soDeliveryService;

    public Long insertOrder(SoRequest request) throws BizCheckedException {
        //OutbSoHeader
        ObdHeader obdHeader = new ObdHeader();
        ObjUtils.bean2bean(request, obdHeader);

        //设置订单状态
        obdHeader.setOrderStatus(BusiConstant.EFFECTIVE_YES);

//        //设置订单插入时间
//        obdHeader.setCreatedAt(DateUtils.getCurrentSeconds());

        //设置orderId
        String idKey = "obd_id";
        Long orderId = idGenerator.genId(idKey, true, true);
        obdHeader.setOrderId(orderId);

        //初始化List<OutbSoDetail>
        List<ObdDetail> obdDetailList = new ArrayList<ObdDetail>();

        for(SoItem soItem : request.getItems()) {
            ObdDetail obdDetail = new ObdDetail();

            ObjUtils.bean2bean(soItem, obdDetail);

            //设置orderId
            obdDetail.setOrderId(obdHeader.getOrderId());

            //根据ItemId及OwnerUid获取List<BaseinfoItem>
            BaseinfoItem baseinfoItem = itemService.getItemsBySkuCode(obdHeader.getOwnerUid(),
                    obdDetail.getSkuCode());

            if(baseinfoItem == null) {
                throw new BizCheckedException("2900001");
            }

            //设置skuId
            obdDetail.setSkuId(baseinfoItem.getSkuId());
            //设置itemId
            obdDetail.setItemId(baseinfoItem.getItemId());
            //设置skuName
            obdDetail.setSkuName(baseinfoItem.getSkuName());
            //设置新增时间
            obdDetail.setCreatedAt(DateUtils.getCurrentSeconds());
            //设置订单数量
            obdDetail.setOriOrderQty(soItem.getOrderQty());
            CsiOwner owner = csiOwnerService.getOwner(baseinfoItem.getOwnerId());
            if (owner == null) {
                throw new BizCheckedException("2900008");
            }

            if ( obdHeader.getOrderType().equals(SoConstant.ORDER_TYPE_DIRECT) ||
                    owner.getSoCheckStrategy().equals(SoConstant.STOCK_NOT_CHECK)) {
                // 直流订单或者不需要做库存检查
                obdDetail.setOriOrderQty(soItem.getOrderQty());
            } else {
                obdDetail.setOriOrderQty(soItem.getOrderQty());
                Double avQty = stockSummaryService.getStockSummaryByItemId(obdDetail.getItemId()).getAvailQty().doubleValue();
                if (avQty.compareTo(obdDetail.getOriOrderQty().doubleValue()) <= 0 ) {
                    if (owner.getSoCheckStrategy().equals(SoConstant.STOCK_HARD_CHECK)) {
                        throw new BizCheckedException("2900009");
                    }
                    if (owner.getSoCheckStrategy().equals(SoConstant.STOCK_SOFT_CHECK)) {
                        obdDetail.setOrderQty(new BigDecimal(avQty));
                    }
                }
            }

            obdDetailList.add(obdDetail);
        }

        //插入订单
        soOrderService.insertOrder(obdHeader, obdDetailList);

//        TaskEntry taskEntry = new TaskEntry();
//        TaskInfo taskInfo = new TaskInfo();
//        taskInfo.setType(TaskConstant.TYPE_PICK);
//        taskInfo.setOrderId(outbSoHeader.getOrderId());
//        taskEntry.setTaskInfo(taskInfo);
//        iTaskRpcService.create(TaskConstant.TYPE_PICK,taskEntry);
        return orderId;

    }

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException {
        if((map.get("orderOtherId") == null && map.get("orderId") == null)
                || map.get("orderStatus") == null) {
            throw new BizCheckedException("1030001", "参数不能为空");
        }

        if(map.get("orderOtherId") == null && map.get("orderId") != null) {
            if(!StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") == null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") != null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))
                    && !StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("orderStatus")))) {
            throw new BizCheckedException("1030002", "参数类型不正确");
        }

        ObdHeader obdHeader = new ObdHeader();
        if(map.get("orderOtherId") != null && !StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
            obdHeader.setOrderOtherId(String.valueOf(map.get("orderOtherId")));
        }
        if(map.get("orderId") != null && StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
            obdHeader.setOrderId(Long.valueOf(String.valueOf(map.get("orderId"))));
        }
        obdHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));

        soOrderService.updateOutbSoHeaderByOrderOtherIdOrOrderId(obdHeader);

        return true;
    }

    public ObdHeader getOutbSoHeaderDetailByOrderId(Long orderId) throws BizCheckedException {
        if(orderId == null) {
            throw new BizCheckedException("1030001", "参数不能为空");
        }

        return soOrderService.getOutbSoHeaderByOrderId(orderId);
    }

    public Integer countOutbSoHeader(Map<String, Object> params) {
        return soOrderService.countOutbSoHeader(params);
    }

    public List<ObdHeader> getOutbSoHeaderList(Map<String, Object> params) {
        return soOrderService.getOutbSoHeaderList(params);
    }

    public void eliminateDiff(Long orderId) {
        //取出order的head
        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(orderId);
        if(header == null){
            logger.warn("so get fail "+orderId);
            return;
        }
        if( header.getOrderType() == SoConstant.ORDER_TYPE_DIRECT || header.getIsClosed() == 1L || header.getWaveId() <= 1 ) {
            return;
        }

        //取出order的所有detail
        List<ObdDetail> obdDetailList = soOrderService.getOutbSoDetailListByOrderId(orderId);

        // 订单明细按照itemId做排序，避免死锁
        Collections.sort(obdDetailList, new Comparator<ObdDetail>() {
            public int compare(ObdDetail o1, ObdDetail o2) {
                return o1.getItemId().compareTo(o2.getItemId());
            }
        });

        // 根据排序后的detaiList进行库存占用
        List<StockMove> moveList = new ArrayList<StockMove>();
        for (ObdDetail detail : obdDetailList) {
            StockMove move = new StockMove();
            move.setItemId(detail.getItemId());
            BigDecimal deliveryQty = soDeliveryService.getDeliveryQtyBySoOrderIdAndItemId(detail.getOrderId(), detail.getItemId());
            BigDecimal orderQty = detail.getOrderQty();
            if (orderQty.compareTo(deliveryQty) < 0) {
                // 订单数量小于发货数量
                throw new BizCheckedException("2990045");
            }
            if (orderQty.equals(deliveryQty)) {
                // 订单数量等于发货数量
                continue;
            }

            move.setQty(orderQty.subtract(deliveryQty));
            move.setFromLocationId(locationService.getSoAreaInbound().getLocationId());
            move.setToLocationId(locationService.getNullArea().getLocationId());
            move.setTaskId(detail.getOrderId());
            moveList.add(move);
        }
        if (!CollectionUtils.isEmpty(moveList)) {
            stockMoveService.move(moveList);
        }
        // 关闭订单
        header.setIsClosed(1L);
        soOrderService.update(header);
    }
}
