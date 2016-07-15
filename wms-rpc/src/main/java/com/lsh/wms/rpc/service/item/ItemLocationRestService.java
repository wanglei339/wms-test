package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.item.IItemLocationRestService;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lixin-mac on 16/7/14.
 */
@Service(protocol = "rest")
@Path("ItemLocation")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ItemLocationRestService implements IItemLocationRestService{


    private static Logger logger = LoggerFactory.getLogger(ItemLocationRestService.class);
    @Autowired
    private ItemLocationRpcService itemLocationRpcService;

    @GET
    @Path("getItemLocation")
    public String getItemLocation(@QueryParam("skuId") long iSkuId,@QueryParam("ownerId") long iOwnerId) {
        return JsonUtils.SUCCESS(itemLocationRpcService.getItemLocationList(iSkuId,iOwnerId));
    }
    @GET
    @Path("getItemLocationByLocationID")
    public String getItemLocationByLocationID(@QueryParam("locationId") long iLocationId) {
        return JsonUtils.SUCCESS(itemLocationRpcService.getItemLocationByLocationID(iLocationId));
    }
    @POST
    @Path("insertItemLocation")
    public String insertItemLocation(BaseinfoItemLocation itemLocation) {
        return JsonUtils.SUCCESS(itemLocationRpcService.insertItemLocation(itemLocation));
    }
    @POST
    @Path("updateItemLocation")
    public String updateItemLocation(BaseinfoItemLocation itemLocation) {
        int result = itemLocationRpcService.updateItemLocation(itemLocation);
        if (result == 0)
            return "update success!";
        else
            return "update failed";
    }
}
