package com.lsh.wms.task.service;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.core.service.task.MessageService;
import com.lsh.wms.model.task.TaskMsg;
import com.lsh.wms.task.service.event.EventHandlerFactory;
import com.lsh.wms.task.service.event.IEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mali on 16/12/1.
 */
@Component
public class TaskRetryService {
    private static Logger logger = LoggerFactory.getLogger(TaskRpcService.class);

    @Autowired
    private MessageService msgService;

    @Autowired
    private EventHandlerFactory handlerFactory;

    //构造公平的可重入锁
    private ReentrantLock lock = new ReentrantLock(true);


    @PostConstruct
    public void start() throws BizCheckedException {
        Long refreshCapacity = 60 * 1000L;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
				/*锁住此刷新线程，否则容易出现重复刷新，即第一次刷新还没有操作完，第二次刷新又开始了。造成资源同步问题 */
                lock.lock();
                TaskMsg msg = new TaskMsg();
                try {
                    Map<String, Object> mapQuery = new HashMap<String, Object>();
                    mapQuery.put("retryTimes", 100L);
                    List<TaskMsg> msgList = msgService.getTaskMsgList(mapQuery);
                    logger.info(msgList.toString());
                    for(TaskMsg x : msgList) {
                        msg = x;

                        if (null == msg) {
                            break;
                        }

                        logger.info("Retry msg id = " + msg.getId() + ", type = " + msg.getType() + " , sourceTaskId = " + msg.getSourceTaskId());
                        IEventHandler handler = handlerFactory.getEventHandler(msg.getType());
                        handler.process(msg);
                        msg.setRetryTimes(msg.getRetryTimes() + 1);
                        msg.setStatus(1L);
                        msg.setErrorCode("0");
                        msgService.update(msg);
                        logger.debug("msg is done");
                    }
                } catch (BizCheckedException ex) {
                    logger.error("----业务异常信息----"+ex.getMessage()+"------",ex);
                    msg.setErrorCode(ex.getCode());
                    msg.setErrorMsg(ex.getMessage());
                    msg.setRetryTimes(msg.getRetryTimes()+1);
                    msgService.update(msg);
                } catch (Exception e) {
                    logger.error("Exception",e);
                }
                finally{
                    lock.unlock();
                }
            }
        }, 0, refreshCapacity);
    }
}
