package com.lsh.wms.rpc.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.google.common.collect.Maps;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.staff.IStaffRestService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */


@Service(protocol = "rest")
@Path("staff")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class StaffRestService implements IStaffRestService{
    private static Logger logger = LoggerFactory.getLogger(StaffRestService.class);

    @Autowired
    private StaffRpcService staffRpcService;

    @GET
    @Path("getDepartmentList")
    public String getDepartmentList() {
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        List<BaseinfoDepartment> departmentList = this.staffRpcService.getDepartmentList(mapQuery);
        return JsonUtils.SUCCESS(departmentList);
    }

    @POST
    @Path("addDepartment")
    public String addDepartment(String sDepartmentName) {
        long now = System.currentTimeMillis();
        long iDepartmentId = Long.parseLong(RandomUtils.uuid2());
        BaseinfoDepartment department = new BaseinfoDepartment();
        department.setDepartmentId(iDepartmentId);
        department.setDepartmentName(sDepartmentName);
        department.setStatus(1);
        department.setCreatedAt(now);
        department.setUpdatedAt(now);
        staffRpcService.addDepartment(department);
        return JsonUtils.SUCCESS(department);
    }

    public String updateDepartment(String sDepartmentName) {
        return null;
    }

    public String deleteDepartment(long iDepartmentId) {
        return null;
    }
}