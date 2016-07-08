package com.lsh.wms.service.po;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.po.IPORestService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
@Path("order")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class PORestService implements IPORestService{

    @Autowired
    private PoOrderService poOrderService;

    @POST
    @Path("init")
    public String init(String poOrderInfo) {
        InbPoHeader inbPoHeader = JSON.parseObject(poOrderInfo,InbPoHeader.class);
        List<InbPoDetail> inbPoDetailList = JSON.parseArray(inbPoHeader.getOrderDetails(),InbPoDetail.class);
        poOrderService.orderInit(inbPoHeader,inbPoDetailList);
        return JsonUtils.SUCCESS();
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
