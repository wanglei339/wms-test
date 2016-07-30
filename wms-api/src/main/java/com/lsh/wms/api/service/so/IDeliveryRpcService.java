package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.so.DeliveryRequest;
import com.lsh.wms.model.so.OutbDeliveryHeader;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.so.
 * desc:类功能描述
 */
public interface IDeliveryRpcService {

    public void insertOrder(DeliveryRequest request) throws BizCheckedException;

//    public Boolean updateDeliveryType(Map<String, Object> map) throws BizCheckedException;

    public OutbDeliveryHeader getOutbDeliveryHeaderDetailByDeliveryId(Long deliveryId) throws BizCheckedException;

    public Integer countOutbDeliveryHeader(Map<String, Object> params);

    public List<OutbDeliveryHeader> getOutbDeliveryHeaderList(Map<String, Object> params);

}
