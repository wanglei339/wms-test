package com.lsh.wms.rpc.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.staff.IStaffRestService;
import com.lsh.wms.core.constant.StaffConstant;
import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;
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
        List<BaseinfoStaffDepartment> departmentList = staffRpcService.getDepartmentList(mapQuery);
        return JsonUtils.SUCCESS(departmentList);
    }

    @POST
    @Path("addDepartment")
    public String addDepartment(String sDepartmentName) {
        long iDepartmentId = Long.parseLong(RandomUtils.uuid2());
        BaseinfoStaffDepartment department = new BaseinfoStaffDepartment();
        department.setDepartmentId(iDepartmentId);
        department.setDepartmentName(sDepartmentName);
        department.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
        staffRpcService.addDepartment(department);
        return JsonUtils.SUCCESS(department);
    }

    @POST
    @Path("updateDepartment")
    public String updateDepartment(@QueryParam("departmentId") Long iDepartmentId, @QueryParam("departmentName") String sDepartmentName) {
        BaseinfoStaffDepartment department = staffRpcService.getDepartmentById(iDepartmentId);
        department.setDepartmentName(sDepartmentName);
        try {
            staffRpcService.updateDepartment(department);
        } catch (RuntimeException e){
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        } catch (Exception e) {
            return JsonUtils.BIZ_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS(department);
    }

    @POST
    @Path("delDepartment")
    public String deleteDepartment(@QueryParam("departmentId") long iDepartmentId) {
        BaseinfoStaffDepartment department = staffRpcService.getDepartmentById(iDepartmentId);
        department.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        try {
            staffRpcService.updateDepartment(department);
        } catch (RuntimeException e){
            return JsonUtils.EXCEPTION_ERROR(e.getMessage());
        } catch (Exception e) {
            return JsonUtils.BIZ_ERROR(e.getMessage());
        }
        return JsonUtils.SUCCESS(department);
    }
}