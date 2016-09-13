package com.lsh.wms.rf.service.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.user.IUserRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.user.IUserRpcService;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
    private RedisStringDao redisStringDao;

    @Reference
    private IUserRpcService userRpcService;

    @Value("classpath:AndroidMenu.json")
    private Resource menuResource;


    @Path("login")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String userLogin() throws BizCheckedException {

        Map<String,Object> request = RequestUtils.getRequest();
        String userName = (String) request.get("userName");
        String passwd = (String) request.get("passwd");
        System.out.println("userName : " + userName + "passwd : "+passwd);
        Map<String,Object> map = userRpcService.login(userName,passwd);
        //
//        HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse();
//        response.addHeader("uid",map.get("uid").toString());
//        response.addHeader("utoken",map.get("utoken").toString());
//        //创建两个cookie对象
//        Cookie idCookie = new Cookie("uid", map.get("uid").toString());
//        Cookie tokenCookie = new Cookie("utoken", map.get("utoken").toString());
//        idCookie.setMaxAge(PropertyUtils.getInt("maxAge"));
//        tokenCookie.setMaxAge(PropertyUtils.getInt("maxAge"));
//        response.addCookie(idCookie);
//        response.addCookie(tokenCookie);
////        // TODO: 16/8/1 剩余基本信息放在session 所属库区等。
//        HttpServletRequest sessionRequest = (HttpServletRequest) RpcContext.getContext().getRequest();
//        HttpSession session = sessionRequest.getSession();
//        session.setAttribute("","");
//        session.setAttribute("","");
//        session.setAttribute("","");
        return JsonUtils.SUCCESS(map);
    }

    @Path("getMenuList")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getMenuList() throws BizCheckedException {
        try {
            String menu = FileUtils.readFileToString(menuResource.getFile());
            List<Map> menuList = JSON.parseArray(menu, Map.class);
            Map<String, Object> rst = new HashMap<String, Object>();
            rst.put("menuList", menuList);
            return JsonUtils.SUCCESS(rst);
        } catch (IOException e) {
            e.printStackTrace();
            return JsonUtils.OTHER_EXCEPTION("配置读取错误");
        }
    }

    @Path("logout")
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String userLogout(){
        RequestUtils.destroySession();

        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }


}
