package com.lsh.wms.rpc.service.so;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.DeliveryItem;
import com.lsh.wms.api.model.so.DeliveryRequest;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.so.IDeliveryRestService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import com.lsh.wms.model.so.OutbSoDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.so.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/so/delivery")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SODeliveryRestService implements IDeliveryRestService {

    @Autowired
    private SoDeliveryService soDeliveryService;

    @Autowired
    private SoOrderService soOrderService;

    @POST
    @Path("init")
    public String init(String soDeliveryInfo) {
        OutbDeliveryHeader outbDeliveryHeader = JSON.parseObject(soDeliveryInfo,OutbDeliveryHeader.class);
        List<OutbDeliveryDetail> outbDeliveryDetailList = JSON.parseArray((String) outbDeliveryHeader.getDeliveryDetails(),OutbDeliveryDetail.class);
        soDeliveryService.insert(outbDeliveryHeader,outbDeliveryDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(DeliveryRequest request) throws BizCheckedException {
        BaseResponse response = new BaseResponse();

        //OutbDeliveryHeader
        OutbDeliveryHeader outbDeliveryHeader = new OutbDeliveryHeader();
        ObjUtils.bean2bean(request, outbDeliveryHeader);

        //设置订单状态
        outbDeliveryHeader.setDeliveryType(BusiConstant.EFFECTIVE_YES);

        //设置订单插入时间
        outbDeliveryHeader.setInserttime(new Date());

        //设置deliveryId
        outbDeliveryHeader.setDeliveryId(RandomUtils.genId());

        //初始化List<OutbDeliveryDetail>
        List<OutbDeliveryDetail> outbDeliveryDetailList = new ArrayList<OutbDeliveryDetail>();

        for(DeliveryItem deliveryItem : request.getItems()) {
            OutbDeliveryDetail outbDeliveryDetail = new OutbDeliveryDetail();

            ObjUtils.bean2bean(deliveryItem, outbDeliveryDetail);

            //设置deliveryId
            outbDeliveryDetail.setDeliveryId(outbDeliveryHeader.getDeliveryId());

            //查询订货数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", deliveryItem.getOrderId());
            params.put("itemId", deliveryItem.getItemId());
            params.put("start", 0);
            params.put("limit", 1);
            List<OutbSoDetail> outbSoDetailList = soOrderService.getOutbSoDetailList(params);

            if(outbSoDetailList.size() <= 0) {
//                throw new BizCheckedException("2900002", "出库订单明细数据异常");
            }

            //设置订货数
            outbDeliveryDetail.setOrderQty(outbSoDetailList.get(0).getOrderQty());

            outbDeliveryDetailList.add(outbDeliveryDetail);
        }

        //插入订单
        soDeliveryService.insertOrder(outbDeliveryHeader, outbDeliveryDetailList);

        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);

    }

//    @POST
//    @Path("updateDeliveryType")
//    public String updateDeliveryType() throws BizCheckedException {
//        Map<String, Object> map = RequestUtils.getRequest();
//
//        if(map.get("deliveryId") == null || map.get("deliveryType") == null) {
//            throw new BizCheckedException("1040001", "参数不能为空");
//        }
//
//        if(!StringUtils.isInteger(String.valueOf(map.get("deliveryId")))
//                || !StringUtils.isInteger(String.valueOf(map.get("deliveryType")))) {
//            throw new BizCheckedException("1040002", "参数类型不正确");
//        }
//
//        OutbDeliveryHeader outbDeliveryHeader = new OutbDeliveryHeader();
//        outbDeliveryHeader.setDeliveryId(Long.valueOf(String.valueOf(map.get("deliveryId"))));
//        outbDeliveryHeader.setDeliveryType(Integer.valueOf(String.valueOf(map.get("deliveryType"))));
//
//        soDeliveryService.updateOutbDeliveryHeaderByDeliveryId(outbDeliveryHeader);
//
//        return JsonUtils.SUCCESS();
//    }

    @GET
    @Path("getOutbDeliveryHeaderDetailByDeliveryId")
    public String getOutbDeliveryHeaderDetailByDeliveryId(@QueryParam("deliveryId") Long deliveryId) throws BizCheckedException {
        if(deliveryId == null) {
            throw new BizCheckedException("1040001", "参数不能为空");
        }

        OutbDeliveryHeader outbDeliveryHeader = soDeliveryService.getOutbDeliveryHeaderByDeliveryId(deliveryId);

        soDeliveryService.fillDetailToHeader(outbDeliveryHeader);

        return JsonUtils.SUCCESS(outbDeliveryHeader);
    }

    @POST
    @Path("countOutbDeliveryHeader")
    public String countOutbDeliveryHeader() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(soDeliveryService.countOutbDeliveryHeader(params));
    }

    @POST
    @Path("getOutbDeliveryHeaderList")
    public String getOutbDeliveryHeaderList() {
        Map<String, Object> params = RequestUtils.getRequest();
        return JsonUtils.SUCCESS(soDeliveryService.getOutbDeliveryHeaderList(params));
    }

}
