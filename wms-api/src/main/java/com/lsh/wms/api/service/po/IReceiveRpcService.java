package com.lsh.wms.api.service.po;

import com.lsh.wms.model.po.ReceiveHeader;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
public interface IReceiveRpcService {

    List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params);

    Integer countReceiveHeader(Map<String, Object> params);

    ReceiveHeader getReceiveDetailList(Long receiveId);
}
