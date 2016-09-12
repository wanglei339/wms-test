package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.IbdRequest;

import java.util.Map;

/**
 * Created by mali on 16/9/2.
 */
public interface IIbdService {
    BaseResponse add(IbdRequest request)throws BizCheckedException;

    String Test();
}
