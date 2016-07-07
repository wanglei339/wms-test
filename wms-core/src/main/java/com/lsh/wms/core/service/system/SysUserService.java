package com.lsh.wms.core.service.system;

import com.google.common.collect.Maps;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.system.SysUserRoleRelation;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.system.SysUserDao;
import com.lsh.wms.core.dao.system.SysUserRoleRelationDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户管理业务类.
 */
@Component
@Transactional(readOnly = true)
public class SysUserService {

    private static Logger logger = LoggerFactory.getLogger(SysUserService.class);

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleRelationDao sysUserRoleRelationDao;

    public SysUser getEffectiveUserByLoginName(String loginName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("loginName", loginName);
        params.put("isEffective", BusiConstant.EFFECTIVE_YES);
        return sysUserDao.getSysUser(params);
    }

    public Integer countSysUser(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return sysUserDao.countSysUser(params);
    }

    public List<SysUser> getSysUserList(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("start", start);
        params.put("limit", limit);
        return sysUserDao.getSysUserList(params);
    }

    public SysUser getSysUserById(Integer id) {
        if (id == null) {
            return null;
        }
        return sysUserDao.getSysUserById(id);
    }

    public boolean existSysUser(String loginName, Integer userId) {
        if (StringUtils.isEmpty(loginName)) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("loginName", loginName.trim());
        List<SysUser> userList = sysUserDao.getSysUserList(params);
        if (userList == null || userList.isEmpty()) {
            return false;
        }
        // 如果新增，有记录则存在
        if (userId == null) {
            return true;
        }
        // 如果是修改，则多于一条记录则存在
        if (userList.size() > 1) {
            return true;
        }
        // 如果是修改，只有一条记录且ID与自己不同，则存在
        if (!userList.get(0).getId().equals(userId)) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public void insertSysUser(SysUser sysUser, List<SysUserRoleRelation> userRoleList) {
        if (sysUser == null) {
            logger.warn("要新增的用户为空！");
            return;
        }
        // 新增用户
        sysUser.setCreatedTime(new Date());
        sysUserDao.insert(sysUser);
        // 新增角色
        if (userRoleList == null || userRoleList.isEmpty()) {
            return;
        }
        for (SysUserRoleRelation userRole : userRoleList) {
            userRole.setUserId(sysUser.getId());
            sysUserRoleRelationDao.insert(userRole);
        }
    }

    @Transactional(readOnly = false)
    public void updateSysUser(SysUser sysUser, List<SysUserRoleRelation> userRoleList) {
        if (sysUser == null || sysUser.getId() == null) {
            logger.warn("要修改的用户为空！");
            return;
        }
        // 修改用户
        sysUser.setUpdatedTime(new Date());
        sysUserDao.update(sysUser);
        if (userRoleList == null || userRoleList.isEmpty()) {
            return;
        }
        // 删除角色
        sysUserRoleRelationDao.deleteByUserId(sysUser.getId());
        // 新增角色
        for (SysUserRoleRelation userRole : userRoleList) {
            userRole.setUserId(sysUser.getId());
            sysUserRoleRelationDao.insert(userRole);
        }
    }

}
