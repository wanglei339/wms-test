package com.lsh.wms.rpc.service.po;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
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
    @Path("updateOrderStatus")
    public String updateOrderStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        //OrderOtherId与OrderId都为NULL 或者 OrderStatus为NULL
        if((map.get("orderOtherId") == null && map.get("orderId") == null)
                || map.get("orderStatus") == null) {
            throw new BizCheckedException("1010001", "参数不能为空");
        }

        if(map.get("orderOtherId") == null && map.get("orderId") != null) {
            if(!StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1010002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") == null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
                throw new BizCheckedException("1010002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") != null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))
                    && !StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1010002", "参数类型不正确");
            }
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("orderStatus")))) {
            throw new BizCheckedException("1010002", "参数类型不正确");
        }

        InbPoHeader inbPoHeader = new InbPoHeader();
        if(map.get("orderOtherId") != null && !StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
            inbPoHeader.setOrderOtherId(String.valueOf(map.get("orderOtherId")));
        }
        if(map.get("orderId") != null && StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
            inbPoHeader.setOrderId(Long.valueOf(String.valueOf(map.get("orderId"))));
        }
        inbPoHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));

        poOrderService.updateInbPoHeaderByOrderOtherIdOrOrderId(inbPoHeader);

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getPoHeaderList")
    public String getPoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poOrderService.getInbPoHeaderList(params));
    }

    @GET
    @Path("getPoDetailByOrderId")
    public String getPoDetailByOrderId(@QueryParam("orderId") Long orderId) throws BizCheckedException {
        if(orderId == null) {
            throw new BizCheckedException("1010001", "参数不能为空");
        }

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
