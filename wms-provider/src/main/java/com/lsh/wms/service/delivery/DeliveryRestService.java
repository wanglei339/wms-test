package com.lsh.wms.service.delivery;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.DeliveryRequest;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.IDeliveryRestService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
@Service(protocol = "rest")
@Path("order/so/delivery")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class DeliveryRestService implements IDeliveryRestService {

    private static Logger logger = LoggerFactory.getLogger(DeliveryRestService.class);

    @Autowired
    private DeliveryRpcService deliveryRpcService;

    @POST
    @Path("insert")
    public BaseResponse insertOrder(DeliveryRequest request) throws BizCheckedException {
        deliveryRpcService.insertOrder(request);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1,ResponseConstant.RES_MSG_OK,null);

    }

    @GET
    @Path("getOutbDeliveryHeaderDetailByDeliveryId")
    public String getOutbDeliveryHeaderDetailByDeliveryId(@QueryParam("deliveryId") Long deliveryId) throws BizCheckedException {
        return JsonUtils.SUCCESS(deliveryRpcService.getOutbDeliveryHeaderDetailByDeliveryId(deliveryId));
    }

    @POST
    @Path("countOutbDeliveryHeader")
    public String countOutbDeliveryHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(deliveryRpcService.countOutbDeliveryHeader(params));
    }

    @POST
    @Path("getOutbDeliveryHeaderList")
    public String getOutbDeliveryHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(deliveryRpcService.getOutbDeliveryHeaderList(params));
    }

}
