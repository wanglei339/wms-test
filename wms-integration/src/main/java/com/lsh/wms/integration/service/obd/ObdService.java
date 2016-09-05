package com.lsh.wms.integration.service.obd;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.base.ResUtils;
import com.lsh.wms.api.model.base.ResponseConstant;
import com.lsh.wms.api.model.so.ObdDetail;
import com.lsh.wms.api.model.so.ObdRequest;
import com.lsh.wms.api.model.so.SoItem;
import com.lsh.wms.api.model.so.SoRequest;
import com.lsh.wms.api.service.so.IObdService;
import com.lsh.wms.api.service.so.ISoRpcService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.so.OutbSoHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mali on 16/9/2.
 */
@Service(protocol = "rest", validation = "true")
@Path("obd")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ObdService implements IObdService{

    @Reference
    private ISoRpcService soRpcService;

    @Autowired
    private SoOrderService soOrderService;

    @POST
    @Path("add")
    public BaseResponse add(ObdRequest request) throws BizCheckedException{

        //数量做转换 ea转化为外包装箱数
        List<ObdDetail> details = request.getDetailList();

        List<ObdDetail> newDetails = new ArrayList<ObdDetail>();
        List<SoItem> items = new ArrayList<SoItem>();

        for(ObdDetail obdDetail : details){
            BigDecimal qty = obdDetail.getOrderQty().divide(obdDetail.getPackUnit(),2);
            obdDetail.setOrderQty(qty);
            newDetails.add(obdDetail);
            SoItem soItem = new SoItem();
            ObjUtils.bean2bean(obdDetail,soItem);
            items.add(soItem);
        }
        request.setDetailList(newDetails);


        //初始化SoRequest
        SoRequest soRequest = new SoRequest();
        ObjUtils.bean2bean(request,soRequest);

        soRequest.setItems(items);

        // TODO: 16/9/5  重复的order_other_id 校验
        String orderOtherId = request.getOrderOtherId();
        Integer orderType = request.getOrderType();
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("orderOtherId" , orderOtherId);
        mapQuery.put("orderType",orderType);
        List<OutbSoHeader> lists = soOrderService.getOutbSoHeaderList(mapQuery);
        if(lists.size() > 0){
            throw new BizCheckedException("2020099");
        }

        soRpcService.insertOrder(soRequest);
        return ResUtils.getResponse(ResponseConstant.RES_CODE_1, ResponseConstant.RES_MSG_OK, null);
    }
}
