package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.so.ISORestService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.model.po.InbPoDetail;
import com.lsh.wms.model.po.InbPoHeader;
import com.lsh.wms.model.so.OutbSoDetail;
import com.lsh.wms.model.so.OutbSoHeader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/11
 * Time: 16/7/11.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.service.so.
 * desc:类功能描述
 */
@Service(protocol = "rest")
@Path("order/so")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SORestService  implements ISORestService{

    @Autowired
    private SoOrderService soOrderService;

    @POST
    @Path("init")
    public String init(String soOrderInfo) {
        OutbSoHeader outbSoHeader = JSON.parseObject(soOrderInfo,OutbSoHeader.class);
        List<OutbSoDetail> outbSoDetailList = JSON.parseArray(outbSoHeader.getOrderDetails(),OutbSoDetail.class);
        soOrderService.insert(outbSoHeader,outbSoDetailList);
        return JsonUtils.SUCCESS();
    }
}
