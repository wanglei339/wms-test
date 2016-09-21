package com.lsh.wms.api.service.po;


import com.lsh.wms.api.model.po.IbdBackRequest;

/**
 * Created by lixin-mac on 16/9/6.
 */

public interface IIbdBackService {
    String createOrderByPost(Object request, String url);
    String createOfcOrderByPost(Object request, String url);
    Boolean receivePurchaseOrder(IbdBackRequest request);
}
