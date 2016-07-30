package com.lsh.wms.api.service.po;


import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.model.po.InbPoHeader;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IPoRpcService {

    public void insertOrder(PoRequest request) throws BizCheckedException;

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException;

    public List<InbPoHeader> getPoHeaderList(Map<String, Object> params);

    public InbPoHeader getPoDetailByOrderId(Long orderId) throws BizCheckedException;

    public Integer countInbPoHeader(Map<String, Object> params);

    public List<InbPoHeader> getPoDetailList(Map<String, Object> params);

}
