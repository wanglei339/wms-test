package com.lsh.wms.rpc.service.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.user.IUserRpcService;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.model.system.SysUser;
import com.lsh.wms.rpc.service.system.SysUserRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/3.
 */
@Service(protocol = "dubbo")
public class userRpcService implements IUserRpcService{
    @Autowired
    private SysUserRpcService sysUserRpcService;
    @Autowired
    RedisStringDao redisStringDao;


    public Map<String, Object> login(String userName, String passwd) throws BizCheckedException {
        //返回token UName
        // TODO: 16/7/28  登陆成功
        //校验是否用户名密码
        if(sysUserRpcService.checkLogin(userName,passwd)){
            long token = RandomUtils.genId();
            //获取uid
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("username", userName);
            List<SysUser> dList = sysUserRpcService.getSysUserList(params);
            SysUser sysUser = dList.get(0);

            long uid = sysUser.getUid();
            //userid token 加入缓存
            String key = StrUtils.formatString(RedisKeyConstant.USER_UID_TOKEN,uid);
            redisStringDao.set(key,token);
            //设置token失效时间 8小时
            redisStringDao.expire(key, PropertyUtils.getLong("tokenExpire"));
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("uid",uid);
            map.put("utoken",token);
            map.put("validTime",PropertyUtils.getLong("tokenExpire"));
            map.put("userName",sysUserRpcService.getSysUserById(uid).getScreenname());
            return map;
        }
        return null;
    }
}
