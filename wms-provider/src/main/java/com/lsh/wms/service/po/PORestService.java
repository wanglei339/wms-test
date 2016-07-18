package com.lsh.wms.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.base.BaseResponse;
import com.lsh.wms.api.model.po.PoItem;
import com.lsh.wms.api.model.po.PoRequest;
import com.lsh.wms.api.service.po.IPoRestService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("init")
    public String init(String poOrderInfo) { // test
        InbPoHeader inbPoHeader = JSON.parseObject(poOrderInfo,InbPoHeader.class);
        List<InbPoDetail> inbPoDetailList = JSON.parseArray(inbPoHeader.getOrderDetails(),InbPoDetail.class);
        poOrderService.insertOrder(inbPoHeader,inbPoDetailList);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insert")
    public BaseResponse insertOrder(PoRequest request) {
        BaseResponse response = new BaseResponse();

        InbPoHeader inbPoHeader = new InbPoHeader();
        ObjUtils.bean2bean(request, inbPoHeader);

        List<InbPoDetail> inbPoDetailList = new ArrayList<InbPoDetail>();

        for(PoItem poItem : request.getItems()) {
            InbPoDetail inbPoDetail = new InbPoDetail();

            ObjUtils.bean2bean(poItem, inbPoDetail);

            inbPoDetailList.add(inbPoDetail);
        }

        try {
            poOrderService.insertOrder(inbPoHeader, inbPoDetailList);

            response.setStatus(0);
            response.setMsg("ok");
            response.setDataKey(new Date());
        } catch (Exception ex) {
            response.setStatus(1);
            response.setMsg(ex.getMessage());
            response.setDataKey(new Date());
        }

        return response;
    }

    public void editOrder(InbPoHeader inbPoHeader){

    }

    public InbPoHeader getInbPoHeaderById(Integer id){
        return null;
    }

    public Integer countInbPoHeader(Map<String, Object> params){
        return null;
    }

    public List<InbPoHeader> getInbPoHeaderList(Map<String, Object> params){
        return null;
    }
}
