package com.lsh.wms.integration.service.back;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.po.ReceiveHeader;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mali on 16/11/17.
 */
@Component
public class TransporterManager {
    public static final Logger logger = LoggerFactory.getLogger(TransporterManager.class);

    @Autowired
    private SoDeliveryService soDeliveryService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private ReceiveService receiveService;

//    @Reference
//    private ITransporter transporter;
    @Autowired
    private IbdErpTransporter ibdErpTransporter;

    @Autowired
    private DirectTransporter directTransporter;

    @Autowired
    private ObdSapStoTransporter obdSapStoTransporter;

    @Autowired
    private ObdSapTransporter obdSapTransporter;

    @Autowired
    private ObdOfcTransporter obdOfcTransporter;

    @Autowired
    private IbdSapTransporter ibdSapTransporter;

    @Autowired
    private InventoryTransporter inventoryTransporter;


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
                        transporter = directTransporter;
                    }else if (obdHeader.getOrderType() == SoConstant.ORDER_TYPE_STO) {
                        transporter = obdSapStoTransporter;
                    }else {
                        transporter = obdSapTransporter;
                    }
                }else {
                    transporter = obdOfcTransporter;
                }
                break;
            case SysLogConstant.LOG_TYPE_IBD:
                Long receiveId = sysLog.getBusinessId();
                ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);
                if(receiveHeader.getOwnerUid() == 1){
                    transporter = ibdSapTransporter;
                }else{
                    transporter = ibdErpTransporter;
                }
                break;
            case SysLogConstant.LOG_TYPE_LOSS_WIN:
                transporter = inventoryTransporter;
                break;
            case SysLogConstant.LOG_TYPE_DIRECT:
                transporter = directTransporter;
//            case SysLogConstant.LOG_TYPE_WIN:
//                transporter = new InventoryWinTransporter();
        }
        transporter.process(sysLog);
    }
}