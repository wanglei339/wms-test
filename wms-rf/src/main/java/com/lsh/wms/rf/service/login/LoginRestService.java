package com.lsh.wms.rf.service.login;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.api.service.login.ILoginRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.service.login.LoginService;
import org.apache.http.HttpResponse;
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
public class LoginRestService implements ILoginRestService{

    @Autowired
    private LoginService loginService;

    @Path("login")
    @POST
    public String userLogin() {
        Map<String,Object> request = RequestUtils.getRequest();
        String userName = (String) request.get("userName");
        String passwd = (String) request.get("passwd");
        System.out.println("userName : " + userName + "passwd : "+passwd);
        Map<String,Long> map = loginService.login(userName,passwd);
        HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse();
        response.addHeader("uId",map.get("uId").toString());
        response.addHeader("token",map.get("token").toString());
        return JsonUtils.SUCCESS();
    }
}
