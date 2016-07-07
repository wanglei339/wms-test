package com.lsh.wms.core.service.system;

import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.core.common.BaseSpringTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangdong on 16/6/23.
 */
public class SysUserServiceTest extends BaseSpringTest {

    @Autowired
    private SysUserService service;

    @Test
    public void test() throws Exception {
        SysUser user = service.getEffectiveUserByLoginName("admin");
        System.out.println(user.getLoginName());
        Assert.assertTrue("admin".equals(user.getLoginName()));
    }
}
