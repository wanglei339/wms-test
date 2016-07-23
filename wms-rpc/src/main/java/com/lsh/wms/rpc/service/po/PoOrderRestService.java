package com.lsh.wms.rpc.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.po.IPoOrderRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.po.PoOrderService;
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
public class PoOrderRestService implements IPoOrderRestService {

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("getPoHeaderList")
    public String getPoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poOrderService.getInbPoHeaderList(params));
    }

    @GET
    @Path("getPoDetailByOrderId")
    public String getPoDetailByOrderId(@QueryParam("orderId") Long orderId) {
        InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderId(orderId);

        poOrderService.fillDetailToHeader(inbPoHeader);

        return JsonUtils.SUCCESS(inbPoHeader);
    }

    @POST
    @Path("countInbPoHeader")
    public String countInbPoHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poOrderService.countInbPoHeader(params));
    }

    @POST
    @Path("getPoDetailList")
    public String getPoDetailList() {
        Map<String, Object> params = RequestUtils.getRequest();

        List<InbPoHeader> inbPoHeaderList = poOrderService.getInbPoHeaderList(params);

        poOrderService.fillDetailToHeaderList(inbPoHeaderList);

        return JsonUtils.SUCCESS(inbPoHeaderList);
    }

}