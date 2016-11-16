package com.lsh.wms.api.service.back;


import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;

/**
 * Created by lixin-mac on 16/9/6.
 */

public interface IDataBackService {
    String wmDataBackByPost(String request, String url , Integer type);
    String ofcDataBackByPost(String request, String url);
    Boolean erpDataBack(CreateIbdHeader createIbdHeader);
}
