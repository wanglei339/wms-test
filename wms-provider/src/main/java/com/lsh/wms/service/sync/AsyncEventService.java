package com.lsh.wms.service.sync;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

/**
 * Project Name: lsh-wms
 * Created by wuhao
 * Date: 16/12/26
 * 北京链商电子商务有限公司
 * Package com.lsh.wms.service.sync.AsyncEventService
 * desc:异步处理类
 */
@Component
@Lazy(value = false)
public class AsyncEventService {

    private static AsyncEventBus asyncEventBus;

    @Autowired
    private AsyncEventListener asyncEventListener;

//    @Value("${async.thread.num}")
    private int threadNum = 10;

    @PostConstruct
    public void init() {
        asyncEventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadNum));
        asyncEventBus.register(asyncEventListener);
    }

    public static void post(Object obj){
        asyncEventBus.post(obj);
    }
}
