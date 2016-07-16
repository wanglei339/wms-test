package com.lsh.wms.service.so;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.so.IDeliveryRestService;
import com.lsh.wms.core.service.so.SoDeliveryService;
import com.lsh.wms.model.so.OutbDeliveryDetail;
import com.lsh.wms.model.so.OutbDeliveryHeader;
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
        List<OutbDeliveryDetail> outbDeliveryDetailList = JSON.parseArray(outbDeliveryHeader.getDeliveryDetails(),OutbDeliveryDetail.class);
        soDeliveryService.insert(outbDeliveryHeader,outbDeliveryDetailList);
        return JsonUtils.SUCCESS();
    }
}
