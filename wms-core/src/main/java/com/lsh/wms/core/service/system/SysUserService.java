package com.lsh.wms.core.service.system;

import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.dao.system.SysUserDao;
import com.lsh.wms.model.system.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulin on 16/7/30.
 */

@Component
@Transactional(readOnly = true)
public class SysUserService {
    private static final Logger logger = LoggerFactory.getLogger(SysUser.class);

    @Autowired
    private SysUserDao sysUserDao;

    public List<SysUser> getSysUserList(Map<String, Object> params) {
        return sysUserDao.getSysUserList(params);
    }
    public SysUser getSysUserByUid(String uid) {
        SysUser sysUser = null;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uid", uid);
        List<SysUser> list = sysUserDao.getSysUserList(map);
        if(list.size() == 1){
            sysUser = list.get(0);
        }
        return sysUser;
    }

    public Integer getSysUserListCount(Map<String, Object> params) {
        return sysUserDao.countSysUser(params);
    }

    @Transactional(readOnly = false)
    public void addSysUser(SysUser sysUser) {
        long now = (System.currentTimeMillis() / 1000);
        sysUser.setCreatedAt(now);
        sysUser.setUpdatedAt(now);
        sysUserDao.insert(sysUser);
    }

    @Transactional(readOnly = false)
    public void updateSysUser(SysUser sysUser) {
        long now = (System.currentTimeMillis() / 1000);
        sysUser.setUpdatedAt(now);
        sysUserDao.update(sysUser);
    }
}
