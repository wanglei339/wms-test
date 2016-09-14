package com.lsh.wms.service.po;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.po.Header;
import com.lsh.wms.api.model.po.IbdBackRequest;
import com.lsh.wms.api.model.po.IbdItem;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.service.po.IIbdBackService;
import com.lsh.wms.api.service.po.IPoRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class PORestService implements IPoRestService {

    private static Logger logger = LoggerFactory.getLogger(PORestService.class);

    @Autowired
    private PoRpcService poRpcService;

    @Autowired
    private PoOrderService poOrderService;

//    @Reference
//    private IIbdBackService ibdBackService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;

    @POST
    @Path("init")
    public String init(String poOrderInfo) { // test
        InbPoHeader inbPoHeader = JSON.parseObject(poOrderInfo,InbPoHeader.class);
        List<InbPoDetail> inbPoDetailList = JSON.parseArray((String)inbPoHeader.getOrderDetails(),InbPoDetail.class);
        poOrderService.insertOrder(inbPoHeader,inbPoDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(PoRequest request) throws BizCheckedException{
        poRpcService.insertOrder(request);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1,ResponseConstant.RES_MSG_OK,null);
    }

    @POST
    @Path("updateOrderStatus")
    public String updateOrderStatus() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();

        poRpcService.updateOrderStatus(map);

//        //确认收货之后将验收单回传到上游系统
//        if("5".equals(map.get("orderStatus").toString())){
//            IbdBackRequest ibdBackRequest = new IbdBackRequest();
//            Header header = new Header();
//            BaseinfoLocationWarehouse warehouse = (BaseinfoLocationWarehouse) baseinfoLocationWarehouseService.getBaseinfoItemLocationModelById(0L);
//            String warehouseName = warehouse.getWarehouseName();
//            header.setPlant(warehouseName);
//            String poNumber =map.get("orderOtherId").toString();
//            header.setPoNumber(poNumber);
//            List<InbPoDetail> inbPoDetails = poOrderService.getInbPoDetailListByOrderId((Long) map.get("orderId"));
//            InbPoHeader poHeader = poOrderService.getInbPoHeaderById((Long) map.get("orderId"));
//            List<IbdItem>  items = new ArrayList<IbdItem>();
//            for(InbPoDetail inbPoDetail : inbPoDetails){
//                IbdItem ibdItem = new IbdItem();
//                //转成ea
//                BigDecimal inboudQty =  inbPoDetail.getInboundQty().multiply(inbPoDetail.getPackUnit()).setScale(3);
//                BigDecimal orderQty = inbPoDetail.getOrderQty().multiply(inbPoDetail.getPackUnit()).setScale(3);
//                BigDecimal entryQnt = poHeader.getOrderType().equals(3) ? orderQty : inboudQty;
//
//                ibdItem.setEntryQnt(entryQnt);
//                ibdItem.setMaterialNo(inbPoDetail.getSkuCode());
//                ibdItem.setPoItem(inbPoDetail.getDetailOtherId());
//                //回传baseinfo_item中的unitName
//                //ibdItem.setPackName(inbPoDetail.getPackName());
//                String unitName = itemService.getItem(poHeader.getOwnerUid(),inbPoDetail.getSkuId()).getUnitName();
//                ibdItem.setPackName(unitName);
//                items.add(ibdItem);
//            }
//            ibdBackRequest.setItems(items);
//            ibdBackRequest.setHeader(header);
//            ibdBackService.createOrderByPost(ibdBackRequest, IntegrationConstan.URL_IBD);
//        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("canReceipt")
    public String canReceipt(){
        Map<String, Object> map = RequestUtils.getRequest();
        poRpcService.canReceipt(map);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getPoHeaderList")
    public String getPoHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poRpcService.getPoHeaderList(params));
    }

    @GET
    @Path("getPoDetailByOrderId")
    public String getPoDetailByOrderId(@QueryParam("orderId") Long orderId) throws BizCheckedException {
        return JsonUtils.SUCCESS(poRpcService.getPoDetailByOrderId(orderId));
    }

    @POST
    @Path("countInbPoHeader")
    public String countInbPoHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poRpcService.countInbPoHeader(params));
    }

    @POST
    @Path("getPoDetailList")
    public String getPoDetailList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(poRpcService.getPoDetailList(params));
    }
}
