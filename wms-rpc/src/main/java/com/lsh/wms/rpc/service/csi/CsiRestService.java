package com.lsh.wms.rpc.service.csi;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.remoting.ExecutionException;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.csi.ICsiRestService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.csi.CsiCustomerService;
import com.lsh.wms.core.service.csi.CsiOwnerService;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.model.csi.*;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

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
    @Autowired
    private CsiSupplierService supplierService;
    @Autowired
    private CsiCustomerService customerService;
    @Autowired
    private LocationService locationService;

    @GET
    @Path("getCatInfo")
    public String getCatInfo(@QueryParam("catId") long iCatId)throws BizCheckedException {
        return JsonUtils.SUCCESS(csiRpcService.getCatInfo(iCatId));
    }

    @GET
    @Path("getCatFull")
    public String getCatFull(@QueryParam("catId") long iCatId) throws BizCheckedException{
        return JsonUtils.SUCCESS(csiRpcService.getCatFull(iCatId));
    }

    @GET
    @Path("getCatChilds")
    public String getCatChilds(@QueryParam("catId") long iCatId)throws BizCheckedException {
        return JsonUtils.SUCCESS(csiRpcService.getCatChilds(iCatId));
    }

    @POST
    @Path("insertCategory")
    public String insertCategory(CsiCategory category) throws BizCheckedException{
        try{
            csiRpcService.insertCategory(category);
        }catch(Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }

        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("updateCategory")
    public String updateCategory(CsiCategory category)throws BizCheckedException {
        try{
            csiRpcService.updateCategory(category);
        }catch(Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getSku")
    public String getSku(@QueryParam("skuId") long iSkuId) throws BizCheckedException{
        return JsonUtils.SUCCESS(csiRpcService.getSku(iSkuId));
    }


    @GET
    @Path("getSkuByCode")
    public String getSkuByCode(@QueryParam("codeType") int iCodeType, @QueryParam("code") String sCode) throws BizCheckedException{
        return JsonUtils.SUCCESS(csiRpcService.getSkuByCode(iCodeType, sCode));
    }
    @POST
    @Path("insertSku")
    public String insertSku(CsiSku sku) throws BizCheckedException {
        CsiSku newSku = csiRpcService.getSkuByCode(Integer.parseInt(sku.getCodeType()),sku.getCode());
        if(newSku != null){
            throw new BizCheckedException("2050004");
        }
        try{
             csiRpcService.insertSku(sku);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }



    @POST
    @Path("updateSku")
    public String updateSku(CsiSku sku)throws BizCheckedException{
        try{
            csiRpcService.updateSku(sku);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getOwner")
    public String getOwner(@QueryParam("ownerId") long iOwnerId) throws BizCheckedException{
        return JsonUtils.SUCCESS(csiRpcService.getOwner(iOwnerId));
    }


    @POST
    @Path("getOwnerList")
    public String getOwnerList(Map<String, Object> mapQuery)throws BizCheckedException {
        return JsonUtils.SUCCESS(ownerService.getOwnerList(mapQuery));
    }
    @POST
    @Path("getOwnerCount")
    public String getOwnerCount(Map<String, Object> mapQuery) throws BizCheckedException{
        return JsonUtils.SUCCESS(ownerService.getOwnerCount(mapQuery));
    }

    @POST
    @Path("insertOwner")
    public String insertOwner(CsiOwner owner)throws BizCheckedException {
        try{
            csiRpcService.insertOwner(owner);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("updateOwner")
    public String updateOwner(CsiOwner owner)throws BizCheckedException {
        try{
            csiRpcService.updateOwner(owner);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getSupplier")
    public String getSupplier(@QueryParam("supplierId") long iSupplierId) throws BizCheckedException{
        return JsonUtils.SUCCESS(csiRpcService.getSupplier(iSupplierId));
    }


    @POST
    @Path("insertSupplier")
    public String insertSupplier(CsiSupplier supplier)throws BizCheckedException{
        try{
            csiRpcService.insertSupplier(supplier);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("insertSuppliers")
    public String insertSuppliers(CsiSupplier supplier)throws BizCheckedException{

            CsiSupplier oldSupplier = csiRpcService.getSupplier(supplier.getSupplierCode(), supplier.getOwnerId());
            if(oldSupplier ==null) {
                csiRpcService.insertSupplier(supplier);
            }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateSupplier")
    public String updateSupplier(CsiSupplier supplier)throws BizCheckedException{
        try{
            csiRpcService.updateSupplier(supplier);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("getSupplierList")
    public String getSupplierList(Map<String, Object> mapQuery)throws BizCheckedException{
        return JsonUtils.SUCCESS(supplierService.getSupplerList(mapQuery));
    }

    @POST
    @Path("getSupplierCount")
    public String getSupplierCount(Map<String, Object> mapQuery)throws BizCheckedException{
        return JsonUtils.SUCCESS(supplierService.getSupplerCount(mapQuery));
    }

    @POST
    @Path("getCustomerList")
    public String getCustomerList(Map<String, Object> mapQuery)throws BizCheckedException{
        return JsonUtils.SUCCESS(customerService.getCustomerList(mapQuery));
    }

    @POST
    @Path("getCustomerCount")
    public String getCustomerCount(Map<String, Object> mapQuery)throws BizCheckedException{
        return JsonUtils.SUCCESS(customerService.getCustomerCount(mapQuery));
    }

    @POST
    @Path("getCustomer")
    public String getCustomerByCustomerCode(Map<String, Object> mapQuery)throws BizCheckedException{
        //Long ownerId = Long.valueOf(mapQuery.get("ownerId").toString());
        String customerCode = mapQuery.get("customerCode").toString();
        return JsonUtils.SUCCESS(customerService.getCustomerByCustomerCode(customerCode));
    }

    @POST
    @Path("updateCustomer")
    public String updateCustomer(CsiCustomer csiCustomer)throws BizCheckedException {
        if (!csiCustomer.getCollectRoadId().equals(0l)
                && (locationService.getLocation(csiCustomer.getCollectRoadId()) == null
                    || (locationService.getLocation(csiCustomer.getCollectRoadId()).getType() != LocationConstant.COLLECTION_ROAD
                        && locationService.getLocation(csiCustomer.getCollectRoadId()).getType() != LocationConstant.COLLECTION_BIN
                        )
                    )
                ) {
            throw new BizCheckedException("2180011");
        }
        if (!csiCustomer.getSeedRoadId().equals(0l)
                && (locationService.getLocation(csiCustomer.getSeedRoadId()) == null
                || (locationService.getLocation(csiCustomer.getSeedRoadId()).getRegionType() != LocationConstant.SOW_AREA
                  ))){
            //不是播种区下的位置
            throw new BizCheckedException("2180026");
        }
        try {
            if(!csiCustomer.getCollectRoadId().equals(0l)) {
               CsiCustomer customer = customerService.getCustomerByCollectRoadId(csiCustomer.getCollectRoadId());
                if(customer!=null){
                    return JsonUtils.TOKEN_ERROR("该集货位(道)已被占用");
                }
            }
            if(!csiCustomer.getSeedRoadId().equals(0l)) {
                CsiCustomer customer = customerService.getCustomerByseedRoadId(csiCustomer.getSeedRoadId());
                if(customer!=null){
                    return JsonUtils.TOKEN_ERROR("该播种位已被占用");
                }
            }
            customerService.update(csiCustomer);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getCustomerById")
    public String getCustomerByCustomerId(@QueryParam("customerId")Long customerId) throws BizCheckedException{
        return JsonUtils.SUCCESS(customerService.getCustomerByCustomerId(customerId));
    }
    @POST
    @Path("insertCustomer")
    public String insertCustomer(CsiCustomer csiCustomer) throws BizCheckedException{
        customerService.insertCustomer(csiCustomer);
        return JsonUtils.SUCCESS();
    }

}
