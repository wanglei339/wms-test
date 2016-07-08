package com.lsh.wms.core.service.system;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lsh.wms.model.system.SysFunction;
import com.lsh.wms.core.dao.system.SysFunctionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Transactional(readOnly = true)
public class SysFunctionService {

    private static Logger logger = LoggerFactory.getLogger(SysFunctionService.class);

    @Autowired
    private SysFunctionDao sysFunctionDao;

    public List<SysFunction> getUserSysFuncList(Integer userId, Integer parentFuncId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        params.put("parentFuncId", parentFuncId);
        return sysFunctionDao.getUserSysFunctionList(params);
    }

    public Set<Integer> getAllParentSysFuncIdSet(Integer funcId) {
        Set<Integer> funcIdSet = Sets.newHashSet();
        SysFunction sysFunction = sysFunctionDao.getSysFunctionById(funcId);
        if (sysFunction == null) {
            return funcIdSet;
        }
        Integer parentFuncId = sysFunction.getParentFuncId();
        while (parentFuncId != null && !funcIdSet.contains(parentFuncId)) {
            SysFunction parentSysFunction = sysFunctionDao.getSysFunctionById(parentFuncId);
            if (parentSysFunction == null) {
                return funcIdSet;
            }
            funcIdSet.add(parentFuncId);
            parentFuncId = parentSysFunction.getParentFuncId();
        }
        return funcIdSet;
    }

    public List<SysFunction> getAllParentSysFuncList(Integer funcId) {
        List<SysFunction> parentFuncList = Lists.newArrayList();
        SysFunction sysFunction = sysFunctionDao.getSysFunctionById(funcId);
        if (sysFunction == null) {
            return parentFuncList;
        }
        Set<Integer> funcIdSet = Sets.newHashSet();
        Integer parentFuncId = sysFunction.getParentFuncId();
        while (parentFuncId != null && !funcIdSet.contains(parentFuncId)) {
            SysFunction parentSysFunction = sysFunctionDao.getSysFunctionById(parentFuncId);
            if (parentSysFunction == null) {
                return parentFuncList;
            }
            funcIdSet.add(parentFuncId);
            parentFuncList.add(parentSysFunction);
            parentFuncId = parentSysFunction.getParentFuncId();
        }
        return parentFuncList;
    }

    public SysFunction getTopSysFunc(Integer funcId) {
        SysFunction sysFunction = sysFunctionDao.getSysFunctionById(funcId);
        if (sysFunction == null) {
            return null;
        }
        Set<Integer> funcIdSet = Sets.newHashSet();
        Integer parentFuncId = sysFunction.getParentFuncId();
        while (parentFuncId != null && !funcIdSet.contains(parentFuncId)) {
            SysFunction parentSysFunction = sysFunctionDao.getSysFunctionById(parentFuncId);
            if (parentSysFunction == null) {
                return null;
            }
            parentFuncId = parentSysFunction.getParentFuncId();
            if (parentFuncId == null || parentFuncId == 0) {
                return parentSysFunction;
            }
        }
        return null;
    }

    /**
     * 获取功能树
     *
     * @return
     */
    public Map<String, Object> getFunctionTreeMap() {
        // 获取所有功能
        Map<String, Object> params = Maps.newHashMap();
        List<SysFunction> funcList = sysFunctionDao.getSysFunctionList(params);
        // 组织成树状结构
        Map<String, Object> treeMap = Maps.newHashMap();
        treeMap.put("id", "0");
        treeMap.put("text", "功能菜单(根节点)");
        Map<String, Object> stateMap = Maps.newHashMap();
        stateMap.put("opened", true);
        treeMap.put("state", stateMap);
        treeMap.put("icon", "fa fa-folder icon-state-success");
        treeMap.put("isLeaf", 0);
        treeMap.put("isEffective", 1);
        treeMap.put("children", getFunctionChildList(funcList, 0));
        return treeMap;
    }

    /**
     * 设置子菜单结合
     *
     * @param funcList
     * @param parentId
     * @return
     */
    private List<Map<String, Object>> getFunctionChildList(List<SysFunction> funcList, Integer parentId) {

        List<Map<String, Object>> childList = Lists.newArrayList();
        for (SysFunction func : funcList) {
            if (func.getParentFuncId().equals(parentId)) {
                Map<String, Object> nodeMap = Maps.newHashMap();
                nodeMap.put("id", func.getId());
                nodeMap.put("text", func.getFuncName());
                String icon = "fa";
                if (func.getIsLeaf() == 1) {
                    icon = icon + " fa-file";
                } else {
                    icon = icon + " fa-folder";
                }
                if (func.getIsEffective() == 1) {
                    icon = icon + " icon-state-success";
                } else {
                    icon = icon + " icon-state-danger";
                }
                nodeMap.put("icon", icon);
                nodeMap.put("isLeaf", func.getIsLeaf());
                nodeMap.put("isEffective", func.getIsEffective());
                nodeMap.put("children", getFunctionChildList(funcList, func.getId()));
                childList.add(nodeMap);
            }
        }
        return childList;
    }

    public SysFunction getSysFunctionById(Integer id) {
        if (id == null) {
            return null;
        }
        return sysFunctionDao.getSysFunctionById(id);
    }

    @Transactional(readOnly = false)
    public void insert(SysFunction func) {
        func.setCreatedTime(new Date());
        func.setUpdatedTime(new Date());
        sysFunctionDao.insert(func);
    }

    @Transactional(readOnly = false)
    public void update(SysFunction func) {
        func.setUpdatedTime(new Date());
        sysFunctionDao.update(func);
    }

    @Transactional(readOnly = false)
    public void move(Integer funcId, Integer parentId, Integer old_parent, Integer position) {
        // 1、更新当前节点的父节点
        SysFunction sysFunction = new SysFunction();
        sysFunction.setId(funcId);
        sysFunction.setParentFuncId(parentId);
        sysFunction.setFuncOrder(position);
        sysFunction.setUpdatedTime(new Date());
        sysFunctionDao.update(sysFunction);
        // 2、对父节点的子节点重新排序
        Map<String, Object> param = Maps.newHashMap();
        param.put("parentFuncId", parentId);
        List<SysFunction> childList = sysFunctionDao.getSysFunctionList(param);
        int i = 1;
        for (SysFunction func : childList) {
            SysFunction upFunc = new SysFunction();
            if (func.getId() == funcId) {
                continue;
            }
            upFunc.setId(func.getId());
            if (i == position) {
                i = i + 1; // 位置预留出来
            }
            upFunc.setFuncOrder(i);
            upFunc.setUpdatedTime(new Date());
            sysFunctionDao.update(upFunc);
            i = i + 1;
        }
        // 3、对原父节点的子节点重新排序，可忽略
    }

}
