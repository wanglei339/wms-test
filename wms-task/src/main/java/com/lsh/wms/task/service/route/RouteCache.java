package com.lsh.wms.task.service.route;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mali on 16/8/16.
 */
@Component
public class RouteCache {
    private static RouteCache singleton = new RouteCache();

    private volatile Map<String,List<TaskTrigger>> mapRouteConf = new ConcurrentHashMap<String,List<TaskTrigger>>();

    private RouteCache() {
    }

    public static RouteCache getInstance(){
        return singleton;
    }

    public List<TaskTrigger> getRoute(String key) throws BizCheckedException {
        return null == mapRouteConf.get(key) ? new ArrayList<TaskTrigger>() : mapRouteConf.get(key);
    }
}
