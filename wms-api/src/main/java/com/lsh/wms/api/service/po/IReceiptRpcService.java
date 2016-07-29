package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.model.po.InbReceiptHeader;

import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/29
 * Time: 16/7/29.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.po.
 * desc:类功能描述
 */
public interface IReceiptRpcService {
    public void insertOrder(ReceiptRequest request) throws BizCheckedException;

    /* 投单接口 */
    public Boolean throwOrder(String orderOtherId) throws BizCheckedException;

    public Boolean updateReceiptStatus(Long receiptId, Integer receiptStatus) throws BizCheckedException;

    public InbReceiptHeader getPoReceiptDetailByReceiptId(Long receiptId) throws BizCheckedException;

    public List<InbReceiptHeader> getPoReceiptDetailByOrderId(Long orderId) throws BizCheckedException;

    public Integer countInbPoReceiptHeader(Map<String, Object> params);

    public List<InbReceiptHeader> getPoReceiptDetailList(Map<String, Object> params);
}