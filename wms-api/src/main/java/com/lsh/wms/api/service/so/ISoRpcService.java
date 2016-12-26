package com.lsh.wms.api.service.so;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.model.so.ObdHeader;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.so.
 * desc:类功能描述
 */
public interface ISoRpcService {

    public Long insertOrder(SoRequest request) throws BizCheckedException;

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException;

    public ObdHeader getOutbSoHeaderDetailByOrderId(Long orderId) throws BizCheckedException;

    public Integer countOutbSoHeader(Map<String, Object> params);

    public List<ObdHeader> getOutbSoHeaderList(Map<String, Object> params);

    void eliminateDiff(Long orderId) throws BizCheckedException;

    void confirmBack(Long orderId,Long uid) throws BizCheckedException;
}