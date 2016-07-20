package com.lsh.wms.rpc.service.staff;


import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.staff.IStaffRpcService;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lsh.wms.core.service.staff.StaffService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

@Service(protocol = "dubbo")
public class StaffRpcService implements IStaffRpcService {

    private static final Logger logger = LoggerFactory.getLogger(StaffRpcService.class);

    @Autowired
    private StaffService staffService;

    public List<BaseinfoStaffDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        return staffService.getDepartmentList(mapQuery);
    }

    public void addDepartment(BaseinfoStaffDepartment department) {
        staffService.addDepartment(department);
    }

    public void updateDepartment(BaseinfoStaffDepartment department) {
        staffService.updateDepartment(department);
    }

    public BaseinfoStaffDepartment getDepartmentById(Long iDepartmentId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("departmentId", iDepartmentId);
        List<BaseinfoStaffDepartment> dList = getDepartmentList(params);
        BaseinfoStaffDepartment department = null;
        if (!dList.isEmpty()) {
            department = dList.get(0);
        }
        return department;
    }

    public List<BaseinfoStaffGroup> getGroupList(Map<String, Object> mapQuery) {
        return staffService.getGroupList(mapQuery);
    }

    public void addGroup(BaseinfoStaffGroup group) {
        staffService.addGroup(group);
    }

    public void updateGroup(BaseinfoStaffGroup group) {
        staffService.updateGroup(group);
    }

    public BaseinfoStaffGroup getGroupById(Long iGroupId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", iGroupId);
        List<BaseinfoStaffGroup> dList = getGroupList(params);
        BaseinfoStaffGroup group = null;
        if (!dList.isEmpty()) {
            group = dList.get(0);
        }
        return group;
    }

    public List<BaseinfoStaffLevel> getLevelList(Map<String, Object> mapQuery) {
        return staffService.getLevelList(mapQuery);
    }

    public void addLevel(BaseinfoStaffLevel level) {
        staffService.addLevel(level);
    }

    public void updateLevel(BaseinfoStaffLevel level) {
        staffService.updateLevel(level);
    }

    public BaseinfoStaffLevel getLevelById(Long iLevelId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("levelId", iLevelId);
        List<BaseinfoStaffLevel> dList = getLevelList(params);
        BaseinfoStaffLevel level = null;
        if (!dList.isEmpty()) {
            level = dList.get(0);
        }
        return level;
    }

    public List<BaseinfoStaffJob> getJobList(Map<String, Object> mapQuery) {
        return staffService.getJobList(mapQuery);
    }

    public void addJob(BaseinfoStaffJob job) {
        staffService.addJob(job);
    }

    public void updateJob(BaseinfoStaffJob job) {
        staffService.updateJob(job);
    }

    public BaseinfoStaffJob getJobById(Long iJobId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobId", iJobId);
        List<BaseinfoStaffJob> dList = getJobList(params);
        BaseinfoStaffJob job = null;
        if (!dList.isEmpty()) {
            job = dList.get(0);
        }
        return job;
    }

    public List<BaseinfoStaffInfo> getStaffList(Map<String, Object> mapQuery) {
        return staffService.getStaffList(mapQuery);
    }

    public void addStaff(BaseinfoStaffInfo staffInfo) {
        staffService.addStaff(staffInfo);
    }

    public void assignJobToStaff(Long iStaffId, ArrayList<Object> jobIds) {
        staffService.assignJobToStaff(iStaffId, jobIds);
    }

    public void updateStaff(BaseinfoStaffInfo staffInfo) {
        staffService.updateStaff(staffInfo);
    }

    public BaseinfoStaffInfo getStaffById(Long staffId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staffId", staffId);
        List<BaseinfoStaffInfo> dList = getStaffList(params);
        BaseinfoStaffInfo staffInfo = null;
        if (!dList.isEmpty()) {
            staffInfo = dList.get(0);
        }
        return staffInfo;
    }

    public BaseinfoStaffInfo createStaff(Map<String, Object> params) {
        return staffService.createStaff(params);
    }

    public BaseinfoStaffInfo saveStaff(Map<String, Object> params) {
        return staffService.saveStaff(params);
    }
}
