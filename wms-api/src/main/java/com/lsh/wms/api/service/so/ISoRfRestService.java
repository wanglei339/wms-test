package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.so.SoRequest;

/**
 * Created by lixin-mac on 2017/1/7.
 */
public interface ISoRfRestService {

    //public String init(String soOrderInfo);

    public BaseResponse insertOrder(SoRequest request) throws BizCheckedException;

    public String updateOrderStatus() throws BizCheckedException;

    public String getOutbSoHeaderDetailByOrderId(Long orderId) throws BizCheckedException;

    public String countOutbSoHeader();

    public String getOutbSoHeaderList();

    String confirmBack(Long orderId,Long uid) throws BizCheckedException;
}
