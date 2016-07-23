package com.lsh.wms.rpc.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.staff.IStaffRestService;
import com.lsh.wms.core.constant.StaffConstant;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.InterceptorContext;
import java.util.ArrayList;
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
public class StaffRestService implements IStaffRestService {
    private static Logger logger = LoggerFactory.getLogger(StaffRestService.class);

    @Autowired
    private StaffRpcService staffRpcService;

    @POST
    @Path("getDepartmentList")
    public String getDepartmentList(Map<String, Object> params) {
        List<BaseinfoStaffDepartment> departmentList = staffRpcService.getDepartmentList(params);
        return JsonUtils.SUCCESS(departmentList);
    }

    @POST
    @Path("getDepartmentListCount")
    public String getDepartmentListCount(Map<String, Object> params) {
        return JsonUtils.SUCCESS(staffRpcService.getDepartmentListCount(params));
    }

    @POST
    @Path("addDepartment")
    public String addDepartment(Map<String, Object> params) {
        String sDepartmentName = (String) params.get("departmentName");
        Long iDepartmentId = RandomUtils.genId();
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
        Long iDepartmentId = (Long)params.get("departmentId");
        String sDepartmentName = (String) params.get("departmentName");
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
        Long iDepartmentId = (Long)params.get("departmentId");
        BaseinfoStaffDepartment department = staffRpcService.getDepartmentById(iDepartmentId);
        department.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        staffRpcService.updateDepartment(department);
        return JsonUtils.SUCCESS(department);
    }

    @POST
    @Path("getGroupList")
    public String getGroupList(Map<String, Object> params) {
        List<BaseinfoStaffGroup> groupList = staffRpcService.getGroupList(params);
        return JsonUtils.SUCCESS(groupList);
    }

    @POST
    @Path("getGroupListCount")
    public String getGroupListCount(Map<String, Object> params) {
        return JsonUtils.SUCCESS(staffRpcService.getGroupListCount(params));
    }

    @POST
    @Path("addGroup")
    public String addGroup(Map<String, Object> params) throws BizCheckedException {
        String sGroupName = (String) params.get("groupName");
        Long iDepartmentId = (Long)params.get("departmentId");
        BaseinfoStaffDepartment department = staffRpcService.getDepartmentById(iDepartmentId);
        if (department == null) {
            throw new BizCheckedException("部门不存在");
        }
        Long iGroupId = RandomUtils.genId();
        BaseinfoStaffGroup group = new BaseinfoStaffGroup();
        group.setGroupId(iGroupId);
        group.setDepartmentId(iDepartmentId);
        group.setGroupName(sGroupName);
        group.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
        staffRpcService.addGroup(group);
        return JsonUtils.SUCCESS(group);
    }

    @POST
    @Path("updateGroup")
    public String updateGroup(Map<String, Object> params) throws BizCheckedException {
        Long iGroupId = Long.parseLong((String) params.get("groupId"));
        BaseinfoStaffGroup group = staffRpcService.getGroupById(iGroupId);
        if (group == null) {
            throw new BizCheckedException("组别不存在");
        }
        params.put("groupId", iGroupId);
        group = BeanMapTransUtils.map2Bean(params, BaseinfoStaffGroup.class);
        staffRpcService.updateGroup(group);
        BaseinfoStaffGroup grp = staffRpcService.getGroupById(iGroupId);
        return JsonUtils.SUCCESS(grp);
    }

    @POST
    @Path("delGroup")
    public String deleteGroup(Map<String, Object> params) {
        Long iGroupId = (Long)params.get("groupId");
        BaseinfoStaffGroup group = staffRpcService.getGroupById(iGroupId);
        group.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        staffRpcService.updateGroup(group);
        return JsonUtils.SUCCESS(group);
    }

    @POST
    @Path("getLevelList")
    public String getLevelList(Map<String, Object> params) {
        List<BaseinfoStaffLevel> levelList = staffRpcService.getLevelList(params);
        return JsonUtils.SUCCESS(levelList);
    }

    @POST
    @Path("getLevelListCount")
    public String getLevelListCount(Map<String, Object> params) {
        return JsonUtils.SUCCESS(staffRpcService.getLevelListCount(params));
    }

