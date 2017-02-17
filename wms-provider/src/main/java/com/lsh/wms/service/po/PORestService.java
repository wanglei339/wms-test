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
import com.lsh.wms.api.model.po.*;
import com.lsh.wms.api.service.back.IDataBackService;
import com.lsh.wms.api.service.po.IPoRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationWarehouseService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoLocationWarehouse;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
//
//    @Reference
//    private IDataBackService dataBackService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BaseinfoLocationWarehouseService baseinfoLocationWarehouseService;

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
        int index = 0;
        for(PoItem item :request.getItems()){
            String skuCode = item.getSkuCode();
            Long ownerId = request.getOwnerUid();
            BaseinfoItem baseinfoItem =itemService.getItemsBySkuCode(ownerId,skuCode);
            if(baseinfoItem == null){
                throw new BizCheckedException("1010003",skuCode,"");//商品信息不存在
            }
            String msg = "";

            if(!baseinfoItem.getPackName().equals(item.getPackName())){
                msg += "包装名称,";
            }
            if(baseinfoItem.getPackUnit().compareTo(item.getPackUnit()) != 0){
                msg += "包装单位,";
            }
            if(item.getOrderQty()==null || item.getOrderQty().compareTo(BigDecimal.ZERO) <= 0){
                msg += "进货数量,";
            }
            if(StringUtils.isNotBlank(msg)){
                throw new BizCheckedException("1010004",skuCode+":"+msg,"");//商品信息错误
            }
            item.setSkuName(baseinfoItem.getSkuName());
            item.setUnitName(baseinfoItem.getUnitName());
            item.setUnitQty(PackUtil.UomQty2EAQty(item.getOrderQty(),item.getPackUnit()));
            index = index +1;
            item.setDetailOtherId(index+"");
        }
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
////            String poNumber =map.get("orderOtherId").toString();
////            header.setPoNumber(poNumber);
//            List<IbdDetail> ibdDetails = poOrderService.getInbPoDetailListByOrderId((Long) map.get("orderId"));
//            IbdHeader ibdHeader = poOrderService.getInbPoHeaderById((Long) map.get("orderId"));
//
//            //回传类型
//            String docType = ibdHeader.getOrderType().equals(3) ? "08" : "02";
//            String docStyle = ibdHeader.getOrderType().equals(3) ? "02" : "00";
//            header.setDocStyle(docStyle);
//            header.setDocType(docType);
//            header.setPoNumber(ibdHeader.getOrderOtherId());
//            header.setDelivNumber(ibdHeader.getOrderOtherRefId());
//
//
//            List<IbdItem>  items = new ArrayList<IbdItem>();
//            for(IbdDetail ibdDetail : ibdDetails){
//                IbdItem ibdItem = new IbdItem();
//                //转成ea
//                BigDecimal inboudQty =  ibdDetail.getInboundQty().multiply(ibdDetail.getPackUnit()).setScale(3);
//                BigDecimal orderQty = ibdDetail.getOrderQty().multiply(ibdDetail.getPackUnit()).setScale(3);
//                BigDecimal entryQnt = ibdHeader.getOrderType().equals(3) ? orderQty : inboudQty;
//                ibdItem.setEntryQnt(entryQnt);
//                ibdItem.setMaterialNo(ibdDetail.getSkuCode());
//                ibdItem.setDelivItem(ibdDetail.getDetailOtherId());
//                ibdItem.setPoItem(ibdDetail.getDetailOtherId());
//                //回传baseinfo_item中的unitName
//                //ibdItem.setPackName(inbPoDetail.getPackName());
//                //String unitName = itemService.getItem(ibdHeader.getOwnerUid(),inbPoDetail.getSkuId()).getUnitName().toUpperCase();
//                ibdItem.setPackName(ibdDetail.getUnitName());
//                items.add(ibdItem);
//            }
//            ibdBackRequest.setItems(items);
//            ibdBackRequest.setHeader(header);
//            if(ibdHeader.getOwnerUid() == 1){
//                dataBackService.wmDataBackByPost(ibdBackRequest, IntegrationConstan.URL_IBD);
//            }else{
//                dataBackService.erpDataBack(ibdBackRequest);
//            }
//
//        }
        return JsonUtils.SUCCESS();
    }

    /**
     * 投单(支持批量投单)
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("throwOrders")
    public String throwOrders() throws BizCheckedException {
        Map<String, Object> map = RequestUtils.getRequest();
        if(map.get("orderOtherId") == null || map.get("orderId") == null){
            throw new BizCheckedException("1010001", "参数不能为空");
        }
        String orderStatus = String.valueOf(PoConstant.ORDER_THROW);
        if(map.get("orderStatus") != null ){
            orderStatus = map.get("orderStatus").toString();
        }
        String orderOtherIds = String.valueOf(map.get("orderOtherId"));
        String orderIds = String.valueOf(map.get("orderId"));
        String orderOtherIdArr [] = orderOtherIds.split(",");
        String orderIdArr [] = orderIds.split(",");
        if(orderOtherIdArr.length != orderIdArr.length){
            throw new BizCheckedException("1010001", "参数不能为空");
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0 ;i < orderOtherIdArr.length ;i++){
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("orderOtherId",orderOtherIdArr[i]);
            queryMap.put("orderId",orderIdArr[i]);
            queryMap.put("orderStatus",orderStatus);
            list.add(queryMap);
        }
        Map<String,Object> result = poRpcService.throwOrder(list);

        return JsonUtils.SUCCESS(result);
    }

        @POST
    @Path("canReceipt")
    public String canReceipt(){
        Map<String, Object> map = RequestUtils.getRequest();
        poRpcService.canReceipt(map);
        return JsonUtils.SUCCESS();
    }


    @GET
    @Path("getStoreInfo")
    public String getStoreInfo(@QueryParam("orderId") Long orderId, @QueryParam("detailOtherId") String detailOtherId) {

        return JsonUtils.SUCCESS(poRpcService.getStoreInfo(orderId,detailOtherId));
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
