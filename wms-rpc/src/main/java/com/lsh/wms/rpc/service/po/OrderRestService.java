package com.lsh.wms.rpc.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.po.IOrderRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.po.PoOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
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
public class OrderRestService implements IOrderRestService{

    @Autowired
    private PoOrderService poOrderService;

    @GET
    @Path("getPoHeaderList")
    public String getPoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poOrderService.getInbPoHeaderList(params));
    }

    @POST
    @Path("getPoDetailByOrderId")
    public String getPoDetailByOrderId(Long orderId) {
        return JsonUtils.obj2Json(poOrderService.getInbPoDetailListByOrderId(orderId));
    }

    @POST
    @Path("countInbPoHeader")
    public String countInbPoHeader(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", poOrderService.countInbPoHeader(params));

        return JsonUtils.obj2Json(map);
    }

    @GET
    @Path("getPoDetailList")
    public String getPoDetailList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poOrderService.getInbPoDetailList(params));
    }

}
