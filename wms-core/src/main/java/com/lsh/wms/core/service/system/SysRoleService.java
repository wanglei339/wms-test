package com.lsh.wms.core.service.system;

import com.google.common.collect.Maps;
import com.lsh.wms.model.system.SysFunction;
import com.lsh.wms.model.system.SysRole;
import com.lsh.wms.model.system.SysRoleFunctionRelation;
import com.lsh.wms.model.system.SysUserRoleRelation;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.system.SysFunctionDao;
import com.lsh.wms.core.dao.system.SysRoleDao;
import com.lsh.wms.core.dao.system.SysRoleFunctionRelationDao;
import com.lsh.wms.core.dao.system.SysUserRoleRelationDao;
import org.apache.commons.lang3.StringUtils;
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
 * 角色管理
 */
@Component
@Transactional(readOnly = true)
public class SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleService.class);

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysUserRoleRelationDao sysUserRoleRelationDao;

    @Autowired
    private SysRoleFunctionRelationDao sysRoleFunctionRelationDao;

    @Autowired
    private SysFunctionDao sysFunctionDao;

    public List<SysRole> getSysRoleList() {
        Map<String, Object> params = Maps.newHashMap();
        return sysRoleDao.getSysRoleList(params);
    }

    public Integer countSysRole(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return sysRoleDao.countSysRole(params);
    }

    public List<SysRole> getSysRoleListByParams(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("start", start);
        params.put("limit", limit);
        return sysRoleDao.getSysRoleList(params);
    }

    public SysRole getSysRoleById(Integer id) {
        return sysRoleDao.getSysRoleById(id);
    }

    public boolean existRole(String roleName, Integer roleId) {
        if (StringUtils.isEmpty(roleName)) {
            return true;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("roleName", roleName.trim());
        List<SysRole> roleList = sysRoleDao.getSysRoleList(params);
        if (roleList == null || roleList.isEmpty()) {
            return false;
        }
        // 如果新增，有记录则存在
        if (roleId == null) {
            return true;
        }
        // 如果是修改，则多于一条记录则存在
        if (roleList.size() > 1) {
            return true;
        }
        // 如果是修改，只有一条记录且ID与自己不同，则存在
        if (!roleList.get(0).getId().equals(roleId)) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false)
    public void insertSysRole(SysRole sysRole) {
        if (sysRole == null) {
            logger.warn("新增角色失败，角色不存在。");
            return;
        }
        sysRoleDao.insert(sysRole);
    }

    @Transactional(readOnly = false)
    public void updateSysRole(SysRole role) {
        if (role == null) {
            logger.warn("修改角色失败，角色不存在。");
            return;
        }
        sysRoleDao.update(role);
    }

    public List<SysUserRoleRelation> getSysUserRoleRelationListByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        return sysUserRoleRelationDao.getSysUserRoleRelationList(params);
    }

    public Map<String, Object> getFuncTreeMap(Integer roleId) {

        Map<String, Object> treeMap = new HashMap<String, Object>();
        Integer rootId = 0;
        treeMap.put("id", rootId);
        treeMap.put("text", "资源树");
        treeMap.put("icon", "fa fa-folder icon-state-success");
        if (roleId == null) {
            return treeMap;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        List<SysRoleFunctionRelation> relationList = sysRoleFunctionRelationDao.getSysRoleFunctionRelationList(params);
        treeMap.put("children", getFuncChildTreeList(rootId, relationList));
        return treeMap;
    }

    private List<Map<String, Object>> getFuncChildTreeList(int parentId, List<SysRoleFunctionRelation> relationList) {

        List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentFuncId", parentId);
        params.put("isEffective", BusiConstant.EFFECTIVE_YES);
        List<SysFunction> funcList = sysFunctionDao.getSysFunctionList(params);
        for (SysFunction sysFunction : funcList) {
            Map<String, Object> childMap = new HashMap<String, Object>();
            childMap.put("id", sysFunction.getId());
            childMap.put("text", sysFunction.getFuncName());
            for (SysRoleFunctionRelation relation : relationList) {
                if (relation.getFuncId() == sysFunction.getId()) {
                    Map<String, Object> stateMap = new HashMap<String, Object>();
                    stateMap.put("selected", true);
                    childMap.put("state", stateMap);
                    break;
                }
            }
            String icon = "fa fa-folder icon-state-success";
            if (sysFunction.getIsLeaf() == 1) {
                icon = icon + "fa fa-file icon-state-success";
            }
            childMap.put("icon", icon);
            childMap.put("children", getFuncChildTreeList(sysFunction.getId(), relationList));
            rsList.add(childMap);
        }
        return rsList;
    }

    @Transactional(readOnly = false)
    public void insertSysRoleFuncRelation(Integer roleId, Integer funcId) {
        if (roleId == null || funcId == null) {
            return;
        }
        SysRoleFunctionRelation sysRoleFunctionRelation = new SysRoleFunctionRelation();
        sysRoleFunctionRelation.setRoleId(roleId);
        sysRoleFunctionRelation.setFuncId(funcId);
        sysRoleFunctionRelationDao.insert(sysRoleFunctionRelation);
    }

    /**
     * 根据角色id查询出所有的权限
     *
     * @param roleId
     * @return
     */
    public List<SysRoleFunctionRelation> getSysRoleFunRelationList(Integer roleId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        return sysRoleFunctionRelationDao.getSysRoleFunctionRelationList(params);
    }

    /**
     * 根据id删除权限
     *
     * @param id
     */
    @Transactional(readOnly = false)
    public void deleteRoleFunRelation(Integer id) {
        sysRoleFunctionRelationDao.deleteById(id);
    }

}
