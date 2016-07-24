package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;

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
public interface IPoOrderRestService {

    public String updateOrderStatusByOrderOtherId() throws BizCheckedException;

    public String updateOrderStatusByOrderId() throws BizCheckedException;

    public String getPoHeaderList();

    public String getPoDetailByOrderId(Long orderId);

    public String countInbPoHeader();

    public String getPoDetailList();

}
