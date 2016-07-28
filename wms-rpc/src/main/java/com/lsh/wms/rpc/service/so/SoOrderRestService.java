package com.lsh.wms.rpc.service.so;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoOrderRestService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.so.OutbSoHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
@Path("order/so")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SoOrderRestService implements ISoOrderRestService {

    @Autowired
    private SoOrderService soOrderService;

    @POST
    @Path("updateOrderStatus")
    public String updateOrderStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        if((map.get("orderOtherId") == null && map.get("orderId") == null)
                || map.get("orderStatus") == null) {
            throw new BizCheckedException("1030001", "参数不能为空");
        }

        if(map.get("orderOtherId") == null && map.get("orderId") != null) {
            if(!StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") == null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(map.get("orderOtherId") != null && map.get("orderId") != null) {
            if(StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))
                    && !StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
                throw new BizCheckedException("1030002", "参数类型不正确");
            }
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("orderStatus")))) {
            throw new BizCheckedException("1030002", "参数类型不正确");
        }

        OutbSoHeader outbSoHeader = new OutbSoHeader();
        if(map.get("orderOtherId") != null && !StringUtils.isBlank(String.valueOf(map.get("orderOtherId")))) {
            outbSoHeader.setOrderOtherId(String.valueOf(map.get("orderOtherId")));
        }
        if(map.get("orderId") != null && StringUtils.isInteger(String.valueOf(map.get("orderId")))) {
            outbSoHeader.setOrderId(Long.valueOf(String.valueOf(map.get("orderId"))));
        }
        outbSoHeader.setOrderStatus(Integer.valueOf(String.valueOf(map.get("orderStatus"))));

        soOrderService.updateOutbSoHeaderByOrderOtherIdOrOrderId(outbSoHeader);

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getOutbSoHeaderDetailByOrderId")
    public String getOutbSoHeaderDetailByOrderId(@QueryParam("orderId") Long orderId) throws BizCheckedException {
        if(orderId == null) {
            throw new BizCheckedException("1030001", "参数不能为空");
        }

        return JsonUtils.SUCCESS(soOrderService.getOutbSoHeaderByOrderId(orderId));
    }

    @POST
    @Path("countOutbSoHeader")
    public String countOutbSoHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(soOrderService.countOutbSoHeader(params));
    }

    @POST
    @Path("getOutbSoHeaderList")
    public String getOutbSoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(soOrderService.getOutbSoHeaderList(params));
    }
}
