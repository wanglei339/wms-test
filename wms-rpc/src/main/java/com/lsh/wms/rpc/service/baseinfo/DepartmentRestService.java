package com.lsh.wms.rpc.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.baseinfo.IDepartmentRestService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lixin-mac on 16/7/12.
 */
@Path("department")
@Service(protocol = "rest")

@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class DepartmentRestService implements IDepartmentRestService {

    private static Logger logger = LoggerFactory.getLogger(DepartmentRestService.class);

    @Autowired
    private DepartmentRpcService departmentRpcService;
    @GET
    @Path("getDepartment")
    public String getDepartment(@QueryParam("departmentId") long departmentId) {

        return JsonUtils.SUCCESS(departmentRpcService.getDepartment(departmentId));
    }
    @POST
    @Path("insertDepartment")
    public String insertDepartment(BaseinfoDepartment department) {
        try{
            departmentRpcService.insertDepartment(department);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            return JsonUtils.EXCEPTION_ERROR("failed");
        }
        return JsonUtils.SUCCESS();
    }
    @POST
    @Path("updateDepartment")
    public String updateDepartment(BaseinfoDepartment department) {
        //查询该记录是否存在
        if(departmentRpcService.getDepartment(department.getDepartmentId()) == null){
            return JsonUtils.EXCEPTION_ERROR("The record does not exist");
        }
        try{
            departmentRpcService.updateDepartment(department);
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            JsonUtils.EXCEPTION_ERROR("update failed");
        }
        return JsonUtils.SUCCESS();
    }
}
