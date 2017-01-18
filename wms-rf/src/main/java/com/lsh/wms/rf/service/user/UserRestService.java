package com.lsh.wms.rf.service.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.so.ObdBackRequest;
import com.lsh.wms.api.service.exception.SysExceptionMapper;
import com.lsh.wms.api.service.system.IRolePermissionRpcService;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.api.service.user.IUserRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.user.IUserRpcService;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.model.system.RolePermission;
import com.lsh.wms.model.system.SysUser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    private static Logger logger = LoggerFactory.getLogger(UserRestService.class);


    @Autowired
    private RedisStringDao redisStringDao;

    @Reference
    private IUserRpcService userRpcService;

    @Reference
    private ISysUserRpcService sysUserRpcService;

    @Reference
    private IRolePermissionRpcService rolePermissionRpcService;

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
        // System.out.println("userName : " + userName + "passwd : "+passwd);
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
            List<Map> menuTmpList = JSON.parseArray(menu, Map.class);
            List<Map> menuList = new ArrayList<Map>();
            List<String> menuRfList = new ArrayList<String>();
            HashMap<String,Object> permissionMap = new HashMap<String, Object>() {
                {
                    put("do_receipt", "收货");
                    put("do_shelve", "上架");
                    put("do_atticshelve", "阁楼上架");
                    put("do_stack", "盘点");
                    put("do_createscrap", "转残次");
                    put("do_createtransfer", "创建移库");
                    put("do_createreturn", "转退货");
                    put("do_transfer", "移库");
                    put("do_procurement", "补货");
                    put("do_pick", "拣货");
                    put("do_qc", "QC");
                    put("do_seed", "播种");
                    put("do_setGoods", "集货");
                    put("do_storeReceipt", "门店收货");
                    put("do_mergeboard", "合板");
                    put("do_load", "装车");
                    put("do_releasecollection", "一键装车");
                    put("do_search", "查询");
                    put("do_return", "反仓");

                }
            };
            Long uid = Long.valueOf(RequestUtils.getHeader("uid"));
            SysUser user = sysUserRpcService.getSysUserById(uid);
            if(user==null){
                return JsonUtils.TOKEN_ERROR("用户不存在");
            }
            RolePermission role =  rolePermissionRpcService.getRolePermissionById(Long.valueOf(user.getRole()));
            if(role==null){
                return JsonUtils.TOKEN_ERROR("该账号没有分配角色,请联系管理员分配");
            }
            List<String> permissionList = JSON.parseArray(role.getPermission(), String.class);

            if(permissionList==null){
                return JsonUtils.TOKEN_ERROR("该账号角色权限为空，请联系管理员");
            }
            for(Map one:menuTmpList) {
                for (String key : permissionMap.keySet()) {
                    if (permissionMap.get(key).equals(one.get("name"))) {
                        for (String permission : permissionList) {
                            if (key.equals(permission)) {
                                menuList.add(one);
                                menuRfList.add(key);
                            }

                        }
                    }
                }
            }
            Map<String, Object> rst = new HashMap<String, Object>();
            rst.put("menuList", menuList);
            rst.put("menuRfList",menuRfList);
            return JsonUtils.SUCCESS(rst);
        } catch (IOException e) {
            // e.printStackTrace();
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            return JsonUtils.TOKEN_ERROR("配置读取错误");
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
    Map<String,String> genMap (List<String> stringList){
        Map<String,String> result = new HashMap<String, String>();
        for(String role : stringList){
            result.put(role,role);
        }
        return result;
    }

}
