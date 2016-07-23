package com.lsh.wms.api.service.staff;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public interface IStaffRestService {
    public String getDepartmentList(Map<String, Object> params);

    public String getDepartmentListCount(Map<String, Object> params);

    public String addDepartment(Map<String, Object> params);

    public String updateDepartment(Map<String, Object> params) throws BizCheckedException;

    public String deleteDepartment(Map<String, Object> params);


    public String getGroupList(Map<String, Object> params);

    public String getGroupListCount(Map<String, Object> params);

    public String addGroup(Map<String, Object> params) throws BizCheckedException;

    public String updateGroup(Map<String, Object> params) throws BizCheckedException;

    public String deleteGroup(Map<String, Object> params) throws BizCheckedException;


    public String getLevelList(Map<String, Object> params);

    public String getLevelListCount(Map<String, Object> params);

    public String addLevel(Map<String, Object> params);

    public String updateLevel(Map<String, Object> params) throws BizCheckedException;

    public String deleteLevel(Map<String, Object> params) throws BizCheckedException;


    public String getJobList(Map<String, Object> params);

    public String getJobListCount(Map<String, Object> params);

    public String addJob(Map<String, Object> params);

    public String updateJob(Map<String, Object> params) throws BizCheckedException;

    public String deleteJob(Map<String, Object> params) throws BizCheckedException;


    public String getStaffList(Map<String, Object> params);

    public String getStaffListCount(Map<String, Object> params);

    public String addStaff(Map<String, Object> params);

    public String updateStaff(Map<String, Object> params) throws BizCheckedException;

    public String deleteStaff(Map<String, Object> params) throws BizCheckedException;

}
