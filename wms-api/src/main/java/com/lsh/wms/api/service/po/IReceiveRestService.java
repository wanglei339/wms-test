package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.po.ReceiveHeader;

import java.math.BigDecimal;
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

    String accountBack(Long receiveId,String detailOtherId) throws BizCheckedException;

    String updateQty() throws BizCheckedException;

    String getLotByReceiptContainerId(Long containerId) throws BizCheckedException;
}
