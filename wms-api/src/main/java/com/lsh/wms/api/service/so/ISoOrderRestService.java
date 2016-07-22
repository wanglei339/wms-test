package com.lsh.wms.api.service.so;

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
public interface ISoOrderRestService {

    public String getOutbSoHeaderDetailByOrderId(Long orderId);

    public String countOutbSoHeader();

    public String getOutbSoHeaderList();

}
