package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.so.ObdRequest;

/**
 * Created by lixin-mac on 16/9/5.
 */
public interface IObdService {
    BaseResponse add(ObdRequest request) throws BizCheckedException;
}
