package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.system.IItemTypeRestService;
import com.lsh.wms.model.baseinfo.BaseinfoExceptionCode;
import com.lsh.wms.model.baseinfo.BaseinfoItemType;
import com.lsh.wms.model.baseinfo.BaseinfoItemTypeRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanghongling on 16/11/10.
 */
@Service(protocol = "rest")
@Path("itemType")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ItemTypeRestService implements IItemTypeRestService {

    private static Logger logger = LoggerFactory.getLogger(ItemTypeRestService.class);
    @Autowired
    private ItemTypeRpcService itemTypeRpcService;
    @POST
    @Path("getBaseinfoItemTypeList")
    public String getBaseinfoItemTypeList() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        return JsonUtils.SUCCESS(itemTypeRpcService.getBaseinfoItemTypeList(mapQuery));
    }
    @POST
    @Path("getItemTypeList")
    public String getItemTypeList(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(itemTypeRpcService.getItemTypeList(mapQuery));
    }
    @POST
    @Path("getItemTypeListCount")
    public String  getItemTypeListCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(itemTypeRpcService.countItemTypeList(mapQuery));
    }
    @POST
    @Path("insertItemType")
    public String insertItemType(BaseinfoItemType baseinfoItemType) {
        try {
            itemTypeRpcService.insertItemType(baseinfoItemType);
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("insert failed");
        }

        return JsonUtils.SUCCESS();
    }
    @GET
    @Path("getItemTypeRelationList")
    public String getItemTypeRelationList(@QueryParam("itemTypeId") String itemTypeId) {

        return JsonUtils.SUCCESS(itemTypeRpcService.getItemTypeRelationListByItemTypeId(itemTypeId));
    }
    @GET
    @Path("deleteItemTypeRelation")
    public String deleteItemTypeRelation(@QueryParam("id") Long id) {
        itemTypeRpcService.deleteItemTypeRelation(id);
        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("insertItemTypeRelation")
    public String insertItemTypeRelation(BaseinfoItemTypeRelation baseinfoItemTypeRelation) {
        try {
            itemTypeRpcService.insertItemTypeRelation(baseinfoItemTypeRelation);
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("insert failed");
        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateItemType")
    public String update(BaseinfoItemType baseinfoItemType) {
        try {
            itemTypeRpcService.updateItemType(baseinfoItemType);
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonUtils.EXCEPTION_ERROR("update failed");
        }

        return JsonUtils.SUCCESS();
    }

}