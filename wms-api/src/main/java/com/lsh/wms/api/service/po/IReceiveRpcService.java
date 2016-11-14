package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.po.ReceiveHeader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
public interface IReceiveRpcService {

    List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params);

    Integer countReceiveHeader(Map<String, Object> params);

    ReceiveHeader getReceiveDetailList(Long receiveId);
    Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException;

    void accountBack(Long receiveId,String detailOtherId) throws BizCheckedException;

    void updateQty(Long receiveId,String detailOtherId,BigDecimal qty) throws BizCheckedException;
}
