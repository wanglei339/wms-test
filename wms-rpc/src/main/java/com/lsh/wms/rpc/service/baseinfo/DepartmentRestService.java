package com.lsh.wms.rpc.service.baseinfo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.baseinfo.IDepartmentRestService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
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
        return JsonUtils.SUCCESS(departmentRpcService.insertDepartment(department));
    }
    @POST
    @Path("updateDepartment")
    public String updateDepartment(BaseinfoDepartment department) {
        int result = departmentRpcService.updateDepartment(department);
        if (result == 0)
            return "更新成功!!";
        else return "更新失败!!!";
    }
}
