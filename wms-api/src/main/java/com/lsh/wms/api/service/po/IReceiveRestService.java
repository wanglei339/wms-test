package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.po.ReceiveHeader;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
public interface IReceiveRestService {
    String getReceiveHeaderList(Map<String, Object> params);

    String countReceiveHeader(Map<String, Object> params);

    String getReceiveDetailList(Long receiveId);

    String updateOrderStatus() throws BizCheckedException;
}
