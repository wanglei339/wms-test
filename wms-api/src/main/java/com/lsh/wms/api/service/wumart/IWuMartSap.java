package com.lsh.wms.api.service.wumart;

import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public interface IWuMartSap {

    CreateIbdHeader ibd2Sap(CreateIbdHeader createIbdHeader);

    CreateObdHeader obd2Sap(CreateObdHeader createObdHeader);

    String ibd2SapAccount(CreateIbdHeader createIbdHeader);

    String obd2SapAccount(CreateObdHeader createObdHeader);

    String ibd2SapBack(String accountId,String accountDetailId);

    String soObd2Sap(CreateObdHeader createObdHeader);





}
