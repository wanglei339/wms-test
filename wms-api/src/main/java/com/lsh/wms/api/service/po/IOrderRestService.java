package com.lsh.wms.api.service.po;

import com.lsh.wms.model.po.InbPoHeader;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/21
 * Time: 16/7/21.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IOrderRestService {
    public String getPoDetailByOrderId(Long orderId);

    public String countInbPoHeader(Map<String, Object> params);

    public String getPoDetailList(Map<String, Object> params);
}
