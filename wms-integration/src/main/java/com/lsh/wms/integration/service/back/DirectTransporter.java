package com.lsh.wms.integration.service.back;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.integration.service.wumartsap.WuMart;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.system.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Component
public class DirectTransporter implements ITransporter{
    private static Logger logger = LoggerFactory.getLogger(DirectTransporter.class);

    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private WuMart wuMart;


    public void process(SysLog sysLog) {

        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(sysLog.getBusinessId());
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(sysLog.getBusinessId());
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }
        List<CreateObdDetail> createObdDetailList = new ArrayList<CreateObdDetail>();
        List<CreateIbdDetail> createIbdDetailList = new ArrayList<CreateIbdDetail>();
        //List<CreateObdDetail>  createStoObdDetails = new LinkedList<CreateObdDetail>();
        for(OutbDeliveryDetail detail : details) {
            Long itemId = detail.getItemId();
            Long orderId = detail.getOrderId();
            CreateObdDetail createObdDetail = new CreateObdDetail();
            //obd的detail
            ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndItemId(orderId, itemId);
            if (null == obdDetail) {
                throw new BizCheckedException("2900004");
            }
            if(detail.getBackStatus() != null && detail.getBackStatus() == SoConstant.DELIVERY_DETAIL_STATUS_SUCCESS){
                continue;
            }
            //sto obd order_other_id
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(obdDetail.getOrderId());
            createObdDetail.setRefDoc(obdHeader.getOrderOtherId());
            //销售单位
            createObdDetail.setSalesUnit(obdDetail.getPackName());
            //交货量 qc的ea/销售单位
            createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(detail.getDeliveryNum(), obdDetail.getPackName()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //sto obd detail detail_other_id
            createObdDetail.setRefItem(obdDetail.getDetailOtherId());
            createObdDetail.setOrderType(obdHeader.getOrderType());
            //找关系 sto和cpo
            List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationListByObd(obdHeader.getOrderOtherId(), obdDetail.getDetailOtherId());
            if (null == ibdObdRelations || ibdObdRelations.size() < 1) {
//                createStoObdDetails.add(createObdDetail);
//                continue;
                throw new BizCheckedException("2900005");
            }
            createObdDetailList.add(createObdDetail);
            IbdObdRelation ibdObdRelation = ibdObdRelations.get(0);
            IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(ibdObdRelation.getIbdOtherId());
            IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(ibdHeader.getOrderId(), ibdObdRelation.getIbdDetailId());
            //拼装CreateIbdDetail
            CreateIbdDetail createIbdDetail = new CreateIbdDetail();
            //采购凭证号
            createIbdDetail.setPoNumber(ibdHeader.getOrderOtherId());
            //采购订单的计量单位
            createIbdDetail.setUnit(ibdDetail.getPackName());
            //实际出库数量
            createIbdDetail.setDeliveQty(PackUtil.EAQty2UomQty(detail.getDeliveryNum(), ibdDetail.getPackName()).setScale(2, BigDecimal.ROUND_HALF_UP));
            //行项目号
            createIbdDetail.setPoItme(ibdDetail.getDetailOtherId());
            createIbdDetail.setVendMat(ibdHeader.getOrderId().toString());
            createIbdDetail.setOrderType(ibdHeader.getOrderType());
            createIbdDetailList.add(createIbdDetail);
        }
        CreateObdHeader createObdHeader = new CreateObdHeader();
        createObdHeader.setDeliveryId(sysLog.getBusinessId());
        createObdHeader.setTuId(header.getTuId());
        createObdHeader.setWarehouseCode(PropertyUtils.getString("wumart.werks"));
        createObdHeader.setItems(createObdDetailList);
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        createIbdHeader.setWarehouseCode(PropertyUtils.getString("wumart.werks"));
        createIbdHeader.setItems(createIbdDetailList);

        Map<String, Object> ibdObdMap = new HashMap<String, Object>();
        ibdObdMap.put("createIbdHeader", createIbdHeader);
        ibdObdMap.put("createObdHeader", createObdHeader);

        wuMart.sendSap(ibdObdMap,sysLog);


    }
}
