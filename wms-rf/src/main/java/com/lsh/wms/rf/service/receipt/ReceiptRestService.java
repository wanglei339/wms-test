package com.lsh.wms.rf.service.receipt;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.ReceiptItem;
import com.lsh.wms.api.model.po.ReceiptRequest;
import com.lsh.wms.api.service.po.IReceiptRfService;
import com.lsh.wms.api.service.po.IReceiptRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.po.PoReceiptService;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
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
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ReceiptRestService implements IReceiptRfService {

    private static Logger logger = LoggerFactory.getLogger(ReceiptRestService.class);

    @Reference
    private IReceiptRpcService iReceiptRpcService;

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("insert")
    public BaseResponse insertOrder() throws BizCheckedException{
        Map<String, Object> request = RequestUtils.getRequest();

        List<ReceiptItem> receiptItemList = JSON.parseArray((String)request.get("items"), ReceiptItem.class);
        request.put("items", receiptItemList);

        ReceiptRequest receiptRequest = BeanMapTransUtils.map2Bean(request, ReceiptRequest.class);

        HttpSession session = RequestUtils.getSession();
        if(session.getAttribute("wareHouseId") == null) {
            receiptRequest.setWarehouseId(PropertyUtils.getLong("wareHouseId", 1L));
        } else {
            receiptRequest.setWarehouseId((Long) session.getAttribute("wareHouseId"));
        }

        receiptRequest.setReceiptUser((String) session.getAttribute("uid"));

        receiptRequest.setReceiptTime(new Date());

        InbPoHeader inbPoHeader = poOrderService.getInbPoHeaderByOrderOtherId(receiptRequest.getOrderOtherId());
        if(inbPoHeader == null) {
            throw new BizCheckedException("2010001", "订单不存在");
        }

        for(ReceiptItem receiptItem : receiptRequest.getItems()) {
            InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndBarCode(inbPoHeader.getOrderId(), receiptItem.getBarCode());

            receiptItem.setSkuName(inbPoDetail.getSkuName());
            receiptItem.setPackUnit(inbPoDetail.getPackUnit());
            receiptItem.setMadein(inbPoDetail.getMadein());
        }

        receiptRequest.setItems(receiptItemList);

        iReceiptRpcService.insertOrder(receiptRequest);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);
    }

    @POST
    @Path("getorderinfo")
    public String getPoDetailByOrderIdAndBarCode(@FormParam("orderId") Long orderId, @FormParam("barCode") String barCode) throws BizCheckedException {
        if(orderId == null || barCode == null) {
            throw new BizCheckedException("1020001", "参数不能为空");
        }

        InbPoDetail inbPoDetail = poOrderService.getInbPoDetailByOrderIdAndBarCode(orderId, barCode);

        Map<String, Object> orderInfoMap = new HashMap<String, Object>();
        orderInfoMap.put("skuName", inbPoDetail.getSkuName());
        orderInfoMap.put("packUnit", inbPoDetail.getPackUnit());
        orderInfoMap.put("orderQty", inbPoDetail.getOrderQty());

        return JsonUtils.SUCCESS(orderInfoMap);
    }
}
