package com.lsh.wms.rpc.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.staff.IStaffRestService;
import com.lsh.wms.core.constant.StaffConstant;
import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
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
    public String addDepartment() {
        HttpServletRequest  request = (HttpServletRequest)RpcContext .getContext().getRequest();
        String sDepartmentName = request.getParameter("departmentName");
        long iDepartmentId = RandomUtils.genId();
        BaseinfoStaffDepartment department = new BaseinfoStaffDepartment();
        department.setDepartmentId(iDepartmentId);
        department.setDepartmentName(sDepartmentName);
        department.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
        staffRpcService.addDepartment(department);
        return JsonUtils.SUCCESS(department);
    }

    @POST
    @Path("updateDepartment")
    public String updateDepartment(Map<String, Object> params) throws BizCheckedException {
        Long iDepartmentId = Long.parseLong((String)params.get("departmentId"));
        String sDepartmentName = (String)params.get("departmentName");
        BaseinfoStaffDepartment department = staffRpcService.getDepartmentById(iDepartmentId);
        if (department == null) {
            throw new BizCheckedException("部门不存在");
        }
        department.setDepartmentName(sDepartmentName);
        staffRpcService.updateDepartment(department);
        return JsonUtils.SUCCESS(department);
    }

    @POST
    @Path("delDepartment")
    public String deleteDepartment(Map<String, Object> params) {
        Long iDepartmentId = Long.parseLong((String)params.get("departmentId"));
        BaseinfoStaffDepartment department = staffRpcService.getDepartmentById(iDepartmentId);
        department.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        staffRpcService.updateDepartment(department);
        return JsonUtils.SUCCESS(department);
    }
}