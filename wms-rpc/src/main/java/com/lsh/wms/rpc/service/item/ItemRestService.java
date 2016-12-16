package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.item.IItemRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.BaseinfoLocationDockService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoItemRequest;
import com.lsh.wms.model.baseinfo.BaseinfoLocationDock;
import com.lsh.wms.model.csi.CsiSku;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;

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
    private ItemLocationService itemLocationService;

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
        CsiSku csiSku = itemRpcService.getSkuByCode(iCodeType, sCode);
        return JsonUtils.SUCCESS(csiSku);
    }

    @GET
    @Path("getItemsBySkuCode")
    public String getItemsBySkuCode(@QueryParam("ownerId") long iOwnerId,@QueryParam("skuCode")  String sSkuCode) {
        BaseinfoItem baseinfoItem = itemRpcService.getItemsBySkuCode(iOwnerId, sSkuCode);
        return  JsonUtils.SUCCESS(baseinfoItem);
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
        BaseinfoItem newItem = null;
        try{
            newItem = itemRpcService.insertItem(item);
        }catch (BizCheckedException e) {
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("Create failed");
        }
        return JsonUtils.SUCCESS(newItem.getItemId());
    }
    @POST
    @Path("insertItems")
    public String insertItems(BaseinfoItemRequest request) throws BizCheckedException {
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("skuCode",request.getSkuCode());
        mapQuery.put("ownerId",request.getOwnerId());
        mapQuery.put("status",1);
        BaseinfoItem newItem = new BaseinfoItem();
        ObjUtils.bean2bean(request,newItem);
        if(!(request.getCode()!=null && (request.equals("")))) {

            List<BaseinfoItem> items = itemService.searchItem(mapQuery);
            if (items!=null && items.size() > 0) {
                BaseinfoItem baseinfoItem = items.get(0);
                if(!baseinfoItem.getCode().equals(request.getCode()) || baseinfoItem.getPackUnit().compareTo(request.getPackUnit())!=0){
                    return JsonUtils.TOKEN_ERROR("箱规或国条不一致，不允许修改");
                }
                //itemService.updateBarcode(items.get(0).getItemId(),item.getCode());
                ObjUtils.bean2bean(request, baseinfoItem);
                itemService.updateItem(baseinfoItem);
                return JsonUtils.SUCCESS();
            }
            itemRpcService.insertItem(newItem);
        }else {
            itemRpcService.insertItem(newItem);
        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateItem")
    public String updateItem(BaseinfoItem item) throws BizCheckedException {
        try{
            itemRpcService.updateItem(item);
        }catch (BizCheckedException e) {
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("Update failed");
        }
        return JsonUtils.SUCCESS();
    }



    @GET
    @Path("getItemLocation")
    public String getItemLocation(@QueryParam("itemId") long iItemId) {
        Map<String, List<BaseinfoItemLocation>> map = new HashMap<String, List<BaseinfoItemLocation>>();
        map.put("list",itemRpcService.getItemLocationList(iItemId));
        return JsonUtils.SUCCESS(map);
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
        }catch (BizCheckedException e) {
            throw e;
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
 /*   @POST
    @Path("updateItemLocation")
    public String updateItemLocation(BaseinfoItemLocation itemLocation) {
        try{
            itemRpcService.updateItemLocation(itemLocation);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }*/
    @POST
    @Path("updateItemLocation")
    public String updateItemLocation(BaseinfoItemLocation itemLocation) {
        try{
            itemRpcService.updateByItemIdAndPicId(itemLocation);
        }catch (BizCheckedException e) {
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage());
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

    @POST
    @Path("deleteItemLocation")
    public String deleteItemLocation(BaseinfoItemLocation itemLocation) {
        try{
            itemLocationService.deleteItemLocation(itemLocation);
        }catch (BizCheckedException e) {
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }


    @POST
    @Path("updateBarcode")
    public String updateBarcode() throws BizCheckedException {

        Map<String,Object> request = RequestUtils.getRequest();
        String barcode = request.get("barcode").toString();
        Long itemId = Long.valueOf(request.get("itemId").toString());
        itemRpcService.updateBarcode(itemId,barcode);

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("showCodes")
    public String showCodes(@QueryParam("itemId") Long itemId) {
        return JsonUtils.SUCCESS(itemRpcService.showCodes(itemId));
    }


}
