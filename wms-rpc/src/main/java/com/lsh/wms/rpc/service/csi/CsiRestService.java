package com.lsh.wms.rpc.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.csi.ICsiRestService;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by zengwenjun on 16/7/8.
 */
@Service(protocol = "rest")
@Path("csi")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class CsiRestService implements ICsiRestService {
    private static Logger logger = LoggerFactory.getLogger(ICsiRestService.class);
    @Autowired
    private CsiRpcService csiRpcService;

    @GET
    @Path("getCatInfo")
    public String getCatInfo(@QueryParam("cat_id") int iCatId) {
        return JsonUtils.SUCCESS(csiRpcService.getCatInfo(iCatId));
    }

    @GET
    @Path("getCatFull")
    public String getCatFull(@QueryParam("cat_id") int iCatId) {
        return JsonUtils.SUCCESS(csiRpcService.getCatFull(iCatId));
    }

    @GET
    @Path("getCatChilds")
    public String getCatChilds(@QueryParam("cat_id") int iCatId) {
        return JsonUtils.SUCCESS(csiRpcService.getCatChilds(iCatId));
    }

    @GET
    @Path("getOwner")
    public String getOwner(@QueryParam("owner_id") int iOwnerId) {
        return JsonUtils.SUCCESS(csiRpcService.getOwner(iOwnerId));
    }

    @GET
    @Path("getSku")
    public String getSku(@QueryParam("sku_id") long iSkuId) {
        return JsonUtils.SUCCESS(csiRpcService.getSku(iSkuId));
    }

    @GET
    @Path("getSkuByCode")
    public String getSkuByCode(@QueryParam("code_type") int iCodeType, @QueryParam("code") String sCode) {
        return JsonUtils.SUCCESS(csiRpcService.getSkuByCode(iCodeType, sCode));
    }

    @GET
    @Path("getSupplier")
    public String getSupplier(@QueryParam("supplier_id") int iSupplierId) {
        return JsonUtils.SUCCESS(csiRpcService.getSupplier(iSupplierId));
    }
}
