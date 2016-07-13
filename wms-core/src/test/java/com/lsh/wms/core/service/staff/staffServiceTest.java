package com.lsh.wms.core.service.staff;

import com.lsh.wms.core.common.BaseSpringTest;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.model.baseinfo.BaseinfoDepartment;
import com.lsh.wms.model.system.SysUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/9.
 */

public class staffServiceTest extends BaseSpringTest {

   /* @Autowired
    private StaffService service;*/

    @Test
    public void test() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", 1);
       /* service.countBaseinfoDepartment(params);
        Integer ret = service.countBaseinfoDepartment(params);
        System.out.println(ret);*/
    }
}
