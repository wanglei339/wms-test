package com.lsh.wms.core.service.staff;

import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.StaffConstant;
import com.lsh.wms.core.dao.baseinfo.*;
import com.lsh.wms.model.baseinfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

@Component
@Transactional(readOnly = true)
public class StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    private BaseinfoStaffDepartmentDao departmentDao;

    @Autowired
    private BaseinfoStaffGroupDao groupDao;

    @Autowired
    private BaseinfoStaffLevelDao levelDao;

    @Autowired
    private BaseinfoStaffJobDao jobDao;

    @Autowired
    private BaseinfoStaffInfoDao staffInfoDao;

    @Autowired
    private BaseinfoStaffJobRelationDao staffJobRelationDao;

    public List<BaseinfoStaffDepartment> getDepartmentList(Map<String, Object> mapQuery) {
        return departmentDao.getBaseinfoStaffDepartmentList(mapQuery);
    }

    public Integer countBaseinfoStaffDepartment(Map<String, Object> params) {
        return departmentDao.countBaseinfoStaffDepartment(params);
    }

    @Transactional(readOnly = false)
    public void addDepartment(BaseinfoStaffDepartment department) {
        long now = (System.currentTimeMillis() / 1000);
        department.setCreatedAt(now);
        department.setUpdatedAt(now);
        departmentDao.insert(department);
    }

    @Transactional(readOnly = false)
    public void updateDepartment(BaseinfoStaffDepartment department) {
        long now = (System.currentTimeMillis() / 1000);
        department.setUpdatedAt(now);
        departmentDao.update(department);
    }

    public List<BaseinfoStaffGroup> getGroupList(Map<String, Object> mapQuery) {
        return groupDao.getBaseinfoStaffGroupList(mapQuery);
    }

    public Integer countBaseinfoStaffGroup(Map<String, Object> params) {
        return groupDao.countBaseinfoStaffGroup(params);
    }

    @Transactional(readOnly = false)
    public void addGroup(BaseinfoStaffGroup group) {
        long now = (System.currentTimeMillis() / 1000);
        group.setCreatedAt(now);
        group.setUpdatedAt(now);
        groupDao.insert(group);
    }

    @Transactional(readOnly = false)
    public void updateGroup(BaseinfoStaffGroup group) {
        long now = (System.currentTimeMillis() / 1000);
        group.setUpdatedAt(now);
        groupDao.update(group);
    }

    public List<BaseinfoStaffLevel> getLevelList(Map<String, Object> mapQuery) {
        return levelDao.getBaseinfoStaffLevelList(mapQuery);
    }

    public Integer countBaseinfoStaffLevel(Map<String, Object> params) {
        return levelDao.countBaseinfoStaffLevel(params);
    }

    @Transactional(readOnly = false)
    public void addLevel(BaseinfoStaffLevel level) {
        long now = (System.currentTimeMillis() / 1000);
        level.setCreatedAt(now);
        level.setUpdatedAt(now);
        levelDao.insert(level);
    }

    @Transactional(readOnly = false)
    public void updateLevel(BaseinfoStaffLevel level) {
        long now = (System.currentTimeMillis() / 1000);
        level.setUpdatedAt(now);
        levelDao.update(level);
    }

    public List<BaseinfoStaffJob> getJobList(Map<String, Object> mapQuery) {
        return jobDao.getBaseinfoStaffJobList(mapQuery);
    }

    public Integer countBaseinfoStaffJob(Map<String, Object> params) {
        return jobDao.countBaseinfoStaffJob(params);
    }

    @Transactional(readOnly = false)
    public void addJob(BaseinfoStaffJob job) {
        long now = (System.currentTimeMillis() / 1000);
        job.setCreatedAt(now);
        job.setUpdatedAt(now);
        jobDao.insert(job);
    }

    @Transactional(readOnly = false)
    public void updateJob(BaseinfoStaffJob job) {
        long now = (System.currentTimeMillis() / 1000);
        job.setUpdatedAt(now);
        jobDao.update(job);
    }


    public List<BaseinfoStaffInfo> getStaffList(Map<String, Object> mapQuery) {
        return staffInfoDao.getBaseinfoStaffInfoList(mapQuery);
    }

    public Integer countBaseinfoStaffInfo(Map<String, Object> params) {
        return staffInfoDao.countBaseinfoStaffInfo(params);
    }

    @Transactional(readOnly = false)
    public void addStaff(BaseinfoStaffInfo staffInfo) {
        long now = (System.currentTimeMillis() / 1000);
        staffInfo.setCreatedAt(now);
        staffInfo.setUpdatedAt(now);
        staffInfoDao.insert(staffInfo);
    }

    @Transactional(readOnly = false)
    public void updateStaff(BaseinfoStaffInfo staffInfo) {
        long now = (System.currentTimeMillis() / 1000);
        staffInfo.setUpdatedAt(now);
        staffInfoDao.update(staffInfo);
    }

    @Transactional(readOnly = false)
    public void assignJobToStaff(Long iStaffId, ArrayList<Object> jobIds) {
        long now = (System.currentTimeMillis() / 1000);
        Map<String, Object> dParams = new HashMap<String, Object>();
        dParams.put("staffId", iStaffId);
        List<BaseinfoStaffJobRelation> rList;
        rList = staffJobRelationDao.getBaseinfoStaffJobRelationList(dParams);
        if (rList != null) {
            BaseinfoStaffJobRelation element;
            for (int j = 0; j < rList.size(); j++) {
                element = rList.get(j);
                element.setRecordStatus(StaffConstant.RECORD_STATUS_DELETED);
                element.setUpdatedAt(now);
                staffJobRelationDao.update(element);
            }
        }

        for (int i = 0; i < jobIds.size(); i ++) {
            Long iJobId = ObjUtils.toLong(jobIds.get(i));
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("staffId", iStaffId);
            params.put("jobId", iJobId);
            rList = staffJobRelationDao.getBaseinfoStaffJobRelationList(params);
            if (rList == null || rList.isEmpty()) {
                BaseinfoStaffJobRelation staffJobRelation = new BaseinfoStaffJobRelation();
                staffJobRelation.setStaffId(iStaffId);
                staffJobRelation.setJobId(iJobId);
                staffJobRelation.setCreatedAt(now);
                staffJobRelation.setUpdatedAt(now);
                staffJobRelation.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
                staffJobRelationDao.insert(staffJobRelation);
            } else {
                BaseinfoStaffJobRelation staffJobRelation = rList.get(0);
                staffJobRelation.setUpdatedAt(now);
                staffJobRelation.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
                staffJobRelationDao.update(staffJobRelation);
            }
        }
    }

    @Transactional(readOnly = false)
    public BaseinfoStaffInfo createStaff(Map<String, Object> params) {
        BaseinfoStaffInfo staffInfo = BeanMapTransUtils.map2Bean(params, BaseinfoStaffInfo.class);
        staffInfo.setStaffId(RandomUtils.genId());
        staffInfo.setRecordStatus(StaffConstant.RECORD_STATUS_NORMAL);
        addStaff(staffInfo);
        Long iStaffId = staffInfo.getStaffId();
        if (params.get("jobIds") != null) {
            ArrayList<Object> jobIds = (ArrayList<Object>) params.get("jobIds");
            assignJobToStaff(iStaffId, jobIds);
        }
        return staffInfo;
    }

    @Transactional(readOnly = false)
    public BaseinfoStaffInfo saveStaff(Map<String, Object> params) {
        BaseinfoStaffInfo staffInfo = BeanMapTransUtils.map2Bean(params, BaseinfoStaffInfo.class);
        updateStaff(staffInfo);
        Long iStaffId = staffInfo.getStaffId();
        if (params.get("jobIds") != null) {
            ArrayList<Object> jobIds = (ArrayList<Object>) params.get("jobIds");
            assignJobToStaff(iStaffId, jobIds);
        }
        return staffInfo;
    }

}