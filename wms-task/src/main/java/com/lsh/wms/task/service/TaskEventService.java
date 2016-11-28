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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mali on 16/8/17.
 */
@Component
public class TaskEventService {

    private static Logger logger = LoggerFactory.getLogger(TaskEventService.class);

    @Autowired
    private MessageService msgService;

    @Autowired
    private EventHandlerFactory handlerFactory;

    //构造公平的可重入锁
    private ReentrantLock lock = new ReentrantLock(true);


    @PostConstruct
    public void start() throws BizCheckedException {
        Long refreshCapacity = 1000L;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
				/*锁住此刷新线程，否则容易出现重复刷新，即第一次刷新还没有操作完，第二次刷新又开始了。造成资源同步问题 */
                lock.lock();
                TaskMsg msg = new TaskMsg();
                try {
                    while (true) {
                        logger.debug("begin to detail one");
                        msg = msgService.getMessage();
                        if (null == msg) {
                            logger.debug("nothing to do, go sleep");
                            break;
                        }
                        logger.debug("msg id = "+msg.getId()+", type = "+ msg.getType()+" , sourceTaskId = "+ msg.getSourceTaskId());
                        IEventHandler handler = handlerFactory.getEventHandler(msg.getType());
                        handler.process(msg);
                        logger.debug("msg is done");
                    }
                } catch (BizCheckedException ex) {
                    logger.error("----业务异常信息----"+ex.getMessage()+"------",ex);
                    msg.setErrorCode(ex.getCode());
                    msg.setErrorMsg(ex.getMessage());
                    msgService.saveMessage(msg);
                } catch (Exception e) {
                    logger.error("Exception",e);
                    msgService.sendMessage(msg);
                    logger.error("遇到系统错误，将消息重新塞回队列");
                }
                finally{
                    lock.unlock();
                }
            }
        }, 0, refreshCapacity);
    }
}

