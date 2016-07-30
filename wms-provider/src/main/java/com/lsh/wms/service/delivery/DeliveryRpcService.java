package com.lsh.wms.service.delivery;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.so.DeliveryItem;
import com.lsh.wms.api.model.so.DeliveryRequest;
import com.lsh.wms.api.service.so.IDeliveryRpcService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.so.OutbSoDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.so.
 * desc:类功能描述
 */
@Service(protocol = "dubbo")
public class DeliveryRpcService implements IDeliveryRpcService {

    private static Logger logger = LoggerFactory.getLogger(DeliveryRpcService.class);

    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private SoOrderService soOrderService;

    public void insertOrder(DeliveryRequest request) throws BizCheckedException {
        //OutbDeliveryHeader
        OutbDeliveryHeader outbDeliveryHeader = new OutbDeliveryHeader();
        ObjUtils.bean2bean(request, outbDeliveryHeader);

        //设置订单状态
        outbDeliveryHeader.setDeliveryType(BusiConstant.EFFECTIVE_YES);

        //设置订单插入时间
        outbDeliveryHeader.setInserttime(new Date());

        //设置deliveryId
        outbDeliveryHeader.setDeliveryId(RandomUtils.genId());

        //初始化List<OutbDeliveryDetail>
        List<OutbDeliveryDetail> outbDeliveryDetailList = new ArrayList<OutbDeliveryDetail>();

        for(DeliveryItem deliveryItem : request.getItems()) {
            OutbDeliveryDetail outbDeliveryDetail = new OutbDeliveryDetail();

            ObjUtils.bean2bean(deliveryItem, outbDeliveryDetail);

            //设置deliveryId
            outbDeliveryDetail.setDeliveryId(outbDeliveryHeader.getDeliveryId());

            //查询订货数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", deliveryItem.getOrderId());
            params.put("itemId", deliveryItem.getItemId());
            params.put("start", 0);
            params.put("limit", 1);
            List<OutbSoDetail> outbSoDetailList = soOrderService.getOutbSoDetailList(params);

            if(outbSoDetailList.size() <= 0) {
//                throw new BizCheckedException("2900002", "出库订单明细数据异常");
            }

            //设置订货数
            outbDeliveryDetail.setOrderQty(outbSoDetailList.get(0).getOrderQty());

            outbDeliveryDetailList.add(outbDeliveryDetail);
        }

        //插入订单
        soDeliveryService.insertOrder(outbDeliveryHeader, outbDeliveryDetailList);

    }

//    public Boolean updateDeliveryType(Map<String, Object> map) throws BizCheckedException {
//        if(map.get("deliveryId") == null || map.get("deliveryType") == null) {
//            throw new BizCheckedException("1040001", "参数不能为空");
//        }
//
//        if(!StringUtils.isInteger(String.valueOf(map.get("deliveryId")))
//                || !StringUtils.isInteger(String.valueOf(map.get("deliveryType")))) {
//            throw new BizCheckedException("1040002", "参数类型不正确");
//        }
//
//        OutbDeliveryHeader outbDeliveryHeader = new OutbDeliveryHeader();
//        outbDeliveryHeader.setDeliveryId(Long.valueOf(String.valueOf(map.get("deliveryId"))));
//        outbDeliveryHeader.setDeliveryType(Integer.valueOf(String.valueOf(map.get("deliveryType"))));
//
//        soDeliveryService.updateOutbDeliveryHeaderByDeliveryId(outbDeliveryHeader);
//
//        return true;
//    }

    public OutbDeliveryHeader getOutbDeliveryHeaderDetailByDeliveryId(Long deliveryId) throws BizCheckedException {
        if(deliveryId == null) {
            throw new BizCheckedException("1040001", "参数不能为空");
        }

        OutbDeliveryHeader outbDeliveryHeader = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(deliveryId);

        soDeliveryService.fillDetailToHeader(outbDeliveryHeader);

        return outbDeliveryHeader;
    }

    public Integer countOutbDeliveryHeader(Map<String, Object> params) {
        return soDeliveryService.countOutbDeliveryHeader(params);
    }

    public List<OutbDeliveryHeader> getOutbDeliveryHeaderList(Map<String, Object> params) {
        return soDeliveryService.getOutbDeliveryHeaderList(params);
    }

}
