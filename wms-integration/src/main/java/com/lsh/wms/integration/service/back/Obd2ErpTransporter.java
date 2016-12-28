package com.lsh.wms.integration.service.back;

import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/12/24.
 */
@Component
public class Obd2ErpTransporter implements ITransporter{
    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private DataBackService dataBackService;
    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private ItemService itemService;
    public void process(SysLog sysLog) {
        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(sysLog.getBusinessId());
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(sysLog.getBusinessId());
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }

        ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(header.getOrderId());
        CreateObdHeader createObdHeader = new CreateObdHeader();
        createObdHeader.setOrderOtherId(obdHeader.getOrderOtherId());
        createObdHeader.setDeliveryId(header.getDeliveryId());
        List<CreateObdDetail> createObdDetails = new ArrayList<CreateObdDetail>();
        for (OutbDeliveryDetail detail : details){
            CreateObdDetail createObdDetail = new CreateObdDetail();
            BaseinfoItem item = itemService.getItem(detail.getItemId());
            createObdDetail.setMaterial(item.getSkuCode());
            createObdDetail.setDlvQty(detail.getDeliveryNum());
            createObdDetails.add(createObdDetail);
        }

        createObdHeader.setItems(createObdDetails);
        dataBackService.obd2Erp(createObdHeader,sysLog);
    }
}
