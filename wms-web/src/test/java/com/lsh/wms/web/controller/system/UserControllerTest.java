package com.lsh.wms.web.controller.system;

import com.google.common.collect.Maps;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.web.common.BaseMvcTest;
import com.lsh.wms.web.common.MockitoDependencyInjectionTestExecutionListener;
import com.lsh.wms.web.constant.WebConstant;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by huangdong on 16/6/23.
 */
@TestExecutionListeners(MockitoDependencyInjectionTestExecutionListener.class)
public class UserControllerTest extends BaseMvcTest {

    @Mock
    private SysUserService sysUserService;

    @InjectMocks
    @Autowired
    private UserController controller;

    @Test
    public void userList() throws Exception {
        String keyword = "admin";
        Integer start = 0;
        Integer limit = 10;
        Integer records = 1;
        List<SysUser> users = Collections.singletonList(new SysUser());
        Mockito.when(sysUserService.countSysUser(Mockito.eq(keyword))).thenReturn(records);
        Mockito.when(sysUserService.getSysUserList(Mockito.eq(keyword), Mockito.eq(start), Mockito.eq(limit))).thenReturn(users);
        Map<String, Object> result = Maps.newTreeMap();
        result.put("rsCode", "1");
        result.put("rsMsg", "成功！");
        result.put("draw", 1); //draw
        result.put("recordsTotal", records); //total
        result.put("recordsFiltered", records); //totalAfterFilter
        result.put("data", users);
        this.mvc.perform(MockMvcRequestBuilders.get("/system/user/list").servletPath("/system/user/list")
                .param("draw", "1").param("start", start.toString()).param("length", limit.toString()).param("query_username", keyword)
                .sessionAttr(WebConstant.SESSION_KEY_USER, new SysUser()))
                .andExpect(MockMvcResultMatchers.content().string(this.toJson(result)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
