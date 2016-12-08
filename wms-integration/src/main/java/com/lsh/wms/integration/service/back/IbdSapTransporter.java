package com.lsh.wms.integration.service.back;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.integration.service.wumartsap.WuMart;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.po.ReceiveHeader;
import com.lsh.wms.model.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mali on 16/11/17.
 */
@Component
public class IbdSapTransporter implements ITransporter {

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private WuMart wuMart;


    public void process(SysLog sysLog) {

        List<ReceiveDetail> receiveDetails = receiveService.getReceiveDetailListByReceiveId(sysLog.getBusinessId());
        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(sysLog.getBusinessId());
        // TODO: 2016/11/3 回传WMSAP 组装信息
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        List<CreateIbdDetail> details = new ArrayList<CreateIbdDetail>();
        for(ReceiveDetail receiveDetail : receiveDetails){
            if(PoConstant.RECEIVE_DETAIL_STATUS_SUCCESS == receiveDetail.getBackStatus()){
                continue;
            }
            CreateIbdDetail detail = new CreateIbdDetail();
            String poNumber = receiveHeader.getOrderType().equals(3) ? receiveHeader.getOrderOtherRefId() : receiveHeader.getOrderOtherId();
            detail.setPoNumber(poNumber);
            detail.setPoItme(receiveDetail.getDetailOtherId());
            BigDecimal inboudQty =  receiveDetail.getInboundQty().divide(receiveDetail.getPackUnit()).setScale(2,BigDecimal.ROUND_HALF_UP);

            BigDecimal orderQty = receiveDetail.getOrderQty();
            BigDecimal deliveQty = receiveHeader.getOrderType().equals(3) ? orderQty : inboudQty;
            if(deliveQty.compareTo(BigDecimal.ZERO) <= 0 || receiveDetail.getBackStatus() == PoConstant.RECEIVE_DETAIL_STATUS_SUCCESS){
                continue;
            }
            detail.setDeliveQty(deliveQty);
            detail.setUnit(receiveDetail.getPackName());
            detail.setMaterial(receiveDetail.getSkuCode());
            detail.setOrderType(receiveHeader.getOrderType());
            detail.setVendMat(receiveHeader.getReceiveId().toString());

            details.add(detail);
        }
        createIbdHeader.setWarehouseCode(PropertyUtils.getString("wumart.werks"));
        createIbdHeader.setItems(details);
        wuMart.sendIbd(createIbdHeader,sysLog);

    }
}
