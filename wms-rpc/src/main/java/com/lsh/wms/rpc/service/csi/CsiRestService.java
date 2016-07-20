package com.lsh.wms.rpc.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.remoting.ExecutionException;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.csi.ICsiRestService;
import com.lsh.wms.core.service.csi.CsiOwnerService;
import com.lsh.wms.model.csi.CsiCategory;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.csi.CsiSupplier;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Category;
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
@Path("csi")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class CsiRestService implements ICsiRestService {
    private static Logger logger = LoggerFactory.getLogger(ICsiRestService.class);
    @Autowired
    private CsiRpcService csiRpcService;
    @Autowired
    private CsiOwnerService ownerService;

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
        CsiSku newSku;
        try{
            newSku = csiRpcService.insertSku(sku);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS(newSku);
    }



    @POST
    @Path("updateSku")
    public String updateSku(CsiSku sku){
        if(csiRpcService.getSku(sku.getSkuId()) == null){
            return JsonUtils.EXCEPTION_ERROR("The record does not exist");
        }

        try{
            csiRpcService.updateSku(sku);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insertSupplier")
    public String insertSupplier(CsiSupplier supplier){
        try{
            csiRpcService.insertSupplier(supplier);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateSupplier")
    public String updateSupplier(CsiSupplier supplier){
        try{
            csiRpcService.updateSupplier(supplier);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insertOwner")
    public String insertOwner(CsiOwner owner) {
        try{
            csiRpcService.insertOwner(owner);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("updateOwner")
    public String updateOwner(CsiOwner owner) {
        if(csiRpcService.getOwner(owner.getOwnerId()) == null){
            return JsonUtils.EXCEPTION_ERROR("The record does not exist");
        }
        try{
            csiRpcService.updateOwner(owner);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("insertCategory")
    public String insertCategory(CsiCategory category) {
        try{
            csiRpcService.insertCategory(category);
        }catch(Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }

        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("updateCategory")
    public String updateCategory(CsiCategory category) {
        if(csiRpcService.getCatChilds(category.getCatId()) == null){
            return JsonUtils.EXCEPTION_ERROR("The record does not exist");
        }
        try{
            csiRpcService.updateCategory(category);
        }catch(Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("getOwnerList")
    public String getOwnerList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(ownerService.getOwnerList(mapQuery));
    }
    @POST
    @Path("getOwnerCount")
    public String getOwnerCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(ownerService.getOwnerCount(mapQuery));
    }

}
