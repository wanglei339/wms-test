package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.system.IRolePermissionRestService;
import com.lsh.wms.model.system.RolePermission;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/17.
 */

@Service(protocol = "rest")
@Path("sys")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class RolePermissionRestService implements IRolePermissionRestService {
    private static Logger logger = LoggerFactory.getLogger(RolePermissionRestService.class);


    @Autowired
    private RolePermissionRpcService rolePermissionRpcService;


    @POST
    @Path("getRolePermissionList")
    public String getRolePermissionList(Map<String, Object> mapQuery) {

        return JsonUtils.SUCCESS(rolePermissionRpcService.getRolePermissionList(mapQuery));
    }

    @POST
    @Path("getRolePermissionCount")
    public String getRolePermissionCount(Map<String, Object> mapQuery) {
        return JsonUtils.SUCCESS(rolePermissionRpcService.getRolePermissionCount(mapQuery));
    }

    @POST
    @Path("insertRolePermission")
    public String insert(RolePermission rolePermission) {
        try {
            rolePermissionRpcService.insert(rolePermission);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            JsonUtils.EXCEPTION_ERROR("insert failed");
        }

        return JsonUtils.SUCCESS();
    }

    @POST
    @Path("updateRolePermission")
    public String update(RolePermission rolePermission) {
        try {
            rolePermissionRpcService.update(rolePermission);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            JsonUtils.EXCEPTION_ERROR("update failed");
        }

        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getRolePermissionByName")
    public String getRolePermissionByName(@QueryParam("role") String role) {
        return JsonUtils.SUCCESS(rolePermissionRpcService.getRolePermissionByName(role));
    }
    @GET
    @Path("getRolePermissionById")
    public String getRolePermissionById(@QueryParam("id") Long id) {
        return JsonUtils.SUCCESS(rolePermissionRpcService.getRolePermissionById(id));
    }


}
