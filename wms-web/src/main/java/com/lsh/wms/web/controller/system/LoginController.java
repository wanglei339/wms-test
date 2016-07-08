package com.lsh.wms.web.controller.system;

import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.web.constant.WebConstant;
import com.lsh.wms.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 登录控制页面
 */
@Controller
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        logger.info("skip to login page...");
        return "login";
    }

    @RequestMapping(value = "/loginsubmit", method = RequestMethod.POST)
    public String loginSubmit(@RequestParam("username") String loginName,
                              @RequestParam("password") String password,
                              HttpSession httpSession,
                              RedirectAttributes redirectAttributes) {
        logger.info("submit login...");
        try {
            if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)) {
                redirectAttributes.addFlashAttribute("message", "用户名或密码不能为空。");
                return "redirect:/login";
            }
            redirectAttributes.addFlashAttribute("username", loginName);
            SysUser sysUser = sysUserService.getEffectiveUserByLoginName(loginName);
            if (sysUser == null) {
                redirectAttributes.addFlashAttribute("message", "该用户不存在，请联系管理员。");
                return "redirect:/login";
            }
            if (!EncodeUtils.md5(password).equalsIgnoreCase(sysUser.getPazzword())) {
                redirectAttributes.addFlashAttribute("message", "用户名密码错误。");
                return "redirect:/login";
            }
            httpSession.setAttribute(WebConstant.SESSION_KEY_USER, sysUser);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("登录异常，loginName:" + loginName, e);
            redirectAttributes.addFlashAttribute("message", "登录异常。");
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession httpSession) {
        try {
            if (httpSession != null) {
                httpSession.invalidate();
            }
        } catch (Exception e) {
            logger.error("注销异常。", e);
        }
        return "redirect:/login";
    }

}
