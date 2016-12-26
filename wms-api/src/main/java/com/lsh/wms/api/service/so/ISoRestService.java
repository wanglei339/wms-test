package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.so.SoRequest;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.so.
 * desc:类功能描述
 */
public interface ISoRestService {

    //public String init(String soOrderInfo);

    public BaseResponse insertOrder(SoRequest request) throws BizCheckedException;

    public String updateOrderStatus() throws BizCheckedException;

    public String getOutbSoHeaderDetailByOrderId(Long orderId) throws BizCheckedException;

    public String countOutbSoHeader();

    public String getOutbSoHeaderList();

    String confirmBack(Long orderId,Long uid) throws BizCheckedException;

}
