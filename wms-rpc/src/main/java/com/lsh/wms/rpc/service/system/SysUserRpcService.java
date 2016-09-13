package com.lsh.wms.rpc.service.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.base.q.Utilities.MD5;
import com.lsh.base.qiniu.pili.common.Utils;
import com.lsh.wms.api.service.system.ISysUserRpcService;
import com.lsh.wms.core.constant.StaffConstant;
import com.lsh.wms.core.service.system.SysUserService;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoStaffDepartment;
import com.lsh.wms.model.system.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/30.
 */
@Service(protocol = "dubbo")
public class SysUserRpcService implements ISysUserRpcService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private IdGenerator idGenerator;

    public List<SysUser> getSysUserList(Map<String, Object> params) {
        return sysUserService.getSysUserList(params);
    }

    public Integer getSysUserListCount(Map<String, Object> params) {
        return sysUserService.getSysUserListCount(params);
    }

    public void addSysUser(SysUser sysUser) throws BizCheckedException {
        //sysUser.setUid(RandomUtils.genId());

        //校验,新增的用户的username不能相同,因为是唯一key
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", sysUser.getUsername());
        if (this.getSysUserList(params).size() > 0) {
            throw new BizCheckedException("2660004");
        }
        //使用Id生成器
        sysUser.setUid(idGenerator.genId("uid", false, false));
        String salt = RandomUtils.randomStr(10);
        sysUser.setSalt(salt);
        sysUser.setPassword(genPwd(sysUser.getPassword(), salt));
        //sysUser.setScreenname(sysUser.getUsername());
        sysUserService.addSysUser(sysUser);
    }

    public void updateSysUser(SysUser sysUser) throws BizCheckedException {
        if (sysUser.getPassword() != null) {
            String salt = RandomUtils.randomStr(10);
            sysUser.setSalt(salt);
            sysUser.setPassword(genPwd(sysUser.getPassword(), salt));
        }
        sysUserService.updateSysUser(sysUser);
    }

    public SysUser getSysUserById(Long iUid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", iUid);
        List<SysUser> dList = getSysUserList(params);
        SysUser sysUser = null;
        if (!dList.isEmpty()) {
            sysUser = dList.get(0);
        }
        return sysUser;
    }

    public SysUser getSysUserByUsername(String username) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        List<SysUser> dList = getSysUserList(params);
        SysUser sysUser = null;
        if (!dList.isEmpty()) {
            sysUser = dList.get(0);
        }
        return sysUser;
    }

    public String genPwd(String password, String salt) {
        String pwd = salt + password;
        return EncodeUtils.md5(pwd);
    }

    public Boolean checkLogin(String username, String password) throws BizCheckedException {
        SysUser user = getSysUserByUsername(username);
        if (user != null) {
            String salt = user.getSalt();
            String signPwd = genPwd(password, salt);
            //用户禁用,直接抛异常提示
            if (user.getStatus().equals(StaffConstant.USER_FORBIDDEN)) {  //常量放在StaffConstant中
                throw new BizCheckedException("2660003");
            }
            //密码提示
            if (signPwd.equals(user.getPassword())) {
                return true;
            } else {
                throw new BizCheckedException("2660002");
            }
        } else {
            throw new BizCheckedException("2660001");
        }

    }
}
