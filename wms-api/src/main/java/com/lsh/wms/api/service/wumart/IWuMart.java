package com.lsh.wms.api.service.wumart;

import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdHeader;

/**
 * Created by lixin-mac on 2016/11/4.
 */
public interface IWuMart {
    void sendIbd(CreateIbdHeader createIbdHeader);
    void sendObd(CreateObdHeader createObdHeader);
    String ibdAccountBack(String accountId,String accountDetailId);
}
