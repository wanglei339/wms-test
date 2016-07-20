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
    public String getDepartmentList();

    public String addDepartment(Map<String, Object> params);

    public String updateDepartment(Map<String, Object> params) throws BizCheckedException;

    public String deleteDepartment(Map<String, Object> params);


    public String getGroupList();

    public String addGroup(Map<String, Object> params) throws BizCheckedException;

    public String updateGroup(Map<String, Object> params) throws BizCheckedException;

    public String deleteGroup(Map<String, Object> params) throws BizCheckedException;


    public String getLevelList();

    public String addLevel(Map<String, Object> params);

    public String updateLevel(Map<String, Object> params) throws BizCheckedException;

    public String deleteLevel(Map<String, Object> params) throws BizCheckedException;


    public String getJobList();

    public String addJob(Map<String, Object> params);

    public String updateJob(Map<String, Object> params) throws BizCheckedException;

    public String deleteJob(Map<String, Object> params) throws BizCheckedException;


    public String getStaffList();

    public String addStaff(Map<String, Object> params);

    public String updateStaff(Map<String, Object> params) throws BizCheckedException;

    public String deleteStaff(Map<String, Object> params) throws BizCheckedException;

}
