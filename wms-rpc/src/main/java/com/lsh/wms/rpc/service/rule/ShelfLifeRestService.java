package com.lsh.wms.rpc.service.rule;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.baseinfo.IShelfLifeRestService;
import com.lsh.wms.model.baseinfo.BaseinfoShelflifeRule;
import com.lsh.wms.rpc.service.item.ItemRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/10.
 */
@Service(protocol = "rest")
@Path("rule")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ShelfLifeRestService implements IShelfLifeRestService{
    private static Logger logger = LoggerFactory.getLogger(ItemRestService.class);

    @Autowired
    private ShelfLifeRpcService shelfLifeRpcService;

    @POST
    @Path("getShelflifeRuleList")
    public String getShelflifeRuleList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(shelfLifeRpcService.getShelflifeRuleList(mapQuery));
    }

    @POST
    @Path("getShelflifeRuleCount")
    public String getShelflifeRuleCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(shelfLifeRpcService.getShelflifeRuleCount(mapQuery));
    }

    @POST
    @Path("updateShelflifeRule")
    public String updateShelflifeRule(BaseinfoShelflifeRule shelflifeRule) {
        try {
            shelfLifeRpcService.updateShelflifeRule(shelflifeRule);
        }catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("update failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insertShelflifeRule")
    public String insertShelflifeRule(BaseinfoShelflifeRule shelflifeRule) {
        try {
            shelfLifeRpcService.insertShelflifeRule(shelflifeRule);
        }catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("deleteShelflifeRule")
    public String deleteShelflifeRule(BaseinfoShelflifeRule shelflifeRule) {
        try {
            shelfLifeRpcService.deleteShelflifeRule(shelflifeRule);
        }catch (Exception e) {
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("delete failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getShelflifeRule")
    public String getShelflifeRule(@QueryParam("ruleId") Long ruleId) {
        return JsonUtils.SUCCESS(shelfLifeRpcService.getShelflifeRule(ruleId));
    }
}
