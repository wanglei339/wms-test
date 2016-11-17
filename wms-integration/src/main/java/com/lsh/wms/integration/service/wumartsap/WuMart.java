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

    /**
     * 收货创建sap ibd 并过账接口
     * @param createIbdHeader
     * @return
     */
    public String sendIbd(CreateIbdHeader createIbdHeader) {
        CreateIbdHeader backDate = wuMartSap.ibd2Sap(createIbdHeader);
        if(backDate != null) {
            return wuMartSap.ibd2SapAccount(backDate);
        }
        return null;
    }

    /**
     * 发货sto 创建sap obd并过账接口
     * @param createObdHeader
     * @return
     */
    public String sendObd(CreateObdHeader createObdHeader) {
        CreateObdHeader backDate = wuMartSap.obd2Sap(createObdHeader);
        if(backDate != null){
            wuMartSap.obd2SapAccount(backDate);
        }
        return null;
    }

    /**
     * ibd冲销过账接口
     * @param accountId
     * @param accountDetailId
     * @return
     */
    public String ibdAccountBack(String accountId, String accountDetailId) {

        String result = wuMartSap.ibd2SapBack(accountId,accountDetailId);
        return result;
    }


    /**
     * 直流创建 sap ibd创建并过账 以及obd创建并过账
     * @param ibdObdMap
     */
    public void sendSap(Map<String,Object> ibdObdMap){
        String ibdResult = this.sendIbd((CreateIbdHeader) ibdObdMap.get("createIbdHeader"));

        if(!"E".equals(ibdResult) && ibdResult != null){
            this.sendObd((CreateObdHeader) ibdObdMap.get("createObdHeader"));
        }
    }

    /**
     * 在库so obd创建接口。
     * @param createObdHeader
     * @return
     */
    public String sendSo2Sap(CreateObdHeader createObdHeader) {
        return JsonUtils.SUCCESS(wuMartSap.soObd2Sap(createObdHeader));
    }


}
