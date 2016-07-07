package com.lsh.wms.web.controller.system;

import com.google.common.collect.Lists;
import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.system.SysRole;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.model.system.SysUserRoleRelation;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.service.system.SysRoleService;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.web.constant.MediaTypes;
import com.lsh.wms.web.constant.WebConstant;
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
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 用户首页
     *
     * @return
     */
    @RequestMapping("")
    public String userIndex() {
        return "system/user/user";
    }

    /**
     * 用户列表
     *
     * @param draw
     * @param start
     * @param limit
     * @param query_userName
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Map<String, Object> userList(
            @RequestParam(value = "draw", required = false) Integer draw,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "length", required = false) Integer limit,
            @RequestParam(value = "query_username", required = false) String query_userName,
            HttpServletResponse response) {

        // 分页显示
        Integer userNum = sysUserService.countSysUser(query_userName);
        List<SysUser> userList = sysUserService.getSysUserList(query_userName, start, limit);
        // 返回结果
        Map<String, Object> result = getSuccessMap();
        result.put("draw", draw); //draw
        result.put("recordsTotal", userNum); //total
        result.put("recordsFiltered", userNum); //totalAfterFilter
        result.put("data", userList.toArray());
        setResContent2Json(response);
        return result;
    }

    /**
     * 用户编辑
     *
     * @param id
     * @return
     */
    @RequestMapping("/page/user/edit")
    public ModelAndView userEdit(@RequestParam(value = "id", required = false) Integer id) {
        SysUser user = null;
        if (id == null) {
            user = new SysUser();
        } else {
            user = sysUserService.getSysUserById(id);
            user.setSysUserRoleRelationList(sysRoleService.getSysUserRoleRelationListByUserId(id));
        }
        List<SysRole> roleList = sysRoleService.getSysRoleList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("system/user/user_edit");
        return modelAndView;
    }

    /**
     * 用户保存
     *
     * @param sysUser
     * @param roles
     * @param response
     * @return
     */
    @RequestMapping("/user/save")
    @ResponseBody
    public Map<String, Object> userSave(SysUser sysUser,
                                        @RequestParam(value = "roles", required = false) String roles,
                                        HttpServletResponse response) {
        Integer[] roleIdAry = ObjUtils.string2IntAry(roles, ",", null);
        if (roleIdAry == null || roleIdAry.length == 0) {
            setResContent2Json(response);
            return getFailMap("请选择至少一个角色！");
        }
        if (StringUtils.isBlank(sysUser.getLoginName())) {
            setResContent2Json(response);
            return getFailMap("登录名不能为空！");
        }
        if (sysUserService.existSysUser(sysUser.getLoginName(), sysUser.getId())) {
            setResContent2Json(response);
            return getFailMap("用户已存在！");
        }
        List<SysUserRoleRelation> userRoleList = Lists.newArrayList();
        for (Integer roleId : roleIdAry) {
            SysUserRoleRelation userRole = new SysUserRoleRelation();
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }
        if (sysUser.getId() == null) {
            // 新增用户
            sysUser.setIsEffective(BusiConstant.EFFECTIVE_YES);
            // 密码加密
            String password = sysUser.getPazzword();
            if (StringUtils.isBlank(password)) {
                password = WebConstant.PAZZWORD;
            }
            sysUser.setPazzword(EncodeUtils.md5(password));
            // 保存用户和角色信息
            sysUserService.insertSysUser(sysUser, userRoleList);
        } else {
            if (StringUtils.isNotBlank(sysUser.getPazzword())) {
                sysUser.setPazzword(EncodeUtils.md5(sysUser.getPazzword()));
            } else {
                sysUser.setPazzword(null);
            }
            sysUserService.updateSysUser(sysUser, userRoleList);
        }
        // 返回结果
        setResContent2Json(response);
        return getSuccessMap();
    }

}
