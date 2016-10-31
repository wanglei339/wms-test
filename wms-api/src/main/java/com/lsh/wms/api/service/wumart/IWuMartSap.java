package com.lsh.wms.api.service.wumart;

import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;

/**
 * Created by lixin-mac on 2016/10/28.
 */
public interface IWuMartSap {

    String ibd2Sap(CreateIbdHeader createIbdHeader);

    String obd2Sap(CreateObdHeader createObdHeader);

}
