package com.lsh.wms.api.service.back;


import com.lsh.wms.api.model.po.IbdBackRequest;

/**
 * Created by lixin-mac on 16/9/6.
 */

public interface IDataBackService {
    String wmDataBackByPost(Object request, String url , Integer type);
    String ofcDataBackByPost(Object request, String url);
    Boolean erpDataBack(IbdBackRequest request);
}
