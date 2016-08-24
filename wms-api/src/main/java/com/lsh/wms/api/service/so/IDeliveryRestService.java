package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.so.DeliveryRequest;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.so.
 * desc:类功能描述
 */
public interface IDeliveryRestService {

    public BaseResponse insertOrder(DeliveryRequest request) throws BizCheckedException;

    public String getOutbDeliveryHeaderDetailByDeliveryId(Long deliveryId) throws BizCheckedException;

    public String countOutbDeliveryHeader();

    public String getOutbDeliveryHeaderList();
}
