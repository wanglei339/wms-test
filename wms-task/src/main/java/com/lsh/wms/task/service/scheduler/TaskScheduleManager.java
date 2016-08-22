package com.lsh.wms.task.service.scheduler;

import com.lsh.base.common.exception.BizCheckedException;
import javafx.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mali on 16/8/20.
 */
@Component
public class TaskScheduleManager {
    private static Logger logger = LoggerFactory.getLogger(TaskScheduleManager.class);

    //构造公平的可重入锁
    private ReentrantLock lock = new ReentrantLock(true);

    @PostConstruct
    public void start() throws BizCheckedException {
        Long refreshCapacity = 10000L;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
				/*锁住此刷新线程，否则容易出现重复刷新，即第一次刷新还没有操作完，第二次刷新又开始了。造成资源同步问题 */
                lock.lock();
                try {
                    // 调用刷新逻辑方法
                    //refreshRoute();
                } catch (BizCheckedException ex) {
                    logger.error("----刷新路由缓存的异常信息为：----"+ex.getMessage()+"------",ex);
                }finally{
                    lock.unlock();
                }
            }
        }, 0, refreshCapacity);
    }
}
