package com.lsh.wms.rf.service.po;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.service.po.IPoRestService;
import com.lsh.wms.api.service.po.IPoRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
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
 * Date: 16/7/8
 * Time: 16/7/8.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.po.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class PoRestService implements IPoRestService {

    private static Logger logger = LoggerFactory.getLogger(PoRestService.class);

    @Reference
    private IPoRpcService iPoRpcService;

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("init")
    public String init(String poOrderInfo) { // test
        IbdHeader ibdHeader = JSON.parseObject(poOrderInfo,IbdHeader.class);
        List<IbdDetail> ibdDetailList = JSON.parseArray((String) ibdHeader.getOrderDetails(),IbdDetail.class);
        poOrderService.insertOrder(ibdHeader, ibdDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(PoRequest request) throws BizCheckedException{
        iPoRpcService.insertOrder(request);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1,ResponseConstant.RES_MSG_OK,null);
    }

    @POST
    @Path("updateOrderStatus")
    public String updateOrderStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        iPoRpcService.updateOrderStatus(map);

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getPoHeaderList")
    public String getPoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iPoRpcService.getPoHeaderList(params));
    }

    @GET
    @Path("getPoDetailByOrderId")
    public String getPoDetailByOrderId(@QueryParam("orderId") Long orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS(iPoRpcService.getPoDetailByOrderId(orderId));
    }

    @POST
    @Path("countInbPoHeader")
    public String countInbPoHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iPoRpcService.countInbPoHeader(params));
    }

    @POST
    @Path("getPoDetailList")
    public String getPoDetailList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iPoRpcService.getPoDetailList(params));
    }

    @POST
    @Path("canReceipt")
    public String canReceipt() {
        return null;
    }


    public String getStoreInfo(Long orderId, String detailOtherId) {
        return null;
    }
}
