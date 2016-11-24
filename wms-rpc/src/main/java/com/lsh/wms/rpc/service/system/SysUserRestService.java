package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.system.ISysUserRestService;
import com.lsh.wms.model.system.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

import javax.ws.rs.*;
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
    @Path("getUserList")
    public String getSysUserList(Map<String, Object> params) throws BizCheckedException{
        List<SysUser> userList = sysUserRpcService.getSysUserList(params);
        return JsonUtils.SUCCESS(userList);
    }

    @POST
    @Path("getUserListCount")
    public String getSysUserListCount(Map<String, Object> params) throws BizCheckedException{
        return JsonUtils.SUCCESS(sysUserRpcService.getSysUserListCount(params));
    }

    @POST
    @Path("addUser")
    public String addSysUser(SysUser sysUser) throws BizCheckedException{
        sysUserRpcService.addSysUser(sysUser);
        return JsonUtils.SUCCESS(sysUser);
    }

    @POST
    @Path("updateUser")
    public String updateSysUser(SysUser sysUser) throws BizCheckedException{
        sysUserRpcService.updateSysUser(sysUser);
        return JsonUtils.SUCCESS(sysUser);
    }

    @POST
    @Path("checkUserLogin")
    public String checkLogin(Map<String, Object> params) throws BizCheckedException {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        return JsonUtils.SUCCESS(sysUserRpcService.checkLogin(username, password));
    }

    @POST
    @Path("getUserByUsername")
    public String getSysUserByUsername(Map<String, Object> params) {
        return JsonUtils.SUCCESS(sysUserRpcService.getSysUserByUsername((String)params.get("username")));
    }

    @GET
    @Path("getSysUserById")
    public String getSysUserById(@QueryParam("uid") Long iUid){
        return JsonUtils.SUCCESS(sysUserRpcService.getSysUserById(iUid));
    }

    @POST
    @Path("updatePassword")
    public String updatePassword() throws BizCheckedException {
        Map<String, Object> request = RequestUtils.getRequest();
        Long uid = (Long) request.get("uid");
        String sourcePassword = (String) request.get("sourcePassword");
        String newPassword = (String) request.get("newPassword");
        String confirmPassWord = (String) request.get("confirmPassWord");

        sysUserRpcService.updatePassword(uid,sourcePassword,newPassword,confirmPassWord);

        return JsonUtils.SUCCESS();
    }
}
