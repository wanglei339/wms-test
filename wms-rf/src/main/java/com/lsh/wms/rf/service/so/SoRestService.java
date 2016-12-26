package com.lsh.wms.rf.service.so;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.ISoRestService;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.so.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/so")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SoRestService implements ISoRestService {

    private static Logger logger = LoggerFactory.getLogger(SoRestService.class);

    @Reference
    private ISoRpcService iSoRpcService;

    @Autowired
    private SoOrderService soOrderService;

//    @POST
//    @Path("init")
//    public String init(String soOrderInfo) {
//        ObdHeader obdHeader = JSON.parseObject(soOrderInfo,ObdHeader.class);
//        List<ObdDetail> obdDetailList = JSON.parseArray((String) obdHeader.getOrderDetails(),ObdDetail.class);
//        soOrderService.insert(obdHeader, obdDetailList);
//        return JsonUtils.SUCCESS();
//    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(SoRequest request) throws BizCheckedException {
        iSoRpcService.insertOrder(request);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1,ResponseConstant.RES_MSG_OK,null);
    }

    @POST
    @Path("updateOrderStatus")
    public String updateOrderStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        iSoRpcService.updateOrderStatus(map);

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getOutbSoHeaderDetailByOrderId")
    public String getOutbSoHeaderDetailByOrderId(@QueryParam("orderId") Long orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iSoRpcService.getOutbSoHeaderDetailByOrderId(orderId));
    }

    @POST
    @Path("countOutbSoHeader")
    public String countOutbSoHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iSoRpcService.countOutbSoHeader(params));
    }

    @POST
    @Path("getOutbSoHeaderList")
    public String getOutbSoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iSoRpcService.getOutbSoHeaderList(params));
    }

    public String confirmBack(Long orderId, Long uid) throws BizCheckedException {
        return null;
    }
}
