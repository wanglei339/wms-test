package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.system.ISysUserRestService;
import com.lsh.wms.model.system.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;;
import java.util.Map;

/**
 * Created by wulin on 16/7/30.
 */

@Service(protocol = "rest")
@Path("sys")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SysUserRestService implements ISysUserRestService {

    private static Logger logger = LoggerFactory.getLogger(SysUserRestService.class);

    @Autowired
    private SysUserRpcService sysUserRpcService;

    @POST
    @Path("getSysUserList")
    public String getSysUserList(Map<String, Object> params) {
        List<SysUser> userList = sysUserRpcService.getSysUserList(params);
        return JsonUtils.SUCCESS(userList);
    }

    public String getSysUserListCount(Map<String, Object> params) {
        return JsonUtils.SUCCESS(sysUserRpcService.getSysUserListCount(params));
    }

    public String addSysUser(SysUser sysUser) {
        sysUserRpcService.addSysUser(sysUser);
        return JsonUtils.SUCCESS(sysUser);
    }

    public String updateSysUser(SysUser sysUser) {
        sysUserRpcService.updateSysUser(sysUser);
        return JsonUtils.SUCCESS(sysUser);
    }

    public String checkLogin(String username, String password) {
        return JsonUtils.SUCCESS(sysUserRpcService.checkLogin(username, password));
    }
}
