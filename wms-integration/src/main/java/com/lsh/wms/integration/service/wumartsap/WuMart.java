package com.lsh.wms.integration.service.wumartsap;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.wumart.IWuMart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by lixin-mac on 2016/11/4.
 */
@Service(protocol = "dubbo",async=true)
public class WuMart implements IWuMart {
    private static Logger logger = LoggerFactory.getLogger(WuMart.class);

    @Autowired
    private WuMartSap wuMartSap;


    public String sendIbd(CreateIbdHeader createIbdHeader) {
        CreateIbdHeader backDate = wuMartSap.ibd2Sap(createIbdHeader);
        if(backDate != null) {
            return wuMartSap.ibd2SapAccount(backDate);
        }
        return null;
    }

    public String sendObd(CreateObdHeader createObdHeader) {
        CreateObdHeader backDate = wuMartSap.obd2Sap(createObdHeader);
        if(backDate != null){
            wuMartSap.obd2SapAccount(backDate);
        }
        return null;
    }

    public String ibdAccountBack(String accountId, String accountDetailId) {

        String result = wuMartSap.ibd2SapBack(accountId,accountDetailId);
        return result;
    }

    public void sendSap(Map<String,Object> ibdObdMap){

        String ibdResult = this.sendIbd((CreateIbdHeader) ibdObdMap.get("createIbdHeader"));

        if(!"E".equals(ibdResult) && ibdResult != null){
            this.sendObd((CreateObdHeader) ibdObdMap.get("createObdHeader"));
        }
    }

    public String sendSo2Sap(CreateObdHeader createObdHeader) {
        return JsonUtils.SUCCESS(wuMartSap.soObd2Sap(createObdHeader));
    }


}
