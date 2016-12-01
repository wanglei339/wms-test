package com.lsh.wms.integration.service.back;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.service.item.ItemService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Component
public class ObdSapStoTransporter implements ITransporter{
    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private WuMart wuMart;


    public void process(SysLog sysLog) {
        //buildDataRpcService.BuildSapObdData(sysLog.getBusinessId());
        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(sysLog.getBusinessId());
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(sysLog.getBusinessId());
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }
        CreateObdHeader createObdHeader = new CreateObdHeader();
        List<CreateObdDetail> createObdDetails = new ArrayList<CreateObdDetail>();
        for (OutbDeliveryDetail detail : details) {
            CreateObdDetail createObdDetail = new CreateObdDetail();
            //TODO 这段代码是非常危险的,根据itemid去获取唯一的一条 有问题,稍后review影响
            //ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndItemId(detail.getOrderId(), detail.getItemId());
            ObdDetail obdDetail = soOrderService.getObdDetailByOrderIdAndDetailOtherId(detail.getOrderId(),detail.getRefDetailOtherId());
            if (null == obdDetail) {
                throw new BizCheckedException("2900004");
            }
//            if(detail.getBackStatus() == SoConstant.DELIVERY_DETAIL_STATUS_SUCCESS){
//                continue;
//            }
            BigDecimal outQty = detail.getDeliveryNum();
            //ea转换为包装数量。
            ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(detail.getOrderId());
            createObdDetail.setDlvQty(PackUtil.EAQty2UomQty(outQty, detail.getPackUnit()).setScale(2,BigDecimal.ROUND_HALF_UP));
            createObdDetail.setRefDoc(obdHeader.getOrderOtherId());
            createObdDetail.setRefItem(obdDetail.getDetailOtherId());
            createObdDetail.setMaterial(obdDetail.getSkuCode());
            createObdDetail.setOrderType(obdHeader.getOrderType());
            createObdDetail.setSalesUnit(obdDetail.getPackName());
            createObdDetails.add(createObdDetail);
            createObdHeader.setOrderOtherId(obdHeader.getOrderOtherId());

        }
        createObdHeader.setWarehouseCode(PropertyUtils.getString("wumart.werks"));
        createObdHeader.setDeliveryId(sysLog.getBusinessId());
        createObdHeader.setTuId(header.getTuId());
        createObdHeader.setItems(createObdDetails);
        wuMart.sendObd(createObdHeader,sysLog);
    }
}
