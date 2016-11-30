package com.lsh.wms.service.receive;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.po.IReceiveRpcService;
import com.lsh.wms.api.service.wumart.IWuMart;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.model.po.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 2016/10/21.
 */
@Service(protocol = "dubbo")
public class ReceiveRpcService implements IReceiveRpcService{

    @Autowired
    private ReceiveService receiveService;

    @Reference
    private IWuMart wuMart;

    @Autowired
    private PoOrderService poOrderService;

    @Autowired
    private PoReceiptService receiptService;

    public List<ReceiveHeader> getReceiveHeaderList(Map<String, Object> params) {
        return receiveService.getReceiveHeaderList(params);
    }

    public Integer countReceiveHeader(Map<String, Object> params) {
        return receiveService.countReceiveHeader(params);
    }

    public ReceiveHeader getReceiveDetailList(Long receiveId) {


        if (receiveId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);

        receiveService.fillDetailToHeader(receiveHeader);

        return receiveHeader;
    }

    public Boolean updateOrderStatus(Map<String, Object> map) throws BizCheckedException {
        ReceiveHeader receiveHeader = new ReceiveHeader();
        receiveHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));
        receiveHeader.setReceiveId(Long.valueOf(String.valueOf(map.get("receiveId"))));
        receiveService.updateStatus(receiveHeader);
        return true;
    }


    public void updateQty(Long receiveId, String detailOtherId, BigDecimal qty,Long uid) throws BizCheckedException {

        //获取receiveHeader
        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);
        ReceiveDetail receiveDetail = receiveService.getReceiveDetailByReceiveIdAnddetailOtherId(receiveId,detailOtherId);

        //原数量 inboundqty
        BigDecimal inBoundQty = receiveDetail.getInboundQty();
        receiveDetail.setInboundQty(qty);
        BigDecimal subQty = inBoundQty.subtract(qty);
        //查询ibdHeader 修改实收数量
        //IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderId(receiveHeader.getOrderId());
        IbdDetail ibdDetail = poOrderService.getInbPoDetailByOrderIdAndDetailOtherId(receiveHeader.getOrderId(),detailOtherId);
        if(ibdDetail.getInboundQty().subtract(subQty).compareTo(ibdDetail.getOrderQty().multiply(ibdDetail.getPackUnit())) > 0){
            throw new BizCheckedException("2020005");
        }
        ibdDetail.setInboundQty(ibdDetail.getInboundQty().subtract(subQty));

        List<InbReceiptDetail> receiptDetails = receiptService.getInbReceiptDetailListByOrderIdAndCode(receiveHeader.getOrderId(),receiveDetail.getCode());
        List<InbReceiptDetail> updateReceiptDetails = new ArrayList<InbReceiptDetail>();

        for(InbReceiptDetail detail : receiptDetails){
            InbReceiptDetail inbReceiptDetail = new InbReceiptDetail();
            BigDecimal receiptQty = detail.getInboundQty();
            BigDecimal sourceQty = detail.getInboundQty();
            if(sourceQty.subtract(subQty).compareTo(BigDecimal.ZERO) < 0 ){
                detail.setInboundQty(BigDecimal.ZERO);
                ObjUtils.bean2bean(detail,inbReceiptDetail);
                subQty = subQty.subtract(sourceQty);
                updateReceiptDetails.add(inbReceiptDetail);
            }else{
                detail.setInboundQty(receiptQty.subtract(subQty));
                ObjUtils.bean2bean(detail,inbReceiptDetail);
                updateReceiptDetails.add(inbReceiptDetail);
                break;
            }
//            if(detail.getInboundQty().subtract(subQty).compareTo(BigDecimal.ZERO) < 0 ){
//                continue;
//            }else{
//                detail.setInboundQty(receiptQty.subtract(subQty));
//                ObjUtils.bean2bean(detail,inbReceiptDetail);
//                break;
//            }
        }

        receiveService.updateQty(receiveDetail,ibdDetail,updateReceiptDetails);

    }

    public void accountBack(Long receiveId, String detailOtherId) throws BizCheckedException {
        //获取receiveHeader
        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);
        ReceiveDetail detail = receiveService.getReceiveDetailByReceiveIdAnddetailOtherId(receiveId,detailOtherId);

        if (receiveHeader.getOrderStatus() == PoConstant.ORDER_RECTIPT_ALL){
            wuMart.ibdAccountBack(detail.getAccountId(),detail.getAccountDetailId());
        }
    }
}