    @POST
    @Path("addLevel")
    public String addLevel(Map<String, Object> params) {
        String sLevelName = (String) params.get("levelName");
        Long iLevelId = RandomUtils.genId();
        BaseinfoStaffLevel level = new BaseinfoStaffLevel();
        level.setLevelId(iLevelId);
        level.setLevelName(sLevelName);
        level.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
        staffRpcService.addLevel(level);
        return JsonUtils.SUCCESS(level);
    }

    @POST
    @Path("updateLevel")
    public String updateLevel(Map<String, Object> params) throws BizCheckedException {
        Long iLevelId = (Long) params.get("levelId");
        String sLevelName = (String) params.get("levelName");
        BaseinfoStaffLevel level = staffRpcService.getLevelById(iLevelId);
        if (level == null) {
            throw new BizCheckedException("职级不存在");
        }
        level.setLevelName(sLevelName);
        staffRpcService.updateLevel(level);
        return JsonUtils.SUCCESS(level);
    }

    @POST
    @Path("delLevel")
    public String deleteLevel(Map<String, Object> params) {
        Long iLevelId = (Long)params.get("levelId");
        BaseinfoStaffLevel level = staffRpcService.getLevelById(iLevelId);
        level.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        staffRpcService.updateLevel(level);
        return JsonUtils.SUCCESS(level);
    }


    @POST
    @Path("getJobList")
    public String getJobList(Map<String, Object> params) {
        List<BaseinfoStaffJob> jobList = staffRpcService.getJobList(params);
        return JsonUtils.SUCCESS(jobList);
    }

    @POST
    @Path("getJobListCount")
    public String getJobListCount(Map<String, Object> params) {
        return JsonUtils.SUCCESS(staffRpcService.getJobListCount(params));
    }

    @POST
    @Path("addJob")
    public String addJob(Map<String, Object> params) {
        String sJobName = (String) params.get("jobName");
        Long iJobId = RandomUtils.genId();
        BaseinfoStaffJob job = new BaseinfoStaffJob();
        job.setJobId(iJobId);
        job.setJobName(sJobName);
        job.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
        staffRpcService.addJob(job);
        return JsonUtils.SUCCESS(job);
    }

    @POST
    @Path("updateJob")
    public String updateJob(Map<String, Object> params) throws BizCheckedException {
        Long iJobId = (Long) params.get("jobId");
        String sJobName = (String) params.get("jobName");
        BaseinfoStaffJob job = staffRpcService.getJobById(iJobId);
        if (job == null) {
            throw new BizCheckedException("工种不存在");
        }
        job.setJobName(sJobName);
        staffRpcService.updateJob(job);
        return JsonUtils.SUCCESS(job);
    }

    @POST
    @Path("delJob")
    public String deleteJob(Map<String, Object> params) {
        Long iJobId = (Long)params.get("jobId");
        BaseinfoStaffJob job = staffRpcService.getJobById(iJobId);
        job.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        staffRpcService.updateJob(job);
        return JsonUtils.SUCCESS(job);
    }

    @POST
    @Path("getStaffList")
    public String getStaffList(Map<String, Object> params) {
        List<BaseinfoStaffInfo> staffList = staffRpcService.getStaffList(params);
        return JsonUtils.SUCCESS(staffList);
    }

    @POST
    @Path("getStaffListCount")
    public String getStaffListCount(Map<String, Object> params) {
        return JsonUtils.SUCCESS(staffRpcService.getStaffListCount(params));
    }

    @POST
    @Path("addStaff")
    public String addStaff(Map<String, Object> params) {
        BaseinfoStaffInfo staffInfo = staffRpcService.createStaff(params);
        return JsonUtils.SUCCESS(staffInfo);
    }

    @POST
    @Path("updateStaff")
    public String updateStaff(Map<String, Object> params) {
        BaseinfoStaffInfo staffInfo = staffRpcService.saveStaff(params);
        return JsonUtils.SUCCESS(staffInfo);
    }

    @POST
    @Path("delStaff")
    public String deleteStaff(Map<String, Object> params) throws BizCheckedException {
        BaseinfoStaffInfo staffInfo = BeanMapTransUtils.map2Bean(params, BaseinfoStaffInfo.class);
        staffInfo.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
        staffRpcService.updateStaff(staffInfo);
        return JsonUtils.SUCCESS(staffInfo);
    }

}