package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.item.IItemRestService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
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
        BaseinfoItem item_new = itemRpcService.insertItem(item);
        return JsonUtils.SUCCESS(item_new);
    }

    public String updateItem(BaseinfoItem item) {
        int result = itemRpcService.updateItem(item);
        if (result == 0)
            return "更新成功!!";
        else
            return "更新失败!!!";

    }

}
