package com.lsh.wms.web.controller.system;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.system.SysRole;
import com.lsh.wms.model.system.SysRoleFunctionRelation;
import com.lsh.wms.core.service.system.SysRoleService;
import com.lsh.wms.web.constant.MediaTypes;
import com.lsh.wms.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 角色首页
     *
     * @return
     */
    @RequestMapping("")
    public String roleIndex() {
        return "system/role/role";
    }

    /**
     * 角色列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param queryName
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> roleList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "query_rolename", required = false) String queryName,
            HttpServletResponse response) {

        // 分页显示
        Integer roleNum = sysRoleService.countSysRole(queryName);
        List<SysRole> roleList = sysRoleService.getSysRoleListByParams(queryName, start, limit);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", roleNum); //total
        result.put("recordsFiltered", roleNum); //totalAfterFilter
        result.put("data", roleList.toArray());
        setResContent2Json(response);
        return result;
    }

    /**
     * 角色编辑页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/page/role/edit")
    @ResponseBody
    public ModelAndView roleNew(@RequestParam(value = "id", required = false) Integer id) {

        SysRole role = null;
        if (id == null) {
            role = new SysRole();
        } else {
            role = sysRoleService.getSysRoleById(id);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("role", role);
        modelAndView.setViewName("system/role/role_edit");
        return modelAndView;
    }

    /**
     * 角色保存
     *
     * @param role
     * @param response
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> roleSave(SysRole role, HttpServletResponse response) {

        if (StringUtils.isBlank(role.getRoleName())) {
            setResContent2Json(response);
            return getFailMap("角色名称不能为空！");
        }
        if (sysRoleService.existRole(role.getRoleName(), role.getId())) {
            setResContent2Json(response);
            return getFailMap("该角色名称已存在！");
        }
        if (role.getId() == null) {
            // 新增角色
            role.setCreatedTime(new Date());
            role.setUpdatedTime(new Date());
            sysRoleService.insertSysRole(role);
        } else {
            role.setUpdatedTime(new Date());
            sysRoleService.updateSysRole(role);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

    /**
     * 获取资源树
     *
     * @param id
     * @param response
     * @return
     */
    @RequestMapping(value = "/auth/tree", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> getTree(@RequestParam(value = "id", required = false) Integer id,
                                       HttpServletResponse response) {

        Map<String, Object> treeMap = sysRoleService.getFuncTreeMap(id);
        setResContent2Json(response);
        return treeMap;
    }

    /**
     * 授权保存
     *
     * @param funcIds
     * @param roleId
     * @param response
     * @return
     */
    @RequestMapping(value = "/auth/save", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> roleAuthorizeSave(
            @RequestParam(value = "funcIds", required = false) String funcIds,
            @RequestParam(value = "roleId", required = false) Integer roleId,
            HttpServletResponse response) {

        List<SysRoleFunctionRelation> sysRoleFuncRelationList = sysRoleService.getSysRoleFunRelationList(roleId);
        for (SysRoleFunctionRelation sysRoleFunctionRelation : sysRoleFuncRelationList) {
            sysRoleService.deleteRoleFunRelation(sysRoleFunctionRelation.getId());
        }
        Integer[] funcIdAry = ObjUtils.string2IntAry(funcIds, ",", null);
        for (Integer funcId : funcIdAry) {
            sysRoleService.insertSysRoleFuncRelation(roleId, funcId);
        }
        setResContent2Json(response);
        return getSuccessMap();
    }

}
