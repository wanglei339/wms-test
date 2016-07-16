package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.item.IItemRestService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.csi.CsiSku;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/8.
 */

@Service(protocol = "rest")
@Path("item")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})

public class ItemRestService implements IItemRestService {
    private static Logger logger = LoggerFactory.getLogger(ItemRestService.class);

    @Autowired
    private ItemRpcService itemRpcService;

    @GET
    @Path("getItem")
    public String getItem(@QueryParam("ownerId") long iOwnerId, @QueryParam("skuId") long iSkuId) {
        logger.info("caonima"+iOwnerId+iSkuId);
        BaseinfoItem baseinfoItem = itemRpcService.getItem(iOwnerId,iSkuId);
        return JsonUtils.SUCCESS(baseinfoItem);
    }

    @GET
    @Path("getBaseInfo")
    public String getSku(@QueryParam("skuId") long iSkuId) {
        CsiSku csiSku = itemRpcService.getSku(iSkuId);
        return JsonUtils.SUCCESS(csiSku);
    }

    @GET
    @Path("getSkuByCode")
    public String getSkuByCode(@QueryParam("codeType") int iCodeType,@QueryParam("code")  String sCode) {
        CsiSku csiSku = itemRpcService.getSkuByCode(iCodeType,sCode);
        return JsonUtils.SUCCESS(csiSku);
    }

    @GET
    @Path("getItemsBySkuCode")
    public String getItemsBySkuCode(@QueryParam("ownerId") long iOwnerId,@QueryParam("skuCode")  String sSkuCode) {
        List<BaseinfoItem>   baseinfoItemList = itemRpcService.getItemsBySkuCode(iOwnerId,sSkuCode);
        return  JsonUtils.SUCCESS(baseinfoItemList);
    }

    @POST
    @Path("searchItem")
    public String searchItem(Map<String, Object> mapQuery) {
        List<BaseinfoItem>  baseinfoItemList = itemRpcService.searchItem(mapQuery);
        return  JsonUtils.SUCCESS(baseinfoItemList);
    }

    @POST
    @Path("insertItem")
    public String insertItem(BaseinfoItem item) {
        BaseinfoItem item_new = null;
        try{
            item_new = itemRpcService.insertItem(item);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }

        return JsonUtils.SUCCESS(item_new);
    }

    @POST
    @Path("updateItem")
    public String updateItem(BaseinfoItem item) {
        BaseinfoItem  newItem = null;
        try{
           newItem =  itemRpcService.updateItem(item);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        if(newItem == null){
            return JsonUtils.EXCEPTION_ERROR("The record does not exist");
        }
        return JsonUtils.SUCCESS(newItem);
    }



    @GET
    @Path("getItemLocation")
    public String getItemLocation(@QueryParam("skuId") long iSkuId,@QueryParam("ownerId") long iOwnerId) {
        return JsonUtils.SUCCESS(itemRpcService.getItemLocationList(iSkuId,iOwnerId));
    }
    @GET
    @Path("getItemLocationByLocationID")
    public String getItemLocationByLocationID(@QueryParam("locationId") long iLocationId) {
        return JsonUtils.SUCCESS(itemRpcService.getItemLocationByLocationID(iLocationId));
    }
    @POST
    @Path("insertItemLocation")
    public String insertItemLocation(BaseinfoItemLocation itemLocation) {
        try{
            itemRpcService.insertItemLocation(itemLocation);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("updateItemLocation")
    public String updateItemLocation(BaseinfoItemLocation itemLocation) {

        BaseinfoItemLocation  newItemLocation = null;
        try{
            newItemLocation =  itemRpcService.updateItemLocation(itemLocation);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        if(newItemLocation == null){
            return JsonUtils.EXCEPTION_ERROR("The record does not exist");
        }
        return JsonUtils.SUCCESS(itemLocation);
    }

}
