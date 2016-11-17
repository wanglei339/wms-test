package com.lsh.wms.integration.service.back;

import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.po.ReceiveHeader;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mali on 16/11/17.
 */
@Component
public class TransporterManager {

    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private ReceiveService receiveService;


    //@Autowired
    public void dealOne(SysLog sysLog) {
        ITransporter transporter = new ITransporter() {
            public void process(SysLog sysLog) {

            }
        };

        switch (sysLog.getLogType()){
            case SysLogConstant.LOG_TYPE_OBD:
                Long obdId = sysLog.getBusinessId();
                OutbDeliveryHeader deliveryHeader = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(obdId);
                ObdHeader obdHeader = soOrderService.getOutbSoHeaderByOrderId(deliveryHeader.getOrderId());
                if(obdHeader.getOwnerUid() == 1){
                    if(obdHeader.getOrderType() == SoConstant.ORDER_TYPE_DIRECT){
                        transporter = new DirectTransporter();
                    }else if (obdHeader.getOrderType() == SoConstant.ORDER_TYPE_STO) {
                        transporter = new ObdSapStoTransporter();
                    }else {
                        transporter = new ObdSapTransporter();
                    }
                }else {
                    transporter = new ObdOfcTransporter();
                }
                break;
            case SysLogConstant.LOG_TYPE_IBD:
                Long receiveId = sysLog.getBusinessId();
                ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);
                if(receiveHeader.getOwnerUid() == 1){
                    transporter = new IbdSapTransporter();
                }else{
                    transporter = new IbdErpTransporter();
                }
                break;
            case SysLogConstant.LOG_TYPE_LOSS_WIN:
                transporter = new InventoryLossTransporter();
                break;
//            case SysLogConstant.LOG_TYPE_WIN:
//                transporter = new InventoryWinTransporter();
        }
        transporter.process(sysLog);
    }
}