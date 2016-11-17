package com.lsh.wms.api.service.wumart;

import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.model.system.SysLog;

import java.util.Map;

/**
 * Created by lixin-mac on 2016/11/4.
 */
public interface IWuMart {
    String sendIbd(CreateIbdHeader createIbdHeader, SysLog sysLog);
    String sendObd(CreateObdHeader createObdHeader, SysLog sysLog);
    String ibdAccountBack(String accountId,String accountDetailId);

    void sendSap(Map<String,Object> ibdObdMap, SysLog sysLog);

    String sendSo2Sap(CreateObdHeader createObdHeader, SysLog sysLog);


}
