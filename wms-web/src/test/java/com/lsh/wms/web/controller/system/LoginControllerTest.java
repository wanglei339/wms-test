package com.lsh.wms.web.controller.system;

import com.lsh.wms.web.common.BaseMvcTest;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by huangdong on 16/6/23.
 */
public class LoginControllerTest extends BaseMvcTest {

    @Test
    public void login() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/login").servletPath("/login"))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void loginSubmit() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.post("/loginsubmit").servletPath("/loginsubmit")
                .param("username", "admin").param("password", "admin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(MockMvcResultMatchers.status().is(HttpServletResponse.SC_MOVED_TEMPORARILY))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }
}
