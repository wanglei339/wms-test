package com.lsh.wms.rpc.service.item;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.item.IItemRestService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
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
    public String getItem(@QueryParam("iOwnerId") long iOwnerId, @QueryParam("iSkuId") long iSkuId) {
        BaseinfoItem baseinfoItem = itemRpcService.getItem(iOwnerId,iSkuId);
        return JsonUtils.SUCCESS(baseinfoItem);
    }

    @GET
    @Path("getBaseInfo")
    public String getBaseInfo(@QueryParam("iSkuId") long iSkuId) {
        CsiSku csiSku = itemRpcService.getBaseInfo(iSkuId);
        return JsonUtils.SUCCESS(csiSku);
    }

    @GET
    @Path("getSkuByCode")
    public String getSkuByCode(@QueryParam("iCodeType") int iCodeType,@QueryParam("sCode")  String sCode) {
        CsiSku csiSku = itemRpcService.getSkuByCode(iCodeType,sCode);
        return JsonUtils.SUCCESS(csiSku);
    }

    @GET
    @Path("getItemsBySkuCode")
    public String getItemsBySkuCode(@QueryParam("iOwnerId") long iOwnerId,@QueryParam("sSkuCode")  String sSkuCode) {
        List<BaseinfoItem>   baseinfoItemList = itemRpcService.getItemsBySkuCode(iOwnerId,sSkuCode);
        return  JsonUtils.SUCCESS(baseinfoItemList);
    }

    @GET
    @Path("searchItem")
    public String searchItem(Map<String, Object> mapQuery) {
        List<BaseinfoItem>  baseinfoItemList = itemRpcService.searchItem(mapQuery);
        return  JsonUtils.SUCCESS(baseinfoItemList);
    }
}
