package com.lsh.wms.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.back.IBackProviderRestService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.po.IbdObdRelation;
import com.lsh.wms.api.service.back.IBackInStorageProviderRpcService;
import com.lsh.wms.model.so.ObdHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 16/8/16.
 */
@Service(protocol = "rest")
@Path("back")
public class BackProviderRestService implements IBackProviderRestService {
    private static Logger logger = LoggerFactory.getLogger(BackProviderRestService.class);


    @Autowired
    SoOrderService soOrderService;
    @Autowired
    PoOrderService poOrderService;

    /**
     * 根据orderId获得供商类信息详情
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("getOrderInfo")
    public String getOrderInfo(@QueryParam("orderId") Long orderId) throws BizCheckedException {

        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(orderId);
        if(header==null){
            return JsonUtils.TOKEN_ERROR("订单不存在");
        }

        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("obdOtherId",header.getOrderOtherId());
        List<IbdObdRelation> ibdObdRelations = poOrderService.getIbdObdRelationList(queryMap);
        String poOtherId = ibdObdRelations.get(0).getIbdOtherId();
        IbdHeader ibdHeader = poOrderService.getInbPoHeaderByOrderOtherId(poOtherId);
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("poId",ibdHeader.getOrderId());
        result.put("soId",header.getOrderId());
        result.put("supplierNo",header.getSupplierNo());
        result.put("storeNo",ibdHeader.getOrderUser());
        result.put("returnDate",ibdHeader.getCreatedAt());
        result.put("soStatus",header.getOrderStatus());
        return JsonUtils.SUCCESS();
    }
    /**
     * 根据orderId获得供商类信息详情
     * @return
     * @throws BizCheckedException
     */
    @GET
    @Path("updateSoStatus")
    public String updateSoStatus(@QueryParam("orderId") Long orderId,@QueryParam("orderStatus") int orderStatus) throws BizCheckedException {

        ObdHeader header = soOrderService.getOutbSoHeaderByOrderId(orderId);
        if(header==null){
            return JsonUtils.TOKEN_ERROR("订单不存在");
        }
        header.setOrderStatus(orderStatus);
        soOrderService.update(header);
        return JsonUtils.SUCCESS();
    }
}
