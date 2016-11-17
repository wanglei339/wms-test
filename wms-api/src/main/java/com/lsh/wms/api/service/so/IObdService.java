package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.so.ObdRequest;
import com.lsh.wms.api.model.wumart.CreateObdHeader;

/**
 * Created by lixin-mac on 16/9/5.
 */
public interface IObdService {
    BaseResponse add(ObdRequest request) throws BizCheckedException;

    String bdSendObd2Sap(CreateObdHeader createObdHeader);
}
