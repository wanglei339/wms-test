package com.lsh.wms.integration.service.customer;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.csi.ICsiCustomerRestService;
import com.lsh.wms.core.dao.csi.CsiCustomerDao;
import com.lsh.wms.model.csi.CsiCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/11/15 上午12:42
 */
@Service(protocol = "rest", validation = "true")
@Path("obd")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class CsiCustomerService implements ICsiCustomerRestService{
    @Autowired
    private CsiCustomerService customerService;

    @POST
    @Path("getCustomerList")
    public String getCustomerList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(customerService.getCustomerList(mapQuery));
    }

    @POST
    @Path("getCustomerCount")
    public String getCustomerCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(customerService.getCustomerCount(mapQuery));
    }
}
