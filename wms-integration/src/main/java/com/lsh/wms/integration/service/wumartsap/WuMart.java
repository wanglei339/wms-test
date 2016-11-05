package com.lsh.wms.integration.service.wumartsap;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.wumart.IWuMart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lixin-mac on 2016/11/4.
 */
@Service(protocol = "dubbo")
public class WuMart implements IWuMart {
    private static Logger logger = LoggerFactory.getLogger(WuMart.class);

    @Autowired
    private WuMartSap wuMartSap;


    public void sendIbd(CreateIbdHeader createIbdHeader) {
        wuMartSap.ibd2Sap(createIbdHeader);

    }

    public void sendObd(CreateObdHeader createObdHeader) {
        wuMartSap.obd2Sap(createObdHeader);
    }
}
