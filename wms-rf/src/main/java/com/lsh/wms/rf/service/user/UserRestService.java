package com.lsh.wms.rf.service.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.user.IUserRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by lixin-mac on 16/7/28.
 */
@Service(protocol = "rest")
@Path("user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class UserRestService implements IUserRestService {

    @Autowired
    private UserService userService;

    @Path("login")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String userLogin() {

        Map<String,Object> request = RequestUtils.getRequest();
        String userName = (String) request.get("userName");
        String passwd = (String) request.get("passwd");
        System.out.println("userName : " + userName + "passwd : "+passwd);
        Map<String,Long> map = userService.login(userName,passwd);
        HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse();
        response.addHeader("uId",map.get("uId").toString());
        response.addHeader("token",map.get("token").toString());
        return JsonUtils.SUCCESS();
    }
}
