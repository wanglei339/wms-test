package com.lsh.wms.rpc.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.po.IPoOrderRestService;
import com.lsh.wms.api.service.so.ISoOrderRestService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.po.InbPoHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/21
 * Time: 16/7/21.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.rpc.service.po.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SoOrderRestService implements ISoOrderRestService {

    @Autowired
    private SoOrderService soOrderService;

    @GET
    @Path("getOutbSoHeaderDetailByOrderId")
    public String getOutbSoHeaderDetailByOrderId(@QueryParam("orderId") Long orderId) {
        return JsonUtils.SUCCESS(soOrderService.getOutbSoHeaderByOrderId(orderId));
    }

    @POST
    @Path("countOutbSoHeader")
    public String countOutbSoHeader(Map<String, Object> params) {
        return JsonUtils.SUCCESS(soOrderService.countOutbSoHeader(params));
    }

    @POST
    @Path("getOutbSoHeaderList")
    public String getOutbSoHeaderList(Map<String, Object> params) {
        return JsonUtils.SUCCESS(soOrderService.getOutbSoHeaderList(params));
    }
}
