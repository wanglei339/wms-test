package com.lsh.wms.rf.service.receipt;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRestService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
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
 * Date: 16/7/29
 * Time: 16/7/29.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.rf.service.receipt.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/po/receipt")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ReceiptRestService implements IReceiptRestService {
    private static Logger logger = LoggerFactory.getLogger(ReceiptRestService.class);

    @Reference
    private IReceiptRpcService iReceiptRpcService;

    @Autowired
    private PoReceiptService poReceiptService;

    @POST
    @Path("init")
    public String init(String poReceiptInfo) {
        InbReceiptHeader inbReceiptHeader = JSON.parseObject(poReceiptInfo,InbReceiptHeader.class);
        List<InbReceiptDetail> inbReceiptDetailList = JSON.parseArray((String)inbReceiptHeader.getReceiptDetails(),InbReceiptDetail.class);
        poReceiptService.orderInit(inbReceiptHeader,inbReceiptDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("throw")
    public String throwOrder(String orderOtherId) throws BizCheckedException {
        if(iReceiptRpcService.throwOrder(orderOtherId)){
            return JsonUtils.SUCCESS();
        }else {
            return JsonUtils.FAIL("2020002");
        }

    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(ReceiptRequest request) throws BizCheckedException{
        iReceiptRpcService.insertOrder(request);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);
    }

    @POST
    @Path("updateReceiptStatus")
    public String updateReceiptStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        if(map.get("receiptId") == null || map.get("receiptStatus") == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        if(!StringUtils.isInteger(String.valueOf(map.get("receiptId")))
                || !StringUtils.isInteger(String.valueOf(map.get("receiptStatus")))) {
            throw new BizCheckedException("1020002", "参数类型不正确");
        }

        iReceiptRpcService.updateReceiptStatus((Long)map.get("receiptId"),(Integer)map.get("receiptStatus"));

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getPoReceiptDetailByReceiptId")
    public String getPoReceiptDetailByReceiptId(@QueryParam("receiptId") Long receiptId) throws BizCheckedException {
        if(receiptId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }
        InbReceiptHeader inbReceiptHeader = iReceiptRpcService.getPoReceiptDetailByReceiptId(receiptId);
        return JsonUtils.SUCCESS(inbReceiptHeader);
    }

    @GET
    @Path("getPoReceiptDetailByOrderId")
    public String getPoReceiptDetailByOrderId(@QueryParam("orderId") Long orderId) throws BizCheckedException {
        if(orderId == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }
        List<InbReceiptHeader> inbReceiptHeaderList = iReceiptRpcService.getPoReceiptDetailByOrderId(orderId);

        return JsonUtils.SUCCESS(inbReceiptHeaderList);
    }

    @POST
    @Path("countInbPoReceiptHeader")
    public String countInbPoReceiptHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iReceiptRpcService.countInbPoReceiptHeader(params));
    }

    @POST
    @Path("getPoReceiptDetailList")
    public String getPoReceiptDetailList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(iReceiptRpcService.getPoReceiptDetailList(params));
    }
}
