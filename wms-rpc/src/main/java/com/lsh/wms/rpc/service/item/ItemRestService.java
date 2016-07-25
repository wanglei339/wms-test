package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.item.IItemRestService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationDockService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
import com.lsh.wms.model.csi.CsiSku;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
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

    @Autowired
    private ItemService itemService;

    @Autowired
    private LocationService locationService;


    @GET
    @Path("getItem")
    public String getItem(@QueryParam("itemId") long itemId) {
        BaseinfoItem baseinfoItem = itemRpcService.getItem(itemId);
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
    @Path("getItemList")
    public String searchItem(Map<String, Object> mapQuery) throws BizCheckedException {
        List<BaseinfoItem>  baseinfoItemList = itemRpcService.searchItem(mapQuery);
        return  JsonUtils.SUCCESS(baseinfoItemList);
    }
    @POST
    @Path("getItemCount")
    public String countItem(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(itemService.countItem(mapQuery));
    }


    @POST
    @Path("insertItem")
    public String insertItem(BaseinfoItem item) throws BizCheckedException {
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("codeType",item.getCodeType());
        mapQuery.put("code",item.getCode());
        mapQuery.put("ownerId",item.getOwnerId());
        List<BaseinfoItem> items = itemService.searchItem(mapQuery);
        if(items.size() > 0){
            long status = items.get(0).getStatus();
            //是否存在正常状态的item
            if(status == 1)
                throw new BizCheckedException("2050002");
        }
        try{
            itemRpcService.insertItem(item);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Create failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateItem")
    public String updateItem(BaseinfoItem item) throws BizCheckedException {
        try{
            itemRpcService.updateItem(item);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("Update failed");
        }
        return JsonUtils.SUCCESS();
    }



    @GET
    @Path("getItemLocation")
    public String getItemLocation(@QueryParam("itemId") long iItemId) {
        return JsonUtils.SUCCESS(itemRpcService.getItemLocationList(iItemId));
    }
    @GET
    @Path("getItemLocationByLocationID")
    public String getItemLocationByLocationID(@QueryParam("locationId") long iLocationId) {
        return JsonUtils.SUCCESS(itemRpcService.getItemLocationByLocationID(iLocationId));
    }
    @POST
    @Path("insertItemLocation")
    public String insertItemLocation(BaseinfoItemLocation itemLocation) throws BizCheckedException {
        long locationId = itemLocation.getPickLocationid();
        //检查该location是否存在
        if(locationService.getLocation(locationId) == null){
            throw new BizCheckedException("2050003");
        }
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
        try{
            itemRpcService.updateItemLocation(itemLocation);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("setStatus")
    public String setStatus(@QueryParam("itemId") long iItemId,@QueryParam("status") long iStatus) {
        itemService.setStatus(iItemId,iStatus);
        return JsonUtils.SUCCESS();
    }


}
