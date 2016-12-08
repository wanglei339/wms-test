package com.lsh.wms.api.service.po;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import com.lsh.wms.model.so.ObdDetail;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
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
    public void insertOrder(ReceiptRequest request) throws BizCheckedException, ParseException;

    /* 投单接口 */
    public Boolean throwOrder(String orderOtherId) throws BizCheckedException;

    public Boolean updateReceiptStatus(Map<String, Object> map) throws BizCheckedException;

    public InbReceiptHeader getPoReceiptDetailByReceiptId(Long receiptId) throws BizCheckedException;

    public List<InbReceiptHeader> getPoReceiptDetailByOrderId(Long orderId) throws BizCheckedException;

    public Integer countInbPoReceiptHeader(Map<String, Object> params);

    public List<InbReceiptHeader> getInbReceiptHeaderList(Map<String, Object> params);

    void insertReceipt(Long orderId,Long staffId) throws BizCheckedException, ParseException;

    void addStoreReceipt(ReceiptRequest request) throws BizCheckedException, ParseException;

    public List<InbReceiptDetail> getInbReceiptDetailListByOrderId(Long orderId);
    //验证生产日期
    boolean checkProTime(BaseinfoItem baseinfoItem, Date proTime,Date dueTime, String exceptionCode)throws BizCheckedException;

    List<InbReceiptHeader> getInbReceiptHeaderDetailList(Map<String,Object> param) throws BizCheckedException;

    List<InbReceiptDetail> getInbReceiptDetailList(Map<String,Object> param) throws BizCheckedException;

    Integer countInbPoReceiptDetail(Map<String, Object> params);

    Map<String,Object> mergeObdDetailList(List<ObdDetail> obdDetailList,BigDecimal receiptEaQty);

    Map<String,Object> mergeIbdDetailList(List<IbdDetail> IbdDetailList,BigDecimal receiptEaQty);

}
