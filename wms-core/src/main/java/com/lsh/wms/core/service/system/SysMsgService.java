package com.lsh.wms.core.service.system;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.BeanMapTransUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisListDao;
import com.lsh.wms.model.system.SysMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lixin-mac on 2016/10/21.
 */
@Component
public class SysMsgService {
    @Autowired
    private RedisListDao redisListDao;


    public void sendMessage(SysMsg msg) throws BizCheckedException {
        String value = JsonUtils.obj2Json(msg);
        String key = StrUtils.formatString(RedisKeyConstant.SYS_MSG,msg.getId());
        redisListDao.leftPush(key, JsonUtils.obj2Json(msg));
    }
}
