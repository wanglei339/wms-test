package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.tu.ITuOrdersRestService;
import com.lsh.wms.api.service.tu.ITuOrdersRpcService;
import org.jsoup.helper.StringUtil;

/**
 * Created by zhanghongling on 16/11/4.
 */
@Service(protocol = "rest")
@Path("outbound/tuOrders")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TuOrdersRestService implements ITuOrdersRestService{
    //private static Logger logger = LoggerFactory.getLogger(TuOrdersRestService.class);
    @Reference
    private ITuOrdersRpcService iTuOrdersRpcService;

    @GET
    @Path("getTuOrdersList")
    //获取大店直流行程单数据
    public String getTuOrdersList(@QueryParam("tuId") String tuId) throws BizCheckedException{
        if(StringUtil.isBlank(tuId)){
            throw new BizCheckedException("2990021");
        }else{
            return JsonUtils.SUCCESS(iTuOrdersRpcService.getTuOrdersList(tuId));
        }

    }

    @GET
    @Path("getDeliveryOrderList")
    //获取大店直流发货单数据
    public String getDeliveryOrdersList(@QueryParam("tuId") String tuId) throws BizCheckedException{
        if(StringUtil.isBlank(tuId)){
            return JsonUtils.FAIL("failed! param error:" + tuId);
        }else{
            return JsonUtils.SUCCESS(iTuOrdersRpcService.getDeliveryOrdersList(tuId));
        }

    }
    @GET
    @Path("getSendCarOrdersList")
    public String getSendCarOrdersList(@QueryParam("tuId") String tuId) throws BizCheckedException {
        if(StringUtil.isBlank(tuId)){
            return JsonUtils.FAIL("failed! param error:" + tuId);
        }else{
            return JsonUtils.SUCCESS(iTuOrdersRpcService.getSendCarOrdersList(tuId));
        }
    }

}
