package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.PoRequest;

/**
 * Created by lixin-mac on 2017/1/7.
 */
public interface IPoRfRestService {
    public String init(String poOrderInfo);

    public BaseResponse insertOrder(PoRequest request) throws BizCheckedException;

    public String updateOrderStatus() throws BizCheckedException;

    public String getPoHeaderList();

    public String getPoDetailByOrderId(Long orderId) throws BizCheckedException;

    public String countInbPoHeader();

    public String getPoDetailList();
    String canReceipt();
    String getStoreInfo(Long orderId, String detailOtherId);
    public String throwOrders() throws BizCheckedException;
}
