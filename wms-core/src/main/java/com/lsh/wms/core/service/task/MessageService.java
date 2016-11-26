package com.lsh.wms.core.service.task;

/**
 * Created by mali on 16/8/16.
 */

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.dao.redis.RedisListDao;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.dao.task.TaskMsgDao;
import com.lsh.wms.model.task.TaskMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@Transactional(readOnly = true)
public class MessageService {

    private static final String key = "msg_queue";

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private RedisListDao redisListDao;

    @Autowired
    private RedisStringDao dao;

    @Autowired
    private TaskMsgDao taskMsgDao;

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

    // get error msg
    public TaskMsg getMessage(Long businessId) throws BizCheckedException {
        TaskMsg taskMsg =  taskMsgDao.getTaskMsgByBusinessId(businessId);
        return taskMsg;
    }

    @Transactional(readOnly = false)
    public void saveMessage(TaskMsg taskMsg)  {
        try {
            taskMsgDao.insert(taskMsg);
        } catch (Exception ex) {
            logger.error("Exception occured during save taskMsg", ex);
        }
    }
}
