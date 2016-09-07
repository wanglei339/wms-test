package com.lsh.wms.api.service.po;

import com.lsh.wms.api.model.po.IbdBackRequest;

/**
 * Created by lixin-mac on 16/9/6.
 */

public interface IIbdBackService {
    void createOrderByPost(IbdBackRequest request, String token);
}
