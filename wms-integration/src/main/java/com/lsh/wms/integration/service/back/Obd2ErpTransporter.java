package com.lsh.wms.integration.service.back;

import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void process(SysLog sysLog) {
        OutbDeliveryHeader header = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(sysLog.getBusinessId());
        List<OutbDeliveryDetail> details = soDeliveryService.getOutbDeliveryDetailListByDeliveryId(sysLog.getBusinessId());
        if(header == null){
            //error
        }
        if(details == null || details.size() == 0) {
            //error
        }
        CreateObdHeader createObdHeader = new CreateObdHeader();

        dataBackService.obd2Erp(createObdHeader,sysLog);
    }
}
