package com.lsh.wms.rpc.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.csi.ICsiRestService;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
import net.sf.json.util.JSONUtils;
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
    public String getCatInfo(@QueryParam("catId") int iCatId) {
        return JsonUtils.SUCCESS(csiRpcService.getCatInfo(iCatId));
    }

    @GET
    @Path("getCatFull")
    public String getCatFull(@QueryParam("catId") int iCatId) {
        return JsonUtils.SUCCESS(csiRpcService.getCatFull(iCatId));
    }

    @GET
    @Path("getCatChilds")
    public String getCatChilds(@QueryParam("catId") int iCatId) {
        return JsonUtils.SUCCESS(csiRpcService.getCatChilds(iCatId));
    }

    @GET
    @Path("getOwner")
    public String getOwner(@QueryParam("ownerId") int iOwnerId) {
        return JsonUtils.SUCCESS(csiRpcService.getOwner(iOwnerId));
    }

    @GET
    @Path("getSku")
    public String getSku(@QueryParam("skuId") long iSkuId) {
        return JsonUtils.SUCCESS(csiRpcService.getSku(iSkuId));
    }

    @GET
    @Path("getSkuByCode")
    public String getSkuByCode(@QueryParam("codeType") int iCodeType, @QueryParam("code") String sCode) {
        return JsonUtils.SUCCESS(csiRpcService.getSkuByCode(iCodeType, sCode));
    }

    @GET
    @Path("getSupplier")
    public String getSupplier(@QueryParam("supplierId") int iSupplierId) {
        return JsonUtils.SUCCESS(csiRpcService.getSupplier(iSupplierId));
    }

    @POST
    @Path("insertSku")
    public String insertSku(CsiSku sku){
        return JsonUtils.SUCCESS(csiRpcService.insertSku(sku));
    }

    @POST
    @Path("updateSku")
    public String updateSku(CsiSku sku){
        int result = csiRpcService.updateSku(sku);
        if (result == 0)
            return "update success!";
        else
            return "update failed";

    }

    @POST
    @Path("insertSupplier")
    public String insertSupplier(CsiSupplier supplier){
        return JsonUtils.SUCCESS(csiRpcService.insertSupplier(supplier));
    }

    @POST
    @Path("updateSupplier")
    public String updateSupplier(CsiSupplier supplier){
        int result = csiRpcService.updateSupplier(supplier);
        if (result == 0)
            return "update success!";
        else
            return "update failed";
    }

    @POST
    @Path("insertOwner")
    public String insertOwner(CsiOwner owner) {
        return JsonUtils.SUCCESS(csiRpcService.insertOwner(owner));
    }
    @POST
    @Path("updateOwner")
    public String updateOwner(CsiOwner owner) {
        int result = csiRpcService.updateOwner(owner);
        if (result == 0)
            return "update success!";
        else
            return "update failed";
    }
    @POST
    @Path("insertCategory")
    public String insertCategory(CsiCategory category) {
        return JsonUtils.SUCCESS(csiRpcService.insertCategory(category));
    }
    @POST
    @Path("updateCategory")
    public String updateCategory(CsiCategory category) {
        int result = csiRpcService.updateCategory(category);
        if (result == 0)
            return "update success!";
        else
            return "update failed";
    }

}
