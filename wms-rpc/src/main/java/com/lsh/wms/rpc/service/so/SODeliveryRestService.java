package com.lsh.wms.rpc.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.DeliveryItem;
import com.lsh.wms.api.model.so.DeliveryRequest;
import com.lsh.wms.api.service.so.IDeliveryRestService;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @POST
    @Path("init")
    public String init(String soDeliveryInfo) {
        OutbDeliveryHeader outbDeliveryHeader = JSON.parseObject(soDeliveryInfo,OutbDeliveryHeader.class);
        //List<OutbDeliveryDetail> outbDeliveryDetailList = JSON.parseArray(outbDeliveryHeader.getDeliveryDetails(),OutbDeliveryDetail.class);
        //soDeliveryService.insert(outbDeliveryHeader,outbDeliveryDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(DeliveryRequest request) {
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

            outbDeliveryDetailList.add(outbDeliveryDetail);
        }

        //插入订单
        soDeliveryService.insertOrder(outbDeliveryHeader, outbDeliveryDetailList);

        return ResUtils.getResponse(ResponseConstant.RES_CODE_0,ResponseConstant.RES_MSG_OK,null);

    }
}
