package com.lsh.wms.api.service.staff;

import com.lsh.wms.model.baseinfo.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public interface IStaffRpcService {
    public List<BaseinfoStaffDepartment> getDepartmentList(Map<String, Object> mapQuery);

    public Integer getDepartmentListCount(Map<String, Object> mapQuery);

    public void addDepartment(BaseinfoStaffDepartment department);

    public void updateDepartment(BaseinfoStaffDepartment department);

    public BaseinfoStaffDepartment getDepartmentById(Long iDepartmentId);


    public List<BaseinfoStaffGroup> getGroupList(Map<String, Object> mapQuery);

    public Integer getGroupListCount(Map<String, Object> mapQuery);

    public void addGroup(BaseinfoStaffGroup group);

    public void updateGroup(BaseinfoStaffGroup group);

    public BaseinfoStaffGroup getGroupById(Long iGroupId);


    public List<BaseinfoStaffLevel> getLevelList(Map<String, Object> mapQuery);

    public Integer getLevelListCount(Map<String, Object> mapQuery);

    public void addLevel(BaseinfoStaffLevel level);

    public void updateLevel(BaseinfoStaffLevel level);

    public BaseinfoStaffLevel getLevelById(Long iLevelId);


    public List<BaseinfoStaffJob> getJobList(Map<String, Object> mapQuery);

    public Integer getJobListCount(Map<String, Object> mapQuery);

    public void addJob(BaseinfoStaffJob job);

    public void updateJob(BaseinfoStaffJob job);

    public BaseinfoStaffJob getJobById(Long iJobId);


    public List<BaseinfoStaffInfo> getStaffList(Map<String, Object> mapQuery);

    public Integer getStaffListCount(Map<String, Object> mapQuery);

    public void addStaff(BaseinfoStaffInfo staffInfo);

    public void updateStaff(BaseinfoStaffInfo staffInfo);

    public BaseinfoStaffInfo getStaffById(Long staffId);


    public BaseinfoStaffInfo createStaff(Map<String, Object> params);

    public BaseinfoStaffInfo saveStaff(Map<String, Object> params);

}
