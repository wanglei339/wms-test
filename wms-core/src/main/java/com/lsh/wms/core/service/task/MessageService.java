package com.lsh.wms.core.service.task;

/**
 * Created by mali on 16/8/16.
 */

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.dao.redis.RedisListDao;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.model.task.TaskMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    @Autowired
    private RedisListDao redisListDao;

    @Autowired
    private RedisStringDao dao;
    private static final String key = "msg_queue";

    @Autowired
    private MessageService msgService;


    public void sendMessage(TaskMsg msg) throws BizCheckedException {
        String value = JsonUtils.obj2Json(msg);
        redisListDao.leftPush(MessageService.key, JsonUtils.obj2Json(msg));
    }

    public TaskMsg getMessage() throws BizCheckedException {
        TaskMsg msg = null;
        String value = redisListDao.rightPop(MessageService.key);
        if (null != value){
            msg = JsonUtils.json2Obj(value, TaskMsg.class);
        }
        return msg;
    }
}
